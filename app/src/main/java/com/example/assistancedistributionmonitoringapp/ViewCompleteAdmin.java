package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ViewCompleteAdmin extends AppCompatActivity {
    // EditText
    EditText search;

    // ListView
    ListView residentListView;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("CompleteRecipient");

    // ArrayList
    ArrayList<String> completedList;

    // ArrayAdapter
    ArrayAdapter<String> adapter;

    // ResidentList Class
    CompleteListAdmin complete;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complete_admin);

        // ListView
        residentListView = findViewById(R.id.residentListView);

        // EditText
        search = findViewById(R.id.search);

        // List
        completedList =  new ArrayList<>();
        adapter = new ArrayAdapter<String>(ViewCompleteAdmin.this, R.layout.listinfo, R.id.residentName, completedList);
        complete = new CompleteListAdmin();

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
                ViewCompleteAdmin.this.adapter.getFilter().filter(s);
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
                        complete = snapshot1.getValue(CompleteListAdmin.class);
                        completedList.add("Resident Info: \n"  +  complete.getSurname().toString() + ", " + complete.getFirstName().toString() + " " + complete.getMiddleName().toString()
                                + "\n\tAge: " + complete.getAge().toString() + "\n\tAddress: " + complete.getAddress().toString() + "\n\tGender: " + complete.getGender().toString()
                                + "\n\tCivil Status: "  + complete.getCivilStatus().toString() + "\n\tContact: " + complete.getContact().toString() + "\n\tReligion: " + complete.getReligion().toString()
                                + "\n\tOccupation: " + complete.getOccupation().toString() + "\n\nBenefit Info" + "\n\tDate: " + complete.getDateSchedule().toString()
                                + "\n\tTime: " + complete.getTime().toString() + "\n\tType: " + complete.getType().toString() + "\n\tDetails: " + complete.getSpecify().toString() + "\n\tSponsor: "
                                + complete.getSponsor().toString() + "\n\tDate Received: " + complete.getDateReceived().toString() + "\n\tInstances: " + complete.getInstances().toString()
                                + "\n\tNoted By: " + complete.getNotedBy().toString() );
                        //+ "\n\tInstances: " + complete.getInstances().toString()
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                HomeScreenAdminComplete.class));
        finish();
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
}