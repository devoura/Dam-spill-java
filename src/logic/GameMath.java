package logic;

import static model.Tile.TILE_SIZE;

/**
 * Created by Webby Debby on 11/19/2016.
 */

//all matte som brukes i mer enn en klasse
//flyttes hit for gjenbruk.
//GameMath er en singleton
class GameMath {

    private GameMath(){}
    //tar en pixelverdi og oversetter til tiles
    static int toBoard(double pixel){
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }
}
