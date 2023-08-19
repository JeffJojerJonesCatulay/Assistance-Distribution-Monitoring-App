package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class HomeScreenAdminIncoming extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference admin = db.getReference().child("Admins");
    private DatabaseReference root = db.getReference().child("Residence");
    private DatabaseReference incomingRecipient = db.getReference().child("IncomingRecipient");

    // Button
    Button scanQrBtn, dateSelector, incomingBtn, viewIncoming;

    // Spinner
    Spinner timeSelector, benefitSelector;

    // Progress Bar
    ProgressBar loading;

    // EditText
    EditText fullNameInput, addressInput, contactInput, emailInput,
            dateInput, specifyInput, sponsorInput;

    // Strings
    String  LName, fName, MName, age, gender,
            stat, contact, religion,
            occupation, email, address,
            residentId, date, time, type, specify,
            sponsor;
    String current;
    String fullName;

    // ArrayList
    ArrayList<String> timeSession = new ArrayList<>();
    ArrayList<String> benefits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_screen_admin_incoming);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // Button
        scanQrBtn = findViewById(R.id.scanQrBtn);
        dateSelector = findViewById(R.id.dateSelector);
        incomingBtn = findViewById(R.id.incomingBtn);
        viewIncoming = findViewById(R.id.viewResidentBtn);

        // Spinner
        timeSelector = findViewById(R.id.sessionInput);
        benefitSelector = findViewById(R.id.benefitInput);

        // EditText
        fullNameInput = findViewById(R.id.fullNameInput);
        addressInput = findViewById(R.id.addressInput);
        contactInput = findViewById(R.id.contactInput);
        emailInput = findViewById(R.id.emailInput);
        dateInput = findViewById(R.id.dateInput);
        specifyInput = findViewById(R.id.specifyInput);
        sponsorInput = findViewById(R.id.sponsorInput);

        // To Disable EditText
        fullNameInput.setEnabled(false);
        addressInput.setEnabled(false);
        contactInput.setEnabled(false);
        emailInput.setEnabled(false);
        dateInput.setEnabled(false);

        // Current Date and Time
        Calendar c = Calendar.getInstance();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        current = String.valueOf(currentDateString);

        // On Create Function
        bottomNavigation();
        scanQr();
        dateScheduleSelector();
        spinnerItems();
        addToIncoming();
        viewIncomingAdmin();
        searchAdmin();
    }

    public void viewIncomingAdmin(){
        viewIncoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        ViewIncomingAdmin.class));
                finish();
            }
        });
    }

    public void addToIncoming(){
        incomingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((fullNameInput.getText().toString().isEmpty()) || (addressInput.getText().toString().isEmpty())
                        || (contactInput.getText().toString().isEmpty()) || (emailInput.getText().toString().isEmpty())
                        || (dateInput.getText().toString().isEmpty()) || (specifyInput.getText().toString().isEmpty())
                        || (specifyInput.getText().toString().isEmpty()) || (sponsorInput.getText().toString().isEmpty())
                ){
                    if (fullNameInput.getText().toString().isEmpty()){
                        showMessage("Hello Admin", "Please make sure to scan the QR Code");
                    }
                    if (dateInput.getText().toString().isEmpty()){
                        showMessage("Date Required", "Please Specify a Date");
                    }
                    if (specifyInput.getText().toString().isEmpty()){
                        specifyInput.setError("Please Specify");
                    }
                    if (sponsorInput.getText().toString().isEmpty()){
                        sponsorInput.setError("Sponsor is Required");
                    }
                }
                else {
                    confirmation("Add Resident to incoming Recipient?");
                }
            }
        });
    }

    public void addToFirebase(){
        try {
            loading.setVisibility(View.VISIBLE);
            date = dateInput.getText().toString();
            time = timeSelector.getSelectedItem().toString();
            type = benefitSelector.getSelectedItem().toString();
            specify = specifyInput.getText().toString();
            sponsor = sponsorInput.getText().toString();

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
            usersInfo.put("DateSchedule", date);
            usersInfo.put("Time", time);
            usersInfo.put("Type", type);
            usersInfo.put("Specify", specify);
            usersInfo.put("Sponsor", sponsor);
            usersInfo.put("DatePosted", current);
            usersInfo.put("PostedBy", fullName);

            incomingRecipient.child(residentId).setValue(usersInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        completion("Success!, You have successfully Added a Incoming Recipient!");
                        loading.setVisibility(View.GONE);
                    }
                    else {
                        showMessage("Failed", "Sorry Adding Recipient failed!");
                        loading.setVisibility(View.GONE);
                    }
                }
            });

        }catch (Exception exception){

        }
    }

    public void searchAdmin(){
        String idUsers = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = admin.orderByChild(idUsers);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    if (snapshot.exists()){
                        fullName = snapshot.child(idUsers).child("Full Name").getValue(String.class);
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

    public void spinnerItems(){
        timeSession.add("Morning");
        timeSession.add("Afternoon");

        ArrayAdapter<String> session = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeSession );
        session.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSelector.setAdapter(session);

        benefits.add("Cash");
        benefits.add("SAP");
        benefits.add("Goods");
        benefits.add("Others");

        ArrayAdapter<String> benefit = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, benefits );
        benefit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        benefitSelector.setAdapter(benefit);
    }

    public void dateScheduleSelector(){
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateSelected = new DatePickerFragment();
                dateSelected.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    public void searchResident(){
        Query query = root.child(residentId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    if (snapshot.exists()){
                        LName = snapshot.child("Surname").getValue(String.class);
                        fName = snapshot.child("FirstName").getValue(String.class);
                        MName = snapshot.child("MiddleName").getValue(String.class);
                        age = snapshot.child("Age").getValue(String.class);
                        address = snapshot.child("Address").getValue(String.class);
                        gender = snapshot.child("Gender").getValue(String.class);
                        stat = snapshot.child("CivilStatus").getValue(String.class);
                        contact = snapshot.child("Contact").getValue(String.class);
                        religion = snapshot.child("Religion").getValue(String.class);
                        occupation = snapshot.child("Occupation").getValue(String.class);
                        email = snapshot.child("Email").getValue(String.class);

                        String fullName = LName + ", " + fName + " " + MName + ".";
                        fullNameInput.setText(fullName);
                        addressInput.setText(address);
                        contactInput.setText(contact);
                        emailInput.setText(email);
                    }
                    else {
                        message("Sorry!", "Make Sure that the user is registered as a Residence!");
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
    public void onBackPressed() {
        Intent intent = new Intent(HomeScreenAdminIncoming.this, logIn.class);
        startActivity(intent);
        finish();
    }

    private void bottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_incoming);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_resident:
                        startActivity(new Intent(getApplicationContext(),
                                HomeScreenAdminResident.class));
                        finish();
                        bottomNavigationView.setSelectedItemId(R.id.nav_incoming);
                        return true;
                    case R.id.nav_incoming:
                        return true;
                    case R.id.nav_complete:
                        startActivity(new Intent(getApplicationContext(),
                                HomeScreenAdminComplete.class));
                        finish();
                        bottomNavigationView.setSelectedItemId(R.id.nav_incoming);
                        return true;
                }
                finish();
                return false;
            }
        });
    }

    public void scanQr(){
        scanQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HomeScreenAdminIncoming.this);
                intentIntegrator.setCaptureActivity(CaptureAct.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scanning Code");
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                residentId = result.getContents();
                searchResident();
            }
            else {
                message("Sorry!", "You did not scan any QR ");
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            dateInput.setText(currentDateString);
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
                            fullNameInput.setText("");
                            addressInput.setText("");
                            contactInput.setText("");
                            emailInput.setText("");
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