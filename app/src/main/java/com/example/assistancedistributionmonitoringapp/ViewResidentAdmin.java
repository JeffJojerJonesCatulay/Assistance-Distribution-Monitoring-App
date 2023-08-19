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

public class ViewResidentAdmin extends AppCompatActivity {
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
    ResidentListAdmin resident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resident_admin);

        // ListView
        residentListView = findViewById(R.id.residentListView);

        // EditText
        search = findViewById(R.id.search);

        // List
        residentList =  new ArrayList<>();
        adapter = new ArrayAdapter<String>(ViewResidentAdmin.this, R.layout.listinfo, R.id.residentName, residentList);
        resident = new ResidentListAdmin();

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
                ViewResidentAdmin.this.adapter.getFilter().filter(s);
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
                        resident = snapshot1.getValue(ResidentListAdmin.class);
                        residentList.add(resident.getSurname().toString() + ", " + resident.getFirstName().toString() + " " + resident.getMiddleName().toString()
                                + "\n\tAge: " + resident.getAge().toString() + "\n\tAddress: " + resident.getAddress().toString() + "\n\tGender: " + resident.getGender().toString()
                                + "\n\tCivil Status: "  + resident.getCivilStatus().toString() + "\n\tContact: " + resident.getContact().toString() + "\n\tReligion: " + resident.getReligion().toString()
                                + "\n\tOccupation: " + resident.getOccupation().toString() );
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
                HomeScreenAdminResident.class));
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