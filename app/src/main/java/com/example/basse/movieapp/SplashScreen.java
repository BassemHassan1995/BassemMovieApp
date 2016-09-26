package com.example.basse.movieapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                checkConnection();

                // close this activity
            }
        }, SPLASH_TIME_OUT);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Android Insomnia Regular.ttf");
        TextView textView = (TextView) findViewById(R.id.splach_text_view);
        textView.setTypeface(typeface);
    }

    private void checkConnection() {
        // first, check connectivity
        if (DetectConnection
                .checkInternetConnection(SplashScreen.this)) {
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            finish();
            // do things if it there's network connection
        } else {
            // as it seems there's no Internet connection
            // ask the user to activate it
            new AlertDialog.Builder(SplashScreen.this)
                    .setTitle("Connection failed")
                    .setMessage("This application requires network access. Please, enable " +
                            "mobile network or Wi-Fi.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // THIS IS WHAT YOU ARE DOING, Jul
                            SplashScreen.this.startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SplashScreen.this.finish();
                        }
                    })
                    .show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            checkConnection();
        }
    }


}