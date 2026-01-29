package com.example.assignment_ii_orientationhandling;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private EditText firstName, lastName, birthday, phone, home, email;
    private TextView randomNumberLabel, lastRandomNumberLabel;
    private ImageView logoImage;

    private int lastRandomNumber = 0;
    private Timer timer;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        birthday = findViewById(R.id.birthday);
        phone = findViewById(R.id.phone);

        home = findViewById(R.id.home);
        email = findViewById(R.id.email);

        randomNumberLabel = findViewById(R.id.random_number_label);
        lastRandomNumberLabel = findViewById(R.id.last_random_number_label);
        logoImage = findViewById(R.id.logo_image);

        if (savedInstanceState != null) {
            firstName.setText(savedInstanceState.getString("firstName"));
            lastName.setText(savedInstanceState.getString("lastName"));
            birthday.setText(savedInstanceState.getString("birthday"));
            phone.setText(savedInstanceState.getString("phone"));

            if (home != null)
                home.setText(savedInstanceState.getString("home"));

            if (email != null)
                email.setText(savedInstanceState.getString("email"));

            lastRandomNumber = savedInstanceState.getInt("lastRandomNumber", 0);
            lastRandomNumberLabel.setText("Last random number: " + lastRandomNumber);

            long lastTime = savedInstanceState.getLong("lastTime", 0);
            if (lastTime != 0) {
                Toast.makeText(
                        this,
                        "Orientation changed at: " + new Date(lastTime),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

        startRandomNumberTimer();
    }

    private void startRandomNumberTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    int number = new Random().nextInt(100);
                    randomNumberLabel.setText("Random number: " + number);
                    lastRandomNumber = number;
                });
            }
        }, 0, 1000);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("firstName", firstName.getText().toString());
        outState.putString("lastName", lastName.getText().toString());
        outState.putString("birthday", birthday.getText().toString());
        outState.putString("phone", phone.getText().toString());

        if (home != null)
            outState.putString("home", home.getText().toString());

        if (email != null)
            outState.putString("email", email.getText().toString());

        outState.putInt("lastRandomNumber", lastRandomNumber);
        outState.putLong("lastTime", System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}