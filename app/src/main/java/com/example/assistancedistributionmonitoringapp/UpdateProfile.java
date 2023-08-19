package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateProfile extends AppCompatActivity {
    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    // EditText
    EditText surnameInput, firstNameInput, middleNameInput,
            ageInput, addressInput, contactInput, religionInput,
            occupationInput;

    // Strings
    String surnameVal, firstNameVal, middleNameVal,
            ageVal, addressVal, genderVal, civilVal, contactVal,
            religionVal, occupationVal;
    String userId;
    String Email, Access;

    // Spinner
    Spinner genderInput, civilStatusInput;

    // Button
    Button updateBtn;

    // CheckBox
    CheckBox showPassword;

    // Progress Bar
    ProgressBar loading;

    // ArrayList
    ArrayList<String> genderChoice = new ArrayList<>();
    ArrayList<String> civilStatusChoice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_profile);

        // EditText
        surnameInput = findViewById(R.id.surnameInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        middleNameInput = findViewById(R.id.middleNameInput);
        ageInput = findViewById(R.id.ageInput);
        addressInput = findViewById(R.id.addressInput);
        contactInput = findViewById(R.id.contactInput);
        religionInput = findViewById(R.id.religionInput);
        occupationInput = findViewById(R.id.occupationInput);


        // Spinner
        genderInput = findViewById(R.id.genderInput);
        civilStatusInput = findViewById(R.id.civilStatusInput);

        // Button
        updateBtn = findViewById(R.id.updateBtn);

        // CheckBox
        showPassword = findViewById(R.id.showPassword);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // On Create Functions
        spinnerItems();
        search();
        updateProfile();
    }

    public void updateToFirebase(){
        surnameVal = surnameInput.getText().toString();
        firstNameVal = firstNameInput.getText().toString();
        middleNameVal = middleNameInput.getText().toString();
        ageVal = ageInput.getText().toString();
        addressVal = addressInput.getText().toString();
        genderVal = genderInput.getSelectedItem().toString();
        civilVal = civilStatusInput.getSelectedItem().toString();
        contactVal = contactInput.getText().toString();
        religionVal = religionInput.getText().toString();
        occupationVal = occupationInput.getText().toString();
        try {
            HashMap<String, String> usersInfo = new HashMap<>();
            usersInfo.put("Surname", surnameVal);
            usersInfo.put("FirstName", firstNameVal);
            usersInfo.put("MiddleName", middleNameVal);
            usersInfo.put("Age", ageVal);
            usersInfo.put("Address", addressVal);
            usersInfo.put("Gender", genderVal);
            usersInfo.put("CivilStatus", civilVal);
            usersInfo.put("Contact", contactVal);
            usersInfo.put("Religion", religionVal);
            usersInfo.put("Occupation", occupationVal);
            usersInfo.put("Email", Email);
            usersInfo.put("Access", Access);

            loading.setVisibility(View.VISIBLE);
            root.child(userId).setValue(usersInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        completion("Success!, You have successfully updated your profile!");
                        loading.setVisibility(View.GONE);
                    }
                    else {
                        showMessage("Failed", "Sorry Updating failed!");
                        loading.setVisibility(View.GONE);
                    }
                }
            });

        }catch (Exception exception){

        }
    }

    public void updateProfile() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ( (surnameInput.getText().toString().isEmpty()) || (firstNameInput.getText().toString().isEmpty()) || (middleNameInput.getText().toString().isEmpty()) || (ageInput.getText().toString().isEmpty())
                        || (addressInput.getText().toString().isEmpty()) || (contactInput.getText().toString().isEmpty()) || (religionInput.getText().toString().isEmpty())
                        || (occupationInput.getText().toString().isEmpty()) || (genderInput.getSelectedItemPosition() == 0) || (civilStatusInput.getSelectedItemPosition() == 0)
                ) {
                    changeFieldColorIfEmpty();
                }else {
                    confirmation("Update Profile", "Do you want to proceed?");
                }
                }catch (Exception exception){

                }
            }
        });
    }

    public void confirmation(String title, String message){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message)
                    .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            updateToFirebase();
                        }
                    })
                    .setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }

    public void search(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = root.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    String Surname = snapshot.child("Surname").getValue(String.class);
                    String FirstName = snapshot.child("FirstName").getValue(String.class);
                    String MiddleName = snapshot.child("MiddleName").getValue(String.class);
                    String Age = snapshot.child("Age").getValue(String.class);
                    String Address = snapshot.child("Address").getValue(String.class);
                    String Gender = snapshot.child("Gender").getValue(String.class);
                    String CivilStatus = snapshot.child("CivilStatus").getValue(String.class);
                    String Contact = snapshot.child("Contact").getValue(String.class);
                    String Religion = snapshot.child("Religion").getValue(String.class);
                    String Occupation = snapshot.child("Occupation").getValue(String.class);
                    Email = snapshot.child("Email").getValue(String.class);
                    Access = snapshot.child("Access").getValue(String.class);

                   surnameInput.setText(Surname);
                   firstNameInput.setText(FirstName);
                   middleNameInput.setText(MiddleName);
                   ageInput.setText(Age);
                   addressInput.setText(Address);
                   contactInput.setText(Contact);
                   religionInput.setText(Religion);
                   occupationInput.setText(Occupation);

                   switch (Gender){
                       case "Male":
                           genderInput.setSelection(1);
                           break;
                       case "Female":
                           genderInput.setSelection(2);
                           break;
                       case "I Prefer Not to Say":
                           genderInput.setSelection(3);
                           break;
                   }

                   switch (CivilStatus){
                       case "Single":
                           civilStatusInput.setSelection(1);
                           break;
                       case "Married":
                           civilStatusInput.setSelection(2);
                           break;
                       case "Widowed":
                           civilStatusInput.setSelection(3);
                           break;
                   }

                }catch (Exception exception){
                    loading.setVisibility(View.GONE);
                    showMessage("Error", "Something is Wrong!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void changeFieldColorIfEmpty(){
        if (surnameInput.getText().toString().isEmpty()){
            surnameInput.setError("Surname Is Required");
        }
        if (firstNameInput.getText().toString().isEmpty()){
            firstNameInput.setError("FirstName Is Required");
        }
        if (middleNameInput.getText().toString().isEmpty()){
            middleNameInput.setError("Middle Initial Is Required");
        }
        if (ageInput.getText().toString().isEmpty()){
            ageInput.setError("Age Is Required");
        }
        if (addressInput.getText().toString().isEmpty()){
            addressInput.setError("Address Is Required");
        }
        if (contactInput.getText().toString().isEmpty()){
            contactInput.setError("Contact Is Required");
        }
        if (religionInput.getText().toString().isEmpty()){
            religionInput.setError("Religion Is Required");
        }
        if (occupationInput.getText().toString().isEmpty()){
            occupationInput.setError("Occupation Is Required");
        }

        // Spinner Change Color
        // Gender
        if (genderInput.getSelectedItemPosition() == 0){
            genderInput.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else {
            genderInput.setBackgroundColor(getResources().getColor(R.color.white));
        }

        // Civil Status
        if (civilStatusInput.getSelectedItemPosition() == 0){
            civilStatusInput.setBackgroundColor(getResources().getColor(R.color.red));
        }
        else {
            civilStatusInput.setBackgroundColor(getResources().getColor(R.color.white));
        }

        genderInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        genderInput.setBackgroundColor(getResources().getColor(R.color.red));
                        break;
                    case 1:
                        genderInput.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        genderInput.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 3:
                        genderInput.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        civilStatusInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        civilStatusInput.setBackgroundColor(getResources().getColor(R.color.red));
                        break;
                    case 1:
                        civilStatusInput.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        civilStatusInput.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 3:
                        civilStatusInput.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void spinnerItems(){
        // For Gender
        genderChoice.add("Select A Gender");
        genderChoice.add("Male");
        genderChoice.add("Female");
        genderChoice.add("I Prefer Not to Say");

        ArrayAdapter<String> gender_choice = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderChoice );
        gender_choice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderInput.setAdapter(gender_choice);

        // For Civil Status
        civilStatusChoice.add("Select A Civil Status");
        civilStatusChoice.add("Single");
        civilStatusChoice.add("Married");
        civilStatusChoice.add("Widowed");

        ArrayAdapter<String> civil_choice = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, civilStatusChoice );
        civil_choice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        civilStatusInput.setAdapter(civil_choice);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                HomeScreenUser.class));
        finish();
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void completion(String title){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(title)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }
}