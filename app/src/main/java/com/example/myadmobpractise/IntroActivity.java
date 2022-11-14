package com.example.myadmobpractise;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/** Splash Activity that inflates splash activity xml. */
public class IntroActivity extends AppCompatActivity {
    /**
     * Number of seconds to count down before showing the app open ad. This simulates the time needed
     * to load the app.
     */
    private static final long COUNTER_TIME = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Create a timer so the IntroActivity will be displayed for a fixed amount of time.
        createTimer();
    }

    /**
     * Create the countdown timer, which counts down to zero and show the app open ad.
     */
    private void createTimer() {
        final TextView counterTextView = findViewById(R.id.timer);

        new CountDownTimer(IntroActivity.COUNTER_TIME * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = ((millisUntilFinished / 1000) + 1);
                counterTextView.setText("App is done loading in: " + secondsRemaining);
            }

            @Override
            public void onFinish() {
                counterTextView.setText("Done.");
                startMainActivity();
            }
        }.start();
    }

    /** Start the MainActivity. */
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}