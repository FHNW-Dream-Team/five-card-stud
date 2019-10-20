package com.orbitrondev;

import javafx.application.Application;
import javafx.stage.Stage;
import com.orbitrondev.Controller.PokerGameController;
import com.orbitrondev.Model.PokerGameModel;
import com.orbitrondev.View.PokerGameView;

public class PokerGame extends Application {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 10; // Max of 52 cards, divided by 5 cards per player, makes it possible for 10 players to play

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create and initialize the MVC components
        PokerGameModel model = new PokerGameModel();
        PokerGameView view = new PokerGameView(primaryStage, model);
        new PokerGameController(model, view);
        view.show();
    }
}
