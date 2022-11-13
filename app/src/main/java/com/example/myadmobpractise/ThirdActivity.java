package com.example.myadmobpractise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class ThirdActivity extends AppCompatActivity {
    private int coins;
    private AppCompatTextView coinsCountTextView;

    private void initialise() {
        coins = 0;
        coinsCountTextView = findViewById(R.id.coins_count_text_view_third_activity);
        coinsCountTextView.setText("Coins Count = " + coins);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        initialise();
    }

    @Override
    public void onBackPressed() {
        // Calling finish() method to mimic the code flows in 'Feed the Animal' and '2048 Champs' projects
        finish();
    }

    public void gotoMainActivity(View view) {
        Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoSecondActivity(View view) {
        Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoFourthActivity(View view) {
        Intent intent = new Intent(ThirdActivity.this, FourthActivity.class);
        startActivity(intent);
        finish();
    }

    public void rewardInterstitialAdButtonClicked(View view) {
        Toast.makeText(this, "Reward Interstitial Ad Clicked", Toast.LENGTH_SHORT).show();
    }
}