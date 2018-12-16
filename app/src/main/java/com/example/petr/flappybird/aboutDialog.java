package com.example.petr.flappybird;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class aboutDialog extends Activity
{
    private static TextView txt1;
    private static TextView txt2;
    private static TextView txt3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_dialog);
        this.setFinishOnTouchOutside(true);

        txt1 = findViewById(R.id.tvAbout1);
        txt1.setText("This simple 2D game inspired by Flappy Bird was created by Petr Kocur as a project for TAMZ 2 course.");

        txt2 = findViewById(R.id.tvAbout2);
        txt2.setText("The art, including birds, background, and pipes, was created by Kristýna Zimmerová. Thank you!");

        txt3 = findViewById(R.id.tvAbout3);
        txt3.setText("Settings and About icons were made by Gregor Cresnar and Freepik from www.flaticon.com, respectively.");
    }
}
