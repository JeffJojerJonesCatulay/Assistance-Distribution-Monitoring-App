package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeScreenUserResident extends AppCompatActivity {
    // EditText
    EditText search;

    // ListView
    ListView residentListView;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Residence");

    // ArrayList
    ArrayList<String> residentList;

    // ArrayAdapter
    ArrayAdapter<String> adapter;

    // ResidentList Class
    ResidentList resident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_user_resident);

        // ListView
        residentListView = findViewById(R.id.residentListView);

        // EditText
        search = findViewById(R.id.search);

        // List
        residentList =  new ArrayList<>();
        adapter = new ArrayAdapter<String>(HomeScreenUserResident.this, R.layout.listinfo, R.id.residentName, residentList);
        resident = new ResidentList();

        // On Create Functions
        displayResident();
        searchResident();
    }

    public void searchResident(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HomeScreenUserResident.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void displayResident(){
        try {
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        resident = snapshot1.getValue(ResidentList.class);
                        residentList.add(resident.getSurname().toString() + ", " + resident.getFirstName().toString() + " " + resident.getMiddleName().toString() + "");
                    }
                    residentListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception exception){

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                HomeScreenUser.class));
        finish();
    }
}