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
import android.media.MediaPlayer;
import android.util.Log;
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
    public PipeSprite pipe1,pipe2,pipe3;
    private MediaPlayer mpTap;

    public Bitmap bmp;
    public Bitmap bmp2;

    public static String chosenDiff;

    public static int gapHeight = 350;
    public static int velocity = 25;
    public static int pipesGap = 800;

    public static int BirdWidth = 150;
    public static int BirdHeight = 120;

    public int biggestXpipe = 0;

    ArrayList<PipeSprite> pipes = new ArrayList<>();


    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    public GameView(Context context)
    {
        super(context);

        backgroundImage = context.getResources().getDrawable(R.drawable.background);

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
        bmp = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_down), 150, screenHeight);
        bmp2 = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up), 150, screenHeight);

        mpTap = MediaPlayer.create(getContext(),R.raw.tap);
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

        for (int i = 0; i < pipes.size(); i++)
        {
            pipes.get(i).update();
        }
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

            for (int i = 0; i < pipes.size(); i++)
            {
                pipes.get(i).draw(canvas);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        tapSound();
        birdSprite.yVelocity += birdSprite.lift;

        return super.onTouchEvent(event);
    }


    public void setDifficulty()
    {
        switch(chosenDiff)
        {
            case "easy":
                birdSprite = new BirdSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bird_easy), BirdWidth, BirdHeight));
                break;
            case "normal":
                birdSprite = new BirdSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bird_normal), BirdWidth, BirdHeight));
                break;
            case "hard":
                birdSprite = new BirdSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bird_hard), BirdWidth, BirdHeight));
                break;
            default:
        }
    }


    private void makeLevel()
    {
       setDifficulty();

        int y;
        int x;


        pipe1 = new PipeSprite(bmp, bmp2, screenWidth, RandomizePipe());
        pipes.add(pipe1);
        pipe2 = new PipeSprite(bmp, bmp2, pipe1.xX+pipesGap+150, RandomizePipe());
        pipes.add(pipe2);
        pipe3 = new PipeSprite(bmp, bmp2, pipe2.xX+pipesGap+150, RandomizePipe());
        pipes.add(pipe3);

        Log.d("SIZE", screenWidth+","+screenHeight);

    }

    private void tapSound()
    {
        if (mpTap.isPlaying())
        {
            mpTap.seekTo(0);
        }
        else
        {
            mpTap.start();
        }
    }

    public void logic()
    {
        Log.d("PIPES", "Pocet trubek:"+pipes.size());
        Log.d("PIPES", "Obtiznost:"+chosenDiff);

        for (int i = 0; i < pipes.size(); i++)
        {
            //Detect if the bird is touching one of the pipes
            if (birdSprite.y < pipes.get(i).yY || birdSprite.y+BirdHeight > pipes.get(i).yY+gapHeight)
            {
                if (birdSprite.x+BirdWidth > pipes.get(i).xX && birdSprite.x < pipes.get(i).xX + 150)
                {
                    resetLevel();
                }
            }


            //Detect if the pipe has gone off the left of the screen and regenerate further ahead
            if (pipes.get(i).xX + 150 < 0)
            {
                for (int j = 0; j < pipes.size(); j++)
                {
                    if (biggestXpipe < pipes.get(j).xX)
                    {
                        biggestXpipe = pipes.get(j).xX;
                    }
                }

                 pipes.get(i).xX = biggestXpipe + pipesGap;
                 pipes.get(i).yY = RandomizePipe();

                biggestXpipe = 0;
            }
        }

        //Detect if the bird has gone off the
        //bottom or top of the screen
        if (birdSprite.y + 120 < 0)
        { resetLevel(); }
        if (birdSprite.y > screenHeight)
        { resetLevel(); }
    }

    public int RandomizePipe()
    {
        Random r = new Random();
        int upperHeight = r.nextInt((int)(screenHeight*0.75));
        return upperHeight;
    }

    public void resetLevel()
    {
        birdSprite.y = 100;

        pipe1.xX = screenWidth;
        pipe1.yY = RandomizePipe();
        pipe2.xX = pipe1.xX+pipesGap+150;
        pipe2.yY = RandomizePipe();
        pipe3.xX = pipe2.xX+pipesGap+150;
        pipe3.yY = RandomizePipe();
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
