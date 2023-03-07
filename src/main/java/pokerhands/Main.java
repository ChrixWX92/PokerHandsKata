package pokerhands;

import lombok.SneakyThrows;
import org.javatuples.Pair;
import pokerhands.handgenerator.*;
import pokerhands.handgenerator.CardUtils.Hand;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import static pokerhands.handgenerator.CardUtils.Hand.*;

public class Main {

    static InputMismatchException e = new InputMismatchException("Invalid terminal input. Example input:\nBlack: 2H 3D 5S 9C KD  White: 2C 3H 4S 8C AH");

    public static void main(String[] args) {

//        randomHand();

        terminalHand();

    }


    public static String handResult(List<Card> hand1, List<Card> hand2) {

//        System.out.println(hand1 + "\n\nvs\n\n" + hand2);

        HandComparator hc = new HandComparator();

        Hand hand1Result = CardUtils.handCalculator(hand1);
        Hand hand2Result = CardUtils.handCalculator(hand2);

        int score = hc.compare(new Pair<>(hand1Result, hand1), new Pair<>(hand2Result, hand2));

        boolean additionalInfo = hand1Result == hand2Result
                && (hand1Result == FLUSH
                || ((hand1Result == ONE_PAIR || hand1Result ==  THREE_OF_A_KIND || hand1Result == FOUR_OF_A_KIND || hand1Result == FIVE_OF_A_KIND)
                && Objects.requireNonNull(CardUtils.getMostOfSameRank(hand1)).getValue1() == Objects.requireNonNull(CardUtils.getMostOfSameRank(hand2)).getValue1()));

        if (hand1Result == HIGH_CARD && hand2Result == HIGH_CARD){
            if (score > 0) {
                return "Black wins. - with " + CardUtils.highCardSpecificity(hand1, hand2, false);

            } else if (score < 0) {
                return "White wins. - with " + CardUtils.highCardSpecificity(hand1, hand2, false);

            } else {
                return "Tie.";
            }
        }

        else if (additionalInfo && hand1Result == ONE_PAIR){
            if (score > 0) {
                return "Black wins. - with " + hand1Result.name.toLowerCase() + CardUtils.handSpecificity(hand1Result, hand1, true) + CardUtils.highCardSpecificity(hand1, hand2, true);

            } else if (score < 0) {
                return "White wins. - with " + hand2Result.name.toLowerCase() + CardUtils.handSpecificity(hand2Result, hand2, true) + CardUtils.highCardSpecificity(hand1, hand2, true);

            } else {
                return "Tie.";
            }
        }

        else {
            if (score > 0) {
                return "Black wins. - with " + hand1Result.name.toLowerCase() + CardUtils.handSpecificity(hand1Result, hand1, additionalInfo);

            } else if (score < 0) {
                return "White wins. - with " + hand2Result.name.toLowerCase() + CardUtils.handSpecificity(hand2Result, hand2, additionalInfo);

            } else {
                return "Tie.";
            }
        }

    }

    private static void randomHand() {

        List<Card> hand1 = new ArrayList<>();
        List<Card> hand2 = new ArrayList<>();

        Deck deck = new Deck();
        deck.shuffle();

        for (int i = 0 ; i < 10 ; i++) {
            if (i % 2 == 0) hand1.add(deck.draw());
            else hand2.add(deck.draw());
        }

        System.out.println(handResult(hand1, hand2));
    }


    @SneakyThrows
    private static void terminalHand() {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        List<Card> hand1 = new ArrayList<>();
        List<Card> hand2 = new ArrayList<>();

        List<List<Card>> hand = parseHand(reader.readLine());

        System.out.println(handResult(hand.get(0), hand.get(1)));

    }

    private static List<List<Card>> parseHand(String terminalInput) {

        if (!terminalInput.startsWith("Black: ")) throw e;

        List<String> blackCards = Arrays.asList(terminalInput.substring(7).split(" ")).subList(0,5);
        List<String> whiteCards = Arrays.asList(terminalInput.substring(30).split(" "));

        return List.of(parseCards(blackCards), parseCards(whiteCards));

    }

    private static List<Card> parseCards(List<String> cards) {
        List<Card> cardList = new ArrayList<>();
        for (String card : cards) {
            Rank rank = Rank.getByCharacter(card.charAt(0));
            Suit suit = Suit.getByCharacter(card.charAt(1));
            if (rank == null || suit == null) throw e;
            cardList.add(new Card(rank, suit));
        }
        return cardList;
    }
}
