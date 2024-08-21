package com.example.a_zservices;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginSelectionActivity extends AppCompatActivity {

    Button b1, b2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the correct layout
        setContentView(R.layout.activity_login_selection); // Use the correct layout file

        // Initialize buttons
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);

        // Set up click listeners
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSelectionActivity.this, userloginActivity.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSelectionActivity.this, adminloginActivity.class);
                startActivity(intent);
            }
        });
    }
}
