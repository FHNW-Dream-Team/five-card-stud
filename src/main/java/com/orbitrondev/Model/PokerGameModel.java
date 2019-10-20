package com.orbitrondev.Model;

import java.util.ArrayList;

import com.orbitrondev.PokerGame;

public class PokerGameModel {
    private final ArrayList<Player> players = new ArrayList<>();
    private DeckOfCards deck;

    // so the first generated name starts with 1
    private int lastGeneratedNumberForName = 0;

    public PokerGameModel() {
        deck = new DeckOfCards();
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
            // add player with a generated name
            lastGeneratedNumberForName += 1;
            newPlayer = new Player("Player " + lastGeneratedNumberForName);
        } else {
            // add player with a given name
            newPlayer = new Player(name);
        }

        players.add(newPlayer);
        return newPlayer;
    }

    public Player getPlayer(int i) {
        return players.get(i);
    }

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

    public int getPlayerCount() {
        return players.size();
    }

    public DeckOfCards getDeck() {
        return deck;
    }
}
