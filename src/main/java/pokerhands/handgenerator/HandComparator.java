package pokerhands.handgenerator;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static pokerhands.handgenerator.CardUtils.Hand.*;
import static pokerhands.handgenerator.Rank.*;

public class HandComparator implements Comparator<Pair<CardUtils.Hand, List<Card>>> {
    @Override
    public int compare(Pair<CardUtils.Hand, List<Card>> hand1, Pair<CardUtils.Hand, List<Card>> hand2) {
        int comparison1 = Integer.compare(hand1.getValue0().priority, hand2.getValue0().priority);
        if  (comparison1 != 0) {
            return comparison1;
        } else {
            // TODO: Distinguish the winner between tied hands
            switch (hand1.getValue0()) {
                case HIGH_CARD, FLUSH -> {
                    return compareHighCards(hand1, hand2);
                }
                case ONE_PAIR, THREE_OF_A_KIND -> {
                    int result = compareRepeatedRanks(hand1, hand2);
                    return result == 0 ? compareHighCards(hand1, hand2) : result;
                }
                case TWO_PAIR -> {
                    Pair<CardUtils.Hand, List<Card>> newHand1 = new Pair<>(ONE_PAIR, filterNonPairCards(hand1));
                    Pair<CardUtils.Hand, List<Card>> newHand2 = new Pair<>(ONE_PAIR, filterNonPairCards(hand2));
                    int result = compareRepeatedRanks(newHand1, newHand2);
                    return result == 0 ? compareHighCards(newHand1, newHand2) : result;
                }
                case STRAIGHT, STRAIGHT_FLUSH -> {
                    return compareHighCards(hand1, hand2);
                    //Todo: Distinguish between broadway and the wheel
                }
                case FULL_HOUSE -> {

                }
                case ROYAL_FLUSH -> {return 0;}
            }
        }
        return 0; //TODO: Where are we with this?
    }

    private List<Card> filterNonPairCards(Pair<CardUtils.Hand, List<Card>> hand) {
        Rank pairRank1 = Objects.requireNonNull(CardUtils.getMostOfSameRank(hand.getValue1())).getValue1();
        List<Card> newHand = new ArrayList<>();
        for (Card card : hand.getValue1()) {
            if (card.rank != pairRank1) {
                newHand.add(card);
            }
        }
        return newHand;
    }

    private int compareRepeatedRanks(Pair<CardUtils.Hand, List<Card>> hand1, Pair<CardUtils.Hand, List<Card>> hand2) {
        int hand1Priority = Objects.requireNonNull(CardUtils.getMostOfSameRank(hand1.getValue1())).getValue1().priority;
        int hand2Priority = Objects.requireNonNull(CardUtils.getMostOfSameRank(hand2.getValue1())).getValue1().priority;
        return Integer.compare(hand1Priority, hand2Priority);
    }

    private int compareHighCards(Pair<CardUtils.Hand, List<Card>> hand1, Pair<CardUtils.Hand, List<Card>> hand2) {

        List<Card> newHand1 = new ArrayList<>(hand1.getValue1());
        List<Card> newHand2 = new ArrayList<>(hand2.getValue1());

        if (hand1.getValue0() == STRAIGHT) reviseAcePriority(newHand1);
        if (hand2.getValue0() == STRAIGHT) reviseAcePriority(newHand2);

        int i = 1;
        Card hand1Highest = CardUtils.getTopCards(newHand1, 1).get(0);
        Card hand2Highest = CardUtils.getTopCards(newHand2, 1).get(0);
        while (hand1Highest.rank.priority == hand2Highest.rank.priority) {
            i++;
            if (i == 6) return 0;
            hand1Highest = CardUtils.getTopCards(newHand1, i).get(i-1);
            hand2Highest = CardUtils.getTopCards(newHand2, i).get(i-1);
        }
        return hand1Highest.rank.priority > hand2Highest.rank.priority ? 1 : -1;
    }

    private List<Card> reviseAcePriority(List<Card> hand) {
        int ace = -1;
        boolean king = false;
        for (int i = 0 ; i < hand.size() ; i++) {
            if (hand.get(i).rank == ACE) ace = i;
            if (hand.get(i).rank == KING || hand.get(i).rank == JOKER) king = true;
        }
        if (ace > -1 && !king) hand.remove(ace);
        return hand;
    }

}
