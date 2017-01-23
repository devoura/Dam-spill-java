package model;

/**
 * Created by Webby Debby on 11/19/2016.
 */
public enum PieceType {
    RED(1), WHITE(-1);

    public final int moveDir;

    PieceType(int moveDir){
        this.moveDir = moveDir;
    }
}
