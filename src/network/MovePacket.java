package network;

import logic.PieceLogic;
import model.Board;
import java.io.Serializable;

/**
 * Created by Webby Debby on 11/24/2016.
 */
public class MovePacket implements Serializable{

    private final int newX, newY, x0, y0, x1, y1;

    public MovePacket(int newX, int newY, int x0, int y0, int x1, int y1) {
        this.newX = newX;
        this.newY = newY;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }
    
  /**
   * lagde disse metodene her fordi det
   * så stygt ut å lage dem utenfor pakken med
   * 6 getters, mye lettere å lese [x0][y0] enn
   * [movePacket.getX0][movePacket.getY0]...
   */
    void movePiece() {
        PieceLogic.getPL().movePiece(
                Board.getBoard().getBoardTiles()[x0][y0].getPiece(), 
                Board.getBoard().getBoardTiles(), 
                newX, 
                newY, 
                Board.getBoard().getBoardTiles()[x0][y0]
        );
    }
    void capturePiece(){
        PieceLogic.getPL().capturePiece(
                Board.getBoard().getBoardTiles()[x0][y0].getPiece(),
                Board.getBoard().getBoardTiles(),
                Board.getBoard().getPieceGroup(),
                newX,
                newY,
                Board.getBoard().getBoardTiles()[x0][y0],
                Board.getBoard().getBoardTiles()[x1][y1]
        );
    }
    
    int getX1() {
        return x1;
    }

    int getNewX() {
        return newX;
    }

    int getNewY() {
        return newY;
    }
}
