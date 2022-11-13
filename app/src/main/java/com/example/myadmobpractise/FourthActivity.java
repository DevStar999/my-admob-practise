package com.example.myadmobpractise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FourthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
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
}