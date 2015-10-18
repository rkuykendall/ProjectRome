package org.onemoreturn.rome.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.onemoreturn.rome.R;

import org.onemoreturn.rome.graphics.Sprite;

import java.util.Random;

public class Tile {
    public Sprite mSprite;

    public int x;
    public int y;
    public int state;

    public Tile(int x, int y)
    {
        Random rnd = new Random();
        this.state = rnd.nextInt(2);
        this.x = x;
        this.y = y;
    }

//    Sprite getSprite() {
//        return this.state;
//    }

}
