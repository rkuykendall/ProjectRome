package org.onemoreturn.rome.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.onemoreturn.rome.R;

import org.onemoreturn.rome.graphics.Sprite;

public class Tile {
    public Sprite mSprite;

    public int x;
    public int y;

    public int[] tileTypes = new int[] {
            R.drawable.hex_grass_grid,
            R.drawable.hex_sand_grid,
            R.drawable.hex_ice_grid
    };

    public Tile(int x, int y, Context mContext)
    {
        this.x = x;
        this.y = y;

        int texture = tileTypes[(x+y)%tileTypes.length];

        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), texture);
        mSprite = new Sprite(.25f*x + (y%2)*.125f - .7f, .145f*y, .25f, .25f, bmp);
        // bmp.recycle();
    }

    Sprite getSprite() {
        return mSprite;
    }

}
