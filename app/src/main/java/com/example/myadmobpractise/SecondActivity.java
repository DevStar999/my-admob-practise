package com.example.myadmobpractise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class SecondActivity extends AppCompatActivity {
    private int coins;
    private AppCompatTextView coinsCountTextView;
    private AdRequest adRequest;
    private RewardedAd rewardedAd;

    private void initialise() {
        coins = 0;
        coinsCountTextView = findViewById(R.id.coins_count_text_view);
        coinsCountTextView.setText("Coins Count = " + coins);
        adRequest = new AdRequest.Builder().build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initialise();

        loadRewardedAd();
    }

    @Override
    public void onBackPressed() {
        // Calling finish() method to mimic the code flows in 'Feed the Animal' and '2048 Champs' projects
        finish();
    }

    public void gotoMainActivity(View view) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void rewardAdButtonClicked(View view) {
        showRewardedAd();
    }

    private void loadRewardedAd() {
        if (rewardedAd == null) {
            RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                    adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    SecondActivity.this.rewardedAd = rewardedAd;
                    SecondActivity.this.rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {/* Called when a click is recorded for an ad. */}
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed. Set the ad reference to null so you don't show the ad a 2nd time.
                            SecondActivity.this.rewardedAd = null;
                            loadRewardedAd(); // Pre-load the reward ad for the next time
                        }
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) { // Called when ad fails to show.
                            SecondActivity.this.rewardedAd = null;
                        }
                        @Override
                        public void onAdImpression() {/* Called when an impression is recorded for an ad. */}
                        @Override
                        public void onAdShowedFullScreenContent() {/* Called when ad is shown. */}
                    });
                }
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) { // Handle the error.
                    rewardedAd = null;
                    loadRewardedAd();
                }
            });
        }
    }

    private void showRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    coins += 10;
                    coinsCountTextView.setText("Coins Count = " + coins);
                    Toast.makeText(SecondActivity.this, "Rewarded +10 coins", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // The toast message string contains an acceptable message for the user if reward ad hasn't loaded yet.
            Toast.makeText(this, "This ad isn't available in your area", Toast.LENGTH_SHORT).show();
            loadRewardedAd();
            /* TODO -> Loading the reward ad again if it not available and displaying a toast message is fine for this
                       hands-on practise project, but in a real project we should ideally not show the button, view etc.
                       which is the trigger to show the reward ad, until the loading of the reward ad has completed.
            */
        }
    }
}