package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeScreenUserComplete extends AppCompatActivity{
    // EditText
    EditText search;

    // ListView
    ListView residentListView;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("CompleteRecipient");

    // ArrayList
    ArrayList<String> completeList;

    // ArrayAdapter
    ArrayAdapter<String> adapter;

    // ResidentList Class
    CompleteList completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_user_complete);

        // ListView
        residentListView = findViewById(R.id.residentListView);

        // EditText
        search = findViewById(R.id.search);

        // List
        completeList =  new ArrayList<>();
        adapter = new ArrayAdapter<String>(HomeScreenUserComplete.this, R.layout.listinfo, R.id.residentName, completeList);
        completed = new CompleteList();

        // On Create Functions
        displayComplete();
        searchResident();
    }

    public void searchResident(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HomeScreenUserComplete.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void displayComplete(){
        try {
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        completed = snapshot1.getValue(CompleteList.class);
                        completeList.add(completed.getSurname().toString() + ", " + completed.getFirstName().toString() + " " + completed.getMiddleName().toString() + ""
                                + "\n\t Benefit: " + completed.getType().toString() +  "\n\tNoted By: " + completed.getNotedBy().toString());
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