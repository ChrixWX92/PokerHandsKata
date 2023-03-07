import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pokerhands.Main;
import pokerhands.handgenerator.cards.Card;
import pokerhands.handgenerator.cards.Deck;
import pokerhands.handgenerator.cards.Rank;
import pokerhands.handgenerator.cards.Suit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pokerhands.handgenerator.cards.Rank.*;
import static pokerhands.handgenerator.cards.Suit.*;

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
//                            System.out.println(List.of(card, card2, card3, card4, card5));

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
        assertEquals("White wins. - with one pair: 3s", Main.handResult(hand1, hand2));
    }
    
    @Test
    void checkOnePairVsTwoPair() {
        // One Pair
        List<Card> hand1 = List.of(new Card(TWO, SPADES), new Card(THREE, SPADES), new Card(JACK, SPADES), new Card(TWO, HEARTS), new Card(SEVEN, SPADES));
        // Two Pair
        List<Card> hand2 = List.of(new Card(SEVEN, DIAMONDS), new Card(THREE, DIAMONDS), new Card(SEVEN, HEARTS), new Card(FOUR, DIAMONDS), new Card(THREE, CLUBS));
        assertEquals("White wins. - with two pair: 7s and 3s", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTwoPairVsTrips() {
        // Two Pair
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(SEVEN, SPADES), new Card(JACK, SPADES), new Card(ACE, HEARTS), new Card(SEVEN, DIAMONDS));
        // Trips
        List<Card> hand2 = List.of(new Card(QUEEN, DIAMONDS), new Card(THREE, DIAMONDS), new Card(QUEEN, HEARTS), new Card(FOUR, DIAMONDS), new Card(QUEEN, CLUBS));
        assertEquals("White wins. - with three of a kind: Queens", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTwoSixesAndDeuces() {
        // Two Pair
        List<Card> hand1 = List.of(new Card(SIX, SPADES), new Card(TWO, SPADES), new Card(JACK, SPADES), new Card(SIX, HEARTS), new Card(TWO, DIAMONDS));
        // High Card
        List<Card> hand2 = List.of(new Card(NINE, HEARTS), new Card(THREE, SPADES), new Card(JACK, SPADES), new Card(TWO, HEARTS), new Card(SEVEN, CLUBS));;
        assertEquals("Black wins. - with two pair: 6s and 2s", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTripsVsStraight() {
        // Trips
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(ACE, DIAMONDS), new Card(ACE, HEARTS), new Card(TWO, HEARTS), new Card(THREE, DIAMONDS));
        // Straight
        List<Card> hand2 = List.of(new Card(FIVE, DIAMONDS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, HEARTS), new Card(NINE, HEARTS));
        assertEquals("White wins. - with straight: 9-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkWheelVsTrips() {
        // Wheel
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(TWO, DIAMONDS), new Card(THREE, HEARTS), new Card(FOUR, HEARTS), new Card(FIVE, DIAMONDS));
        // Trips
        List<Card> hand2 = List.of(new Card(ACE, HEARTS), new Card(ACE, DIAMONDS), new Card(ACE, CLUBS), new Card(TWO, HEARTS), new Card(THREE, DIAMONDS));
        assertEquals("Black wins. - with straight: 5-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkStraightOverWheel() {
        // Wheel
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(TWO, DIAMONDS), new Card(THREE, HEARTS), new Card(FOUR, HEARTS), new Card(FIVE, DIAMONDS));
        // Straight
        List<Card> hand2 = List.of(new Card(SIX, HEARTS), new Card(TWO, HEARTS), new Card(THREE, DIAMONDS), new Card(FOUR, CLUBS), new Card(FIVE, CLUBS));
        assertEquals("White wins. - with straight: 6-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkBroadwayOverWheel() {
        // Wheel
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(TWO, DIAMONDS), new Card(THREE, HEARTS), new Card(FOUR, HEARTS), new Card(FIVE, DIAMONDS));
        // Broadway
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, DIAMONDS), new Card(KING, CLUBS), new Card(ACE, CLUBS));
        assertEquals("White wins. - with straight: Ace-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkWheelVsFlush() {
        // Wheel
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(TWO, DIAMONDS), new Card(THREE, HEARTS), new Card(FOUR, HEARTS), new Card(FIVE, DIAMONDS));
        // Flush
        List<Card> hand2 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, CLUBS), new Card(TEN, CLUBS));
        assertEquals("White wins. - with flush: Clubs", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTripsVsBroadway() {
        // Trips
        List<Card> hand1 = List.of(new Card(ACE, SPADES), new Card(ACE, DIAMONDS), new Card(ACE, HEARTS), new Card(TWO, HEARTS), new Card(THREE, DIAMONDS));
        // Broadway
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, DIAMONDS), new Card(KING, CLUBS), new Card(ACE, CLUBS));
        assertEquals("White wins. - with straight: Ace-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkBroadwayOverStraight() {
        // Broadway
        List<Card> hand1 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, DIAMONDS), new Card(KING, CLUBS), new Card(ACE, CLUBS));
        // Straight
        List<Card> hand2 = List.of(new Card(SIX, HEARTS), new Card(TWO, HEARTS), new Card(THREE, DIAMONDS), new Card(FOUR, CLUBS), new Card(FIVE, CLUBS));
        assertEquals("Black wins. - with straight: Ace-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkBroadwayVsFlush() {
        // Broadway
        List<Card> hand1 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, DIAMONDS), new Card(KING, CLUBS), new Card(ACE, CLUBS));
        // Flush
        List<Card> hand2 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, CLUBS), new Card(TEN, CLUBS));
        assertEquals("White wins. - with flush: Clubs", Main.handResult(hand1, hand2));
    }

    @Test
    void checkStraightVsFlush() {
        // Straight
        List<Card> hand1 = List.of(new Card(TWO, SPADES), new Card(THREE, DIAMONDS), new Card(FOUR, HEARTS), new Card(FIVE, HEARTS), new Card(SIX, DIAMONDS));
        // Flush
        List<Card> hand2 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, CLUBS), new Card(TEN, CLUBS));
        assertEquals("White wins. - with flush: Clubs", Main.handResult(hand1, hand2));
    }

    @Test
    void checkStraightsDoNotWrapAround() {
        List<Card> hand1 = List.of(new Card(JACK, SPADES), new Card(QUEEN, DIAMONDS), new Card(KING, HEARTS), new Card(ACE, HEARTS), new Card(TWO, DIAMONDS));
        List<Card> hand2 = List.of(new Card(QUEEN, HEARTS), new Card(TWO, HEARTS), new Card(KING, DIAMONDS), new Card(ACE, CLUBS), new Card(TWO, CLUBS));
        assertEquals("White wins. - with one pair: 2s", Main.handResult(hand1, hand2));
    }

    @Test
    void checkFlushVsFullHouse() {
        // Flush
        List<Card> hand1 = List.of(new Card(TWO, HEARTS), new Card(THREE, HEARTS), new Card(FOUR, HEARTS), new Card(FIVE, HEARTS), new Card(SEVEN, HEARTS));
        // Full House
        List<Card> hand2 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(FIVE, DIAMONDS), new Card(SIX, SPADES), new Card(SIX, HEARTS));
        assertEquals("White wins. - with full house: 6s full of 5s", Main.handResult(hand1, hand2));
    }

    @Test
    void checkFullHouseVsStraightFlush() {
        // Full House
        List<Card> hand1 = List.of(new Card(TWO, SPADES), new Card(TWO, DIAMONDS), new Card(FOUR, HEARTS), new Card(FOUR, SPADES), new Card(FOUR, DIAMONDS));
        // Straight Flush
        List<Card> hand2 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, CLUBS), new Card(NINE, CLUBS));
        assertEquals("White wins. - with straight flush: Clubs, 9-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkFullHouseVsFullHouse() {
        List<Card> hand1 = List.of(new Card(TWO, SPADES), new Card(TWO, DIAMONDS), new Card(FOUR, HEARTS), new Card(FOUR, SPADES), new Card(FOUR, DIAMONDS));
        List<Card> hand2 = List.of(new Card(THREE, CLUBS), new Card(SIX, CLUBS), new Card(SIX, DIAMONDS), new Card(SIX, HEARTS), new Card(THREE, DIAMONDS));
        assertEquals("White wins. - with full house: 6s full of 3s", Main.handResult(hand1, hand2));
    }

    @Test
    void checkStraightFlushVsRoyalFlush() {
        // Straight Flush
        List<Card> hand1 = List.of(new Card(FIVE, CLUBS), new Card(SIX, CLUBS), new Card(SEVEN, CLUBS), new Card(EIGHT, CLUBS), new Card(NINE, CLUBS));
        // Royal Flush
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, HEARTS), new Card(KING, HEARTS), new Card(ACE, HEARTS));
        assertEquals("White wins. - with royal flush: Hearts", Main.handResult(hand1, hand2));
    }

    @Test
    void checkHighCardVsHighCard() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(THREE, SPADES), new Card(FIVE, SPADES), new Card(SEVEN, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(TWO, HEARTS), new Card(FIVE, HEARTS), new Card(SEVEN, DIAMONDS));
        assertEquals("Black wins. - with high card: Queen", Main.handResult(hand1, hand2));
    }

    @Test
    void checkHighCardAce() {
        List<Card> hand1 = List.of(new Card(ACE, DIAMONDS), new Card(THREE, SPADES), new Card(FIVE, SPADES), new Card(SEVEN, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(TWO, HEARTS), new Card(FIVE, HEARTS), new Card(SEVEN, DIAMONDS));
        assertEquals("Black wins. - with high card: Ace", Main.handResult(hand1, hand2));
    }

    @Test
    void checkHighCardFinalCard() {
        List<Card> hand1 = List.of(new Card(ACE, DIAMONDS), new Card(KING, SPADES), new Card(QUEEN, SPADES), new Card(JACK, CLUBS), new Card(NINE, DIAMONDS));
        List<Card> hand2 = List.of(new Card(ACE, HEARTS), new Card(KING, HEARTS), new Card(QUEEN, HEARTS), new Card(JACK, HEARTS), new Card(EIGHT, DIAMONDS));
        assertEquals("Black wins. - with high card: 9", Main.handResult(hand1, hand2));
    }

    @Test
    void checkHighCardsTie() {
        List<Card> hand1 = List.of(new Card(ACE, DIAMONDS), new Card(KING, SPADES), new Card(QUEEN, SPADES), new Card(JACK, CLUBS), new Card(NINE, DIAMONDS));
        List<Card> hand2 = List.of(new Card(ACE, HEARTS), new Card(KING, HEARTS), new Card(QUEEN, HEARTS), new Card(JACK, HEARTS), new Card(NINE, CLUBS));
        assertEquals("Tie.", Main.handResult(hand1, hand2));
    }

    @Test
    void checkPairVsPair() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(THREE, SPADES), new Card(EIGHT, SPADES), new Card(SEVEN, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(TWO, HEARTS), new Card(FIVE, HEARTS), new Card(TEN, DIAMONDS));
        assertEquals("White wins. - with one pair: 10s", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTiedPairs() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(JACK, SPADES), new Card(EIGHT, SPADES), new Card(TEN, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(QUEEN, HEARTS), new Card(JACK, HEARTS), new Card(EIGHT, HEARTS), new Card(EIGHT, CLUBS), new Card(TEN, DIAMONDS));
        assertEquals("Tie.", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTiedPairsKickerPlays() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(THREE, SPADES), new Card(EIGHT, SPADES), new Card(SEVEN, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(FOUR, HEARTS), new Card(JACK, HEARTS), new Card(EIGHT, HEARTS), new Card(EIGHT, CLUBS), new Card(TEN, DIAMONDS));
        assertEquals("Black wins. - with one pair: 8s - Queen kicker", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTiedPairsLastKickerPlays() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(THREE, SPADES), new Card(EIGHT, SPADES), new Card(TEN, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(FOUR, HEARTS), new Card(QUEEN, HEARTS), new Card(EIGHT, HEARTS), new Card(EIGHT, CLUBS), new Card(TEN, DIAMONDS));
        assertEquals("White wins. - with one pair: 8s - 4 kicker", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTripsVsTrips() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(THREE, SPADES), new Card(EIGHT, SPADES), new Card(EIGHT, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(TEN, HEARTS), new Card(JACK, HEARTS), new Card(TWO, HEARTS), new Card(TEN, HEARTS), new Card(TEN, DIAMONDS));
        assertEquals("White wins. - with three of a kind: 10s", Main.handResult(hand1, hand2));
    }


    @Test
    void checkStraightOverStraight() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(JACK, SPADES), new Card(TEN, SPADES), new Card(NINE, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(KING, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, HEARTS), new Card(NINE, HEARTS), new Card(TEN, DIAMONDS));
        assertEquals("White wins. - with straight: King-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTiedStraights() {
        List<Card> hand1 = List.of(new Card(QUEEN, DIAMONDS), new Card(JACK, SPADES), new Card(TEN, SPADES), new Card(NINE, CLUBS), new Card(EIGHT, DIAMONDS));
        List<Card> hand2 = List.of(new Card(QUEEN, HEARTS), new Card(JACK, HEARTS), new Card(TEN, HEARTS), new Card(NINE, HEARTS), new Card(EIGHT, CLUBS));
        assertEquals("Tie.", Main.handResult(hand1, hand2));
    }

    @Test
    void checkFlushOverFlush() {
        List<Card> hand1 = List.of(new Card(QUEEN, SPADES), new Card(JACK, SPADES), new Card(TEN, SPADES), new Card(NINE, SPADES), new Card(SEVEN, SPADES));
        List<Card> hand2 = List.of(new Card(KING, DIAMONDS), new Card(JACK, DIAMONDS), new Card(QUEEN, DIAMONDS), new Card(NINE, DIAMONDS), new Card(SIX, DIAMONDS));
        assertEquals("White wins. - with flush: Diamonds, King-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkFlushOverFlushFinalCard() {
        List<Card> hand1 = List.of(new Card(QUEEN, SPADES), new Card(JACK, SPADES), new Card(TEN, SPADES), new Card(NINE, SPADES), new Card(SEVEN, SPADES));
        List<Card> hand2 = List.of(new Card(QUEEN, DIAMONDS), new Card(JACK, DIAMONDS), new Card(TEN, DIAMONDS), new Card(NINE, DIAMONDS), new Card(SIX, DIAMONDS));
        assertEquals("Black wins. - with flush: Spades, Queen-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkStraightFlushOverStraightFlush() {
        List<Card> hand1 = List.of(new Card(QUEEN, SPADES), new Card(JACK, SPADES), new Card(TEN, SPADES), new Card(NINE, SPADES), new Card(EIGHT, SPADES));
        List<Card> hand2 = List.of(new Card(KING, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, HEARTS), new Card(NINE, HEARTS), new Card(TEN, HEARTS));
        assertEquals("White wins. - with straight flush: Hearts, King-high", Main.handResult(hand1, hand2));
    }

    @Test
    void checkTiedStraightFlushes() {
        List<Card> hand1 = List.of(new Card(QUEEN, SPADES), new Card(JACK, SPADES), new Card(TEN, SPADES), new Card(NINE, SPADES), new Card(EIGHT, SPADES));
        List<Card> hand2 = List.of(new Card(QUEEN, HEARTS), new Card(JACK, HEARTS), new Card(EIGHT, HEARTS), new Card(NINE, HEARTS), new Card(TEN, HEARTS));
        assertEquals("Tie.", Main.handResult(hand1, hand2));
    }

    @Test
    void checkRoyalFlushOverStraightFlush() {
        List<Card> hand1 = List.of(new Card(QUEEN, SPADES), new Card(JACK, SPADES), new Card(TEN, SPADES), new Card(KING, SPADES), new Card(ACE, SPADES));
        List<Card> hand2 = List.of(new Card(KING, HEARTS), new Card(JACK, HEARTS), new Card(QUEEN, HEARTS), new Card(NINE, HEARTS), new Card(TEN, HEARTS));
        assertEquals("Black wins. - with royal flush: Spades", Main.handResult(hand1, hand2));
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