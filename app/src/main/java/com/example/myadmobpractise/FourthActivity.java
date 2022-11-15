package com.example.myadmobpractise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

/* TODO -> Things left to do related to Native Ads are as follows:
           (1) Write notes related to Native Ads like we wrote for Rewarded Interstitial Ads in ThirdActivity from the 1st
           resource below which are YouTube videos for Native Ads from Google AdMob's official channel
           (2) Keep the 2nd resource as reference, implement more exhaustively the features related to Native Ads from the
           GitHub projects that we downloaded from official documentation of Google and store them in 'references' directory
           in this project
           (3) Explore how to load multiple ads
           (4) Explore how to make video ads work in Native Ads even if there are multiple ads
*/
/* Notes on Native Ads:
    # Resources for Rewarded Interstitial Ads ->
    (1) Part 1: https://www.youtube.com/watch?v=7HXZFGbDoJo, Part 2: https://www.youtube.com/watch?v=AfCvQ4p_1y8
    (2) Video Tutorial: https://www.youtube.com/watch?v=wENWbEKRfaM,
    GitHub Project Link: https://github.com/shaheershukur/AdmobTutorialForAndroid
*/
public class FourthActivity extends AppCompatActivity {
    private AdRequest adRequest;

    private void initialise() {
        adRequest = new AdRequest.Builder().build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        initialise();

        loadNativeAd();
    }

    @Override
    public void onBackPressed() {
        // Calling finish() method to mimic the code flows in 'Feed the Animal' and '2048 Champs' projects
        finish();
    }

    public void gotoMainActivity(View view) {
        Intent intent = new Intent(FourthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoSecondActivity(View view) {
        Intent intent = new Intent(FourthActivity.this, SecondActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoThirdActivity(View view) {
        Intent intent = new Intent(FourthActivity.this, ThirdActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadNativeAd() {
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) { // Show the ad.
                        NativeAdView nativeAdView = (NativeAdView) getLayoutInflater()
                                .inflate(R.layout.native_ad_layout, null);
                        mapUnifiedNativeAdToLayout(nativeAd, nativeAdView);

                        FrameLayout nativeAdLayout = findViewById(R.id.id_native_ad);
                        nativeAdLayout.removeAllViews();
                        nativeAdLayout.addView(nativeAdView);

                        // If this callback occurs after the activity is destroyed, you must call destroy and return or you
                        // may get a memory leak. [ Note: 'isDestroyed()' is a method on Activity. ]
                        if (isDestroyed()) {
                            nativeAd.destroy();
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdClicked() { // Code to be executed when the user clicks on an ad.
                        super.onAdClicked();
                        /* TODO -> Implement logic to prevent users from clicking the ad too many times in a short
                                   period of time
                        */
                        /* Note: If the user clicks on ad too many times in a short period of time, Google may suspect this
                                 as fishy behaviour and this could lead to serious consequences like AdMob account suspension
                                 or termination OR suspension or termination of App from Play Store. However, we need not
                                 worry too much about this as well, since at first we will be warned by Google about this.
                        */
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) { // Handle the failure by logging,
                        // altering the UI, and so on.
                        super.onAdFailedToLoad(loadAdError);
                        loadNativeAd();
                    }

                    @Override
                    public void onAdClosed() {/* Code to be executed when the user is about to return to the app */}

                    @Override
                    public void onAdImpression() {/* Code to be executed when an impression is recorded for an ad. */}

                    @Override
                    public void onAdLoaded() {/* Code to be executed when an ad finishes loading. */}

                    @Override
                    public void onAdOpened() {/* Code to be executed when an ad opens an overlay that covers the screen. */}
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be used to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAds(adRequest, 5 /* Remember that 5 is the max limit for ads requested to be loaded */);
        // OR adLoader.loadAd(adRequest); // For loading just 1 ad
    }

    private void mapUnifiedNativeAdToLayout(NativeAd adFromGoogle, NativeAdView myNativeAdView) {
        MediaView mediaView = myNativeAdView.findViewById(R.id.ad_media);
        myNativeAdView.setMediaView(mediaView);

        myNativeAdView.setHeadlineView(myNativeAdView.findViewById(R.id.ad_headline));
        myNativeAdView.setBodyView(myNativeAdView.findViewById(R.id.ad_body));
        myNativeAdView.setCallToActionView(myNativeAdView.findViewById(R.id.ad_call_to_action));
        myNativeAdView.setIconView(myNativeAdView.findViewById(R.id.ad_icon));
        myNativeAdView.setPriceView(myNativeAdView.findViewById(R.id.ad_price));
        myNativeAdView.setStarRatingView(myNativeAdView.findViewById(R.id.ad_rating));
        myNativeAdView.setStoreView(myNativeAdView.findViewById(R.id.ad_store));
        myNativeAdView.setAdvertiserView(myNativeAdView.findViewById(R.id.ad_advertiser));

        ((AppCompatTextView) myNativeAdView.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getBody() == null) {
            myNativeAdView.getBodyView().setVisibility(View.GONE);
        } else {
            ((AppCompatTextView) myNativeAdView.getBodyView()).setText(adFromGoogle.getBody());
        }

        if (adFromGoogle.getCallToAction() == null) {
            myNativeAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((AppCompatButton) myNativeAdView.getCallToActionView()).setText(adFromGoogle.getCallToAction());
        }

        if (adFromGoogle.getIcon() == null) {
            myNativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((AppCompatImageView) myNativeAdView.getIconView()).setImageDrawable(adFromGoogle.getIcon().getDrawable());
        }

        if (adFromGoogle.getPrice() == null) {
            myNativeAdView.getPriceView().setVisibility(View.GONE);
        } else {
            ((AppCompatTextView) myNativeAdView.getPriceView()).setText(adFromGoogle.getPrice());
        }

        if (adFromGoogle.getStarRating() == null) {
            myNativeAdView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) myNativeAdView.getStarRatingView()).setRating(adFromGoogle.getStarRating().floatValue());
        }

        if (adFromGoogle.getStore() == null) {
            myNativeAdView.getStoreView().setVisibility(View.GONE);
        } else {
            ((AppCompatTextView) myNativeAdView.getStoreView()).setText(adFromGoogle.getStore());
        }

        if (adFromGoogle.getAdvertiser() == null) {
            myNativeAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((AppCompatTextView) myNativeAdView.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        myNativeAdView.setNativeAd(adFromGoogle);
    }
}