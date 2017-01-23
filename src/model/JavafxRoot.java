package model;


import javafx.scene.image.Image;
import javafx.scene.layout.*;
import network.Game;
import network.UsernamePacket;

import static model.Tile.TILE_SIZE;

/**
 * Created by Webby Debby on 11/21/2016.
 */

//root pane er den containeren som skal inneholde hele programmet
//andre panes kan legges oppå men det kan kun være en root pane
public class JavafxRoot extends Pane {

        public JavafxRoot() {
            setPrefSize(TILE_SIZE*14,TILE_SIZE*10);
            BackgroundImage myBI= new BackgroundImage(new Image("mahogany.jpg",32,TILE_SIZE*10,false,true),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            setBackground(new Background(myBI));
            UsernamePane usernamePane = new UsernamePane();
            ChatBox chatBox = new ChatBox();
            VBox chat = chatBox.createChatbox();
            Pane gamePane = Board.getBoard().createBoardWithPieces();
            try {
            	//klienten sender en usernamePacket til serveren så den kan lage 
            	//usernamePane for motstanderen på serverens side.
                if(!Game.getGame().isServer()) Game.getGame().getConnection().send(new UsernamePacket(Game.getGame().getUsername(), false));
            } catch (Exception e) {
                e.printStackTrace();
            }
            getChildren().add(gamePane);
            getChildren().add(usernamePane);
            getChildren().add(chat);
        }
    
}

