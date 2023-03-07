package pokerhands.handgenerator.cards;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    @Getter @Setter
    List<Card> cards;

    public Deck() {
        this(DeckType.STANDARD);
    }

    public Deck(DeckType type) {
        cards = new ArrayList<>();
        switch (type) {
            case STANDARD -> {
                for (Suit suit : Suit.values()) {
                    for (Rank rank : Rank.values()) {
                        if (rank.id == 12 || rank.id > 14) continue;
                        cards.add(new Card(rank, suit));
                    }
                }
            }
        }
    }

    public Card draw() {
        Card topCard = this.cards.get(0);
        this.cards.remove(0);
        return topCard;
    }

    public List<Card> shuffle() {
        Collections.shuffle(this.cards);
        return this.cards;
    }

    public enum DeckType {
        STANDARD
    }

}
