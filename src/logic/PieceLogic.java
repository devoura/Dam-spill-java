package logic;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import model.Piece;
import model.PieceType;
import model.Tile;
import network.Game;
import network.MovePacket;

import static model.Tile.TILE_SIZE;

/**
 * Created by Webby Debby on 11/19/2016.
 */
public class PieceLogic {

    //Piecelogic er en singleton
    private static PieceLogic pieceLogic = null;
    private double mouseX, mouseY;
    
    private PieceLogic(){}

    public void findPiece(MouseEvent e){
        mouseX = e.getSceneX();
        mouseY = e.getSceneY();
    }

    public void dragPiece(MouseEvent e, Piece piece){
        piece.relocate(e.getSceneX() - mouseX + piece.getX(),
                       e.getSceneY() - mouseY + piece.getY());
    }

    public void setPiece(Piece piece, Tile[][] board, Group pieceGroup) {

        int newX = GameMath.toBoard(piece.getLayoutX());
        int newY = GameMath.toBoard(piece.getLayoutY());
        //koordinater for hvor brikken vi bruker står
        int x0 = GameMath.toBoard(piece.getX()),
            y0 = GameMath.toBoard(piece.getY()),
            //koordinater for hvor brikken vi kaprer står
            x1 = x0 + ((newX - x0) / 2),
            y1 = y0 + ((newY - y0) / 2);
        MoveResult result = MoveLogic.getML().tryMove(piece, newX, newY, board, x0, y0, x1, y1);
        switch (result.getType()) {
            case NONE:
                abortMove(piece);
                break;
            case MOVE:
                //Move er ikke lov hvis det er mulig å kapre
                if (!Game.getGame().canCapture()) {
                    movePiece(piece, board, newX, newY, board[x0][y0]);
                    try {
                        Game.getGame().getConnection().send(new MovePacket(newX, newY, x0, y0, 0, 0));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("failure");
                    }
                }else {
                    abortMove(piece);
                }
                break;
            case CAPTURE:
                capturePiece(piece, board, pieceGroup, newX, newY, board[x0][y0], board[x1][y1]);
                try {
                    Boolean canCapture = pieceGroup
                            .getChildren()
                            .stream()
                            .filter(e -> MoveLogic.getML().canCaptureAdapter(e))
                            .count() != 0;
                    Game.getGame().setYourTurn(canCapture);
                    Game.getGame().setCanCapture(canCapture);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Game.getGame().getConnection().send(new MovePacket(newX, newY, x0, y0, x1, y1));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("failure");
                }
                break;
        }
    }

    public void movePiece(Piece piece, Tile[][] board, int newX, int newY, Tile tile) {

            move(newX, newY, piece);
            tile.setPiece(null);
            board[newX][newY].setPiece(piece);
            if ((newY == 0 || newY == 7) && !piece.isKing()) {
                piece.promotion();
            }
            Game.getGame().setYourTurn(false);
    }

    public void capturePiece(Piece piece, Tile[][] board, Group pieceGroup, int newX, int newY, Tile tile, Tile capturedTile) {

        Piece otherPiece = capturedTile.getPiece();
        move(newX, newY, piece);
        tile.setPiece(null);
        capturedTile.setPiece(null);
        board[newX][newY].setPiece(piece);
        pieceGroup.getChildren().remove(otherPiece);
        if ((newY == 0 || newY == 7) && !piece.isKing()) {
            piece.promotion();
        }
        checkForWin(otherPiece, pieceGroup);

    }

    private void checkForWin(Piece otherPiece, Group pieceGroup) {

        if(pieceGroup.getChildren()
                .stream()
                .filter(node -> filterOutColor(node, otherPiece.getType()))
                .count() == 0){
            Boolean redWins = otherPiece.getType() == PieceType.WHITE;
                    if (redWins) {
                        Game.getGame().redWin();
                    }else {
                        Game.getGame().whiteWin();
                    }
        }
    }

    private boolean filterOutColor(Object node, PieceType color) {
        Piece piece = (Piece) node;
        return piece.getType() == color;
    }

    public void move(int x, int y, Piece piece){
        piece.setX(x * TILE_SIZE);
        piece.setY(y * TILE_SIZE);
        piece.relocate(piece.getX(), piece.getY());
    }

    private void abortMove(Piece piece){
        piece.relocate(piece.getX(), piece.getY());
    }

    public static PieceLogic getPL() {
        //lazy load
        if(pieceLogic == null){
            //threadsafe
            synchronized (PieceLogic.class){
                pieceLogic = new PieceLogic();
            }
        }
        return pieceLogic;
    }

}
