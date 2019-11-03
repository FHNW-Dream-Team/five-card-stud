package com.orbitrondev.Controller;

import com.orbitrondev.Model.*;
import com.orbitrondev.PokerGame;
import com.orbitrondev.View.PlayerPane;
import com.orbitrondev.View.PokerGameView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PokerGameController {
    private PokerGameModel model;
    private PokerGameView view;

    public PokerGameController(PokerGameModel model, PokerGameView view) {
        this.model = model;
        this.view = view;

        // Add players until two are ready
        while (model.getPlayerCount() < PokerGame.MIN_PLAYERS) {
            Player result = addPlayer();

            // Close program if user canceled operation
            if (result == null) {
                System.exit(0);
            }
        }

        // Add an event for all buttons
        view.getAddPlayerButton().setOnAction(e -> addPlayer());
        view.getRemovePlayerButton().setOnAction(e -> removePlayer());
        view.getShuffleButton().setOnAction(e -> shuffle());
        view.getDealButton().setOnAction(e -> deal());
    }

    /**
     * Add a new player to "model" and "view"
     */
    private Player addPlayer() {
        // Stop if max possible players are reached
        if (model.getPlayerCount() == PokerGame.MAX_PLAYERS) {
            showPlayerLimitDialogue();
            return null;
        }

        String name = showAddPlayerDialogue();

        // Stop operation completely if no username was given in the dialogue
        if (name == null) return null;

        // Otherwise add player
        Player newPlayer = model.addPlayer(name);
        view.addPlayerToView(newPlayer);
        view.resizeWindowHeight();
        return newPlayer;
    }

    /**
     * Show dialogue to ask for a username
     */
    private String showAddPlayerDialogue() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new player");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter your name:");

        // Add icon to window
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }

    private void removePlayer() {
        // Stop if min required players are reached
        if (model.getPlayerCount() == PokerGame.MIN_PLAYERS) {
            showPlayerLimitDialogue();
            return;
        }

        String name = showRemovePlayerDialogue();
        // "null" means user canceled, length "0" means no user name given, so repeat until right
        while (name != null && name.length() == 0) {
            name = showRemovePlayerDialogue();
        }
        // Stop operation completely if no username was given in the dialogue
        if (name == null) return;

        // Otherwise remove player
        Player player = null;
        for (Player p : model.getPlayers()) {
            if (!p.getPlayerName().equals(name)) continue;
            player = p;
        }

        view.removePlayerFromView(player);
        model.removePlayer(player);
        view.resizeWindowHeight();
    }

    /**
     * Show dialogue to ask for a user to be removed
     */
    private String showRemovePlayerDialogue() {
        List<String> choices = new ArrayList<>();
        for (Player player : model.getPlayers()) choices.add(player.getPlayerName());

        ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
        dialog.setTitle("Remove player");
        dialog.setHeaderText(null);
        dialog.setContentText("Choose the user to be removed:");
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }


    /**
     * Show a dialogue that no more players can be added
     */
    private void showPlayerLimitDialogue() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Min/Max player count reached");
        alert.setHeaderText(null);
        alert.setContentText("I'm sorry but you need between 2 and 10 players inside a lobby");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));

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
            evaluateWinner();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Not enough cards");
            alert.setHeaderText(null);
            alert.setContentText("Not enough cards - shuffle first");

            alert.showAndWait();
        }
    }

    /**
     * Evaluate winner
     */
    private void evaluateWinner() {
        ArrayList<ArrayList<Player>> winners = new ArrayList<>();

        ArrayList<Player> highCardWinning = new ArrayList<>();
        winners.add(highCardWinning);
        ArrayList<Player> onePairWinning = new ArrayList<>();
        winners.add(onePairWinning);
        ArrayList<Player> twoPairWinning = new ArrayList<>();
        winners.add(twoPairWinning);
        ArrayList<Player> threeOfAKindWinning = new ArrayList<>();
        winners.add(threeOfAKindWinning);
        ArrayList<Player> straightWinning = new ArrayList<>();
        winners.add(straightWinning);
        ArrayList<Player> flushWinning = new ArrayList<>();
        winners.add(flushWinning);
        ArrayList<Player> fullHouseWinning = new ArrayList<>();
        winners.add(fullHouseWinning);
        ArrayList<Player> fourOfAKindWinning = new ArrayList<>();
        winners.add(fourOfAKindWinning);
        ArrayList<Player> straightFlushWinning = new ArrayList<>();
        winners.add(straightFlushWinning);

        for (int i = 0; i < model.getPlayerCount(); i++) {

            Player player = model.getPlayer(i);
            HandType winningHand = player.getHandType();

            switch (winningHand) {
                case HighCard:
                    highCardWinning.add(player);
                    break;
                case OnePair:
                    onePairWinning.add(player);
                    break;
                case TwoPair:
                    twoPairWinning.add(player);
                    break;
                case ThreeOfAKind:
                    threeOfAKindWinning.add(player);
                    break;
                case Straight:
                    straightWinning.add(player);
                    break;
                case Flush:
                    flushWinning.add(player);
                    break;
                case FullHouse:
                    fullHouseWinning.add(player);
                    break;
                case FourOfAKind:
                    fourOfAKindWinning.add(player);
                    break;
                case StraightFlush:
                    straightFlushWinning.add(player);
                    break;
            }
        }

        ArrayList<Player> highestHandType = new ArrayList<>();
        for (ArrayList<Player> currentHands : winners) {
            if (!currentHands.isEmpty()) {
                if (highestHandType.isEmpty()) {
                    highestHandType = currentHands;
                } else if (currentHands.get(0).compareTo(highestHandType.get(0)) == 1) {
                    highestHandType.clear();
                    highestHandType = currentHands;
                }
            }
        }

        showWinnerDialogue(highestHandType);
    }

    /**
     * Show dialogue to show who the winner was
     */
    private void showWinnerDialogue(ArrayList<Player> winningPlayers) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Winner");
        alert.setHeaderText(null);

        if (winningPlayers.size() == 1) {
            String winner = winningPlayers.get(0).getPlayerName();
            alert.setContentText(winner + " won! Congratulations!");
        } else {
            String message = "";
            boolean isFirstPlayer = true;
            for (Player player : winningPlayers) {
                if (!isFirstPlayer) {
                    message += " & ";
                }
                message += player.getPlayerName();
                isFirstPlayer = false;
            }
            alert.setContentText(message + " won! Congratulations!");
        }

        alert.showAndWait();
    }
}
