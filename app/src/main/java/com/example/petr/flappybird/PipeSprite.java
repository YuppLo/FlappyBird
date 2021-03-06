package com.example.petr.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class PipeSprite
{

    private Bitmap image;
    private Bitmap image2;
    public int xX, yY;
    public boolean isPassed = false;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public PipeSprite (Bitmap bmp, Bitmap bmp2, int x, int y)
    {
        image = bmp;
        image2 = bmp2;
        yY = y;
        xX = x;
    }


    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, xX, yY-screenHeight, null);
        canvas.drawBitmap(image2,xX, yY+GameView.gapHeight, null);

    }
    public void update()
    {

        xX -= GameView.velocity;
    }

}