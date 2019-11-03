package com.orbitrondev.Model;

import java.util.ArrayList;

import com.orbitrondev.PokerGame;

public class PokerGameModel {
    private final ArrayList<Player> players = new ArrayList<>();
    private DeckOfCards deck;

    // So the first generated name starts with 1
    private int lastGeneratedNumberForName = 0;

    public PokerGameModel() {
        deck = new DeckOfCards(); // When initiating instantly create a deck with 52 cards
    }

    /**
     * Add a player to the "model"
     */
    public Player addPlayer(String name) {
        // Stop if max players reached
        if (players.size() == PokerGame.MAX_PLAYERS) {
            return null;
        }

        Player newPlayer;
        if (name == null || name.length() == 0) {
            // Add player with a generated name
            lastGeneratedNumberForName += 1;
            newPlayer = new Player("Player " + lastGeneratedNumberForName);
        } else {
            // Add player with a given name
            newPlayer = new Player(name);
        }

        players.add(newPlayer);
        return newPlayer;
    }

    /**
     * Get one player by their index number inside the ArrayList
     */
    public Player getPlayer(int i) {
        return players.get(i);
    }

    /**
     * Get an ArrayList with all the players inside
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Removes player and returns true if successful otherwise false
     */
    public boolean removePlayer(Player playerToDelete) {
        for (Player player : players) {
            if (player != playerToDelete) continue;
            players.remove(player);
            return true;
        }
        return false;
    }

    /**
     * Get a number of how many players are present
     */
    public int getPlayerCount() {
        return players.size();
    }

    public DeckOfCards getDeck() {
        return deck;
    }
}
