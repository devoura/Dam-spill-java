package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Webby Debby on 11/18/2016.
 */
public class Tile extends Rectangle {

    /**
     * hele spillet er laget med relative størrelser basert på
     * TILE_SIZE så hvis du for eksempel gjør TILE_SIZE 50% mindre
     * så blir hele spillet 50% mindre.
     */
    public static final int TILE_SIZE = 100;
    private Piece piece;

    public boolean hasPiece(){
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    Tile(boolean isWhite, int x, int y) {

        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);
        relocate(x * TILE_SIZE, y * TILE_SIZE);
        setFill(isWhite ? Color.valueOf("#aaf799") : Color.valueOf("#4286f4"));
    }

    public Tile(int x, int y){

        setWidth(TILE_SIZE);
        setHeight(TILE_SIZE);
        relocate((x+1) * TILE_SIZE, (y+1) * TILE_SIZE);
        setFill(Color.valueOf("#e9ff00"));
        setOpacity(.2);

    }
}
