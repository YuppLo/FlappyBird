package com.example.petr.flappybird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MenuActivity extends Activity
{
    String shared_prefs = "/data/data/com.example.petr.flappybird/shared_prefs";

    private static Button buttonSettings;

    public static boolean musicEnabled = true;
    public static boolean soundsEnabled = true;

    public static ArrayList<Integer> easyHighscores;
    public static ArrayList<Integer> normalHighscores;
    public static ArrayList<Integer> hardHighscores;

    private static boolean firstTimeLaunch = false;
    private static String easyPath = "easyScores.txt";
    private static String normalPath = "normalScores.txt";
    private static String hardPath = "hardScores.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu_activity);

        buttonSettings = (Button) findViewById(R.id.buttonSettings);

        if(firstTimeLaunch)
        {
            Log.d("SCORECKA", "ALREADY RAN ONCE");
        }
        else
        {
            Log.d("SCORECKA", "first time <3");
            LoadHighScores();

            firstTimeLaunch = true;
        }

        if(GameView.scoreChanged)
        {
            Log.d("SCORECKA", "getting scores from gameview");

            easyHighscores = GameView.ezHS;
            normalHighscores = GameView.noHS;
            hardHighscores = GameView.haHS;

            GameView.scoreChanged = false;
        }









       //easyHighscores = readScoresFromFile(this);
       /*if(easyHighscores == null)
       {
           easyHighscores.add(0);
           easyHighscores.add(0);
           easyHighscores.add(0);
       }*/

        Log.d("SCORECKA","Hydrogen");

        for (Integer score : easyHighscores)
        {
            Log.d("SCORECKA", Integer.toString(score));
        }

        Log.d("SCORECKA","Hydrogen");

        for (Integer score : normalHighscores)
        {
            Log.d("SCORECKA", Integer.toString(score));
        }

        Log.d("SCORECKA","Hydrogen");

        for (Integer score : hardHighscores)
        {
            Log.d("SCORECKA", Integer.toString(score));
        }



    }

    public void openHighscores(View v)
    {
        Intent intent = new Intent(this, HighscoresActivity.class);
        startActivity(intent);
    }

    public void playGame(View v)
    {
        Intent intent = new Intent(this, DifficultyActivity.class);
        startActivity(intent);
    }

    public void openSettings(View v)
    {
        Intent intent = new Intent(this, settingsDialog.class);
        startActivity(intent);
    }

    public void LoadHighScores()
    {
        easyHighscores = new ArrayList<Integer>();
        normalHighscores = new ArrayList<Integer>();
        hardHighscores = new ArrayList<Integer>();

        ArrayList<Integer> tempEasy = readScoresFromFile(this, easyPath);
        if(tempEasy == null)
        {
            easyHighscores.add(0);
            easyHighscores.add(0);
            easyHighscores.add(0);
        }
        else
        {
            easyHighscores = tempEasy;
        }

        ArrayList<Integer> tempNormal = readScoresFromFile(this, normalPath);
        if(tempNormal == null)
        {
            normalHighscores.add(0);
            normalHighscores.add(0);
            normalHighscores.add(0);
        }
        else
        {
            normalHighscores = tempNormal;
        }

        ArrayList<Integer> tempHard = readScoresFromFile(this, hardPath);
        if(tempHard == null)
        {
            hardHighscores.add(0);
            hardHighscores.add(0);
            hardHighscores.add(0);
        }
        else
        {
            hardHighscores = tempHard;
        }
    }

    public void SaveHighScores()
    {
        easyHighscores = GameView.ezHS;
        normalHighscores = GameView.noHS;
        hardHighscores = GameView.haHS;

        saveScoresToFile(this,easyHighscores, easyPath);
        saveScoresToFile(this,normalHighscores, normalPath);
        saveScoresToFile(this,hardHighscores, hardPath);
    }





    public void exitApp(View v)
    {
        Log.d("SCORECKA","exit is called");

        SaveHighScores();

        ActivityCompat.finishAffinity(this);
    }


    private static ArrayList<Integer> readScoresFromFile(Context context, String path)
    {
        Log.d("SCORECKA", "NACITAM");
        /*for (Integer score : easyHighscores)
        {
            Log.d("SCORECKA", Integer.toString(score));
        }*/
        ArrayList<Integer> savedArrayList = null;
        try
        {
            FileInputStream inputStream = context.openFileInput(path);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            savedArrayList = (ArrayList<Integer>) in.readObject();
            in.close();
            inputStream.close();

        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        Log.d("SCORECKA", "NACTENO");
        return savedArrayList;
    }

    public void saveScoresToFile(Context context, ArrayList<Integer> arraylist, String path)
    {
        Log.d("SCORECKA", "UKLADAM");
        /*for (Integer score : easyHighscores)
        {
            Log.d("SCORECKA", Integer.toString(score));
        }*/
        try
        {
            FileOutputStream fileOutputStream = context.openFileOutput(path, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(arraylist);
            out.close();
            fileOutputStream.close();
            Log.d("SCORECKA", "ULOZENO");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}