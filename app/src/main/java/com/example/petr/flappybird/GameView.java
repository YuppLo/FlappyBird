package com.example.petr.flappybird;

import android.content.Context;
import com.example.petr.flappybird.MainThread;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private BirdSprite birdSprite;
    private Drawable backgroundImage;
    //private Drawable pipe_down;
    //private Drawable pipe_up;
    public PipeSprite pipe1,pipe2,pipe3;

    public static int gapHeight = 350;
    public static int velocity = 25;

    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    public GameView(Context context)
    {
        super(context);

        backgroundImage = context.getResources().getDrawable(R.drawable.background);
        //pipe_down = context.getResources().getDrawable(R.drawable.pipe_down);
        //ipe_up = context.getResources().getDrawable(R.drawable.pipe_up);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //birdSprite = new BirdSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.birdhard), 150, 150));

        makeLevel();

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while (retry)
        {
            try
            {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) { e.printStackTrace(); }
            retry = false;
        }
    }

    public void update()
    {
        logic();
        birdSprite.update();
        pipe1.update();
        pipe2.update();
        pipe3.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if(canvas!=null)
        {
            Rect imageBounds = canvas.getClipBounds();  // Adjust this for where you want it

            backgroundImage.setBounds(imageBounds);
            backgroundImage.draw(canvas);
            birdSprite.draw(canvas);
            pipe1.draw(canvas);
            pipe2.draw(canvas);
            pipe3.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        birdSprite.yVelocity += birdSprite.lift;
        //birdSprite.y = birdSprite.y - (birdSprite.yVelocity * 10);
        return super.onTouchEvent(event);
    }

    private void makeLevel()
    {
        birdSprite = new BirdSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bird_hard), 170, 120));
        Bitmap bmp;
        Bitmap bmp2;
        int y;
        int x;
        bmp = getResizedBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.pipe_down), 250, Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up), 250, Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        pipe1 = new PipeSprite(bmp, bmp2, 2000, 100);
        pipe2 = new PipeSprite(bmp, bmp2, 5000, 100);
        pipe3 = new PipeSprite(bmp, bmp2, 3500, 100);

    }

    public void logic()
    {
        ArrayList<PipeSprite> pipes = new ArrayList<PipeSprite>();
        pipes.add(pipe1);
        pipes.add(pipe2);
        pipes.add(pipe3);

        for (int i = 0; i < pipes.size(); i++)
        {
            //Detect if the bird is touching one of the pipes
            if (birdSprite.y < pipes.get(i).yY + (screenHeight / 2)
                    - (gapHeight / 2) && birdSprite.x + 170 > pipes.get(i).xX
                    && birdSprite.x < pipes.get(i).xX + 250) {
                resetLevel();
            } else if (birdSprite.y + 120 > (screenHeight / 2) +
                    (gapHeight / 2) + pipes.get(i).yY
                    && birdSprite.x + 300 > pipes.get(i).xX
                    && birdSprite.x < pipes.get(i).xX + 250) {
                resetLevel();
            }

            //Detect if the pipe has gone off the left of the
            //screen and regenerate further ahead
            if (pipes.get(i).xX + 250 < 0) {
                Random r = new Random();
                int value1 = r.nextInt(500);
                int value2 = r.nextInt(500);
                pipes.get(i).xX = screenWidth + value1 + 1000;
                pipes.get(i).yY = value2 - 250;
            }
        }

        //Detect if the bird has gone off the
        //bottom or top of the screen
        if (birdSprite.y + 240 < 0) {
            resetLevel(); }
        if (birdSprite.y > screenHeight) {
            resetLevel(); }
    }

    public void resetLevel()
    {
        birdSprite.y = 100;
        pipe1.xX = 2000;
        pipe1.yY = 0;
        pipe2.xX = 4500;
        pipe2.yY = 200;
        pipe3.xX = 3200;
        pipe3.yY = 250;

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
