package model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import network.Game;
import network.UsernamePacket;

import static model.Tile.TILE_SIZE;

/**
 * Created by Webby Debby on 11/21/2016.
 */
public class UsernamePane extends Pane {

    //for å lage din egen UsernamePane i ditt vindu
    UsernamePane() {
        makeUsernamePane(Game.getGame().getUsername(), Game.getGame().isServer());
    }
    //for å lage motstanderens UsernamePane i ditt vindu
    public UsernamePane(UsernamePacket username){

        String enemyUsername = username.getUsername();
        Boolean isEnemyWhite = username.isWhite();
        makeUsernamePane(enemyUsername, isEnemyWhite);
    }
    private void makeUsernamePane(String Username, boolean isWhite){
    	setPrefSize(TILE_SIZE, TILE_SIZE*.3);
        Text usernameText = new Text();
        usernameText.setText(Username);
        usernameText.setFont(Font.font(TILE_SIZE*.6));
        if(isWhite) {
            setTranslateX(TILE_SIZE/2);
            setTranslateY(TILE_SIZE*9.8);
            usernameText.setFill(Color.valueOf("WHITE"));
        }else{
            setTranslateX(TILE_SIZE/2);
            setTranslateY(TILE_SIZE*.6);
            usernameText.setFill(Color.valueOf("RED"));
        }
        getChildren().add(usernameText);
    }
}
