package pokerhands;

import org.javatuples.Pair;
import pokerhands.handgenerator.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Deck deck = new Deck();
        deck.shuffle();

        List <Card> hand1 = new ArrayList<>();
        List <Card> hand2 = new ArrayList<>();

        for (int i = 0 ; i < 10 ; i++) {
            if (i % 2 == 0) hand1.add(deck.draw());
            else hand2.add(deck.draw());
        }

        System.out.println(handResult(hand1, hand2));

    }

    public static String handResult(List<Card> hand1, List<Card> hand2) {

        System.out.println(hand1 + "\n\nvs\n\n" + hand2);

        HandComparator hc = new HandComparator();

        Pair<CardUtils.Hand, List<Card>> hand1Result = CardUtils.handCalculator(hand1);
        Pair<CardUtils.Hand, List<Card>> hand2Result = CardUtils.handCalculator(hand2);

        int score = hc.compare(hand1Result, hand2Result);

        if (score > 0) {
            return "Black wins. - with " + hand1Result.getValue0().name + " " + CardUtils.handSpecificity(hand1Result);

        } else if (score < 0) {
            return "White wins. - with " + hand2Result.getValue0().name + " " + CardUtils.handSpecificity(hand2Result);

        } else {
            return "Tie.";
        }

//        System.out.println();


//        return hand1Result.getValue0() + " vs " + hand2Result.getValue0();

//        hc.compare(hand1Result, hand2Result);

    }

}
