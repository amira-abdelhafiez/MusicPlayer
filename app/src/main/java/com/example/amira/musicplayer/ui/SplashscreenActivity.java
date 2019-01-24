package com.example.amira.musicplayer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.example.amira.musicplayer.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashscreenActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchHomeScreen();
            }
        } , SPLASH_DISPLAY_LENGTH);
    }

    private void launchHomeScreen(){
        Intent intent = new Intent(SplashscreenActivity.this , HomeActivity.class);
        startActivity(intent);
        this.finish();
    }
}
