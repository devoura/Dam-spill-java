package model;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import logic.PieceLogic;

/**
 * Created by Webby Debby on 11/18/2016.
 */
public class Board {

    //Board er en singleton
    private static Board board = null;
    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;
    private final Group pieceGroup = new Group();
    private final Group tileGroup = new Group();

    //nested array av tiles som utgj�r brettet (8 tiles * 8 rader)
    //det brukes til � sp�rre om en tile n�r vi skal flytte en brikke
    private final Tile[][] boardTiles = new Tile[WIDTH][HEIGHT];

    private Board() {
    }


    Pane createBoardWithPieces() {
        Pane gamePane = new Pane();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                boardTiles[x][y] = tile;
                tileGroup.getChildren().add(tile);

                //piece blir instansiert og satt til null hver gang for loopen kj�rer
                Piece piece = null;

                //fyller alle m�rke tiles i de f�rste tre radene med r�de brikker
                if(y <= 2 && (x + y) % 2 != 0){
                    piece = makePiece(PieceType.RED, x, y);
                }

                //fyller alle m�rke tiles i de siste tre radene med white brikker
                if(y >= 5 && (x + y) % 2 != 0){
                    piece = makePiece(PieceType.WHITE, x, y);
                }

                if(piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }

            }

        }

        //Rekkef�lgen p� gruppene er viktig! de blir malt i rekkef�lge
        gamePane.getChildren().addAll(tileGroup, pieceGroup);
        gamePane.setTranslateX(Tile.TILE_SIZE);
        gamePane.setTranslateY(Tile.TILE_SIZE);
        return gamePane;
    }

    private Piece makePiece(PieceType type, int x, int y) {

        Piece piece = new Piece(type);
        //plasserer brikken p� riktig startposisjon
        PieceLogic.getPL().move(x , y, piece);
        //bruker Lambda og metode referanse for � sette logikk p� brikkene
        piece.setOnMousePressed(PieceLogic.getPL()::findPiece);
        piece.setOnMouseDragged(e -> PieceLogic.getPL().dragPiece(e, piece));
        piece.setOnMouseReleased(e-> PieceLogic.getPL().setPiece(piece, boardTiles, pieceGroup));
        return piece;

    }

    public Group getPieceGroup() {
        return pieceGroup;
    }
    
    public Tile[][] getBoardTiles(){
    	return boardTiles;
    }

    public static Board getBoard() {
        //lazy load
        if(board == null){
            //threadsafe
            synchronized (Board.class){
                board = new Board();
            }
        }
        return board;
    }
}
