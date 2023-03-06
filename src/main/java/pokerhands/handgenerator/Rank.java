package pokerhands.handgenerator;

public enum Rank {

    ACE(1, "Ace", 14),
    ONE(15, "One", 1),
    TWO(2, "Two", 2),
    THREE(3, "Three", 3),
    FOUR(4, "Four", 4),
    FIVE(5, "Five", 5),
    SIX(6, "Six", 6),
    SEVEN(7, "Seven", 7),
    EIGHT(8, "Eight", 8),
    NINE(9, "Nine", 9),
    TEN(10, "Ten", 10),
    JACK(11, "Jack", 11),
    KNIGHT(12, "Knight", 11), // ??
    QUEEN(13, "Queen", 12),
    KING(14, "King", 13),
    JOKER(16, "Joker", 15);

    public final int id;
    public final String name;
    public int value;
    public int priority; // Higher = higher priority

    /** Values should ideally be dynamic, dependent on context, game, deck, etc.**/
    Rank(int id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.priority = this.value - 1;
    }

    public static Rank getByValue(int value) {
        for (Rank rank : Rank.values()) {
            if (rank.value == value) {
                return rank;
            }
        }
        return null;
    }

    public Rank getAbove() { return getByValue(this.value+1); }
    public Rank getBelow() { return getByValue(this.value-1); }

}
