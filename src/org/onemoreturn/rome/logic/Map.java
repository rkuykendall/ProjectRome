package org.onemoreturn.rome.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.onemoreturn.rome.R;
import org.onemoreturn.rome.graphics.Sprite;
import org.onemoreturn.rome.logic.Tile;

public class Map {
    public Tile[] tiles;
    public int cols;
    public int rows;

    public Map(int cols, int rows)
    {
        this.cols = cols;
        this.rows = rows;

        tiles = new Tile[rows*cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                tiles[y*cols+x] = new Tile(x, y);
            }
        }
    }

    public Sprite[] getSprites(Context mContext) {
        Sprite[] mSprites = new Sprite[rows * cols];

        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.hex_sand_grid);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Sprite mSprite = new Sprite(.25f*x + (y%2)*.125f - .7f, .145f*y, .25f, .25f, bmp);
                mSprites[(rows-y-1)*cols+x] = mSprite;
            }
        }

//        bmp.recycle();
        return mSprites;
    }
}
