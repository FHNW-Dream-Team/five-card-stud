package com.orbitrondev.Model;

public class Card {
    public enum Suit { Clubs, Diamonds, Hearts, Spades;
        @Override
        public String toString() {
            String suit = "";
            switch (this) {
            case Clubs: suit = "clubs"; break;
            case Diamonds: suit = "diamonds"; break;
            case Hearts: suit = "hearts"; break;
            case Spades: suit = "spades"; break;
            }
            return suit;
        }
    };
    
    public enum Rank { Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace;
        @Override
        public String toString() {
            String str = "ace";  // Assume we have an ace, then cover all other cases
            // Get ordinal value, which ranges from 0 to 12
            int ordinal = this.ordinal();
            if (ordinal <= 8) {
                str = Integer.toString(ordinal+2);
            } else if (ordinal == 9) { // Jack
                str = "jack";
            } else if (ordinal == 10) { // Queen
                str = "queen";
            } else if (ordinal == 11) { // King
                str = "king";
            }
            return str;
        }
    };
    
    private final Suit suit;
    private final Rank rank;
    
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }
    
    @Override
    public String toString() {
        return rank.toString() + suit.toString();
    }
}
