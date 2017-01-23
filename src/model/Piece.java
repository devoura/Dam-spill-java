package model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static model.Tile.TILE_SIZE;

/**
 * Created by Webby Debby on 11/18/2016.
 */
public class Piece extends StackPane {

    private final PieceType type;
    private double x, y;
    private boolean isKing = false;

    public boolean isKing() {return isKing;}

    Piece(PieceType type) {
        this.type = type;

        //Ellipse background(bg) med radius bredde og høyde relativ til tile size
        Ellipse bg = new Ellipse(TILE_SIZE * .3125, TILE_SIZE * .26);
        bg.setFill(Color.BLACK);

        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(TILE_SIZE * .03);

        //Tile size minus bredde diameter delt på to for å finne x coordinat
        // slik at vi kan plassere Ellipsen perfekt i midten av en Tile
        bg.setTranslateX((TILE_SIZE - TILE_SIZE * .3125 * 2) / 2);
        //flytter Y 7% av en Tile nedover Y aksen for å få en 3D effekt
        bg.setTranslateY((TILE_SIZE - TILE_SIZE * .26 * 2) / 2 + TILE_SIZE * .07);

        Ellipse piece = new Ellipse(TILE_SIZE * .3125, TILE_SIZE * .26);
        piece.setFill(type == PieceType.RED
                ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

        piece.setStroke(Color.BLACK);
        piece.setStrokeWidth(TILE_SIZE * .03);

        piece.setTranslateX((TILE_SIZE - TILE_SIZE * .3125 * 2) / 2);
        piece.setTranslateY((TILE_SIZE - TILE_SIZE * .26 * 2) / 2);

        Ellipse crown = new Ellipse(TILE_SIZE * .3, TILE_SIZE * .24);
        crown.setFill(Color.valueOf("#D4AF37"));

        crown.setStroke(Color.BLACK);
        crown.setStrokeWidth(TILE_SIZE * .03);

        crown.setTranslateX((TILE_SIZE - TILE_SIZE * .3125 * 2) / 2);
        crown.setTranslateY((TILE_SIZE - TILE_SIZE * .26 * 2) / 2);

        getChildren().addAll(bg, piece);

    }

    public void promotion() {
    	
        isKing = true;
        Ellipse crown = new Ellipse(TILE_SIZE * .3125, TILE_SIZE * .26);
        crown.setFill(type == PieceType.RED
                ? Color.valueOf("#D4AF37") : Color.valueOf("#C0C0C0"));
        crown.setStroke(Color.BLACK);
        crown.setStrokeWidth(TILE_SIZE * .03);
        crown.setTranslateX((TILE_SIZE - TILE_SIZE * .3125 * 2) / 2);
        crown.setTranslateY((TILE_SIZE - TILE_SIZE * .26 * 2) / 2);
        getChildren().add(crown);
    
    }
    
    public PieceType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
