package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class logIn extends AppCompatActivity {
    // EditText
    EditText usernameInput, passwordInput;

    // TextView
    TextView forgetPassword, adminLogInPage;

    // Button
    Button logInBtn, signUpBtn, clearLog;

    // CheckBox
    CheckBox showPassword;

    // Progress Bar
    ProgressBar loading;

    // Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch saveLogInInfo;

    // Authentication
    private FirebaseAuth mAuth;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    // String
    String emailVal, passwordVal;

    // Persistent Data
    private String emailVal_shared, passwordVal_shared;
    private Boolean isSaved;

    // Final Strings
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String SAVE = "save";

    // SharedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        // EditText
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        // TextView
        forgetPassword = findViewById(R.id.forgetPassword);
        adminLogInPage = findViewById(R.id.adminLogInPageBtn);

        // Button
        logInBtn = findViewById(R.id.logInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        clearLog = findViewById(R.id.clearLogs);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // Switch
        saveLogInInfo = findViewById(R.id.saveLogInInfo);

        // Authentication
        mAuth = FirebaseAuth.getInstance();

        // On Create Functions
        signUpOption();
        logInOption();
        loadData();
        updateData();
        clearLogOption();
        forgetPasswordOption();
        adminLogInPage();

        if (isNetworkAvailable()){

        }else {
            retryConnection("No Internet Connection, Please make sure you are connected first");
        }

    }

    @Override
    public void onRestart(){
        super.onRestart();
        if (isNetworkAvailable()){

        }else {
            retryConnection("No Internet Connection, Please make sure you are connected first");
        }
    }

    public void adminLogInPage(){
        adminLogInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logIn.this, adminLogIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void forgetPasswordOption(){
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(logIn.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void clearLogOption(){
        clearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().remove(EMAIL).apply();
                sharedPreferences.edit().remove(PASSWORD).apply();
                sharedPreferences.edit().remove(SAVE).apply();
                usernameInput.setText("");
                passwordInput.setText("");
                saveLogInInfo.setChecked(false);
            }
        });
    }

    public void logInOption(){
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loading.setVisibility(View.VISIBLE);
                    emailVal = usernameInput.getText().toString();
                    passwordVal = passwordInput.getText().toString();

                    if ((usernameInput.getText().toString().isEmpty()) || (passwordInput.getText().toString().isEmpty())){
                        if (usernameInput.getText().toString().isEmpty()){
                            usernameInput.setError("Email is Required");
                            loading.setVisibility(View.GONE);
                        }
                        if (passwordInput.getText().toString().isEmpty()){
                            passwordInput.setError("Password is Required");
                            loading.setVisibility(View.GONE);
                        }
                    }
                    else {
                        mAuth.signInWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user.isEmailVerified()){
                                        if (saveLogInInfo.isChecked()){
                                            saveData();
                                            loading.setVisibility(View.GONE);
                                        }
                                        else {
                                            Intent intent = new Intent(logIn.this, HomeScreenUser.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    else {
                                        user.sendEmailVerification();
                                        showMessage("Email Verification Required", "Please check your email to verify your account");
                                        loading.setVisibility(View.GONE);
                                    }
                                }
                                else {
                                    showMessage("Error!", "Please make sure that the email is registered. If registered please check the Email and Password");
                                    loading.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }catch (Exception exception){

                }
            }
        });
    }

    public void saveData(){
        try {
            sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            editor = sharedPreferences.edit();
            sharedPreferences.edit().remove(EMAIL).apply();
            sharedPreferences.edit().remove(PASSWORD).apply();
            sharedPreferences.edit().remove(SAVE).apply();
            editor.putString(EMAIL, emailVal);
            editor.putString(PASSWORD, passwordVal);
            editor.putBoolean(SAVE, saveLogInInfo.isChecked());
            editor.apply();
            Intent intent = new Intent(logIn.this, HomeScreenUser.class);
            startActivity(intent);
            finish();
        }catch (Exception exception){
            showMessage("Error!", "Something is Wrong!");
        }
    }

    public void loadData(){
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        emailVal_shared = sharedPreferences.getString(EMAIL,"");
        isSaved = sharedPreferences.getBoolean(SAVE,false);
        passwordVal_shared = sharedPreferences.getString(PASSWORD,"");
    }

    public void updateData(){
        usernameInput.setText(emailVal_shared);
        passwordInput.setText(passwordVal_shared);
        saveLogInInfo.setChecked(isSaved);
    }

    public void signUpOption(){
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        signUp.class));
                finish();
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

    public boolean isNetworkAvailable(){
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null){
                networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        } catch (NullPointerException e){
            return false;
        }
        return false;
    }

    public void retryConnection(String title){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(title)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (isNetworkAvailable()){
                                showMessage("Success", "Internet Connected");
                            }else {
                                retryConnection("No Internet Connection, Please make sure you are connected first");
                            }
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }
}