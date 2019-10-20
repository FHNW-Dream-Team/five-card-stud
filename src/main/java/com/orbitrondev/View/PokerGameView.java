package com.orbitrondev.View;

import com.orbitrondev.Model.Player;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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

    private Stage stage = null;

    public PokerGameView(Stage stage, PokerGameModel model) {
        this.model = model;

        // Create table
        table = new HBox();
        table.getStyleClass().add("table");

        // Create all of the player panes we need, and put them into an HBox
        players = new HBox();
        table.getChildren().add(players);

        // Create the control area
        controls = new ControlArea();
        controls.linkDeck(model.getDeck()); // link DeckLabel to DeckOfCards in the logic

        // Put players and controls into a BorderPane
        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(controls);

        // Disallow resizing - which is difficult to get right with images
        //stage.setResizable(false);

        // Create the scene using our layout; then display it
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/poker.css").toExternalForm());
        this.stage = stage;
        this.stage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));
        this.stage.setTitle("Five Card Stud");
        this.stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public PlayerPane getPlayerPane(int i) {
        return (PlayerPane) players.getChildren().get(i);
    }

    public void addPlayerToView(Player player) {
        PlayerPane pp = new PlayerPane();
        // link to player object in the logic
        pp.setPlayer(player);
        players.getChildren().add(pp);
    }

    public Button getAddPlayerButton() {
        return controls.btnAddPlayer;
    }

    public Button getShuffleButton() {
        return controls.btnShuffle;
    }

    public Button getDealButton() {
        return controls.btnDeal;
    }
}
