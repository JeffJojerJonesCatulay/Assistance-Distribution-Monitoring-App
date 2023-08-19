package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class addAdmin extends AppCompatActivity {
    // EditText
    EditText fullNameInput_admin, addressInput_admin, positionInput_admin,
            contactInput_admin, emailInput_admin, passwordInput_admin,
            confirmPasswordInput_admin;

    // Button
    Button registerBtn_admin;

    // Progress Bar
    ProgressBar loading;

    // CheckBox
    CheckBox showPassword;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Admins");

    // Authentication
    private FirebaseAuth mAuth;

    // Strings
    String fullNameInput_admin_val, addressInput_admin_val,
            positionInput_admin_val, contactInput_admin_val,
            emailInput_admin_val, passwordInput_admin_val,
            access_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_admin);

        // EditText
        fullNameInput_admin = findViewById(R.id.fullNameInput_admin);
        addressInput_admin = findViewById(R.id.addressInput_admin);
        positionInput_admin = findViewById(R.id.position_admin);
        contactInput_admin = findViewById(R.id.contactInput_admin);
        emailInput_admin = findViewById(R.id.emailInput_admin);
        passwordInput_admin = findViewById(R.id.pass_admin);
        confirmPasswordInput_admin = findViewById(R.id.passwordConfirmationInput_admin);

        // Button
        registerBtn_admin = findViewById(R.id.registerBtn_admin);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // Authentication
        mAuth = FirebaseAuth.getInstance();

        // CheckBox
        showPassword = findViewById(R.id.showPassword);

        // On Create Functions
        addAdminRegister();
        showPasswordOption();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                HomeScreenAdminResident.class));
        finish();
    }

    public void showPasswordOption(){
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showPassword.isChecked()){
                    passwordInput_admin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPasswordInput_admin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    passwordInput_admin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPasswordInput_admin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    public void addAdminRegister(){
        registerBtn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((fullNameInput_admin.getText().toString().isEmpty()) || (addressInput_admin.getText().toString().isEmpty()) || (positionInput_admin.getText().toString().isEmpty())
                || (emailInput_admin.getText().toString().isEmpty()) || (passwordInput_admin.getText().toString().isEmpty()) || (confirmPasswordInput_admin.getText().toString()).isEmpty()
                || (!Patterns.EMAIL_ADDRESS.matcher(emailInput_admin.getText().toString()).matches()) || (contactInput_admin.getText().toString().isEmpty())
                ){
                    checkIfEmpty();
                } else {
                    if (passwordInput_admin.getText().toString().equals(confirmPasswordInput_admin.getText().toString())){
                        confirmation("Do you want to make this as an Admin?");
                    }
                    else {
                        showMessage("Incorrect!", "Password Doesn't Match");
                    }
                }
            }
        });
    }

    public void addToFirebase(){
        fullNameInput_admin_val = fullNameInput_admin.getText().toString();
        addressInput_admin_val = addressInput_admin.getText().toString();
        positionInput_admin_val = positionInput_admin.getText().toString();
        contactInput_admin_val = contactInput_admin.getText().toString();
        emailInput_admin_val = emailInput_admin.getText().toString();
        passwordInput_admin_val = passwordInput_admin.getText().toString();
        access_val = "Admin";

        try {
            HashMap<String, String> adminInfo = new HashMap<>();
            adminInfo.put("FullName", fullNameInput_admin_val);
            adminInfo.put("Address", addressInput_admin_val);
            adminInfo.put("Position", positionInput_admin_val);
            adminInfo.put("Contact", contactInput_admin_val);
            adminInfo.put("Email", emailInput_admin_val);
            adminInfo.put("Access", access_val);

            loading.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(emailInput_admin_val, passwordInput_admin_val).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        root.child(userId).setValue(adminInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    completion("Success!, You have successfully added an Admin!");
                                    loading.setVisibility(View.GONE);
                                }else {
                                    showMessage("Failed", "Sorry Adding Admin failed!");
                                    loading.setVisibility(View.GONE);
                                }
                            }
                        });

                    }else {
                        showMessage("Failed", "Please make sure your email and password is correct!");
                        loading.setVisibility(View.GONE);
                    }
                }
            });

        }catch (Exception exception){

        }
    }

    public void checkIfEmpty(){
        if (fullNameInput_admin.getText().toString().isEmpty()){
            fullNameInput_admin.setError("Full Name Is Required");
        }
        if (addressInput_admin.getText().toString().isEmpty()){
            addressInput_admin.setError("Address Is Required");
        }
        if (positionInput_admin.getText().toString().isEmpty()){
            positionInput_admin.setError("Position Is Required");
        }
        if (contactInput_admin.getText().toString().isEmpty()){
            contactInput_admin.setError("Contact Is Required");
        }
        if (emailInput_admin.getText().toString().isEmpty()){
            emailInput_admin.setError("Valid Email Is Required");
        }
        if (passwordInput_admin.getText().toString().isEmpty()){
            passwordInput_admin.setError("Password Is Required");
        }
        if (confirmPasswordInput_admin.getText().toString().isEmpty()){
            confirmPasswordInput_admin.setError("Please Confirm Password");
        }
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void confirmation(String title){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(title)
                    .setPositiveButton("Make Admin", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            addToFirebase();
                        }
                    })
                    .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

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