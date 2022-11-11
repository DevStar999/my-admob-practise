package com.example.myadmobpractise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class MainActivity extends AppCompatActivity {
    private AdView adView;

    private void initialise() {
        adView = findViewById(R.id.banner_ad_adview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();

        loadBannerAd();
    }

    @Override
    public void onBackPressed() {
        // Calling finish() method to mimic the code flows in 'Feed the Animal' and '2048 Champs' projects
        finish();
    }
    
    public void gotoSecondActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                // TODO -> Implement logic to prevent users from clicking the ad too many times in a short period of time
                /* Note: If the user clicks on ad too many times in a short period of time, Google may suspect this as fishy
                         behaviour and this could lead to serious consequences like AdMob account suspension or termination
                         OR suspension or termination of App from Play Store. However, we need not worry too much about this
                         as well, since at first we will be warned by Google about this.
                */
            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adView.loadAd(adRequest);
            }
        });
        adView.loadAd(adRequest);
    }
}