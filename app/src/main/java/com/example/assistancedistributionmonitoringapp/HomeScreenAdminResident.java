package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

public class HomeScreenAdminResident extends AppCompatActivity {
    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    private DatabaseReference Residence = db.getReference().child("Residence");

    // Authentication
    private FirebaseAuth mAuth;

    // Button
    Button addResidentBtn, scanQrBtn, viewResident;

    // TextView
    TextView addAdminBtn;

    // Progress Bar
    ProgressBar loading;

    // EditText
    EditText surnameInput, firstNameInput, middleNameInput,
            ageInput, addressInput, contactInput, emailInput,
            religionInput, occupationInput, genderInput, statusInput;

    // Strings
    String  LName, fName,
            MName, age, gender,
            stat, contact, religion,
            occupation, email, address;

    String usersIdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_screen_admin_resident);

        // Button
        addResidentBtn = findViewById(R.id.addToResidentBtn);
        scanQrBtn = findViewById(R.id.scanQrBtn);
        viewResident = findViewById(R.id.viewResidentBtn);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // TextView
        addAdminBtn = findViewById(R.id.addAdminbtn);

        // EditText
        surnameInput = findViewById(R.id.surnameInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        middleNameInput = findViewById(R.id.middleNameInput);
        ageInput = findViewById(R.id.ageInput);
        addressInput = findViewById(R.id.addressInput);
        contactInput = findViewById(R.id.contactInput);
        emailInput = findViewById(R.id.emailInput);
        religionInput = findViewById(R.id.religionInput);
        occupationInput = findViewById(R.id.occupationInput);
        genderInput = findViewById(R.id.genderInput);
        statusInput = findViewById(R.id.statusInput);

        surnameInput.setEnabled(false);
        firstNameInput.setEnabled(false);
        middleNameInput.setEnabled(false);
        ageInput.setEnabled(false);
        addressInput.setEnabled(false);
        contactInput.setEnabled(false);
        emailInput.setEnabled(false);
        religionInput.setEnabled(false);
        occupationInput.setEnabled(false);
        genderInput.setEnabled(false);
        statusInput.setEnabled(false);

        // On Create Function
        bottomNavigation();
        addAdmin();
        scanQr();
        addResident();
        viewResidentAdmin();
    }

    public void viewResidentAdmin(){
        viewResident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        ViewResidentAdmin.class));
                finish();
            }
        });
    }

    public void addResident(){
        addResidentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailInput.getText().toString().isEmpty()){
                    message("Sorry!", "Please Scan QR First");
                }else {
                    confirmation("Add to Resident now?");
                }
            }
        });
    }

    public void addToFirebase(){
        try{
            loading.setVisibility(View.VISIBLE);
            HashMap<String, String> usersInfo = new HashMap<>();
            usersInfo.put("Surname", LName);
            usersInfo.put("FirstName", fName);
            usersInfo.put("MiddleName", MName);
            usersInfo.put("Age", age);
            usersInfo.put("Address", address);
            usersInfo.put("Gender", gender);
            usersInfo.put("CivilStatus", stat);
            usersInfo.put("Contact", contact);
            usersInfo.put("Religion", religion);
            usersInfo.put("Occupation", occupation);
            usersInfo.put("Email", email);

            Residence.child(usersIdValue).setValue(usersInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        completion("Success!, You have successfully Added a Resident!");
                        loading.setVisibility(View.GONE);
                    }
                    else {
                        showMessage("Failed", "Sorry Adding Resident failed!");
                        loading.setVisibility(View.GONE);
                    }
                }
            });

        }catch (Exception exception){

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HomeScreenAdminResident.this, logIn.class);
        startActivity(intent);
        finish();
    }

    public void addAdmin(){
        addAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenAdminResident.this, addAdmin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void scanQr(){
        scanQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HomeScreenAdminResident.this);
                intentIntegrator.setCaptureActivity(CaptureAct.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scanning Code");
                intentIntegrator.initiateScan();
            }
        });
    }

    public void search(){
        Query query = root.orderByChild(usersIdValue);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    if (snapshot.exists()){
                         LName = snapshot.child(usersIdValue).child("Surname").getValue(String.class);
                         fName = snapshot.child(usersIdValue).child("FirstName").getValue(String.class);
                         MName = snapshot.child(usersIdValue).child("MiddleName").getValue(String.class);
                         age = snapshot.child(usersIdValue).child("Age").getValue(String.class);
                         address = snapshot.child(usersIdValue).child("Address").getValue(String.class);
                         gender = snapshot.child(usersIdValue).child("Gender").getValue(String.class);
                         stat = snapshot.child(usersIdValue).child("CivilStatus").getValue(String.class);
                         contact = snapshot.child(usersIdValue).child("Contact").getValue(String.class);
                         religion = snapshot.child(usersIdValue).child("Religion").getValue(String.class);
                         occupation = snapshot.child(usersIdValue).child("Occupation").getValue(String.class);
                         email = snapshot.child(usersIdValue).child("Email").getValue(String.class);

                         surnameInput.setText(LName);
                         firstNameInput.setText(fName);
                         middleNameInput.setText(MName);
                         ageInput.setText(age);
                         addressInput.setText(address);
                         contactInput.setText(contact);
                         emailInput.setText(email);
                         religionInput.setText(religion);
                         occupationInput.setText(occupation);
                         genderInput.setText(gender);
                         statusInput.setText(stat);

                    }else {
                        message("Sorry!", "Data can't be found");
                    }
                }catch (Exception exception){
                    message("Sorry!", "Something is Wrong!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                usersIdValue = result.getContents();
                search();
            }
            else {
                message("Sorry!", "You did not scan any QR ");
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void bottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_resident);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_resident:
                        return true;
                    case R.id.nav_incoming:
                        startActivity(new Intent(getApplicationContext(),
                                HomeScreenAdminIncoming.class));
                        finish();
                        bottomNavigationView.setSelectedItemId(R.id.nav_resident);
                        return true;
                    case R.id.nav_complete:
                        startActivity(new Intent(getApplicationContext(),
                                HomeScreenAdminComplete.class));
                        finish();
                        bottomNavigationView.setSelectedItemId(R.id.nav_resident);
                        return true;
                }
                finish();
                return false;
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
            showMessage("Error", "Something is Wrong!");
        }
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

    public void confirmation(String title){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(title)
                    .setPositiveButton("ADD NOW", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            addToFirebase();
                            surnameInput.setText("");
                            firstNameInput.setText("");
                            middleNameInput.setText("");
                            ageInput.setText("");
                            addressInput.setText("");
                            contactInput.setText("");
                            emailInput.setText("");
                            religionInput.setText("");
                            occupationInput.setText("");
                            genderInput.setText("");
                            statusInput.setText("");
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }
}