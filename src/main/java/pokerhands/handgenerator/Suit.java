package pokerhands.handgenerator;

public enum Suit {

    HEARTS(1, "Hearts", '♥', "U+DCB", 3),
    CLUBS(2, "Clubs", '♣', "U+DCD", 1),
    DIAMONDS(3, "Diamonds", '♦', "U+DCC", 2),
    SPADES(4, "Spades", '♠', "U+DCA", 4);

    public final int id; // Default. Used for ordering in a deck.
    public final String name;
    public final char symbol;
    public final String codepointStub; // Unicode
    public final int priority; // Higher = higher priority

    Suit(int id, String name, char symbol, String codepointStub, int priority) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.codepointStub = codepointStub;
        this.priority = priority;
    }

}
