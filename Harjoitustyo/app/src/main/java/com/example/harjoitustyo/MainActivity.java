package com.example.harjoitustyo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    GameView gameView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
        gameView.setLongClickable(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameView.startAnimationThread();
        getSupportActionBar().hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameView.stopAnimationThread();
    }
}