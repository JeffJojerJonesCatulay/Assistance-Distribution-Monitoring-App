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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HomeScreenAdminComplete extends AppCompatActivity {
    // Button
    Button scanQrBtn, completeBtn,viewResident;

    // Progress Bar
    ProgressBar loading;

    // EditText
    EditText fullNameInput, addressInput, contactInput, emailInput,
            dateInput, specifyInput, sponsorInput, timeInput, typeInput;

    // Strings
    String  LName_incoming, fName_incoming, MName_incoming, age_incoming, gender_incoming,
            stat_incoming, contact_incoming, religion_incoming,
            occupation_incoming, email_incoming, address_incoming,
            date_incoming, time_incoming, type_incoming, specify_incoming,
            sponsor_incoming;
    String LName_complete, fName_complete, MName_complete, age_complete, gender_complete,
            stat_complete, contact_complete, religion_complete,
            occupation_complete, email_complete, address_complete,
            date_complete, time_complete, type_complete, specify_complete,
            sponsor_complete, instances_complete, dateReceived_complete;
    String residentId, current;
    String fullName;

    Integer instances = 1, updatedInstances;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Residence");
    private DatabaseReference incomingRecipient = db.getReference().child("IncomingRecipient");
    private DatabaseReference completeRecipient = db.getReference().child("CompleteRecipient");
    private DatabaseReference admin = db.getReference().child("Admins");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_screen_admin_complete);

        // Button
        scanQrBtn = findViewById(R.id.scanQrBtn);
        completeBtn = findViewById(R.id.completeBtn);
        viewResident = findViewById(R.id.viewResidentBtn);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // EditText
        fullNameInput = findViewById(R.id.fullNameInput);
        addressInput = findViewById(R.id.addressInput);
        contactInput = findViewById(R.id.contactInput);
        emailInput = findViewById(R.id.emailInput);
        dateInput = findViewById(R.id.dateInput);
        specifyInput = findViewById(R.id.specifyInput);
        sponsorInput = findViewById(R.id.sponsorInput);
        timeInput = findViewById(R.id.timeInput);
        typeInput = findViewById(R.id.typeInput);

        // To Disable EditText
        fullNameInput.setEnabled(false);
        addressInput.setEnabled(false);
        contactInput.setEnabled(false);
        emailInput.setEnabled(false);
        dateInput.setEnabled(false);
        specifyInput.setEnabled(false);
        sponsorInput.setEnabled(false);
        timeInput.setEnabled(false);
        typeInput.setEnabled(false);

        // On Create Function
        bottomNavigation();
        scanQr();
        completeTransaction();
        viewCompleteAdmin();
        searchAdmin();

        // Current Date and Time
        Calendar c = Calendar.getInstance();
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        current = String.valueOf(currentDateString);
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

    public void viewCompleteAdmin(){
        viewResident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        ViewCompleteAdmin.class));
                finish();
            }
        });
    }

    public void completeTransaction(){
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullNameInput.getText().toString().isEmpty()){
                    message("Sorry!", "Please Scan QR First");
                }
                else {
                    incomingRecipient.child(residentId).removeValue();
                    searchResidentComplete();
                }
            }
        });
    }

    public void addToFirebase(){
        try {
            loading.setVisibility(View.VISIBLE);

            String instancesVal = String.valueOf(instances);

            HashMap<String, String> usersInfo = new HashMap<>();
            usersInfo.put("Surname", LName_incoming);
            usersInfo.put("FirstName", fName_incoming);
            usersInfo.put("MiddleName", MName_incoming);
            usersInfo.put("Age", age_incoming);
            usersInfo.put("Address", address_incoming);
            usersInfo.put("Gender", gender_incoming);
            usersInfo.put("CivilStatus", stat_incoming);
            usersInfo.put("Contact", contact_incoming);
            usersInfo.put("Religion", religion_incoming);
            usersInfo.put("Occupation", occupation_incoming);
            usersInfo.put("Email", email_incoming);
            usersInfo.put("DateSchedule", date_incoming);
            usersInfo.put("Time", time_incoming);
            usersInfo.put("Type", type_incoming);
            usersInfo.put("Specify", specify_incoming);
            usersInfo.put("Sponsor", sponsor_incoming);
            usersInfo.put("Instances", instancesVal);
            usersInfo.put("DateReceived", current);
            usersInfo.put("NotedBy", fullName);


            completeRecipient.child(residentId).setValue(usersInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        completion("Success!, You have successfully Completed the transaction!");
                        loading.setVisibility(View.GONE);
                        // Display Resident Info
                        fullNameInput.setText("");
                        addressInput.setText("");
                        contactInput.setText("");
                        emailInput.setText("");

                        // Display Benefit Info
                        dateInput.setText("");
                        timeInput.setText("");
                        typeInput.setText("");
                        specifyInput.setText("");
                        sponsorInput.setText("");
                    }
                    else {
                        showMessage("Failed", "Sorry Completion Failed");
                        loading.setVisibility(View.GONE);
                    }
                }
            });

        }catch (Exception exception){

        }
    }

    public void updateToFirebase(){
        try {
            loading.setVisibility(View.VISIBLE);

            String updatedDate = date_complete + " and " + date_incoming;
            String updatedTime = time_complete + " and " + time_incoming;
            String updatedType = type_complete + " and " + type_incoming;
            String updatedSpecify = specify_complete + " and " + specify_incoming;
            String updatedSponsor = sponsor_complete + " and " + sponsor_incoming;
            String updatedDateReceived = dateReceived_complete + " and " + current;

            updatedInstances = Integer.parseInt(instances_complete) + 1;
            String updatedInstances_val = String.valueOf(updatedInstances);

            // Resident Info
            HashMap<String, String> usersInfo = new HashMap<>();
            usersInfo.put("Surname", LName_complete);
            usersInfo.put("FirstName", fName_complete);
            usersInfo.put("MiddleName", MName_complete);
            usersInfo.put("Age", age_complete);
            usersInfo.put("Address", address_complete);
            usersInfo.put("Gender", gender_complete);
            usersInfo.put("CivilStatus", stat_complete);
            usersInfo.put("Contact", contact_complete);
            usersInfo.put("Religion", religion_complete);
            usersInfo.put("Occupation", occupation_complete);
            usersInfo.put("Email", email_complete);

            // Benefit Info
            usersInfo.put("DateSchedule", updatedDate);
            usersInfo.put("Time", updatedTime);
            usersInfo.put("Type", updatedType);
            usersInfo.put("Specify", updatedSpecify);
            usersInfo.put("Sponsor", updatedSponsor);
            usersInfo.put("Instances", updatedInstances_val);
            usersInfo.put("DateReceived", updatedDateReceived);
            usersInfo.put("NotedBy", fullName);

            completeRecipient.child(residentId).setValue(usersInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        completion("Success!, You have successfully Completed the transaction!");
                        loading.setVisibility(View.GONE);
                        // Display Resident Info
                        fullNameInput.setText("");
                        addressInput.setText("");
                        contactInput.setText("");
                        emailInput.setText("");

                        // Display Benefit Info
                        dateInput.setText("");
                        timeInput.setText("");
                        typeInput.setText("");
                        specifyInput.setText("");
                        sponsorInput.setText("");
                    }
                    else {
                        showMessage("Failed", "Sorry Completion Failed");
                        loading.setVisibility(View.GONE);
                    }
                }
            });

        }catch (Exception exception){

        }
    }

    public void searchResidentComplete(){
        Query query = completeRecipient.child(residentId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    if (snapshot.exists()){
                        LName_complete = snapshot.child("Surname").getValue(String.class);
                        fName_complete = snapshot.child("FirstName").getValue(String.class);
                        MName_complete = snapshot.child("MiddleName").getValue(String.class);
                        age_complete = snapshot.child("Age").getValue(String.class);
                        address_complete = snapshot.child("Address").getValue(String.class);
                        gender_complete = snapshot.child("Gender").getValue(String.class);
                        stat_complete = snapshot.child("CivilStatus").getValue(String.class);
                        contact_complete = snapshot.child("Contact").getValue(String.class);
                        religion_complete = snapshot.child("Religion").getValue(String.class);
                        occupation_complete = snapshot.child("Occupation").getValue(String.class);
                        email_complete = snapshot.child("Email").getValue(String.class);

                        // Benefit Info
                        date_complete = snapshot.child("DateSchedule").getValue(String.class);
                        time_complete = snapshot.child("Time").getValue(String.class);
                        type_complete = snapshot.child("Type").getValue(String.class);
                        specify_complete = snapshot.child("Specify").getValue(String.class);
                        sponsor_complete = snapshot.child("Sponsor").getValue(String.class);
                        instances_complete = snapshot.child("Instances").getValue(String.class);
                        dateReceived_complete = snapshot.child("DateReceived").getValue(String.class);


                        confirmationUpdate("Do you want to complete the transaction?");
                    }
                    else {
                        confirmationAdd("Do you want to complete the transaction?");
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

    public void searchResidentRecipient(){
        Query query = incomingRecipient.child(residentId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    if (snapshot.exists()){
                        // Resident Info
                        LName_incoming = snapshot.child("Surname").getValue(String.class);
                        fName_incoming = snapshot.child("FirstName").getValue(String.class);
                        MName_incoming = snapshot.child("MiddleName").getValue(String.class);
                        age_incoming = snapshot.child("Age").getValue(String.class);
                        address_incoming = snapshot.child("Address").getValue(String.class);
                        gender_incoming = snapshot.child("Gender").getValue(String.class);
                        stat_incoming = snapshot.child("CivilStatus").getValue(String.class);
                        contact_incoming = snapshot.child("Contact").getValue(String.class);
                        religion_incoming = snapshot.child("Religion").getValue(String.class);
                        occupation_incoming = snapshot.child("Occupation").getValue(String.class);
                        email_incoming = snapshot.child("Email").getValue(String.class);

                        // Benefit Info
                        date_incoming = snapshot.child("DateSchedule").getValue(String.class);
                        time_incoming = snapshot.child("Time").getValue(String.class);
                        type_incoming = snapshot.child("Type").getValue(String.class);
                        specify_incoming = snapshot.child("Specify").getValue(String.class);
                        sponsor_incoming = snapshot.child("Sponsor").getValue(String.class);

                        String fullName = LName_incoming + ", " + fName_incoming + " " + MName_incoming + ".";
                        // Display Resident Info
                        fullNameInput.setText(fullName);
                        addressInput.setText(address_incoming);
                        contactInput.setText(contact_incoming);
                        emailInput.setText(email_incoming);

                        // Display Benefit Info
                        dateInput.setText(date_incoming);
                        timeInput.setText(time_incoming);
                        typeInput.setText(type_incoming);
                        specifyInput.setText(specify_incoming);
                        sponsorInput.setText(sponsor_incoming);
                    }
                    else {
                        message("Sorry!", "Make Sure that the user is incoming recipient!");
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
        Intent intent = new Intent(HomeScreenAdminComplete.this, logIn.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                residentId = result.getContents();
                searchResidentRecipient();
            }
            else {
                message("Sorry!", "You did not scan any QR ");
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanQr(){
        scanQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HomeScreenAdminComplete.this);
                intentIntegrator.setCaptureActivity(CaptureAct.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scanning Code");
                intentIntegrator.initiateScan();
            }
        });
    }

    private void bottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_complete);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_resident:
                        startActivity(new Intent(getApplicationContext(),
                                HomeScreenAdminResident.class));
                        finish();
                        bottomNavigationView.setSelectedItemId(R.id.nav_complete);
                        return true;
                    case R.id.nav_incoming:
                        startActivity(new Intent(getApplicationContext(),
                                HomeScreenAdminIncoming.class));
                        finish();
                        bottomNavigationView.setSelectedItemId(R.id.nav_complete);
                        return true;
                    case R.id.nav_complete:
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

    public void confirmationAdd(String title){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(title)
                    .setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            addToFirebase();
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

    public void confirmationUpdate(String title){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(title)
                    .setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            updateToFirebase();
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