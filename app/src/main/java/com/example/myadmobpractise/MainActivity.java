package com.example.myadmobpractise;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.example.myadmobpractise.MyApplication.OnShowAdCompleteListener;

/* TODO -> Although we have the code to show ads cooked up, when we will be actually placing ads in the real app,
           we still would need to learn and take decisions related to the following:
           (1) Frequency capping for App Open Ad, also make sure this add is not shown every time app is opened, only a few
           times. Use SharedPreferences to store the time when ad was shown and space out the instances properly when this ad
           is shown. Disable eCPM
           (2) In what fragments and activities do we place the Banner Ad (& Disable eCPM)
           (3) In what transitions do we place the Interstitial Ads (& Frequency capping, also make sure ad is loaded
           before we reach the point of time when it has to be shown, Disable eCPM)
           (4) For Rewarded Ads: Frequency capping, placement of where to keep ad, handle on ad load failed scenario & ensure
           that rewarded ads do get loaded as soon as possible, Disable eCPM
           (5) For Rewarded Interstitial Ads: Frequency capping, Disable eCPM, placement of where to keep ad, handle on ad
           load failed scenario, ensure that rewarded ads do get loaded as soon as possible & close ad reward cancel dialog
           mechanism exists
           (6) In general, learn about how to prevent invalid clicks and other safety tips related to AdMob
*/
public class MainActivity extends AppCompatActivity {
    private AdRequest adRequest;
    private AdView adView;
    private InterstitialAd interstitialAd;

    private void initialise() {
        adRequest = new AdRequest.Builder().build();
        adView = findViewById(R.id.banner_ad_adview);
    }

    private void showAppOpenAd() {
        Application application = getApplication();
        if ((application instanceof MyApplication)) {
            ((MyApplication) application).showAdIfAvailable(MainActivity.this, new OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {}
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();

        showAppOpenAd();

        loadBannerAd();

        loadInterstitialAd();
    }

    @Override
    public void onBackPressed() {
        // Showing Interstitial Ad when user decides to leave the app from MainActivity
        showInterstitialAd();

        // Calling finish() method to mimic the code flows in 'Feed the Animal' and '2048 Champs' projects
        finish();
    }
    
    public void gotoSecondActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
        finish();

        // Showing Interstitial Ad when user decides to switch to SecondActivity from MainActivity.
        showInterstitialAd();
    }

    public void gotoThirdActivity(View view) {
        Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
        startActivity(intent);
        finish();

        // Showing Interstitial Ad when user decides to switch to ThirdActivity from MainActivity.
        showInterstitialAd();
    }

    public void gotoFourthActivity(View view) {
        Intent intent = new Intent(MainActivity.this, FourthActivity.class);
        startActivity(intent);
        finish();

        // Showing Interstitial Ad when user decides to switch to FourthActivity from MainActivity.
        showInterstitialAd();
    }

    private void loadBannerAd() {
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() { // Code to be executed when the user clicks on an ad.
                super.onAdClicked();
                // TODO -> Implement logic to prevent users from clicking the ad too many times in a short period of time
                /* Note: If the user clicks on ad too many times in a short period of time, Google may suspect this as fishy
                         behaviour and this could lead to serious consequences like AdMob account suspension or termination
                         OR suspension or termination of App from Play Store. However, we need not worry too much about this
                         as well, since at first we will be warned by Google about this.
                */
            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) { // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(loadAdError);
                adView.loadAd(adRequest);
            }
            @Override
            public void onAdClosed() {/* Code to be executed when the user is about to return to the app after tapping on an ad. */}
            @Override
            public void onAdImpression() {/* Code to be executed when an impression is recorded for an ad. */}
            @Override
            public void onAdLoaded() {/* Code to be executed when an ad finishes loading. */}
            @Override
            public void onAdOpened() {/* Code to be executed when an ad opens an overlay that covers the screen. */}
        });
        adView.loadAd(adRequest);
    }

    private void loadInterstitialAd() {
        if (interstitialAd == null) {
            InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The interstitialAd reference will be null until an ad is loaded.
                            MainActivity.this.interstitialAd = interstitialAd;
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                                @Override
                                public void onAdClicked() {/* Called when a click is recorded for an ad. */}
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when ad is dismissed. Set the ad reference to null so to not show the ad
                                    // a second time.
                                    MainActivity.this.interstitialAd = null;
                                    /* Un-comment the line below in-case, we come across a scenario where preloading the
                                       ad is the expected behaviour
                                    */
                                    //loadInterstitialAd();
                                }
                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) { // When ad fails to show.
                                    MainActivity.this.interstitialAd = null;
                                }
                                @Override
                                public void onAdImpression() {/* Called when an impression is recorded for an ad. */}
                                @Override
                                public void onAdShowedFullScreenContent() {/* Called when ad is shown. */}
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) { // Handle the error
                            interstitialAd = null;
                            loadInterstitialAd();
                        }
                    }
            );
        }
    }

    private void showInterstitialAd() {
        if (interstitialAd != null) {
            interstitialAd.show(MainActivity.this);
        } else {
            loadInterstitialAd();
        }
    }
}