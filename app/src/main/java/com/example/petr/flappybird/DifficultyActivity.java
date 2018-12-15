package com.example.petr.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DifficultyActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.difficulty_activity);
    }

    public void playEasy(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chosenDiff", "easy");
        startActivity(intent);
    }

    public void playNormal(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chosenDiff", "normal");
        startActivity(intent);
    }

    public void playHard(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chosenDiff", "hard");
        startActivity(intent);
    }


    public void goBack(View v)
    {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}