package com.orbitrondev.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class HandTypeTest {

    // We define the hands using abbreviations. The code at the bottom
    // of this class can translate one of these strings into a card.
    //
    // Another method takes a set of five cards, and translates the whole hand
    //
    // Yet another method does this for a whole set of hands
    private static String[][] highCards = {
        {"2S", "9C", "3H", "5D", "7H"},
        {"7S", "5C", "AH", "JD", "6H"},
        {"2D", "3S", "4S", "5S", "7S"},
        {"AS", "KC", "QH", "JD", "8H"}
    };

    private static String[][] pairs = {
        {"2S", "2C", "3H", "5D", "7H"}, // A pair in order
        {"2S", "AC", "3H", "5D", "AH"}, // A pair in a random position
        {"3S", "2C", "3H", "KD", "QH"}, // A pair in a random position
        {"9S", "2C", "2H", "5D", "7H"} // A pair in a random position
    };

    private static String[][] twoPairs = {
        {"2S", "2C", "7H", "7D", "5H"}, // Two pairs in order
        {"AS", "AC", "5H", "TD", "TH"}, // A space between two pairs
        {"3S", "2C", "3H", "2D", "QH"}, // Two pairs in random position
        {"5D", "2C", "2H", "9H", "5H"} // Two pairs in random position
    };

    private static String[][] threeOfAKind = {
        {"2S", "2C", "2H", "5D", "7H"}, // A three-of-a-king in order
        {"2S", "AC", "5S", "5D", "5H"}, // A three-of-a-king in the end
        {"3S", "2C", "3H", "3D", "QH"}, // A three-of-a-king in random position
        {"9S", "9C", "4H", "5D", "9H"}  // A three-of-a-king in random position
    };

    private static String[][] straight = {
        {"2S", "3C", "4H", "5D", "6H"}, // A simple straight
        {"5S", "6C", "7H", "8D", "9H"}, // Another simple straight
        {"TS", "JC", "QH", "KD", "AH"}, // High straight 10-Ace in order
        {"KS", "JS", "AH", "QD", "TH"}, // High straight 10-Ace with mixed up position
        {"AS", "2C", "3H", "4D", "5H"}, // Low straight Ace-5 in order
        {"3S", "AC", "5H", "4D", "2H"}  // Low straight Ace-5 with mixed up position
    };

    private static String[][] flush = {
        {"2S", "9S", "3S", "5S", "7S"}, // A flush with all spades
        {"7C", "5C", "AC", "JC", "6C"}, // A flush with all clubs
        {"2D", "3D", "4D", "5D", "7D"}, // A flush with all diamonds
        {"AH", "KH", "9H", "JH", "TH"}  // A flush with all hearts
    };

    private static String[][] fullHouse = {
        {"2S", "2C", "2H", "7D", "7H"}, // First a three-of-a-kind in order followed by a pair in order
        {"AS", "AC", "5S", "5D", "5H"}, // First a pair in order followed by a three-of-a-kind in order
        {"3S", "2C", "3H", "2D", "3D"}, // Three-of-a-kind and pair with mixed up positions
        {"9S", "9C", "5H", "5D", "9H"}  // Three-of-a-kind and pair with mixed up positions
    };

    private static String[][] fourOfAKind = {
        {"2S", "2C", "2H", "2D", "7H"}, // A four-of-a-kind in order
        {"2S", "5C", "5S", "5D", "5H"}, // A four-of-a-kind in order at the end
        {"3S", "3C", "4H", "3D", "3H"}, // A four-of-a-kind in order with mixed up positions
        {"9S", "5C", "9H", "9D", "9C"}  // A four-of-a-kind in order with mixed up positions
    };

    private static String[][] straightFlush = {
        {"2S", "3S", "4S", "5S", "6S"}, // Is a straight in order with all spades
        {"4S", "3S", "6S", "5S", "2S"}, // Is a straight with mixed up positions with all spades
        {"5H", "6H", "7H", "8H", "9H"}, // Is a straight in order with all hearts
        {"7H", "9H", "5H", "8H", "6H"}, // Is a straight with mixed up positions with all hearts
        {"TC", "JC", "QC", "KC", "AC"}, // Is a straight in order with all clubs
        {"AC", "QC", "JC", "TC", "KC"}, // Is a straight with mixed up positions with all clubs
        {"AD", "2D", "3D", "4D", "5D"}, // Is a straight in order with all diamonds
        {"2D", "5D", "AD", "3D", "4D"}  // Is a straight with mixed up positions with all diamonds
    };

    // This is where we store the translated hands
    private ArrayList<ArrayList<Card>> highCardHands;
    private ArrayList<ArrayList<Card>> pairHands;
    private ArrayList<ArrayList<Card>> twoPairHands;
    private ArrayList<ArrayList<Card>> threeOfAKindHands;
    private ArrayList<ArrayList<Card>> straightHands;
    private ArrayList<ArrayList<Card>> flushHands;
    private ArrayList<ArrayList<Card>> fullHouseHands;
    private ArrayList<ArrayList<Card>> fourOfAKindHands;
    private ArrayList<ArrayList<Card>> straightFlushHands;

    /**
     * Make an ArrayList of hands from an array of string-arrays
     */
    private ArrayList<ArrayList<Card>> makeHands(String[][] handsIn) {
        ArrayList<ArrayList<Card>> handsOut = new ArrayList<>();
        for (String[] hand : handsIn) {
            handsOut.add(makeHand(hand));
        }
        return handsOut;
    }

    /**
     * Make a hand (ArrayList<Card>) from an array of 5 strings
     */
    private ArrayList<Card> makeHand(String[] inStrings) {
        ArrayList<Card> hand = new ArrayList<>();
        for (String in : inStrings) {
            hand.add(makeCard(in));
        }
        return hand;
    }

    /**
     * Create a card from a 2-character String.
     * First character is the rank (2-9, T, J, Q, K, A)
     * Second character is the suit (C, D, H, S)
     * <p>
     * No validation or error handling!
     */
    private Card makeCard(String in) {
        char r = in.charAt(0);
        Card.Rank rank = null;
        if (r <= '9') rank = Card.Rank.values()[r - '0' - 2];
        else if (r == 'T') rank = Card.Rank.Ten;
        else if (r == 'J') rank = Card.Rank.Jack;
        else if (r == 'Q') rank = Card.Rank.Queen;
        else if (r == 'K') rank = Card.Rank.King;
        else if (r == 'A') rank = Card.Rank.Ace;

        char s = in.charAt(1);
        Card.Suit suit = null;
        if (s == 'C') suit = Card.Suit.Clubs;
        if (s == 'D') suit = Card.Suit.Diamonds;
        if (s == 'H') suit = Card.Suit.Hearts;
        if (s == 'S') suit = Card.Suit.Spades;

        return new Card(suit, rank);
    }

    /**
     * The setUp (makeHands) method is called before each test method,
     * and prepares the translated hands. We recreate these for
     * each test method, in case the test method damages the data.
     */
    @Before
    public void setUp() throws Exception {
        highCardHands = makeHands(highCards);
        pairHands = makeHands(pairs);
        twoPairHands = makeHands(twoPairs);
        threeOfAKindHands = makeHands(threeOfAKind);
        straightHands = makeHands(straight);
        flushHands = makeHands(flush);
        fullHouseHands = makeHands(fullHouse);
        fourOfAKindHands = makeHands(fourOfAKind);
        straightFlushHands = makeHands(straightFlush);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void evaluateHand() {
    }

    /**
     * This is a test method for the isOnePair method in HandType.
     * We expect all HighCard hands to be false, all OnePair hands to
     * be true, all TwoPair hands to be true, etc.
     */

    @Test
    public void isOnePair() {
        for (ArrayList<Card> hand : highCardHands) {
            assertFalse(HandType.isOnePair(hand));
        }
        for (ArrayList<Card> hand : pairHands) {
            assertTrue(HandType.isOnePair(hand)); // There is one pair inside a pair hand
        }
        for (ArrayList<Card> hand : twoPairHands) {
            assertTrue(HandType.isOnePair(hand)); // There is a one pair inside a two pair hand
        }
        for (ArrayList<Card> hand : threeOfAKindHands) {
            assertTrue(HandType.isOnePair(hand)); // There is a one pair inside a three of a kind
        }
        for (ArrayList<Card> hand : straightHands) {
            assertFalse(HandType.isOnePair(hand));
        }
        for (ArrayList<Card> hand : flushHands) {
            assertFalse(HandType.isOnePair(hand));
        }
        for (ArrayList<Card> hand : fullHouseHands) {
            assertTrue(HandType.isOnePair(hand)); // There is a one pair inside a full house hand
        }
        for (ArrayList<Card> hand : fourOfAKindHands) {
            assertTrue(HandType.isOnePair(hand)); // There is a one pair inside a four of a kind hand
        }
        for (ArrayList<Card> hand : straightFlushHands) {
            assertFalse(HandType.isOnePair(hand));
        }
    }

    /**
     * This is the test method for the isTwoPair in HandType.
     */

    @Test
    public void isTwoPair() {
        for (ArrayList<Card> hand : highCardHands) {
            assertFalse(HandType.isTwoPair(hand));
        }
        for (ArrayList<Card> hand : pairHands) {
            assertFalse(HandType.isTwoPair(hand));
        }
        for (ArrayList<Card> hand : twoPairHands) {
            assertTrue(HandType.isTwoPair(hand));
        }
        for (ArrayList<Card> hand : threeOfAKindHands) {
            assertFalse(HandType.isTwoPair(hand));
        }
        for (ArrayList<Card> hand : straightHands) {
            assertFalse(HandType.isTwoPair(hand));
        }
        for (ArrayList<Card> hand : flushHands) {
            assertFalse(HandType.isTwoPair(hand));
        }
        for (ArrayList<Card> hand : fullHouseHands) {
            assertTrue(HandType.isTwoPair(hand));
        }
        for (ArrayList<Card> hand : fourOfAKindHands) {
            assertTrue(HandType.isTwoPair(hand));
        }
        for (ArrayList<Card> hand : straightFlushHands) {
            assertFalse(HandType.isTwoPair(hand));
        }
    }

    @Test
    public void isThreeOfAKind() {
        for (ArrayList<Card> hand : highCardHands) {
            assertFalse(HandType.isThreeOfAKind(hand));
        }
        for (ArrayList<Card> hand : pairHands) {
            assertFalse(HandType.isThreeOfAKind(hand));
        }
        for (ArrayList<Card> hand : twoPairHands) {
            assertFalse(HandType.isThreeOfAKind(hand));
        }
        for (ArrayList<Card> hand : threeOfAKindHands) {
            assertTrue(HandType.isThreeOfAKind(hand));
        }
        for (ArrayList<Card> hand : straightHands) {
            assertFalse(HandType.isThreeOfAKind(hand));
        }
        for (ArrayList<Card> hand : flushHands) {
            assertFalse(HandType.isThreeOfAKind(hand));
        }
        for (ArrayList<Card> hand : fullHouseHands) {
            assertTrue(HandType.isThreeOfAKind(hand));
        }
        for (ArrayList<Card> hand : fourOfAKindHands) {
            assertTrue(HandType.isThreeOfAKind(hand));
        }
        for (ArrayList<Card> hand : straightFlushHands) {
            assertFalse(HandType.isThreeOfAKind(hand));
        }
    }

    @Test
    public void isStraight() {
        for (ArrayList<Card> hand : highCardHands) {
            assertFalse(HandType.isStraight(hand));
        }
        for (ArrayList<Card> hand : pairHands) {
            assertFalse(HandType.isStraight(hand));
        }
        for (ArrayList<Card> hand : twoPairHands) {
            assertFalse(HandType.isStraight(hand));
        }
        for (ArrayList<Card> hand : threeOfAKindHands) {
            assertFalse(HandType.isStraight(hand));
        }
        for (ArrayList<Card> hand : straightHands) {
            assertTrue(HandType.isStraight(hand));
        }
        for (ArrayList<Card> hand : flushHands) {
            assertFalse(HandType.isStraight(hand));
        }
        for (ArrayList<Card> hand : fullHouseHands) {
            assertFalse(HandType.isStraight(hand));
        }
        for (ArrayList<Card> hand : fourOfAKindHands) {
            assertFalse(HandType.isStraight(hand));
        }
        for (ArrayList<Card> hand : straightFlushHands) {
            assertTrue(HandType.isStraight(hand));
        }
    }

    @Test
    public void isFlush() {
        for (ArrayList<Card> hand : highCardHands) {
            assertFalse(HandType.isFlush(hand));
        }
        for (ArrayList<Card> hand : pairHands) {
            assertFalse(HandType.isFlush(hand));
        }
        for (ArrayList<Card> hand : twoPairHands) {
            assertFalse(HandType.isFlush(hand));
        }
        for (ArrayList<Card> hand : threeOfAKindHands) {
            assertFalse(HandType.isFlush(hand));
        }
        for (ArrayList<Card> hand : straightHands) {
            assertFalse(HandType.isFlush(hand));
        }
        for (ArrayList<Card> hand : flushHands) {
            assertTrue(HandType.isFlush(hand));
        }
        for (ArrayList<Card> hand : fullHouseHands) {
            assertFalse(HandType.isFlush(hand));
        }
        for (ArrayList<Card> hand : fourOfAKindHands) {
            assertFalse(HandType.isFlush(hand));
        }
        for (ArrayList<Card> hand : straightFlushHands) {
            assertTrue(HandType.isFlush(hand));
        }
    }

    @Test
    public void isFullHouse() {
        for (ArrayList<Card> hand : highCardHands) {
            assertFalse(HandType.isFullHouse(hand));
        }
        for (ArrayList<Card> hand : pairHands) {
            assertFalse(HandType.isFullHouse(hand));
        }
        for (ArrayList<Card> hand : twoPairHands) {
            assertFalse(HandType.isFullHouse(hand));
        }
        for (ArrayList<Card> hand : threeOfAKindHands) {
            assertFalse(HandType.isFullHouse(hand));
        }
        for (ArrayList<Card> hand : straightHands) {
            assertFalse(HandType.isFullHouse(hand));
        }
        for (ArrayList<Card> hand : flushHands) {
            assertFalse(HandType.isFullHouse(hand));
        }
        for (ArrayList<Card> hand : fullHouseHands) {
            assertTrue(HandType.isFullHouse(hand));
        }
        for (ArrayList<Card> hand : fourOfAKindHands) {
            assertFalse(HandType.isFullHouse(hand));
        }
        for (ArrayList<Card> hand : straightFlushHands) {
            assertFalse(HandType.isFullHouse(hand));
        }
    }

    @Test
    public void isFourOfAKind() {
        for (ArrayList<Card> hand : highCardHands) {
            assertFalse(HandType.isFourOfAKind(hand));
        }
        for (ArrayList<Card> hand : pairHands) {
            assertFalse(HandType.isFourOfAKind(hand));
        }
        for (ArrayList<Card> hand : twoPairHands) {
            assertFalse(HandType.isFourOfAKind(hand));
        }
        for (ArrayList<Card> hand : threeOfAKindHands) {
            assertFalse(HandType.isFourOfAKind(hand));
        }
        for (ArrayList<Card> hand : straightHands) {
            assertFalse(HandType.isFourOfAKind(hand));
        }
        for (ArrayList<Card> hand : flushHands) {
            assertFalse(HandType.isFourOfAKind(hand));
        }
        for (ArrayList<Card> hand : fullHouseHands) {
            assertFalse(HandType.isFourOfAKind(hand));
        }
        for (ArrayList<Card> hand : fourOfAKindHands) {
            assertTrue(HandType.isFourOfAKind(hand));
        }
        for (ArrayList<Card> hand : straightFlushHands) {
            assertFalse(HandType.isFourOfAKind(hand));
        }
    }

    @Test
    public void isStraightFlush() {
        for (ArrayList<Card> hand : highCardHands) {
            assertFalse(HandType.isStraightFlush(hand));
        }
        for (ArrayList<Card> hand : pairHands) {
            assertFalse(HandType.isStraightFlush(hand));
        }
        for (ArrayList<Card> hand : twoPairHands) {
            assertFalse(HandType.isStraightFlush(hand));
        }
        for (ArrayList<Card> hand : threeOfAKindHands) {
            assertFalse(HandType.isStraightFlush(hand));
        }
        for (ArrayList<Card> hand : straightHands) {
            assertFalse(HandType.isStraightFlush(hand));
        }
        for (ArrayList<Card> hand : flushHands) {
            assertFalse(HandType.isStraightFlush(hand));
        }
        for (ArrayList<Card> hand : fullHouseHands) {
            assertFalse(HandType.isStraightFlush(hand));
        }
        for (ArrayList<Card> hand : fourOfAKindHands) {
            assertFalse(HandType.isStraightFlush(hand));
        }
        for (ArrayList<Card> hand : straightFlushHands) {
            assertTrue(HandType.isStraightFlush(hand));
        }
    }
}
