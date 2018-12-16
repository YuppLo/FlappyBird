package com.example.petr.flappybird;

import android.app.Activity;
import android.content.Intent;
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

public class MenuActivity extends Activity
{
    private static Button buttonSettings;

    public static boolean musicEnabled = true;
    public static boolean soundsEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu_activity);

        buttonSettings = (Button) findViewById(R.id.buttonSettings);

        Log.d("NASTAVENI", "AYY LMAO");

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





    public void exitApp(View v)
    {
        ActivityCompat.finishAffinity(this);
        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(1);
    }
}