package com.example.petr.flappybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class endGameDialog extends Activity
{
    private static TextView tvEndStatus;
    private static TextView tvFinalScore;

    public static String endStatus;
    public static int finalScore;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endgame_dialog);
        this.setFinishOnTouchOutside(true);

        tvEndStatus = findViewById(R.id.textViewEndStatus);
        tvFinalScore = findViewById(R.id.textViewFinalScore);

        tvEndStatus.setText(endStatus);
        tvFinalScore.setText(Integer.toString(finalScore));
    }


    public void playAgain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToMenu()
    {
        this.finish();
        overridePendingTransition(0,0);
        /*Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);*/
    }

}
