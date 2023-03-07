package pokerhands.handgenerator.cards;

import lombok.Getter;
import org.javatuples.Pair;
import pokerhands.handgenerator.utils.CardUtils;

import java.util.Map;

public class Card implements Comparable<Card> {

    @Getter
    Rank rank;
    @Getter
    Suit suit;
    @Getter
    String name;
    @Getter
    public static Map<Pair<Integer, Integer>, String> characters = Map.ofEntries(CardUtils.getCharacters());

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.name = rank.name + " of " + suit.name;
    }

    @Override
    public int compareTo(Card o) {
        if (this.rank.value < o.rank.value) return -1;
        else if (this.rank.value > o.rank.value) return 1;
        else return Integer.compare(this.suit.priority, o.suit.priority);
    }

    @Override
    public String toString() {
        return name;
    }

}
