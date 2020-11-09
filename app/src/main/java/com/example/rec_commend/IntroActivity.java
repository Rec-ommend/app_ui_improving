package com.example.rec_commend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();
        Handler handler = new Handler();
        handler.postDelayed(()->{
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        },3000);
    }
}