package com.orbitrondev.View;

import com.orbitrondev.Model.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.orbitrondev.Model.PokerGameModel;

public class PokerGameView {
    private BorderPane root;
    private FlowPane players;
    private ControlArea controls;

    private PokerGameModel model;

    private Stage stage;

    public PokerGameView(Stage stage, PokerGameModel model) {
        this.model = model;
        this.stage = stage;

        // Create table
        HBox table = new HBox();
        table.getStyleClass().add("table");
        table.setAlignment(Pos.CENTER);

        // Create all of the player panes we need, and put them into an HBox (Table)
        players = new FlowPane();
        final int USER_PANEL_WIDTH =
            (5 * 111) // 5 cards with each 111 px
            + (2 * 5) // 5 pixels padding left and right (.card)
            + (2 * 5) // 5 pixels padding left and right (.player)
        ;
        players.setPrefWrapLength(USER_PANEL_WIDTH * 2); // Preferred width allows for 2 columns
        table.getChildren().add(players);

        // Create a pane where the user can scroll through the user panes
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true); // Let's children know the width, and let it adapt therefore (used for when we want to resize width also (maybe future release)
        // Fix content to borders (prevents overflow (weird behaviour of background image))
        AnchorPane.setTopAnchor(scrollPane, 0.);
        AnchorPane.setRightAnchor(scrollPane, 0.);
        AnchorPane.setBottomAnchor(scrollPane, 0.);
        AnchorPane.setLeftAnchor(scrollPane, 0.);
        scrollPane.setContent(table);
        scrollPane.getStyleClass().add("scroll-pane");

        // Create the control area
        controls = new ControlArea();
        controls.linkDeck(model.getDeck()); // link DeckLabel to DeckOfCards in the logic

        // Put players and controls into a BorderPane
        root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(controls);

        // Create the scene using our layout; then display it
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/poker.css").toExternalForm());

        // Fix width of the window to always only show two columns
        final int WINDOW_FIXED_WIDTH = 1170;
        this.stage.setMinWidth(WINDOW_FIXED_WIDTH);
        this.stage.setWidth(WINDOW_FIXED_WIDTH);
        this.stage.setMaxWidth(WINDOW_FIXED_WIDTH);

        // At least always show the first row
        this.stage.setMinHeight(300);

        // Show an icon for the window
        this.stage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString())); // Add icon to window

        this.stage.setTitle("Five Card Stud");
        this.stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public PlayerPane getPlayerPane(int i) {
        return (PlayerPane) players.getChildren().get(i);
    }

    /**
     * Add a Player (PlayerPane) to the view
     */
    public void addPlayerToView(Player player) {
        PlayerPane pp = new PlayerPane();
        //pp.setMaxWidth(567);
        // link to player object in the logic
        pp.setPlayer(player);
        players.getChildren().add(pp);
    }

    /**
     * Remove a Player (PlayerPane) from the view
     */
    public void removePlayerFromView(Player playerToRemove) {
        boolean removed = false;
        for (int i = 0; i < model.getPlayerCount() && !removed; i++) {
            PlayerPane pp = getPlayerPane(i);
            if (pp.getPlayer() == playerToRemove) {
                players.getChildren().remove(pp);
                removed = true;
            }
        }
    }

    /**
     * Define max height of the window
     */
    public void setWindowMaxHeight(double pixels) {
        // DO NOT SET HEIGHT! When there are too many players it could become to high!
        //stage.setHeight(pixels);

        stage.setMaxHeight(pixels);
    }

    /**
     * Calculates how long the window should be according to number of players
     */
    public void resizeWindowHeight() {
        double numRows = Math.ceil(model.getPlayerCount() / 2.0);
        double windowTopHeight = 36; // The top window bar (in Windows)
        double playerPaneHeight =
            226 // One player pane height
        ;
        double controlPanelHeight =
            37 + // The control panel itself
            1    // The 1px border
        ;
        setWindowMaxHeight(windowTopHeight + (playerPaneHeight * numRows) + controlPanelHeight);
    }

    public Button getAddPlayerButton() {
        return controls.btnAddPlayer;
    }

    public Button getRemovePlayerButton() {
        return controls.btnRemovePlayer;
    }

    public Button getShuffleButton() {
        return controls.btnShuffle;
    }

    public Button getDealButton() {
        return controls.btnDeal;
    }
}
