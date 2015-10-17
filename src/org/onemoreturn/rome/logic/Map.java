package org.onemoreturn.rome.logic;

import android.content.Context;

import org.onemoreturn.rome.graphics.Sprite;

public class Map {
    public Tile[] tiles;
    public int cols;
    public int rows;

    public Map(int cols, int rows, Context mContext)
    {
        this.cols = cols;
        this.rows = rows;

        tiles = new Tile[rows*cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                tiles[y*cols+x] = new Tile(x, y, mContext);
            }
        }
    }

    public Sprite[] getSprites() {
        Sprite[] mSprites = new Sprite[rows * cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                // Render backwards so lower tiles are on top?
                mSprites[(rows-y-1)*cols+x] = tiles[(y*rows)+x].getSprite();
            }
        }

        return mSprites;
    }
}
