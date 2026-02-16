package com.example.assignment_ii_orientationhandling;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView randomNumberTextView, savedRandomNumberTextView;

    // Latest random number that was shown before an orientation change
    private int lastRandomNumber = 0;
    // Time (in millis) when the last orientation change happened
    private long lastOrientationChangeTime = 0L;

    private Timer timer;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        randomNumberTextView = findViewById(R.id.random_number_text_view);
        savedRandomNumberTextView = findViewById(R.id.saved_random_number_text_view);

        if (savedInstanceState != null) {
            lastRandomNumber = savedInstanceState.getInt("lastRandomNumber", 0);
            lastOrientationChangeTime = savedInstanceState.getLong("lastTime", 0L);

            // Show the last random number that was visible before the orientation change
            savedRandomNumberTextView.setText("Saved Random Number: " + lastRandomNumber);

            // Show the time of the last orientation change in a Toast
            if (lastOrientationChangeTime != 0L) {
                Toast.makeText(
                        this,
                        "Orientation changed at: " + new Date(lastOrientationChangeTime),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private void startRandomNumberTimer() {
        // Avoid creating multiple timers if startRandomNumberTimer is called twice for some reason
        if (timer != null) {
            return;
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    int number = new Random().nextInt(100);
                    randomNumberTextView.setText("Current Random Number: " + number);
                    lastRandomNumber = number;
                });
            }
        }, 0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRandomNumberTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("lastRandomNumber", lastRandomNumber);
        lastOrientationChangeTime = System.currentTimeMillis();
        outState.putLong("lastTime", lastOrientationChangeTime);
    }
}
