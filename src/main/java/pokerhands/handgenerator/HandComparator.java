package pokerhands.handgenerator;

import org.javatuples.Pair;

import java.util.Comparator;
import java.util.List;

public class HandComparator implements Comparator<Pair<CardUtils.Hand, List<Card>>> {
    @Override
    public int compare(Pair<CardUtils.Hand, List<Card>> hand1, Pair<CardUtils.Hand, List<Card>> hand2) {
        int comparison1 = Integer.compare(hand1.getValue0().priority, hand2.getValue0().priority);
        if  (comparison1 != 0) {
            return comparison1;
        } else {
            // TODO: Distinguish the winner between tied hands
            switch (hand1.getValue0()) {
                case HIGH_CARD -> {
                    int i = 1;
                    Card hand1Highest = CardUtils.getTopCards(hand1.getValue1(), 1).get(0);
                    Card hand2Highest = CardUtils.getTopCards(hand2.getValue1(), 1).get(0);
                    while (hand1Highest.rank.priority == hand2Highest.rank.priority) {
                        i++;
                        if (i == 6) return 0;
                        hand1Highest = CardUtils.getTopCards(hand1.getValue1(), i).get(i-1);
                        hand2Highest = CardUtils.getTopCards(hand2.getValue1(), i).get(i-1);
                    }
                    return hand1Highest.rank.priority > hand2Highest.rank.priority ? 1 : 0;
                }
                case ONE_PAIR -> {

                }
                case TWO_PAIR -> {

                }
                case THREE_OF_A_KIND -> {

                }
                case STRAIGHT -> {

                }
                case FLUSH -> {

                }
                case FULL_HOUSE -> {

                }
                case STRAIGHT_FLUSH -> {

                }
                case ROYAL_FLUSH -> {return 0;}
            }
        }
        return 0; //TODO: Where are we with this?
    }
}
