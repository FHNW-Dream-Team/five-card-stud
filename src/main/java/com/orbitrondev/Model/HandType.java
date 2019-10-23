package com.orbitrondev.Model;

import java.util.ArrayList;

public enum HandType {
    HighCard, OnePair, TwoPair, ThreeOfAKind, Straight, Flush, FullHouse, FourOfAKind, StraightFlush;

    /**
     * Determine the value of this hand. Note that this does not
     * account for any tie-breaking
     */
    public static HandType evaluateHand(ArrayList<Card> cards) {
        HandType currentEval = HighCard;

        if (isOnePair(cards)) currentEval = OnePair;
        if (isTwoPair(cards)) currentEval = TwoPair;
        if (isThreeOfAKind(cards)) currentEval = ThreeOfAKind;
        if (isStraight(cards)) currentEval = Straight;
        if (isFlush(cards)) currentEval = Flush;
        if (isFullHouse(cards)) currentEval = FullHouse;
        if (isFourOfAKind(cards)) currentEval = FourOfAKind;
        if (isStraightFlush(cards)) currentEval = StraightFlush;

        return currentEval;
    }

    public static boolean isOnePair(ArrayList<Card> cards) {
        boolean found = false;
        for (int i = 0; i < cards.size() - 1 && !found; i++) { // go trough all cards except for the last one (you can`t compare it to itself)
            for (int j = i + 1; j < cards.size() && !found; j++) { // go trough all cards except for the first one (which is already compared in the first loop)
                if (cards.get(i).getRank() == cards.get(j).getRank()) found = true;
            }
        }
        return found;
    }

    public static boolean isTwoPair(ArrayList<Card> cards) {
        // Clone the cards, because we will be altering the list
        ArrayList<Card> clonedCards = (ArrayList<Card>) cards.clone();

        // Find the first pair; if found, remove the cards from the list
        boolean firstPairFound = false;
        for (int i = 0; i < clonedCards.size() - 1 && !firstPairFound; i++) {
            for (int j = i + 1; j < clonedCards.size() && !firstPairFound; j++) {
                if (clonedCards.get(i).getRank() == clonedCards.get(j).getRank()) {
                    firstPairFound = true;
                    clonedCards.remove(j);  // Remove the later card
                    clonedCards.remove(i);  // Before the earlier one
                }
            }
        }
        // If a first pair was found, see if there is a second pair
        return firstPairFound && isOnePair(clonedCards);
    }

    public static boolean isThreeOfAKind(ArrayList<Card> cards) { // check for the same rank (3 cards)
        // TODO        
        return false;
    }

    public static boolean isStraight(ArrayList<Card> cards) { //rank in a row (exp. 1,2,3,4,5) Ace is after the King
        // TODO        
        return false;
    }

    public static boolean isFlush(ArrayList<Card> cards) { // 5 of the same suit
        // TODO        
        return false;
    }

    public static boolean isFullHouse(ArrayList<Card> cards) { // three of a kind and a pair
        // TODO        
        return false;
    }

    public static boolean isFourOfAKind(ArrayList<Card> cards) { // check for the same rank (4 cards)
        // TODO        
        return false;
    }

    public static boolean isStraightFlush(ArrayList<Card> cards) { // is straight of the same color / suit (all 5 cards)
        // TODO        
        return false;
    }
}
