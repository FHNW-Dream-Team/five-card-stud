package com.orbitrondev.View;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import com.orbitrondev.Model.DeckOfCards;

public class ControlArea extends HBox {
    private DeckLabel lblDeck = new DeckLabel();
    private Region spacer = new Region(); // Empty spacer
    Button btnAddPlayer = new Button("Add Player");
    Button btnRemovePlayer = new Button("Remove Player");
    Button btnShuffle = new Button("Shuffle");
    Button btnDeal = new Button("Deal");

    public ControlArea() {
        super(); // Always call super-constructor first !!

        this.getChildren().addAll(lblDeck, spacer, btnAddPlayer, btnRemovePlayer, btnShuffle, btnDeal);

        HBox.setHgrow(spacer, Priority.ALWAYS); // Use region to absorb resizing
        this.setId("controlArea"); // Unique ID in the CSS
    }

    public void linkDeck(DeckOfCards deck) {
        lblDeck.setDeck(deck);
    }
}
