package com.example.petr.flappybird;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import com.example.petr.flappybird.MainThread;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Toast;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static android.content.Context.VIBRATOR_SERVICE;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private BirdSprite birdSprite;
    private Drawable backgroundImage;
    public PipeSprite pipe1,pipe2,pipe3;
    private MediaPlayer mpTap;

    private boolean firstLaunch = true;

    public Bitmap bmp;
    public Bitmap bmp2;

    public static String chosenDiff;

    public static int gapHeight;
    public static int velocity;
    public static int pipesGap;

    public static ArrayList<Integer> ezHS;
    public static ArrayList<Integer> noHS;
    public static ArrayList<Integer> haHS;

    public static boolean scoreChanged = false;

    public static String status = "Swipe right to start";

    public static int score;

    public static boolean gameStarted = false;


    public static int BirdWidth = 150;
    public static int BirdHeight = 120;
    public static int PipesWidth = 150;

    public int biggestXpipe = 0;


    AlertDialog.Builder builder;

    ArrayList<PipeSprite> pipes = new ArrayList<>();


    Paint scorePaint;

    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    public GameView(final Context context)
    {
        super(context);

        backgroundImage = context.getResources().getDrawable(R.drawable.background);

        getHolder().addCallback(this);

        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(80);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setShadowLayer(1,0,0,Color.BLACK);
        scorePaint.setTextAlign(Paint.Align.CENTER);

        ezHS = MenuActivity.easyHighscores;
        noHS = MenuActivity.normalHighscores;
        haHS = MenuActivity.hardHighscores;



        thread = new MainThread(getHolder(), this);
        setFocusable(true);


        getRootView().setOnTouchListener(new OnSwipeTouchListener(context)
        {
            public void onDowns()
            {
                doOnTap();
                //Log.d("GESTURES", "single tap");
            }

            public void onSwipeRight()
            {
                if (!gameStarted)
                {
                    //Log.d("GESTURES", "swipe right");
                    new CountDownTimer(4000, 100)
                    {
                        public void onTick(long millisUntilFinished)
                        {
                            Long temp = millisUntilFinished / 1000;
                            if(temp != 0) { status = Long.toString(temp); }
                            else { status = "DONE"; }
                        }

                        public void onFinish()
                        {
                            status = "DONE";
                        }

                    }.start();


                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            gameStarted = true;

                        }
                    }, 3000);
                }
            }
        });

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        bmp = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_down), PipesWidth, screenHeight);
        bmp2 = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up), PipesWidth, screenHeight);

        mpTap = MediaPlayer.create(getContext(),R.raw.tap);

        builder = new AlertDialog.Builder(getContext());

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
                thread.interrupt();
                thread = null;
                score = 0;
                gameStarted = false;
                status = "Swipe right to start";
            } catch (InterruptedException e) { e.printStackTrace(); }
            retry = false;
        }
    }

    public void update()
    {
       if(gameStarted)
       {
           logic();
           birdSprite.update();

           for (int i = 0; i < pipes.size(); i++)
           {
               pipes.get(i).update();
           }
       }
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if(canvas!=null)
        {
            Rect imageBounds = canvas.getClipBounds();

            backgroundImage.setBounds(imageBounds);
            backgroundImage.draw(canvas);
            birdSprite.draw(canvas);



            for (int i = 0; i < pipes.size(); i++)
            {
                pipes.get(i).draw(canvas);
            }

            canvas.drawText(Integer.toString(score), screenWidth/2,100,scorePaint);


            if(!gameStarted)
            {
                canvas.drawText(status, screenWidth/2,screenHeight/2,scorePaint);
            }


        }
    }


    public void doOnTap()
    {
        if(gameStarted)
        {
            if(!thread.isAlive())
            {
                resume();
            }

            if(MenuActivity.soundsEnabled)
            { tapSound(); }

            birdSprite.yVelocity += birdSprite.lift;
        }
    }


    public void setDifficulty()
    {
        switch(chosenDiff)
        {
            case "easy":
                birdSprite = new BirdSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bird_easy), BirdWidth, BirdHeight));
                gapHeight = 350;
                velocity = 15;
                pipesGap = 1000;
                break;
            case "normal":
                birdSprite = new BirdSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bird_normal), BirdWidth, BirdHeight));
                gapHeight = 325;
                velocity = 20;
                pipesGap = 850;
                break;
            case "hard":
                birdSprite = new BirdSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bird_hard), BirdWidth, BirdHeight));
                gapHeight = 300;
                velocity = 25;
                pipesGap = 700;
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
        pipe2 = new PipeSprite(bmp, bmp2, pipe1.xX+pipesGap+PipesWidth, RandomizePipe());
        pipes.add(pipe2);
        pipe3 = new PipeSprite(bmp, bmp2, pipe2.xX+pipesGap+PipesWidth, RandomizePipe());
        pipes.add(pipe3);

        //Log.d("SIZE", screenWidth+","+screenHeight);

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

    private void vibrate()
    {
        if (Build.VERSION.SDK_INT >= 26)
        {
            ((Vibrator) this.getContext().getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else
        {
            ((Vibrator) this.getContext().getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }

    public void resume()
    {
        thread.setRunning(true);
        thread.start();

    }

    public void pause()
    {
        if(thread!=null)
        {
            thread.interrupt();
            thread = null;
        }
    }

    public void logic()
    {
        //Log.d("PIPES", "Pocet trubek:"+pipes.size());
        //Log.d("PIPES", "Obtiznost:"+chosenDiff);
        Log.d("PIPES", "Score:"+score);

        for (int i = 0; i < pipes.size(); i++)
        {
            //Detect if the bird is touching one of the pipes
            if (birdSprite.y < pipes.get(i).yY || birdSprite.y+BirdHeight > pipes.get(i).yY+gapHeight)
            {
                if (birdSprite.x+BirdWidth > pipes.get(i).xX && birdSprite.x < pipes.get(i).xX + PipesWidth)
                {
                    vibrate();
                    endGame();
                }
            }

            if (pipes.get(i).isPassed == false)
            {
                if(birdSprite.x > pipes.get(i).xX+PipesWidth)
                {
                    score++;
                    pipes.get(i).isPassed = true;
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
                 pipes.get(i).isPassed = false;

                biggestXpipe = 0;
            }
        }

        //Detect if the bird has gone off the
        //bottom or top of the screen
        if (birdSprite.y + 120 < 0)
        { vibrate(); endGame(); }
        if (birdSprite.y > screenHeight)
        { vibrate(); endGame(); }
    }

    public int RandomizePipe()
    {
        Random r = new Random();
        int upperHeight = r.nextInt((int)(screenHeight*0.75));
        return upperHeight;
    }

    private static Integer findMinNumberFromArrayList(ArrayList<Integer> list)
    {
        Integer min = Integer.MAX_VALUE;
        for (Integer number : list)
        {
            if(number < min)
            {
                min = number;
            }
        }
        return min;
    }

    public boolean addScore(ArrayList<Integer> list)
    {
        boolean isNewHS = false;

        list.add(score);

        Integer tempMin = findMinNumberFromArrayList(list);

        if(tempMin != score)
        {
            isNewHS = true;
        }

        list.remove(Integer.valueOf(tempMin));

        return isNewHS;
    }

    public void endGame()
    {
        String endGameStatus = "Not bad!";  //default value
        String newHS = "That's a new high score!";
        String notNewHS = "You could have done better.";

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        }
        else
        {
            builder = new AlertDialog.Builder(getContext());
        }*/


        thread.setRunning(false);


        //Log.d("SCORECKA", chosenDiff);

        if(chosenDiff.equals("easy"))
        {
            if(addScore(ezHS))
            {
                endGameStatus = "Your final score is "+score+"! "+ newHS;
            }
            else { endGameStatus = "Your final score is "+score+"! "+ notNewHS; }
        }
        else if(chosenDiff.equals("normal"))
        {
            if(addScore(noHS))
            {
                endGameStatus = "Your final score is "+score+"! "+ newHS;
            }
            else { endGameStatus = "Your final score is "+score+"! "+ notNewHS; }
        }
        else if(chosenDiff.equals("hard"))
        {
            if(addScore(haHS))
            {
                endGameStatus = "Your final score is "+score+"! "+ newHS;
            }
            else { endGameStatus = "Your final score is "+score+"! "+ notNewHS; }
        }

        scoreChanged = true;


        builder.setTitle("Game Over")
                .setMessage(endGameStatus)
                .setPositiveButton("Play again", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {
                        resetLevel();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ((Activity) getContext()).finish();
                    }
                });

        builder.setCancelable(false);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                builder.show();
            }
        }, 500);
    }


    public void resetLevel()
    {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

        score = 0;
        birdSprite.y = 100;

        pipe1.xX = screenWidth;
        pipe1.yY = RandomizePipe();
        pipe2.xX = pipe1.xX+pipesGap+150;
        pipe2.yY = RandomizePipe();
        pipe3.xX = pipe2.xX+pipesGap+150;
        pipe3.yY = RandomizePipe();

        gameStarted = false;
        new CountDownTimer(4000, 100)
        {
            public void onTick(long millisUntilFinished)
            {
                Long temp = millisUntilFinished / 1000;
                if(temp != 0) { status = Long.toString(temp); }
                else { status = "DONE"; }
            }

            public void onFinish()
            {
                status = "DONE";
            }

        }.start();


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                gameStarted = true;

            }
        }, 3000);
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
