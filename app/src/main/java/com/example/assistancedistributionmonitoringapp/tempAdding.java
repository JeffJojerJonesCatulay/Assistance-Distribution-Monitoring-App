package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class tempAdding extends AppCompatActivity {

    // EditText
    EditText surnameInput, firstNameInput, addressInput, contactInput;

    // Strings
    String surnameVal, firstNameVal, addressVal,  contactVal;


    // Progress Bar
    ProgressBar loading;

    // Button
    Button addBtn;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("CompleteRecipient");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_adding);

        // EditText
        surnameInput = findViewById(R.id.surnameInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        addressInput = findViewById(R.id.addressInput);
        contactInput = findViewById(R.id.contactInput);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // Button
        addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                confirmation("SAP Adding", "Would you like to proceed?");
            }
        });

    }

    public void addToFirebase(){
        surnameVal = surnameInput.getText().toString();
        firstNameVal = firstNameInput.getText().toString();
        addressVal = addressInput.getText().toString();
        contactVal = contactInput.getText().toString();

        try {
            HashMap<String, String> usersInfo = new HashMap<>();
            usersInfo.put("Surname", surnameVal);
            usersInfo.put("FirstName", firstNameVal);
            usersInfo.put("MiddleName", " ");
            usersInfo.put("Age", "N/A");
            usersInfo.put("Address", addressVal);
            usersInfo.put("Gender", "N/A");
            usersInfo.put("CivilStatus", "N/A");
            usersInfo.put("Contact", contactVal);
            usersInfo.put("Religion", "N/A");
            usersInfo.put("Occupation", "N/A");
            usersInfo.put("Email", "N/A");
            usersInfo.put("DateSchedule", "N/A");
            usersInfo.put("Time", "N/A");
            usersInfo.put("Type", "SAP");
            usersInfo.put("Specify", "N/A");
            usersInfo.put("Sponsor", "N/A");
            usersInfo.put("Instances", "N/A");
            usersInfo.put("DateReceived", "N/A");
            usersInfo.put("NotedBy", "Norma L. Andrea");
            loading.setVisibility(View.GONE);
            root.push().setValue(usersInfo);
            showMessage("Success", "Resident Added!");

        }catch (Exception exception){
            showMessage("Error", "Something is Wrong!");
            loading.setVisibility(View.GONE);
        }
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void confirmation(String title, String message){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message)
                    .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            addToFirebase();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }
}