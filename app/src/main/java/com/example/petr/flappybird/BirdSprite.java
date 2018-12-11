package com.example.petr.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BirdSprite
{
    private Bitmap image;
    private int x,y;

    public BirdSprite(Bitmap bmp)
    {
        image = bmp;
        x = 100;
        y = 100;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update()
    {
        y++;
    }
}