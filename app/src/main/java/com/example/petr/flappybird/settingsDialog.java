package com.example.petr.flappybird;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;


public class settingsDialog extends Activity
{
    private static CheckBox soundsCB;
    private static CheckBox musicCB;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_dialog);
        this.setFinishOnTouchOutside(true);

        soundsCB = findViewById(R.id.cbSounds);
        musicCB = findViewById(R.id.cbMusic);

        soundsCB.setChecked(MenuActivity.soundsEnabled);
        musicCB.setChecked(MenuActivity.musicEnabled);
    }



    public void checkboxClicked(View v)
    {
        boolean checked = ((CheckBox)v).isChecked();

        switch(v.getId())
        {
            case R.id.cbSounds:
            {
                if(checked)
                {
                    soundsCB.setChecked(true);
                    MenuActivity.soundsEnabled = true;

                    //Log.d("NASTAVENI", "sound checked");
                }
                else
                {
                    soundsCB.setChecked(false);
                    MenuActivity.soundsEnabled = false;

                    //Log.d("NASTAVENI", "sound unchecked");
                }
                break;
            }
            case R.id.cbMusic:
            {
                if(checked)
                {
                    musicCB.setChecked(true);
                    MenuActivity.musicEnabled = true;

                    //Log.d("NASTAVENI", "music checked");
                }
                else
                {
                    musicCB.setChecked(false);
                    MenuActivity.musicEnabled = false;

                    //Log.d("NASTAVENI", "music unchecked");
                }
                break;
            }
        }
    }
}
