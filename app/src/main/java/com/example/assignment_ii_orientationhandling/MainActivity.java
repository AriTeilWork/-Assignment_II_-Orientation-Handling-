package com.example.assignment_ii_orientationhandling;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // EditText fields
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText birthdayEditText;
    private EditText phoneEditText;
    private EditText homeAddressEditText;
    private EditText emailEditText;

    // Keep values for fields not present in some orientations
    private String homeAddressValue = "";
    private String emailValue = "";

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

        // Initialize EditText fields
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        birthdayEditText = findViewById(R.id.birthday_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        
        // These fields only exist in landscape layout - will be null in portrait
        homeAddressEditText = findViewById(R.id.home_address_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);

        // Restore saved state if available
        if (savedInstanceState != null) {
            restoreFormData(savedInstanceState);
        }
    }

    /**
     * Restores form data from saved instance state
     */
    private void restoreFormData(Bundle savedInstanceState) {
        firstNameEditText.setText(savedInstanceState.getString(KEY_FIRST_NAME, ""));
        lastNameEditText.setText(savedInstanceState.getString(KEY_LAST_NAME, ""));
        birthdayEditText.setText(savedInstanceState.getString(KEY_BIRTHDAY, ""));
        phoneEditText.setText(savedInstanceState.getString(KEY_PHONE, ""));

        // Restore landscape-only values even if views are missing in portrait
        homeAddressValue = savedInstanceState.getString(KEY_HOME_ADDRESS, "");
        emailValue = savedInstanceState.getString(KEY_EMAIL, "");

        if (homeAddressEditText != null) {
            homeAddressEditText.setText(homeAddressValue);
        }
        if (emailEditText != null) {
            emailEditText.setText(emailValue);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save all form field values
        outState.putString(KEY_FIRST_NAME, firstNameEditText.getText().toString());
        outState.putString(KEY_LAST_NAME, lastNameEditText.getText().toString());
        outState.putString(KEY_BIRTHDAY, birthdayEditText.getText().toString());
        outState.putString(KEY_PHONE, phoneEditText.getText().toString());
        
        // Save landscape-only values even when current layout is portrait
        String homeToSave = homeAddressEditText != null ? homeAddressEditText.getText().toString() : homeAddressValue;
        String emailToSave = emailEditText != null ? emailEditText.getText().toString() : emailValue;

        outState.putString(KEY_HOME_ADDRESS, homeToSave);
        outState.putString(KEY_EMAIL, emailToSave);
    }
}

