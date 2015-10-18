package org.onemoreturn.rome.logic;

import android.content.Context;

import org.onemoreturn.rome.graphics.Sprite;

public class Map {
    public Tile[] tiles;
    public int cols = 0;
    public int rows = 0;

    public Map()
    {
        this.cols = 0;
        this.rows = 0;
        this.tiles = new Tile[0];
    }

    public Map(int cols, int rows)
    {
        this.cols = cols;
        this.rows = rows;

        this.tiles = new Tile[rows*cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                tiles[y*cols+x] = new Tile(x, y);
            }
        }
    }

//    public Sprite[] getSprites(Context mContext) {
//        Sprite[] mSprites = new Sprite[rows * cols];
//
//        for (int y = 0; y < rows; y++) {
//            for (int x = 0; x < cols; x++) {
//                mSprites[(rows-y-1)*cols+x] = tiles[(y*cols)+x].getSprite(mContext);
//            }
//        }
//
//        return mSprites;
//    }
}
