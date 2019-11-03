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
        view.resizeWindowHeight(); // Update maximum window size, because player count changed
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

    /**
     * Show dialogue to ask for a user to be removed
     */
    private void removePlayer() {
        // Stop if minimum required players are reached
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
        view.resizeWindowHeight(); // Update maximum window size, because player count changed
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
            // Give each player 5 cards
            for (int i = 0; i < model.getPlayerCount(); i++) {
                Player p = model.getPlayer(i);
                p.discardHand();
                for (int j = 0; j < Player.HAND_SIZE; j++) {
                    Card card = deck.dealCard();
                    p.addCard(card);
                }
                p.evaluateHand();
                PlayerPane pp = view.getPlayerPane(i);
                pp.updatePlayerDisplay(); // Update the view with the new cards
            }
            // Initiate to check, who wins the round
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

        // Create array for each possible hand
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

        // Add the players to the according array
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

        // Find the highest available hand type
        ArrayList<Player> highestHandType = new ArrayList<>();
        for (ArrayList<Player> currentHands : winners) {
            if (!currentHands.isEmpty()) {
                highestHandType.clear();
                highestHandType = currentHands;
            }
        }

        // Handle tie-breaks: when we have more than one of the same hand
        if (highestHandType.size() > 1) {
            switch (highestHandType.get(0).getHandType()) {
                case HighCard:
                    Player bestPlayer = null;
                    for (Player player : highestHandType) {
                        if (bestPlayer == null) {
                            bestPlayer = player;
                        } else if (player.getCards().get(4).getRank().ordinal() > bestPlayer.getCards().get(4).getRank().ordinal()) {
                            // Compare the fifth card of each user
                            bestPlayer = player;
                        } else if (player.getCards().get(4).getRank().ordinal() == bestPlayer.getCards().get(4).getRank().ordinal()) {
                            if (player.getCards().get(3).getRank().ordinal() > bestPlayer.getCards().get(3).getRank().ordinal()) {
                                // Compare the fourth card of each user
                                bestPlayer = player;
                            } else if (player.getCards().get(3).getRank().ordinal() == bestPlayer.getCards().get(3).getRank().ordinal()) {
                                if (player.getCards().get(2).getRank().ordinal() > bestPlayer.getCards().get(2).getRank().ordinal()) {
                                    // Compare the third card of each user
                                    bestPlayer = player;
                                } else if (player.getCards().get(2).getRank().ordinal() == bestPlayer.getCards().get(2).getRank().ordinal()) {
                                    if (player.getCards().get(1).getRank().ordinal() > bestPlayer.getCards().get(1).getRank().ordinal()) {
                                        // Compare the second card of each user
                                        bestPlayer = player;
                                    } else if (player.getCards().get(1).getRank().ordinal() == bestPlayer.getCards().get(1).getRank().ordinal()) {
                                        if (player.getCards().get(0).getRank().ordinal() > bestPlayer.getCards().get(0).getRank().ordinal()) {
                                            // Compare the first card of each user
                                            bestPlayer = player;
                                        }
                                        // If all cards are the same rank, then both win
                                    }
                                }
                            }
                        }
                    }
                    // Erase all other players and assign the best player as the only one to the array
                    if (bestPlayer != null) {
                        highestHandType.clear();
                        highestHandType.add(bestPlayer);
                    }
                    break;

                case OnePair:
                    Player bestOnePairPlayer = null;
                    for (Player player : highestHandType) {
                        if (bestOnePairPlayer == null) {
                            bestOnePairPlayer = player;
                        } else {
                            // Find the pair inside the hand, and move it to another array
                            ArrayList<Card> clonedPlayer1Cards = (ArrayList<Card>) player.getCards().clone();
                            ArrayList<Card> player1PairCards = new ArrayList<>();
                            boolean firstPairFound = false;
                            for (int i = 0; i < clonedPlayer1Cards.size() - 1 && !firstPairFound; i++) {
                                for (int j = i + 1; j < clonedPlayer1Cards.size() && !firstPairFound; j++) {
                                    if (clonedPlayer1Cards.get(i).getRank() == clonedPlayer1Cards.get(j).getRank()) {
                                        firstPairFound = true;
                                        player1PairCards.add(clonedPlayer1Cards.remove(j));
                                        player1PairCards.add(clonedPlayer1Cards.remove(i));
                                    }
                                }
                            }
                            // Find the pair inside the hand of the user to compare against, and move it to another array
                            ArrayList<Card> clonedBestPairPlayerCards = (ArrayList<Card>) bestOnePairPlayer.getCards().clone();
                            ArrayList<Card> bestPairPlayerCards = new ArrayList<>();
                            firstPairFound = false;
                            for (int i = 0; i < clonedBestPairPlayerCards.size() - 1 && !firstPairFound; i++) {
                                for (int j = i + 1; j < clonedBestPairPlayerCards.size() && !firstPairFound; j++) {
                                    if (clonedBestPairPlayerCards.get(i).getRank() == clonedBestPairPlayerCards.get(j).getRank()) {
                                        firstPairFound = true;
                                        bestPairPlayerCards.add(clonedBestPairPlayerCards.remove(j));
                                        bestPairPlayerCards.add(clonedBestPairPlayerCards.remove(i));
                                    }
                                }
                            }

                            if (player1PairCards.get(0).getRank().ordinal() > bestPairPlayerCards.get(0).getRank().ordinal()) {
                                // Compare the pairs of both players
                                bestOnePairPlayer = player;
                            } else if (player1PairCards.get(0).getRank().ordinal() == bestPairPlayerCards.get(0).getRank().ordinal()) {
                                if (clonedPlayer1Cards.get(2).getRank().ordinal() > clonedBestPairPlayerCards.get(2).getRank().ordinal()) {
                                    // Compare the third spare card of both players, in case pairs are the same
                                    bestOnePairPlayer = player;
                                } else if (clonedPlayer1Cards.get(2).getRank().ordinal() == clonedBestPairPlayerCards.get(2).getRank().ordinal()) {
                                    if (clonedPlayer1Cards.get(1).getRank().ordinal() > clonedBestPairPlayerCards.get(1).getRank().ordinal()) {
                                        // Compare the second spare card of both players
                                        bestOnePairPlayer = player;
                                    } else if (clonedPlayer1Cards.get(1).getRank().ordinal() == clonedBestPairPlayerCards.get(1).getRank().ordinal()) {
                                        if (clonedPlayer1Cards.get(0).getRank().ordinal() > clonedBestPairPlayerCards.get(0).getRank().ordinal()) {
                                            // Compare the first spare card of both players
                                            bestOnePairPlayer = player;
                                        }
                                        // If all cards are the same rank, then both win
                                    }
                                }
                            }
                        }
                    }
                    // Erase all other players and assign the best player as the only one to the array
                    if (bestOnePairPlayer != null) {
                        highestHandType.clear();
                        highestHandType.add(bestOnePairPlayer);
                    }
                    break;
                case TwoPair:
                    Player bestTwoPairPlayer = null;
                    for (Player player : highestHandType) {
                        if (bestTwoPairPlayer == null) {
                            bestTwoPairPlayer = player;
                        } else {
                            // Find both pairs of the player and move the cards to the appropriate new array
                            ArrayList<Card> clonedPlayerCards = (ArrayList<Card>) player.getCards().clone();
                            ArrayList<Card> highestTwoPairPlayerCards = new ArrayList<>();
                            ArrayList<Card> lowestTwoPairPlayerCards = new ArrayList<>();
                            boolean firstPairFound = false;
                            for (int i = 0; i < clonedPlayerCards.size() - 1 && !firstPairFound; i++) {
                                for (int j = i + 1; j < clonedPlayerCards.size() && !firstPairFound; j++) {
                                    if (clonedPlayerCards.get(i).getRank() == clonedPlayerCards.get(j).getRank()) {
                                        firstPairFound = true;
                                        highestTwoPairPlayerCards.add(clonedPlayerCards.remove(j));
                                        highestTwoPairPlayerCards.add(clonedPlayerCards.remove(i));
                                    }
                                }
                            }
                            boolean secondPairFound = false;
                            for (int i = 0; i < clonedPlayerCards.size() - 1 && !secondPairFound; i++) {
                                for (int j = i + 1; j < clonedPlayerCards.size() && !secondPairFound; j++) {
                                    if (clonedPlayerCards.get(i).getRank() == clonedPlayerCards.get(j).getRank()) {
                                        secondPairFound = true;
                                        Card card1 = clonedPlayerCards.remove(j);
                                        Card card2 = clonedPlayerCards.remove(i);
                                        if (highestTwoPairPlayerCards.get(0).getRank().compareTo(card1.getRank()) == -1) {
                                            lowestTwoPairPlayerCards = highestTwoPairPlayerCards;
                                            highestTwoPairPlayerCards.clear();
                                            highestTwoPairPlayerCards.add(card1);
                                            highestTwoPairPlayerCards.add(card2);
                                        } else {
                                            lowestTwoPairPlayerCards.add(card1);
                                            lowestTwoPairPlayerCards.add(card2);
                                        }
                                    }
                                }
                            }

                            // Find both pairs of the player to compare against and move the cards to the appropriate new array
                            ArrayList<Card> clonedBestTwoPairPlayerCards = (ArrayList<Card>) bestTwoPairPlayer.getCards().clone();
                            ArrayList<Card> highestBestTwoPairPlayerCards = new ArrayList<>();
                            ArrayList<Card> lowestBestTwoPairPlayerCards = new ArrayList<>();
                            firstPairFound = false;
                            for (int i = 0; i < clonedBestTwoPairPlayerCards.size() - 1 && !firstPairFound; i++) {
                                for (int j = i + 1; j < clonedBestTwoPairPlayerCards.size() && !firstPairFound; j++) {
                                    if (clonedBestTwoPairPlayerCards.get(i).getRank() == clonedBestTwoPairPlayerCards.get(j).getRank()) {
                                        firstPairFound = true;
                                        highestBestTwoPairPlayerCards.add(clonedBestTwoPairPlayerCards.remove(j));
                                        highestBestTwoPairPlayerCards.add(clonedBestTwoPairPlayerCards.remove(i));
                                    }
                                }
                            }
                            secondPairFound = false;
                            for (int i = 0; i < clonedBestTwoPairPlayerCards.size() - 1 && !secondPairFound; i++) {
                                for (int j = i + 1; j < clonedBestTwoPairPlayerCards.size() && !secondPairFound; j++) {
                                    if (clonedBestTwoPairPlayerCards.get(i).getRank() == clonedBestTwoPairPlayerCards.get(j).getRank()) {
                                        secondPairFound = true;
                                        Card card1 = clonedBestTwoPairPlayerCards.remove(j);
                                        Card card2 = clonedBestTwoPairPlayerCards.remove(i);
                                        if (highestBestTwoPairPlayerCards.get(0).getRank().compareTo(card1.getRank()) == -1) {
                                            lowestBestTwoPairPlayerCards = highestBestTwoPairPlayerCards;
                                            highestBestTwoPairPlayerCards.clear();
                                            highestBestTwoPairPlayerCards.add(card1);
                                            highestBestTwoPairPlayerCards.add(card2);
                                        } else {
                                            lowestBestTwoPairPlayerCards.add(card1);
                                            lowestBestTwoPairPlayerCards.add(card2);
                                        }
                                    }
                                }
                            }

                            if (highestTwoPairPlayerCards.get(0).getRank().ordinal() > highestBestTwoPairPlayerCards.get(0).getRank().ordinal()) {
                                // Compare the higher pairs of both players
                                bestTwoPairPlayer = player;
                            } else if (highestTwoPairPlayerCards.get(0).getRank().ordinal() == highestBestTwoPairPlayerCards.get(0).getRank().ordinal()) {
                                if (lowestTwoPairPlayerCards.get(0).getRank().ordinal() > lowestBestTwoPairPlayerCards.get(0).getRank().ordinal()) {
                                    // Compare the lower pairs of both players
                                    bestTwoPairPlayer = player;
                                } else if (lowestTwoPairPlayerCards.get(0).getRank().ordinal() == lowestBestTwoPairPlayerCards.get(0).getRank().ordinal()) {
                                    if (clonedPlayerCards.get(0).getRank().ordinal() > clonedBestTwoPairPlayerCards.get(0).getRank().ordinal()) {
                                        // Compare the last spare card of both players, in case all pairs are the same
                                        bestTwoPairPlayer = player;
                                    }
                                }
                            }
                        }
                    }
                    // Erase all other players and assign the best player as the only one to the array
                    if (bestTwoPairPlayer != null) {
                        highestHandType.clear();
                        highestHandType.add(bestTwoPairPlayer);
                    }
                    break;

                case ThreeOfAKind:
                    // TODO
                    break;

                case Straight:
                    // TODO
                    break;

                case Flush:
                    // TODO
                    break;

                case FullHouse:
                    // TODO
                    break;

                case FourOfAKind:
                    // TODO
                    break;

                case StraightFlush:
                    // TODO
                    break;
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
            // Show text if there is only one winner
            String winner = winningPlayers.get(0).getPlayerName();
            alert.setContentText(winner + " won! Congratulations!");
        } else {
            // Show text if there is more than one winner
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

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));
        alert.showAndWait();
    }
}
