package com.orbitrondev.View;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.orbitrondev.PokerGame;
import com.orbitrondev.Model.PokerGameModel;

public class PokerGameView {
    private HBox table;
    private HBox players;
    private ControlArea controls;

    private PokerGameModel model;

    public PokerGameView(Stage stage, PokerGameModel model) {
        this.model = model;

        // Create table
        table = new HBox();
        table.getStyleClass().add("table");

        // Create all of the player panes we need, and put them into an HBox
        players = new HBox();
        for (int i = 0; i < PokerGame.NUM_PLAYERS; i++) {
            PlayerPane pp = new PlayerPane();
            pp.setPlayer(model.getPlayer(i)); // link to player object in the logic
            players.getChildren().add(pp);
        }
        table.getChildren().add(players);

        // Create the control area
        controls = new ControlArea();
        controls.linkDeck(model.getDeck()); // link DeckLabel to DeckOfCards in the logic

        // Put players and controls into a BorderPane
        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(controls);

        // Disallow resizing - which is difficult to get right with images
        stage.setResizable(false);

        // Create the scene using our layout; then display it
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/poker.css").toExternalForm());
        stage.setTitle("Five Card Stud");
        stage.setScene(scene);
        stage.show();
    }

    public PlayerPane getPlayerPane(int i) {
        return (PlayerPane) players.getChildren().get(i);
    }

    public Button getShuffleButton() {
        return controls.btnShuffle;
    }

    public Button getDealButton() {
        return controls.btnDeal;
    }
}
