package com.example.petr.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MenuActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu_activity);
    }

    public void playGame(View v)
    {
        Intent intent = new Intent(this, DifficultyActivity.class);
        startActivity(intent);
    }


    public void exitApp(View v)
    {
        ActivityCompat.finishAffinity(this);
        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(1);
    }
}