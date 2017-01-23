package logic;


import javafx.scene.Node;
import model.*;
import network.Game;

/**
 * Created by Webby Debby on 11/19/2016.
 */
public class MoveLogic {

    //MoveLogic er en singleton
    private static MoveLogic moveLogic = null;
    private boolean canCapture = false;

    private MoveLogic() {
    }

    MoveResult tryMove(Piece piece, int newX, int newY, Tile[][] board, int x0, int y0, int x1, int y1) {

        //sjekker om det er din tur, om du flytter din egen brikke og om du flytter til en firkanten som er opptatt
        if (!Game.getGame().yourTurn() || (Game.getGame().isServer() ? piece.getType() != PieceType.WHITE : piece.getType() != PieceType.RED) || board[newX][newY].hasPiece()) {
            return new MoveResult(MoveType.NONE);
        }

        //sjekker om du prøver å flytte ett steg fram diagonalt
        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir) {
            return new MoveResult((MoveType.MOVE));
            //sjekker om du prøver å kapre framover diagonalt
        } else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) {
            //sjekker om det finnes en brikke å kapre og at den brikken er motsatt farge enn den du flytter
            if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType()) {
                return new MoveResult(MoveType.CAPTURE);
            }
        }
        //sjekker om du flytter bakover med en konge
        if (Math.abs(newX - x0) == 1 && newY - y0 == -piece.getType().moveDir && piece.isKing()) {
            return new MoveResult((MoveType.MOVE));
            //sjekker om du kaprer bakover med en konge
        } else if (Math.abs(newX - x0) == 2 && newY - y0 == -piece.getType().moveDir * 2) {
            if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getType() != piece.getType() && piece.isKing()) {
                return new MoveResult(MoveType.CAPTURE);
            }
        }

        return new MoveResult(MoveType.NONE);
    }

    //sjekker om en brikke kan kapre noe i noen retning
    private boolean canCapture(Tile[][] board, int newX, int newY, Piece piece) {

        canCapture = false;

        int moveReach = 2 * piece.getType().moveDir;
        //hopper over de argumentene som ville kastet ArrayIndexOutOfBounds exception
        if (newX > 1 && newY + moveReach >= 0 && newY + moveReach <8) {
            directionCanCapture(board, newX, newY, piece, -1, +1);
        }
        //samme igjen men denne gangen sjekker vi en annen retning, det er 4 diagonale retninger som må sjekkes: (-1,+1) (+1,-1) (-1,-1) og (+1,-1).
        if(newX < 6 && newY + moveReach >= 0 && newY + moveReach <8){
            directionCanCapture(board, newX, newY, piece, +1, +1);
        }
        //samme men disse to neste retningene må også sjekke om brikken er konge for bare konger kan kapre bakover
        if(newX > 1 && newY - moveReach >= 0 && newY - moveReach < 8 && piece.isKing()){
            directionCanCapture(board, newX, newY, piece, -1, -1);
        }
        if (newX < 6 && newY - moveReach >= 0 && newY - moveReach < 8 && piece.isKing()){
            directionCanCapture(board, newX, newY, piece, +1, -1);
        }
        //chatten sier fra når en spiller må kapre
        if (canCapture){
            ChatBox.messages.appendText("A " + piece.getType() + " piece must capture! \n");
            try {
                Game.getGame().getConnection().send("A " + piece.getType() + " piece must capture!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return canCapture;
    }


    private void directionCanCapture(Tile[][] board, int x, int y, Piece piece, int directionX, int directionY) {

            //Sjekker om det finnes en brikke å kapre
            if ((board[x + directionX]
                    [y + (directionY * piece.getType().moveDir)].hasPiece() &&
                //sjekker at den brikken som kan kapres ikke er samme farge som brikken selv
                board[x + directionX][y + (directionY * piece.getType().moveDir)].getPiece().getType() != piece.getType() &&
                //sjekker at det er ledig på andre siden av brikken som kan kapres
                !board[x + (2 * directionX)][y + (2 * directionY * piece.getType().moveDir)].hasPiece())) {
                //returnerer en true boolean hvis alt over er sant
                canCapture = true;
            }
    }

    public static MoveLogic getML() {
        //lazy load
        if (moveLogic == null){
            //thread safe
            synchronized (MoveLogic.class){
                moveLogic = new MoveLogic();
            }
        }
        return moveLogic;
    }

    public boolean canCaptureAdapter(Node e) {
        Piece piece = (Piece) e;
        if(Game.getGame().isServer() ? piece.getType() == PieceType.WHITE : piece.getType() == PieceType.RED) {
            int newX = GameMath.toBoard(piece.getLayoutX());
            int newY = GameMath.toBoard(piece.getLayoutY());
            Tile[][] board = Board.getBoard().getBoardTiles();
            return canCapture(board, newX, newY, piece);
        }else
        return false;
    }
}
