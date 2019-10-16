package com.orbitrondev;

import javafx.application.Application;
import javafx.stage.Stage;
import com.orbitrondev.Controller.PokerGameController;
import com.orbitrondev.Model.PokerGameModel;
import com.orbitrondev.View.PokerGameView;

public class PokerGame extends Application {
	public static final int NUM_PLAYERS = 2;
    private PokerGameModel model;
    private PokerGameView view;
    private PokerGameController controller;
	
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	// Create and initialize the MVC components
    	model = new PokerGameModel();
    	view = new PokerGameView(primaryStage, model);
    	controller = new PokerGameController(model, view);
    }
}
