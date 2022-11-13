package com.example.myadmobpractise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

/* Notes on Rewarded Interstitial Ads:
    # Resources for Rewarded Interstitial Ads ->
    (1) Part 1: https://www.youtube.com/watch?v=xy-AfNcXkJ4, Part 2: https://www.youtube.com/watch?v=BqUgaWro4G8
    (2) https://www.youtube.com/watch?v=_yz3gAVdbyA
    # What are Rewarded Interstitial Ads:
    They are skip-able ads unlike Rewarded Ads which cannot be skipped, and no opt-in is required i.e. ideally these ads
    should be triggered the app itself instead of the user having to trigger them with a button click etc. Another thing to
    note here is that if the user wants to skip the video, we can show the user a dialog which will say something like,
    'If you close the ad now, you won't get reward'.
    # When to use Rewarded Interstitial Ads:
    (1) We can consider replacing Rewarded Interstitial Ads (a) at start, (b) in-between or (c) at the end of levels for
    better UX instead interstitial ads.
    (2) For e.g. in the X2 2048 Game, there is something called as an 'Ad Break' which is triggered by itself when the user
    has completed 5-10 minutes of uninterrupted gameplay, which shows ad with dialog first that the user can get a reward
    of +5 or +10 coins etc. if the user watches the completed ad.
*/
public class ThirdActivity extends AppCompatActivity {
    private int coins;
    private AppCompatTextView coinsCountTextView;
    private AdRequest adRequest;
    private RewardedInterstitialAd rewardedInterstitialAd;

    private void initialise() {
        coins = 0;
        coinsCountTextView = findViewById(R.id.coins_count_text_view_third_activity);
        coinsCountTextView.setText("Coins Count = " + coins);
        adRequest = new AdRequest.Builder().build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        initialise();

        loadRewardedInterstitialAd();
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
        showRewardInterstitialAd();
    }

    private void loadRewardedInterstitialAd() {
        if (rewardedInterstitialAd == null) {
            RewardedInterstitialAd.load(this, "ca-app-pub-3940256099942544/5354046379", adRequest,
                new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {/* Called when a click is recorded for an ad. */}
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed. Set the ad reference to null so to not show the ad a 2nd time.
                                rewardedInterstitialAd = null;
                                loadRewardedInterstitialAd(); // Pre-load this ad for the next time
                            }
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) { // Called when ad fails to show.
                                rewardedInterstitialAd = null;
                            }
                            @Override
                            public void onAdImpression() {/* Called when an impression is recorded for an ad.*/}
                            @Override
                            public void onAdShowedFullScreenContent() {/* Called when ad is shown. */}
                        });
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) { // Handle the error.
                        rewardedInterstitialAd = null;
                        loadRewardedInterstitialAd();
                    }
                }
            );
        }
    }

    private void showRewardInterstitialAd() {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    coins += 10;
                    coinsCountTextView.setText("Coins Count = " + coins);
                    Toast.makeText(ThirdActivity.this, "Rewarded +10 coins", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // The toast message string contains an acceptable message for the user if reward ad hasn't loaded yet.
            Toast.makeText(this, "This ad isn't available in your area", Toast.LENGTH_SHORT).show();
            loadRewardedInterstitialAd();
            /* TODO -> Loading the rewarded interstitial ad again if it not available and displaying a toast message is
                       fine for this hands-on practise project, but in a real project we should ideally trigger the rewarded
                       interstitial ad by ourselves and of-course we should not try to show this ad until it's loading is
                       completed.
            */
            /* TODO -> Ensure there does exist mechanism to show a dialog where we tell the user that they will not receive
                       a reward if they skip the ad mid-way, if they were trying to skip the ad mid-way.
            */
        }
    }
}