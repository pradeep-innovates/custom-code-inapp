package com.example.androidintegration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.clevertap.android.sdk.CleverTapAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private CleverTapAPI cleverTapDefaultInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);

        // HomeScreen
        Button homescreen = findViewById(R.id.homescreen);
        homescreen.setOnClickListener(view -> {
            Intent homeIntent = new Intent(LoginActivity.this, HomeScreen.class);
            startActivity(homeIntent);
        });

        // Initialize EditText fields
        EditText etName = findViewById(R.id.et_name);
        EditText etIdentity = findViewById(R.id.et_identity);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etPhone = findViewById(R.id.et_phone);
        EditText etGender = findViewById(R.id.et_gender);
        EditText etDate = findViewById(R.id.et_date);
        EditText etKey = findViewById(R.id.et_key);   // New EditText for key
        EditText etValue = findViewById(R.id.et_value); // New EditText for value

        Button onUserLogin = findViewById(R.id.onUserLogin);
        Button pushProfile = findViewById(R.id.pushProfile);

        // Set click listener for onUserLogin button
        onUserLogin.setOnClickListener(view -> handleProfileUpdate("Login Successful", true,
                etName, etIdentity, etEmail, etPhone, etGender, etDate, etKey, etValue));

        // Set click listener for pushProfile button
        pushProfile.setOnClickListener(view -> handleProfileUpdate("Profile Pushed", false,
                etName, etIdentity, etEmail, etPhone, etGender, etDate, etKey, etValue));



    }

    // Method to handle profile update or login
    private void handleProfileUpdate(String eventName, boolean isLogin,
                                     EditText etName, EditText etIdentity, EditText etEmail,
                                     EditText etPhone, EditText etGender, EditText etDate,
                                     EditText etKey, EditText etValue) {

        // Get user input
        String name = etName.getText().toString().trim();
        String identity = etIdentity.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String dateString = etDate.getText().toString().trim();
        String key = etKey.getText().toString().trim();
        String value = etValue.getText().toString().trim();

        // Create a date object from the input string
        Date dob = null;
        if (!dateString.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                dob = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Create profile update map
        HashMap<String, Object> profileUpdate = new HashMap<>();
        if (!name.isEmpty()) profileUpdate.put("Name", name);
        if (!identity.isEmpty()) {
            try {
                long identityNumber = Long.parseLong(identity);
                profileUpdate.put("Identity", identityNumber);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (!email.isEmpty()) profileUpdate.put("Email", email);
        if (!phone.isEmpty()) profileUpdate.put("Phone", phone);
        if (!gender.isEmpty()) profileUpdate.put("Gender", gender);
        if (dob != null) profileUpdate.put("DOB", dob);

        // Additional fields
        profileUpdate.put("MSG-email", true);
        profileUpdate.put("MSG-push", true);
        profileUpdate.put("MSG-sms", true);
        profileUpdate.put("MSG-whatsapp", true);

        // Add some sample stuff
//        ArrayList<String> stuff = new ArrayList<>();
//        stuff.add("bag");
//        stuff.add("shoes");
//        profileUpdate.put("MyStuff", stuff);
//
//        String[] otherStuff = {"Jeans", "Perfume"};
//        profileUpdate.put("YourStuff", otherStuff);

        // Add dynamic key-value pair from user input if both are non-empty
        if (!key.isEmpty() && !value.isEmpty()) {
            // Check if the value contains commas
            if (value.contains(",")) {
                // Split the comma-separated values and trim spaces
                String[] valuesArray = value.split(",");
                ArrayList<String> valuesList = new ArrayList<>();
                for (String val : valuesArray) {
                    valuesList.add(val.trim());
                }
                // Add the ArrayList to the profileUpdate map
                profileUpdate.put(key, valuesList);
            } else {
                // Add the value as a single string if no comma is present
                profileUpdate.put(key, value);
            }
        }

        // Perform the CleverTap action (either login or profile push)
        if (isLogin) {
            cleverTapDefaultInstance.onUserLogin(profileUpdate);
        } else {
            cleverTapDefaultInstance.pushProfile(profileUpdate);
        }
        cleverTapDefaultInstance.pushEvent(eventName, profileUpdate);
    }
}
