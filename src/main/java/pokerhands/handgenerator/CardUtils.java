package pokerhands.handgenerator;

import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;
import static pokerhands.handgenerator.CardUtils.Hand.*;
import static pokerhands.handgenerator.Rank.*;

public class CardUtils {

    /** Five-digit unicode values are determined via surrogate pairs, the fist of the pair is below, the second is an instance variable of the Suit object - codepointStub**/
    private static final String surrogateFirst = "\uD83C";

    static Map.Entry<Pair<Integer, Integer>, String>[] getCharacters(){
        Map.Entry<Pair<Integer, Integer>, String>[] entries = new Map.Entry[Rank.values().length * Suit.values().length];
        int counter = 0;
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                entries[counter] = entry(new Pair<>(rank.id, suit.id), surrogateFirst + Character.toChars(Integer.parseInt(suit.codepointStub.substring(2) + Integer.toHexString(rank.id), 16))[0]);
                counter++;
            }
        }
        return entries;
    }

    public static Hand handCalculator(List<Card> hand) {
        return handCalculator(new Card[]{hand.get(0), hand.get(1)}, List.of(hand.get(2), hand.get(3), hand.get(4)));
    }

    public static Hand handCalculator(Card[] holdCards, List<Card> board) {

        List<Card> hand = new ArrayList<>(board);
        hand.addAll(Arrays.asList(holdCards));

        List<Card> bestCards;
        List<Card> remainder = new ArrayList<>();

        /* The full house check must done before checking for straights and flushes, as none of the
        aforementioned can coexist for the same hand. If quads or a full house is found, we return, otherwise we check for a
        straight, and then a flush, before awarding anything lower.
        */

        // Calculates paired cards | K = Hand that could be made from said duplicates, V = Rank of all the duplicates
        Pair<Hand, Rank> pairHand = getMostOfSameRank(hand);

        if (pairHand != null) { // If at least a pair has been found
            bestCards = hand.stream().filter(o -> o.rank == pairHand.getValue1()).collect(Collectors.toList());
            // bestCards now consists of the cards found by getMostOfSameRank(hand)

            //The below if sorts out any quads
            if (pairHand.getValue0() == FOUR_OF_A_KIND) {
                Card kicker = board.stream().filter(o -> o.rank != pairHand.getValue1()).max(Comparator.naturalOrder()).orElse(null);
                bestCards.add(kicker);
                if (kicker != null) {
                    return kicker.rank == JOKER ? FIVE_OF_A_KIND : FOUR_OF_A_KIND;
                }
            }
            // Handling any other duplicate rank scenarios
            else if (pairHand.getValue0() == THREE_OF_A_KIND || pairHand.getValue0() == ONE_PAIR) {
                // Remainder now consists of any cards that weren't caught by getMostOfSameRank(hand)
                remainder = hand.stream().filter(o -> o.rank != pairHand.getValue1()).collect(Collectors.toList());
                // Now checking the remainder for any further duplicates
                Pair<Hand, Rank> remainingHand = getMostOfSameRank(remainder);
                if (remainingHand != null && remainingHand.getValue0() == ONE_PAIR) {
                    // This covers a full house
                    if (pairHand.getValue0() == THREE_OF_A_KIND) {
                        bestCards.addAll(board.stream().filter(o -> o.rank == remainingHand.getValue1()).collect(Collectors.toList()));
                        return FULL_HOUSE;
                    }
                    // Two pair here
                    else {
                        List<Card> secondRemainder = remainder.stream().filter(o -> o.rank != remainingHand.getValue1()).collect(Collectors.toList());
                        Card kicker = secondRemainder.stream().filter(o -> o.rank != remainingHand.getValue1()).max(Comparator.naturalOrder()).orElse(null);
                        bestCards.addAll(board.stream().filter(o -> o.rank == remainingHand.getValue1()).collect(Collectors.toList()));
                        bestCards.add(kicker);
                        return TWO_PAIR;
                    }
                }
                // If the remaining cards aren't duplicated, we can only assume trips
                else {
                    if (pairHand.getValue0() == THREE_OF_A_KIND) {
                        bestCards.addAll(getTopCards(remainder, 2));
                        return THREE_OF_A_KIND;
                    }
                }
            }

            // Any other scenarios below

            List<Card> straightCards = getStraight(hand);
            List<Card> suitedCards;
            for (Suit suit : Suit.values()) {
                suitedCards = getCardsOfSuit(hand, suit);
                // Flush contingencies
                if (suitedCards.size() > 4) {
                    if (straightCards != null && straightCards.containsAll(suitedCards)) {
                        if (Collections.max(straightCards).rank == ACE) {
                            return ROYAL_FLUSH;
                        } else {
                            return STRAIGHT_FLUSH;
                        }
                    } else {
                        return FLUSH;
                    }
                }
            }
            if (straightCards != null && straightCards.size() > 4) {
                return STRAIGHT;
            }
            else {
                bestCards.addAll(getTopCards(remainder, 3));
                return ONE_PAIR;
            }
        } else {
            // TODO: The below straight and flush logic is duplicated in pair-based scenarios above
            List<Card> straightCards = getStraight(hand);
            List<Card> suitedCards;
            for (Suit suit : Suit.values()) {
                suitedCards = getCardsOfSuit(hand, suit);
                // Flush contingencies
                if (suitedCards.size() > 4) {
                    if (straightCards != null && straightCards.containsAll(suitedCards)) {
                        if (Collections.max(straightCards).rank == ACE) {
                            return ROYAL_FLUSH;
                        } else {
                            return STRAIGHT_FLUSH;
                        }
                    } else {
                        return FLUSH;
                    }
                }
            }
            if (straightCards != null && straightCards.size() > 4) {
                return STRAIGHT;
            }
            bestCards = getTopCards(board, 5);
            return HIGH_CARD;
        }

    }

    public static String handSpecificity(Hand hand, List<Card> cards) {
        return handSpecificity(hand, cards, false);
    }

    public static String handSpecificity(Hand hand, List<Card> cards, boolean additionalInfo) {

        switch (hand) {
            case HIGH_CARD -> {
                if (cards.stream().max(Comparator.naturalOrder()).isPresent()) {
                    return ": " + cards.stream().max(Comparator.naturalOrder()).get().rank.name;
                    }
            }
            case ONE_PAIR, THREE_OF_A_KIND, FOUR_OF_A_KIND, FIVE_OF_A_KIND -> {
                List<Rank> ranks = cards.stream().map(Card::getRank).collect(toList());
                HashSet<Rank> rankSet = new HashSet<>(ranks);
                List<Rank> remainingRanks = new ArrayList<>();
                String result = "";

                for (Rank rank : ranks) {
                    if (!rankSet.remove(rank)) {
                        result = rank.name.equals("Two") ? ": Deuces" : ": " + rank.name + "s";
                        if (!additionalInfo) return result;
                    } else if (additionalInfo) {
                        remainingRanks.add(rank);
                    }
                }
                if (additionalInfo) {
                    if (hand == ONE_PAIR) {return result + " - ";}
                    int kickerValue;
                    if (remainingRanks.size() > 0) {
                        kickerValue = remainingRanks.stream().map(r -> r.value).max(Integer::compareTo).orElse(null);
                        return result + " - " + (Rank.getByValue(kickerValue) != null ? Objects.requireNonNull(getByValue(kickerValue)).name : null) + " kicker";
                    }
                }
                return "NO PAIR FOUND";
            }
            case TWO_PAIR, FULL_HOUSE -> {

                List<Rank> ranks = cards.stream().map(Card::getRank).collect(toList());
                HashSet<Rank> rankSet = new HashSet<>(ranks);
                StringBuilder pairs = new StringBuilder(": ");

                int id = 0;
                boolean first = false;
                Map<Rank, Integer> rankMap = new HashMap<>();
                for (Rank rank : ranks) {
                    if (!rankSet.add(rank) && id != rank.id) {
                        id = rank.id;
                        if (hand == TWO_PAIR) {
                            pairs.append(id == 2 ? "Deuce" : rank.name);
                            if (first) {
                                pairs.append(id == 6 ? "es" : "s");
                                break;
                            } else {pairs.append(id == 6 ? "es and " : "s and ");}
                            first = true;
                        }
                        else {
                            rankMap.put(rank, rankMap.get(rank) == null ? 1 : rankMap.get(rank) + 1);
                        }
                    }
                }
                if (hand == FULL_HOUSE) {
                    Rank rank1 = Collections.max(rankMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
                    rankMap.remove(rank1);
                    Rank rank2 = Collections.max(rankMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
                    pairs.append(rank2.id == 2 ? "Deuce" : rank2.name).append(rank2.id == 6 ? "es full of " : "s full of ");
                    pairs.append(rank1.id == 2 ? "Deuce" : rank1.name).append(rank1.id == 6 ? "es" : "s");
                }
                return pairs.toString();//pairs.substring(0, pairs.length()-6);
            }
            case STRAIGHT, FLUSH, STRAIGHT_FLUSH, ROYAL_FLUSH -> {

                String specificity = (hand == FLUSH || hand == STRAIGHT_FLUSH) ? ", " : ": ";

                if (cards.stream().max(Comparator.naturalOrder()).isPresent()) {
                    if (hand != FLUSH || additionalInfo) {
                        Rank highCard = cards.stream().max(Comparator.naturalOrder()).get().rank;
                        String highCardName = highCard.name;
                        boolean wheel = false;
                        if (highCard == ACE) {
                            wheel = true;
                            for (Card card : cards) {
                                if (card.rank == KING) {
                                    wheel = false;
                                    break;
                                }
                            }
                        }
                        specificity = specificity + (wheel ? "Five-high" : highCardName + "-high");
                    } else {
                        specificity = "";
                    }
                }

                if (hand == STRAIGHT) {
                    return specificity;
                } else {
                    List<Suit> suits = cards.stream().map(Card::getSuit).collect(toList());
                    String suitName = "";
                    int frequency = 0;
                    for (Suit suit : Suit.values()) {
                        int count = Collections.frequency(suits, suit);
                        if (count > frequency) {
                            frequency = count;
                            suitName = ": " + suit.name;
                        }
                    }
                    if (hand == ROYAL_FLUSH) {
                        return suitName;
                    } else {
                        return suitName + specificity;
                    }
                }
            }
        }
        return "";
    }

    public static Pair<Hand, Rank> getMostOfSameRank(List<Card> hand) {

        AtomicReference<Map.Entry<Rank, Long>> mostCommonRank = new AtomicReference<>();

        hand.stream()
                .map(Card::getRank)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(mostCommonRank::set);

//        System.out.println(mostCommonRank);

        switch (mostCommonRank.get().getValue().intValue()) {
            case 2 -> {return new Pair<>(ONE_PAIR, mostCommonRank.get().getKey());}
            case 3 -> {return new Pair<>(THREE_OF_A_KIND, mostCommonRank.get().getKey());}
            case 4 -> {return new Pair<>(FOUR_OF_A_KIND, mostCommonRank.get().getKey());}
            default -> {return null;}
        }

    }

    public static List<Card> getStraight(List<Card> hand) {

        for (Card card : hand) {
            List<Card> straight = new ArrayList<>();
            straight.add(card);
            Rank rank = card.rank;
            List<Card> cardsAbove;

            // Wheel logic
            cardsAbove = getCardsAbove(hand, rank);

            while (!cardsAbove.isEmpty()) {
                straight.addAll(cardsAbove);
                rank = rank.getAbove();
                cardsAbove = getCardsAbove(hand, rank);
            }

            if (Collections.max(straight).rank.value - Collections.min(straight).rank.value < 4) straight.clear();
            else return straight;

        }

        return null;

    }

    public static String highCardSpecificity(List<Card> hand1, List<Card> hand2, boolean pair) {

        if (hand1.size() < 1 || hand2.size() < 1) {return "";}

        List<Integer> hand1Values = hand1.stream().map(o -> o.rank.value).collect(toList());
        List<Integer> hand2Values = hand2.stream().map(o -> o.rank.value).collect(toList());
        List<Integer> duplicateValues = new ArrayList<>();
        for (Integer value : hand1Values) if (hand2Values.contains(value)) duplicateValues.add(value);
        for (Integer duplicate : duplicateValues) {
            hand1Values.remove(duplicate);
            hand2Values.remove(duplicate);
        }
        if (hand1Values.size() < 1 || hand2Values.size() < 1) {return "";}
        int card1 = hand1Values.stream().max(Comparator.naturalOrder()).orElse(null);
        int card2 = hand2Values.stream().max(Comparator.naturalOrder()).orElse(null);

        if (pair) {return (card1 > card2 ? Objects.requireNonNull(getByValue(card1)).name : Objects.requireNonNull(getByValue(card2)).name) + " kicker";}

        return "high card: " + (card1 > card2 ? Objects.requireNonNull(getByValue(card1)).name : Objects.requireNonNull(getByValue(card2)).name);

    }

    public static List<Card> getCardsOfSuit(List<Card> hand, Suit suit) {
        return hand.stream().filter(o -> o.suit.equals(suit)).collect(Collectors.toList());
    }

    public static List<Card> getCardsAbove(final List<Card> hand, final Rank rank){
        AtomicReference<List<Card>> cardsAbove = new AtomicReference<>(new ArrayList<>());
        hand.stream().filter(o -> o.rank.equals(rank.getAbove())).forEach(o -> cardsAbove.get().add(o));
        return cardsAbove.get();
    }

    public static List<Card> getTopCards(List<Card> hand, int number) {
        return hand.stream().sorted(Comparator.reverseOrder()).limit(number).collect(Collectors.toList());
    }

    private static String rankNameToNumber(Rank rank) {
        if (rank.value < 9) {return Character.toString(rank.character);}
        else if (rank == TEN) return "10";
        else return rank.name;
    }

    public enum Hand {
        FIVE_OF_A_KIND(11, "Five of a kind"),
        ROYAL_FLUSH(10, "Royal flush"),
        STRAIGHT_FLUSH(9, "Straight flush"),
        FOUR_OF_A_KIND(8, "Four of a kind"),
        FULL_HOUSE(7, "Full house"),
        FLUSH(6, "Flush"),
        STRAIGHT(5, "Straight"),
        THREE_OF_A_KIND(4, "Three of a kind"),
        TWO_PAIR(3, "Two pair"),
        ONE_PAIR(2, "One pair"),
        HIGH_CARD(1, "High card");

        public int priority; // Higher = higher priority
        public final String name;

        /** Values should ideally be dynamic, dependent on context, game, deck, etc.**/
        Hand(int priority, String name) {
            this.priority = priority;
            this.name = name;
        }

    }
}
