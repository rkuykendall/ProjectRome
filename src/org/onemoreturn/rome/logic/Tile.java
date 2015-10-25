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
    public int type;

    public float imgW = 65f;
    public float imgH = 89f;
    public float imgRatio = imgH / imgW;

    public int[] typeTextures = new int[] {
            R.drawable.hex_grass_grid,
            R.drawable.hex_sand_grid,
            R.drawable.hex_ice_grid
    };

    public Tile(int x, int y, Context mContext)
    {
        this.x = x;
        this.y = y;

        // This will eventually represent tye type of tile.
        this.type = (x+y)%typeTextures.length;

        float tileW = .25f;
        float tileH = .25f * imgRatio;
        float lineH = tileH * (36f/65f);

        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), typeTextures[type]);
        mSprite = new Sprite(
                tileW*x + (y%2)*(tileW/2), lineH*y*-1,
                tileW, tileH,
                bmp);
        // bmp.recycle();
    }

    Sprite getSprite() {
        return mSprite;
    }

}
