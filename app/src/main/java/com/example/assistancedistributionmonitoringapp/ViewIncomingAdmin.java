package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewIncomingAdmin extends AppCompatActivity {
    // EditText
    EditText search;

    // ListView
    ListView residentListView;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("IncomingRecipient");

    // ArrayList
    ArrayList<String> incomingList;

    // ArrayAdapter
    ArrayAdapter<String> adapter;

    // ResidentList Class
    IncomingListAdmin incoming;

    // Boolean
    Boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_incoming_admin);

        // ListView
        residentListView = findViewById(R.id.residentListView);

        // EditText
        search = findViewById(R.id.search);

        // List
        incomingList =  new ArrayList<>();
        adapter = new ArrayAdapter<String>(ViewIncomingAdmin.this, R.layout.listinfo, R.id.residentName, incomingList);
        incoming = new IncomingListAdmin();

        // On Create Functions
        displayResident();
        searchResident();

        // Message
        message("Hello Admin!", "You can search for any information of the resident  \nExample:\n\nName\nAddress\nOccupation\nDate\netc.");

    }

    public void searchResident(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ViewIncomingAdmin.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void displayResident(){
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        incoming = snapshot1.getValue(IncomingListAdmin.class);
                        incomingList.add("Resident Info: \n"  +  incoming.getSurname().toString() + ", " + incoming.getFirstName().toString() + " " + incoming.getMiddleName().toString()
                                + "\n\tAge: " + incoming.getAge().toString() + "\n\tAddress: " + incoming.getAddress().toString() + "\n\tGender: " + incoming.getGender().toString()
                                + "\n\tCivil Status: "  + incoming.getCivilStatus().toString() + "\n\tContact: " + incoming.getContact().toString() + "\n\tReligion: " + incoming.getReligion().toString()
                                + "\n\tOccupation: " + incoming.getOccupation().toString() + "\n\nBenefit Info" + "\n\tDate: " + incoming.getDateSchedule().toString()
                                + "\n\tTime: " + incoming.getTime().toString() + "\n\tType: " + incoming.getType().toString() + "\n\tDetails: " + incoming.getSpecify().toString() + "\n\tSponsor: "
                                + incoming.getSponsor().toString() +  "\n\tPosted By: " + incoming.getPostedBy() + "\n\tPosted Date: " + incoming.getDatePosted().toString());
                    }
                    residentListView.setAdapter(adapter);
                }catch (Exception exception){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void message(String title, String message){
        try {
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.show();
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                HomeScreenAdminIncoming.class));
        finish();
    }
}