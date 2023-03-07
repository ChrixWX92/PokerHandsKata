package pokerhands.handgenerator.cards;

public enum Rank {

    ACE(1, "Ace", 14, 'A'),
    ONE(15, "One", 1, '1'),
    TWO(2, "Two", 2, '2'),
    THREE(3, "Three", 3, '3'),
    FOUR(4, "Four", 4, '4'),
    FIVE(5, "Five", 5, '5'),
    SIX(6, "Six", 6, '6'),
    SEVEN(7, "Seven", 7, '7'),
    EIGHT(8, "Eight", 8, '8'),
    NINE(9, "Nine", 9, '9'),
    TEN(10, "Ten", 10, 'T'),
    JACK(11, "Jack", 11, 'J'),
    KNIGHT(12, "Knight", 11, 'N'), // ??
    QUEEN(13, "Queen", 12, 'Q'),
    KING(14, "King", 13, 'K'),
    JOKER(16, "Joker", 15, '?');

    public final int id;
    public final String name;
    public int value;
    public int priority; // Higher = higher priority
    public int character;

    /** Values should ideally be dynamic, dependent on context, game, deck, etc.**/
    Rank(int id, String name, int value, char character) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.priority = this.value - 1;
        this.character = character;
    }

    public static Rank getByValue(int value) {
        for (Rank rank : Rank.values()) {
            if (rank.value == value) {
                return rank;
            }
        }
        return null;
    }

    public static Rank getByCharacter(char character) {
        for (Rank rank : Rank.values()) {
            if (rank.character == character) {
                return rank;
            }
        }
        return null;
    }

    public Rank getAbove() {
        return getAbove(true);
    }

    public Rank getAbove(boolean standardStraightRules) {
        if (standardStraightRules) {
            if (this == ACE) {return TWO;}
        }
        return getByValue(this.value + 1);
    }

    public Rank getBelow(boolean standardStraightRules) {
        if (standardStraightRules) {
            if (this == ACE) {return KING;}
        }
        return getByValue(this.value-1);
    }

}
