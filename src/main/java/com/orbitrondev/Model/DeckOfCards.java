package com.orbitrondev.Model;

import java.util.ArrayList;
import java.util.Collections;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class represents a deck of playing cards. When initially created, the deck is filled and shuffled.
 * Later, the deck can be refilled and reshuffled by calling the "shuffle" method.
 */
public class DeckOfCards {
    private final ArrayList<Card> cards = new ArrayList<>();
    private final SimpleIntegerProperty cardsRemaining = new SimpleIntegerProperty();

    /**
     * We only ever have one deck of cards, so we do not set an ID attribute.
     */
    public DeckOfCards() {
        shuffle();
    }

    /**
     * How many cards are left in the deck?
     */
    public SimpleIntegerProperty getCardsRemainingProperty() {
        return cardsRemaining;
    }
    public int getCardsRemaining() {
    	return cardsRemaining.get();
    }

    /**
     * Gather all 52 cards, and shuffle them
     */
    public void shuffle() {
        // Remove all cards
        cards.clear();
        
        // Add all 52 cards
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                Card card = new Card(suit, rank);
                cards.add(card);
            }
        }
        
        // Shuffle
        Collections.shuffle(cards);
        cardsRemaining.setValue(cards.size());
    }

    /**
     * Take one card from the deck and return it
     * 
     * This is an example of conditional assignment
     */
    public Card dealCard() {
        Card card = (cards.size() > 0) ? cards.remove(cards.size()-1) : null;
        cardsRemaining.setValue(cards.size());
        return card;
    }
}
