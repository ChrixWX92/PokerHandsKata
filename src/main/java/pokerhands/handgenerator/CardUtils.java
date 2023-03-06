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

    public static Pair<Hand, List<Card>> handCalculator(List<Card> hand) {
        return handCalculator(new Card[]{hand.get(0), hand.get(1)}, List.of(hand.get(2), hand.get(3), hand.get(4)));
    }

    public static Pair<Hand, List<Card>> handCalculator(Card[] holdCards, List<Card> board) {

        List<Card> hand = new ArrayList<>(board);
        hand.addAll(Arrays.asList(holdCards));

        List<Card> bestCards;
        List<Card> remainder = new ArrayList<>();

        /* In Texas hold 'em, the full house check must done before checking for straights and flushes, as none of the
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
                    return new Pair<>(kicker.rank == JOKER ? FIVE_OF_A_KIND : FOUR_OF_A_KIND, bestCards);
                }
            }
            // Handling any other duplicate rank scenarios
            else if (pairHand.getValue0() == THREE_OF_A_KIND || pairHand.getValue0() == ONE_PAIR) {
                // Remainder now consists of any cards that weren't caught by getMostOfSameRank(hand)
                remainder = hand.stream().filter(o -> o.rank != pairHand.getValue1()).collect(Collectors.toList());
                // Now checking the remainder for any further duplicates
                Pair<Hand, Rank> remainingHand = getMostOfSameRank(remainder);
                if (remainingHand == null) {
//                    System.out.println("null");
                } else {
//                    System.out.println("remaining hand = " + remainingHand.getKey().name);
                }
                if (remainingHand != null && remainingHand.getValue0() == ONE_PAIR) {
                    // This covers a full house
                    if (pairHand.getValue0() == THREE_OF_A_KIND) {
                        bestCards.addAll(board.stream().filter(o -> o.rank == remainingHand.getValue1()).collect(Collectors.toList()));
                        //TODO: Full house not recognised sometimes.
                        return new Pair<>(FULL_HOUSE, bestCards);
                    }
                    // Two pair here
                    else {
                        List<Card> secondRemainder = remainder.stream().filter(o -> o.rank != remainingHand.getValue1()).collect(Collectors.toList());
                        Card kicker = secondRemainder.stream().filter(o -> o.rank != remainingHand.getValue1()).max(Comparator.naturalOrder()).orElse(null);
                        bestCards.addAll(board.stream().filter(o -> o.rank == remainingHand.getValue1()).collect(Collectors.toList()));
                        bestCards.add(kicker);
                        //TODO: Two pair not recognised often, probably due to the same thing as above.
                        return new Pair<>(TWO_PAIR, bestCards);
                    }
                }
                // If the remaining cards aren't duplicated, we can only assume trips TODO: CAN WE?
                else {
                    if (pairHand.getValue0() == THREE_OF_A_KIND) {
                        bestCards.addAll(getTopCards(remainder, 2));
                        return new Pair<>(THREE_OF_A_KIND, bestCards);
                    }
                }
            }

            // Anything other scenarios below

            List<Card> straightCards = getStraight(hand);
            List<Card> suitedCards;
            for (Suit suit : Suit.values()) {
                suitedCards = getCardsOfSuit(hand, suit);
                // Flush contingencies
                if (suitedCards.size() > 4) {
                    if (straightCards != null && straightCards.containsAll(suitedCards)) {
                        if (Collections.max(straightCards).rank == ACE) {
                            return new Pair<>(ROYAL_FLUSH, getTopCards(straightCards, 5));
                        } else {
                            return new Pair<>(STRAIGHT_FLUSH, getTopCards(straightCards, 5));
                        }
                    } else {
                        return new Pair<>(FLUSH, getTopCards(suitedCards, 5));
                    }
                }
            }
            if (straightCards != null && straightCards.size() > 4) {
                //TODO: Unknown false positives on this.
                return new Pair<>(STRAIGHT, getTopCards(straightCards, 5));
            }
            else {
                bestCards.addAll(getTopCards(remainder, 3));
                return new Pair<>(ONE_PAIR, bestCards);
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
                            return new Pair<>(ROYAL_FLUSH, getTopCards(straightCards, 5));
                        } else {
                            return new Pair<>(STRAIGHT_FLUSH, getTopCards(straightCards, 5));
                        }
                    } else {
                        return new Pair<>(FLUSH, getTopCards(suitedCards, 5));
                    }
                }
            }
            if (straightCards != null && straightCards.size() > 4) {
                //TODO: Unknown false positives on this.
                return new Pair<>(STRAIGHT, getTopCards(straightCards, 5));
            }
            bestCards = getTopCards(board, 5);
            return new Pair<>(HIGH_CARD, bestCards);
        }

    }

    public static String handSpecificity(Pair<Hand, List<Card>> hand) {

        Hand name = hand.getValue0();
        List<Card> cards = hand.getValue1();

        switch (name) {
            case HIGH_CARD -> {
                if (cards.stream().max(Comparator.naturalOrder()).isPresent()) {
                    return " " + cards.stream().max(Comparator.naturalOrder()).get().rank.name;
                    //TODO: Aces not recognising as high cards - hoping this doesn't affect hand rankings too - may be worth making a custom poker card/Hand Comparator
                }
            }
            case ONE_PAIR, THREE_OF_A_KIND, FOUR_OF_A_KIND, FIVE_OF_A_KIND -> {
                List<Rank> ranks = hand.getValue1().stream().map(Card::getRank).collect(toList());
                HashSet<Rank> rankSet = new HashSet<>(ranks);

                for (Rank rank : ranks) {
                    if (!rankSet.add(rank)) {
                        return rank.name.equals("Two") ? ", Deuces" : ", " + rank.name + "s";
                    }
                }
                return "NO PAIR FOUND";
            }
            case TWO_PAIR, FULL_HOUSE -> {

                List<Rank> ranks = hand.getValue1().stream().map(Card::getRank).collect(toList());
                HashSet<Rank> rankSet = new HashSet<>(ranks);
                StringBuilder pairs = new StringBuilder(", ");

                int id = 0;
                for (Rank rank : ranks) {
                    if (!rankSet.add(rank) && id != rank.id) {
                        id = rank.id;
                        pairs.append(rank.name).append("s and ");
                    }
                }
                return pairs.substring(0, pairs.length()-6);
            }
            case STRAIGHT, FLUSH, STRAIGHT_FLUSH, ROYAL_FLUSH -> {

                String specificity = ", ";

                if (cards.stream().max(Comparator.naturalOrder()).isPresent()) {
                    specificity = specificity + cards.stream().max(Comparator.naturalOrder()).get().rank.name + "-high";
                }

                if (name == STRAIGHT) {
                    return specificity;
                } else {
                    List<Suit> suits = hand.getValue1().stream().map(Card::getSuit).collect(toList());
                    String suitName = "";
                    int frequency = 0;
                    for (Suit suit : Suit.values()) {
                        int count = Collections.frequency(suits, suit);
                        if (count > frequency) {
                            frequency = count;
                            suitName = suit.name;
                        }
                    }
                    if (name == ROYAL_FLUSH) {
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

        System.out.println(mostCommonRank);

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
            //TODO: This seems to override logic for ace-high straights
            if (rank == ACE) cardsAbove = getCardsAbove(hand, ONE);
            else cardsAbove = getCardsAbove(hand, rank);

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
