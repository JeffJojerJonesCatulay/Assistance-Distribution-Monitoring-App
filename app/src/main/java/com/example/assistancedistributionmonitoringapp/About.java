package com.example.assistancedistributionmonitoringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {
    // TextView
    TextView about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // TextView
        about = findViewById(R.id.about);

        about.setText(getString(R.string.about));

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                HomeScreenUser.class));
        finish();
    }
}