package com.example.petr.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighscoresActivity extends Activity
{
    private ListView listViewEasy, listViewNormal, listViewHard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.highscores_activity);

        listViewEasy = (ListView) findViewById(R.id.listViewEasy);
        listViewNormal = (ListView) findViewById(R.id.listViewNormal);
        listViewHard = (ListView) findViewById(R.id.listViewHard);

        sortScores();


        ArrayAdapter<Integer> arrayAdapterEasy = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_list_item_1,
                MenuActivity.easyHighscores );

        listViewEasy.setAdapter(arrayAdapterEasy);

        ArrayAdapter<Integer> arrayAdapterNormal = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_list_item_1,
                MenuActivity.normalHighscores );

        listViewNormal.setAdapter(arrayAdapterNormal);

        ArrayAdapter<Integer> arrayAdapterHard = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_list_item_1,
                MenuActivity.hardHighscores );

        listViewHard.setAdapter(arrayAdapterHard);
    }

    public void sortScores()
    {
        Collections.sort(MenuActivity.easyHighscores, new Comparator<Integer>()
        {
            public int compare(Integer one, Integer other)
            {
                if (one >= other) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        Collections.sort(MenuActivity.normalHighscores, new Comparator<Integer>()
        {
            public int compare(Integer one, Integer other)
            {
                if (one >= other) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        Collections.sort(MenuActivity.hardHighscores, new Comparator<Integer>()
        {
            public int compare(Integer one, Integer other)
            {
                if (one >= other) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }




    public void backToMenu(View v)
    {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}