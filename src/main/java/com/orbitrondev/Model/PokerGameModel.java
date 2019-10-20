package com.orbitrondev.Model;

import java.util.ArrayList;

import com.orbitrondev.PokerGame;

public class PokerGameModel {
    private final ArrayList<Player> players = new ArrayList<>();
    private DeckOfCards deck;
    private int lastGeneratedNumberForName = 0; // so the first name starts with 1

    public PokerGameModel() {
        for (int i = 0; i < PokerGame.MIN_PLAYERS; i++) {
            addPlayer(null);
        }

        deck = new DeckOfCards();
    }

    public Player addPlayer(String name) {
        if (players.size() == PokerGame.MAX_PLAYERS) { // stop if max players reached
            return null;
        }

        Player newPlayer = null;
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

    public int getPlayerCount() {
        return players.size();
    }

    public DeckOfCards getDeck() {
        return deck;
    }
}
