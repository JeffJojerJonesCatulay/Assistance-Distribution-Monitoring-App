package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class HomeScreenUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    // DrawerLayout
    DrawerLayout drawerLayout;

    // ActionBarDrawerToggle
    ActionBarDrawerToggle actionBarDrawerToggle;

    // Toolbar
    Toolbar toolbar;

    // NavigationView
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_user);

        // Toolbar
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // NavigationView
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeScreenUser.this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView.setCheckedItem(R.id.nav_update);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_update){
                    startActivity(new Intent(getApplicationContext(),
                            UpdateProfile.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_resident){
                    startActivity(new Intent(getApplicationContext(),
                            HomeScreenUserResident.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_incoming){
                    startActivity(new Intent(getApplicationContext(),
                            HomeScreenUserIncoming.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_complete){
                    startActivity(new Intent(getApplicationContext(),
                            HomeScreenUserComplete.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_qr){
                    startActivity(new Intent(getApplicationContext(),
                            QRGenerator.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_signOut){
                    startActivity(new Intent(getApplicationContext(),
                            logIn.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_about){
                    startActivity(new Intent(getApplicationContext(),
                            About.class));
                    finish();
                }
                else if (item.getItemId() == R.id.nav_email){
                    openGmail();
                }
                return true;
            }
        });
        return true;
    }

    public void openGmail(){
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        sendIntent.putExtra(Intent.EXTRA_TEXT,"");
        startActivity(sendIntent);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                logIn.class));
        finish();
    }
}