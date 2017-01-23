package logic;

/**
 * Created by Webby Debby on 11/19/2016.
 */
class MoveResult {
    private final MoveType type;
    
    MoveType getType() {
        return type;
    }

    MoveResult(MoveType type) {
        this.type = type;
    }

}
