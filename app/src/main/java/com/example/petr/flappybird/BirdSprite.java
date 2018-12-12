package com.example.petr.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BirdSprite
{
    private Bitmap image;
    public int x,y;

    private int xVelocity = 10;
    public double yVelocity = 0;
    public double gravity = 3;
    public double lift = -20;

    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

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
        yVelocity +=gravity;
        yVelocity *= 0.8;
        y += yVelocity;
    }
}