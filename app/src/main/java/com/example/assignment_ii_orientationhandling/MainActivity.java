package com.example.assignment_ii_orientationhandling;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
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

    // EditText fields (Part I)
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText birthdayEditText;
    private EditText phoneEditText;
    private EditText homeAddressEditText;
    private EditText emailEditText;

    // Keep values for fields not present in portrait
    private String homeAddressValue = "";
    private String emailValue = "";

    // Random number (Part II)
    private TextView randomNumberTextView, savedRandomNumberTextView;
    private int lastRandomNumber = 0;
    private long lastOrientationChangeTime = 0L;
    private Timer timer;
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Keys for saving state
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_HOME_ADDRESS = "homeAddress";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Part I: Form fields
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        birthdayEditText = findViewById(R.id.birthday_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        homeAddressEditText = findViewById(R.id.home_address_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);

        // Part II: Random number labels
        randomNumberTextView = findViewById(R.id.random_number_text_view);
        savedRandomNumberTextView = findViewById(R.id.saved_random_number_text_view);

        if (savedInstanceState != null) {
            // Part I: Restore form data
            restoreFormData(savedInstanceState);

            // Part II: Restore random number state
            lastRandomNumber = savedInstanceState.getInt("lastRandomNumber", 0);
            lastOrientationChangeTime = savedInstanceState.getLong("lastTime", 0L);
            savedRandomNumberTextView.setText("Saved Random Number: " + lastRandomNumber);

            if (lastOrientationChangeTime != 0L) {
                Toast.makeText(
                        this,
                        "Orientation changed at: " + new Date(lastOrientationChangeTime),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    private void restoreFormData(Bundle savedInstanceState) {
        firstNameEditText.setText(savedInstanceState.getString(KEY_FIRST_NAME, ""));
        lastNameEditText.setText(savedInstanceState.getString(KEY_LAST_NAME, ""));
        birthdayEditText.setText(savedInstanceState.getString(KEY_BIRTHDAY, ""));
        phoneEditText.setText(savedInstanceState.getString(KEY_PHONE, ""));

        homeAddressValue = savedInstanceState.getString(KEY_HOME_ADDRESS, "");
        emailValue = savedInstanceState.getString(KEY_EMAIL, "");

        if (homeAddressEditText != null) {
            homeAddressEditText.setText(homeAddressValue);
        }
        if (emailEditText != null) {
            emailEditText.setText(emailValue);
        }
    }

    private void startRandomNumberTimer() {
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

        // Part I: Save form data
        outState.putString(KEY_FIRST_NAME, firstNameEditText.getText().toString());
        outState.putString(KEY_LAST_NAME, lastNameEditText.getText().toString());
        outState.putString(KEY_BIRTHDAY, birthdayEditText.getText().toString());
        outState.putString(KEY_PHONE, phoneEditText.getText().toString());

        String homeToSave = homeAddressEditText != null ? homeAddressEditText.getText().toString() : homeAddressValue;
        String emailToSave = emailEditText != null ? emailEditText.getText().toString() : emailValue;
        outState.putString(KEY_HOME_ADDRESS, homeToSave);
        outState.putString(KEY_EMAIL, emailToSave);

        // Part II: Save random number state
        outState.putInt("lastRandomNumber", lastRandomNumber);
        lastOrientationChangeTime = System.currentTimeMillis();
        outState.putLong("lastTime", lastOrientationChangeTime);
    }
}
