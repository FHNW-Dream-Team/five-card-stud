package com.orbitrondev.Model;

import java.util.ArrayList;

public enum HandType {
    HighCard, OnePair, TwoPair, ThreeOfAKind, Straight, Flush, FullHouse, FourOfAKind, StraightFlush;

    private static ArrayList<Card> sortCards(ArrayList<Card> unsortedCards) {
        ArrayList<Card> sortedCards = new ArrayList<>();

        for (Card card : unsortedCards) {
            if (sortedCards.size() == 0) {
                sortedCards.add(card);
            } else {
                for (Card cardToCompareTo : sortedCards) {
                    if (cardToCompareTo.getRank().ordinal() > card.getRank().ordinal()) {
                        sortedCards.add(sortedCards.indexOf(cardToCompareTo), card);
                        break;

                    } else if (cardToCompareTo.getRank().ordinal() < card.getRank().ordinal()) {
                        if (sortedCards.indexOf(cardToCompareTo) == (sortedCards.size() - 1)) { // If we are checking the last element, add it at the end
                            sortedCards.add(sortedCards.indexOf(cardToCompareTo) + 1, card);
                            break;
                        }
                        // If the element is still higher, go to next loop

                    } else if (cardToCompareTo.getRank().ordinal() == card.getRank().ordinal()) { // If cards are the same rank just put it in there
                        sortedCards.add(sortedCards.indexOf(cardToCompareTo), card);
                        break;
                    }
                }
            }
        }
        return sortedCards;
    }

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

        //Find three cards of the same rank by comparing them
        boolean threeOfAKindFound = false;
        for (int i = 0; i < cards.size() - 2 && !threeOfAKindFound; i++) {
            for (int j = i + 1; j < cards.size() - 1 && !threeOfAKindFound; j++) {
                for (int z = j + 1; z < cards.size() && !threeOfAKindFound; z++) {
                    if (cards.get(i).getRank() == cards.get(j).getRank() && cards.get(i).getRank() == cards.get(z).getRank()) {
                        threeOfAKindFound = true;
                    }
                }
            }
        }
        return threeOfAKindFound;
    }

    public static boolean isStraight(ArrayList<Card> cards) { //rank in a row (exp. 1,2,3,4,5) Ace is after the King - all 5 cards)

        ArrayList<Card> sortedCards = sortCards(cards);

        Card previousCard = null;
        for (Card card : sortedCards) {
            if (previousCard == null ||
                card.getRank().isNext(previousCard) ||
                (card.getRank().equals(Card.Rank.Ace) && sortedCards.get(0).getRank() == Card.Rank.Two) // Handle the case when we have a low straight and an Ace (Ordinal Nr. 12) is included
            ) {
                previousCard = card;
            } else return false;
        }
        return true;
    }

    /**
     * Determine if all cards are of the same suit
     */
    public static boolean isFlush(ArrayList<Card> cards) {

        if (cards.get(0).getSuit() == cards.get(1).getSuit() &&
            cards.get(0).getSuit() == cards.get(2).getSuit() &&
            cards.get(0).getSuit() == cards.get(3).getSuit() &&
            cards.get(0).getSuit() == cards.get(4).getSuit()) {
            return true;
        }
        return false;
    }

    /**
     * Determine if there is a three of a kind and a pair
     */
    public static boolean isFullHouse(ArrayList<Card> cards) {
        ArrayList<Card> clonedCards = (ArrayList<Card>) cards.clone();

        //Find three cards of the same rank by comparing them and remove cards if three of a kind is found
        boolean threeOfAKindFound = false;
        for (int i = 0; i < clonedCards.size() - 2 && !threeOfAKindFound; i++) {
            for (int j = i + 1; j < clonedCards.size() - 1 && !threeOfAKindFound; j++) {
                for (int k = j + 1; k < clonedCards.size() && !threeOfAKindFound; k++) {
                    if (clonedCards.get(i).getRank() == clonedCards.get(j).getRank() && clonedCards.get(i).getRank() == clonedCards.get(k).getRank()) {
                        threeOfAKindFound = true;
                        clonedCards.remove(k); // Delete from behind to forward, otherwise arrays keep getting shifted
                        clonedCards.remove(j);
                        clonedCards.remove(i);
                    }
                }
            }
        }
        return threeOfAKindFound && isOnePair(clonedCards);
    }

    /**
     * Determine if four cards are of the same rank
     */
    public static boolean isFourOfAKind(ArrayList<Card> cards) {

        //Find four cards of the same rank by comparing them
        boolean fourOfAKindFound = false;
        for (int i = 0; i < cards.size() - 3 && !fourOfAKindFound; i++) {
            for (int j = i + 1; j < cards.size() - 2 && !fourOfAKindFound; j++) {
                for (int z = j + 1; z < cards.size() - 1 && !fourOfAKindFound; z++) {
                    for (int x = z + 1; x < cards.size() && !fourOfAKindFound; x++) {
                        if (cards.get(i).getRank() == cards.get(j).getRank() &&
                            cards.get(i).getRank() == cards.get(z).getRank() &&
                            cards.get(i).getRank() == cards.get(x).getRank()) {
                            fourOfAKindFound = true;
                        }
                    }
                }
            }
        }
        return fourOfAKindFound;
    }

    /**
     * Determine if the hand is straight and has the same suit
     */
    public static boolean isStraightFlush(ArrayList<Card> cards) {
        return isStraight(cards) && isFlush(cards);
    }
}
