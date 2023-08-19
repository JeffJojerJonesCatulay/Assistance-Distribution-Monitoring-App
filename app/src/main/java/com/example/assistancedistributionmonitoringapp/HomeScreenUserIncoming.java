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

public class HomeScreenUserIncoming extends AppCompatActivity {
    // ListView
    ListView incomingListView;

    // EditText
    EditText search;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("IncomingRecipient");

    // ArrayList
    ArrayList<String> incomingList;

    // ArrayAdapter
    ArrayAdapter<String> adapter;

    // IncomingList Class
    IncomingList complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_user_incoming);

        // ListView
        incomingListView = findViewById(R.id.incomingListView);

        // EditText
        search = findViewById(R.id.search);

        // List
        incomingList =  new ArrayList<>();
        adapter = new ArrayAdapter<String>(HomeScreenUserIncoming.this, R.layout.completelist, R.id.incomingInfo, incomingList);
        complete = new IncomingList();

        //On Create Function
        displayComplete();
        searchIncoming();
    }

    public void searchIncoming(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HomeScreenUserIncoming.this.adapter.getFilter().filter(s);
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
                        complete = snapshot1.getValue(IncomingList.class);
                        incomingList.add(complete.getSurname().toString() + ", " + complete.getFirstName().toString() + " " + complete.getMiddleName().toString() + "\n\t\t Schedule: "
                                + complete.getDateSchedule().toString() + "\n\t\t Time: " + complete.getTime().toString() + "\n\t\t Type: " + complete.getType().toString() + "\n\t\t Benefit: " + complete.getSpecify().toString()
                                + "\n\t\t Sponsor: " + complete.getSponsor().toString() + "\n\t\tPosted By: " + complete.getPostedBy().toString() + "\n\t\tDate Posted: " + complete.getDatePosted().toString()
                        );

                    }
                    incomingListView.setAdapter(adapter);
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