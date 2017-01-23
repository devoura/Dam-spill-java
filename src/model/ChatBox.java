package model;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import network.Game;

import static model.Tile.TILE_SIZE;

/**
 * Created by Webby Debby on 11/22/2016.
 */
public class ChatBox {

    public final static TextArea messages = new TextArea();

    VBox createChatbox() {
        messages.setPrefHeight(TILE_SIZE*6);
        messages.setEditable(false);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = Game.getGame().getUsername()+": ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");

            try {
                //String er automatisk Serializable
                Game.getGame().getConnection().send(message);
            } catch (Exception e) {
                messages.appendText("Failed to send\n");
            }
        });

        VBox chatBox = new VBox(20, messages, input);
        chatBox.setPrefSize(TILE_SIZE*2, TILE_SIZE*8);
        chatBox.setTranslateX(TILE_SIZE*10);
        chatBox.setTranslateY(TILE_SIZE);
        return chatBox;
    }



}
