package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    // EditText
    EditText emailReset;

    // Button
    Button resetBtn;

    // Progress Bar
    ProgressBar loading;

    // Firebase Auth
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        // EditText
        emailReset = findViewById(R.id.emailResetInput);

        // Button
        resetBtn = findViewById(R.id.resetBtn);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // Firebase Auth
        auth = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEmail();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                logIn.class));
        finish();
    }

    private void resetEmail() {
        String email = emailReset.getText().toString().trim();
        if (  (emailReset.getText().toString().isEmpty()) || (!Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            showMessage("Sorry!", "Please Check your email");
        }else {
            loading.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        showMessage("Success", "Please Check your email to reset password");
                        loading.setVisibility(View.GONE);
                    }
                    else {
                        showMessage("Sorry!", "Reset Password Failed");
                        loading.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}