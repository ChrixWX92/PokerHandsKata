package pokerhands.handgenerator.cards;

public enum Suit {

    HEARTS(1, "Hearts", '♥', "U+DCB", 3, 'H'),
    CLUBS(2, "Clubs", '♣', "U+DCD", 1, 'C'),
    DIAMONDS(3, "Diamonds", '♦', "U+DCC", 2, 'D'),
    SPADES(4, "Spades", '♠', "U+DCA", 4, 'S');

    public final int id; // Default. Used for ordering in a deck.
    public final String name;
    public final char symbol;
    public final String codepointStub; // Unicode
    public final int priority; // Higher = higher priority
    public final char character;

    Suit(int id, String name, char symbol, String codepointStub, int priority, char character) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.codepointStub = codepointStub;
        this.priority = priority;
        this.character = character;
    }

    public static Suit getByCharacter(char character) {
        for (Suit rank : Suit.values()) {
            if (rank.character == character) {
                return rank;
            }
        }
        return null;
    }

}
