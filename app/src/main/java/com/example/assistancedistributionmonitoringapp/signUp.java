package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import java.util.regex.Pattern;

public class signUp extends AppCompatActivity {
    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    // EditText
    EditText surnameInput, firstNameInput, middleNameInput,
            ageInput, addressInput, contactInput, emailInput,
            religionInput, occupationInput, pass, confirmPasswordInput;

    // Strings
    String surnameVal, firstNameVal, middleNameVal,
            ageVal, addressVal, genderVal, civilVal, contactVal, emailVal,
            religionVal, occupationVal, passwordVal, accessVal;

    // Spinner
    Spinner genderInput, civilStatusInput;

    // Button
    Button registerBtn;

    // CheckBox
    CheckBox showPassword;

    // Progress Bar
    ProgressBar loading;

    // ArrayList
    ArrayList<String> genderChoice = new ArrayList<>();
    ArrayList<String> civilStatusChoice = new ArrayList<>();

    // Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

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
        pass = findViewById(R.id.pass);
        confirmPasswordInput = findViewById(R.id.passwordConfirmationInput);

        // Authentication
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Spinner
        genderInput = findViewById(R.id.genderInput);
        civilStatusInput = findViewById(R.id.civilStatusInput);

        // Button
        registerBtn = findViewById(R.id.registerBtn);

        // CheckBox
        showPassword = findViewById(R.id.showPassword);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // Authentication
        mAuth = FirebaseAuth.getInstance();

        // On Create Function
        register();
        spinnerItems();
        showPasswordOption();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                logIn.class));
        finish();
    }

    public void goToLogIn(){
        Intent intent = new Intent(signUp.this, logIn.class);
        startActivity(intent);
        finish();
    }

    public void showPasswordOption(){
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword.isChecked()){
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
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

    public void register(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ( (surnameInput.getText().toString().isEmpty()) || (firstNameInput.getText().toString().isEmpty()) || (middleNameInput.getText().toString().isEmpty()) || (ageInput.getText().toString().isEmpty())
                            || (addressInput.getText().toString().isEmpty()) || (contactInput.getText().toString().isEmpty()) || (emailInput.getText().toString().isEmpty()) || (religionInput.getText().toString().isEmpty())
                            || (occupationInput.getText().toString().isEmpty()) || (pass.getText().toString().isEmpty()) || (confirmPasswordInput.getText().toString().isEmpty())
                            || (genderInput.getSelectedItemPosition() == 0) || (civilStatusInput.getSelectedItemPosition() == 0) || (!Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches())
                    ) {
                        changeFieldColorIfEmpty();
                    }
                    else {
                        if (pass.getText().toString().equals(confirmPasswordInput.getText().toString())){
                            confirmation("END USER LICENSE AGREEMENT (EULA)", getString(R.string.eula));
                        }else {
                            showMessage("Incorrect!", "Password Doesn't Match");
                        }
                    }
                }catch (Exception exception){

                }
            }
        });
    }

    public void addToFirebase(){
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
        emailVal = emailInput.getText().toString();
        passwordVal = pass.getText().toString();
        accessVal = "User";

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
            usersInfo.put("Email", emailVal);
            usersInfo.put("Access", accessVal);

            loading.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        root.child(userId).setValue(usersInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    completion("Success!, You have successfully registered!");
                                    loading.setVisibility(View.GONE);
                                }
                                else {
                                    showMessage("Failed", "Sorry registration failed!");
                                    loading.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else {
                        showMessage("Failed Registration", "Please make sure your email and password is correct!");
                    }
                }
            });

        }catch (Exception exception){

        }
    }

    public void confirmation(String title, String message){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message)
                    .setPositiveButton("I AGREE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            addToFirebase();
                        }
                    })
                    .setNegativeButton("I DON'T AGREE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
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

        if (emailInput.getText().toString().isEmpty()){
            emailInput.setError("Email Is Required");
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches()){
            emailInput.setError("Please Input Valid Email");
        }

        if (religionInput.getText().toString().isEmpty()){
            religionInput.setError("Religion Is Required");
        }
        if (occupationInput.getText().toString().isEmpty()){
            occupationInput.setError("Occupation Is Required");
        }
        if (pass.getText().toString().isEmpty()){
            pass.setError("Password Is Required");
        }
        if (confirmPasswordInput.getText().toString().isEmpty()){
            confirmPasswordInput.setError("Confirm Password Is Required");
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
                            goToLogIn();
                        }
                    });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }
}