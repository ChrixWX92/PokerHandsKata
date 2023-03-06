import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pokerhands.Main;
import pokerhands.handgenerator.Card;
import pokerhands.handgenerator.Deck;
import pokerhands.handgenerator.Rank;
import pokerhands.handgenerator.Suit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pokerhands.handgenerator.Rank.*;
import static pokerhands.handgenerator.Suit.*;

class HandsTests {

    @Disabled
    @Test
    public void allPossibleHands() {

        Deck deck = new Deck();

        List<List<Card>> allPossibleHands = new ArrayList<>();

        List<Card> deckCards = deck.getCards();

        for (Card card : deck.getCards()) {
            List<Card> deckCards2 = new ArrayList<>(deckCards);
            deckCards2.remove(deckCards.indexOf(card));
            for (Card card2 : deckCards2) {
                List<Card> deckCards3 = new ArrayList<>(deckCards2);
                deckCards3.remove(deckCards2.indexOf(card2));
                for (Card card3 : deckCards3) {
                    List<Card> deckCards4 = new ArrayList<>(deckCards3);
                    deckCards4.remove(deckCards3.indexOf(card3));
                    for (Card card4 : deckCards4) {
                        List<Card> deckCards5 = new ArrayList<>(deckCards4);
                        deckCards5.remove(deckCards4.indexOf(card4));
                        for (Card card5 : deckCards5) {
                            List<Card> deckCards6 = new ArrayList<>(deckCards5);
                            deckCards6.remove(deckCards5.indexOf(card5));
                            System.out.println(List.of(card, card2, card3, card4, card5));

                        }
                    }
                }
            }
        }

    }

    @Test
    void checkHighCardVsOnePair() {
        // High Card
        List<Card> hand1 = List.of(new Card(NINE, HEARTS), new Card(THREE, SPADES), new Card(JACK, SPADES), new Card(TWO, HEARTS), new Card(SEVEN, CLUBS));
        // One Pair
        List<Card> hand2 = List.of(new Card(SEVEN, DIAMONDS), new Card(THREE, DIAMONDS), new Card(ACE, HEARTS), new Card(FOUR, DIAMONDS), new Card(THREE, CLUBS));
        assertEquals("HIGH_CARD vs ONE_PAIR", Main.handResult(hand1, hand2));
    }
    
    @Test
    void checkOnePairVsTwoPair() {
        // One Pair
        List<Card> hand1 = List.of(new Card(TWO, SPADES), new Card(THREE, SPADES), new Card(JACK, SPADES), new Card(TWO, HEARTS), new Card(SEVEN, SPADES));
        // Two Pair
        List<Card> hand2 = List.of(new Card(SEVEN, DIAMONDS), new Card(THREE, DIAMONDS), new Card(SEVEN, HEARTS), new Card(FOUR, DIAMONDS), new Card(THREE, CLUBS));
        assertEquals("ONE_PAIR vs TWO_PAIR", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTwoPairVsTrips() {
        // Two Pair
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(SEVEN, SPADES), new Card(JACK, SPADES), new Card(ACE, HEARTS), new Card(SEVEN, DIAMONDS));
        // Trips
        List<Card> hand2 = List.of(new Card(QUEEN, DIAMONDS), new Card(THREE, DIAMONDS), new Card(QUEEN, HEARTS), new Card(FOUR, DIAMONDS), new Card(QUEEN, CLUBS));
        assertEquals("TWO_PAIR vs THREE_OF_A_KIND", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTripsVsStraight() {
        // Trips
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(ACE, DIAMONDS), new Card(ACE, HEARTS), new Card(TWO, HEARTS), new Card(THREE, DIAMONDS));
        // Straight
        List<Card> hand2 = List.of(new Card(FIVE, DIAMONDS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, HEARTS), new Card(NINE, HEARTS));
        assertEquals("THREE_OF_A_KIND vs STRAIGHT", Main.handResult(hand1, hand2));
    }

    @Test
    void checkStraightVsFlush() {
        // Straight
        List<Card> hand1 = List.of(new Card(TWO, SPADES), new Card(THREE, DIAMONDS), new Card(FOUR, HEARTS), new Card(FIVE, HEARTS), new Card(SIX, DIAMONDS));
        // Flush
        List<Card> hand2 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, CLUBS), new Card(TEN, CLUBS));
        assertEquals("STRAIGHT vs FLUSH", Main.handResult(hand1, hand2));
    }

    @Test
    void checkFlushVsFullHouse() {
        // Flush
        List<Card> hand1 = List.of(new Card(TWO, HEARTS), new Card(THREE, HEARTS), new Card(FOUR, HEARTS), new Card(FIVE, HEARTS), new Card(SEVEN, HEARTS));
        // Full House
        List<Card> hand2 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(FIVE, DIAMONDS), new Card(SIX, SPADES), new Card(SIX, HEARTS));
        assertEquals("FLUSH vs FULL_HOUSE", Main.handResult(hand1, hand2));
    }

    @Test
    void checkFullHouseVsStraightFlush() {
        // Full House
        List<Card> hand1 = List.of(new Card(TWO, SPADES), new Card(TWO, DIAMONDS), new Card(FOUR, HEARTS), new Card(FOUR, SPADES), new Card(FOUR, DIAMONDS));
        // Straight Flush
        List<Card> hand2 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, CLUBS), new Card(NINE, CLUBS));
        assertEquals("FULL_HOUSE vs STRAIGHT_FLUSH", Main.handResult(hand1, hand2));
    }

    @Test
    void checkStraightFlushVsRoyalFlush() {
        // Straight Flush
        List<Card> hand1 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, CLUBS), new Card(NINE, CLUBS));
        // Royal Flush
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, HEARTS), new Card(KING, HEARTS), new Card(ACE, HEARTS));
        assertEquals("STRAIGHT_FLUSH vs ROYAL_FLUSH", Main.handResult(hand1, hand2));
    }

    @Test
    void checkHighCardVsHighCard() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(THREE, SPADES), new Card(FIVE, SPADES), new Card(SEVEN, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(TWO, HEARTS), new Card(FIVE, HEARTS), new Card(SEVEN, DIAMONDS));
        assertEquals("Black wins. - with High card , Queen", Main.handResult(hand1, hand2));
    }

    void addCard(List<Card> newHand, int index) {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if (rank == ONE || rank == JOKER) continue;
                Card card = new Card(rank, suit);
                if (!newHand.contains(card)) {
                    newHand.add(index, card);
                }
            }
        }
    }

}