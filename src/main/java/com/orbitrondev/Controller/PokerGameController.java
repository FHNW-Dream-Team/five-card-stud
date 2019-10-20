package com.orbitrondev.Controller;

import com.orbitrondev.PokerGame;
import com.orbitrondev.Model.Card;
import com.orbitrondev.Model.DeckOfCards;
import com.orbitrondev.Model.Player;
import com.orbitrondev.Model.PokerGameModel;
import com.orbitrondev.View.PlayerPane;
import com.orbitrondev.View.PokerGameView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class PokerGameController {
    private PokerGameModel model;
    private PokerGameView view;

    public PokerGameController(PokerGameModel model, PokerGameView view) {
        this.model = model;
        this.view = view;

        view.getAddPlayerButton().setOnAction(e -> addPlayer());
        view.getShuffleButton().setOnAction(e -> shuffle());
        view.getDealButton().setOnAction(e -> deal());
    }

    private void addPlayer() {
        if (model.getPlayerCount() == PokerGame.MAX_PLAYERS) { // stop if max players reached
            showPlayerLimitDialogue();
            return;
        }

        String name = showAddPlayerDialogue();
        while (name != null && name.length() == 0) { // null means user canceled, length 0 means no user name given
            name = showAddPlayerDialogue();
        }
        if (name == null) return; // stop operation if no username was given

        Player newPlayer = model.addPlayer(name);
        view.addPlayerToView(newPlayer);
    }

    private String showAddPlayerDialogue() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new player");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter your name:");

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }

    private void showPlayerLimitDialogue() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Max player count reached");
        alert.setHeaderText(null);
        alert.setContentText("I'm sorry but only 10 players are allowed to play in a lobby");

        alert.showAndWait();
    }

    /**
     * Remove all cards from players hands, and shuffle the deck
     */
    private void shuffle() {
        for (int i = 0; i < model.getPlayerCount(); i++) {
            Player p = model.getPlayer(i);
            p.discardHand();
            PlayerPane pp = view.getPlayerPane(i);
            pp.updatePlayerDisplay();
        }

        model.getDeck().shuffle();
    }

    /**
     * Deal each player five cards, then evaluate the two hands
     */
    private void deal() {
        int cardsRequired = model.getPlayerCount() * Player.HAND_SIZE;
        DeckOfCards deck = model.getDeck();
        if (cardsRequired <= deck.getCardsRemaining()) {
            for (int i = 0; i < model.getPlayerCount(); i++) {
                Player p = model.getPlayer(i);
                p.discardHand();
                for (int j = 0; j < Player.HAND_SIZE; j++) {
                    Card card = deck.dealCard();
                    p.addCard(card);
                }
                p.evaluateHand();
                PlayerPane pp = view.getPlayerPane(i);
                pp.updatePlayerDisplay();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Not enough cards");
            alert.setHeaderText(null);
            alert.setContentText("Not enough cards - shuffle first");
            
            alert.showAndWait();
        }
    }
}
