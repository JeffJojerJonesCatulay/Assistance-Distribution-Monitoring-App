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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adminLogIn extends AppCompatActivity {
    // Button
    Button addAdminBtn;

    // EditText
    EditText emailInputAdmin, passwordInputAdmin;

    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Admins");

    // Authentication
    private FirebaseAuth mAuth;

    // String
    String emailAdminVal, passwordAdminVal;

    // Progress Bar
    ProgressBar loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_log_in);

        // Button
        addAdminBtn = findViewById(R.id.adminLogInBtn);

        // Authentication
        mAuth = FirebaseAuth.getInstance();

        // EditText
        emailInputAdmin = findViewById(R.id.usernameInputAdmin);
        passwordInputAdmin = findViewById(R.id.passwordInputAdmin);

        // Progress Bar
        loading = findViewById(R.id.loading);

        // On Create Functions
        verifyAdmin();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                logIn.class));
        finish();
    }

    public void verifyAdmin(){
        addAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAdminVal = emailInputAdmin.getText().toString();
                passwordAdminVal = passwordInputAdmin.getText().toString();

                if ((emailInputAdmin.getText().toString().isEmpty()) || (passwordInputAdmin.getText().toString().isEmpty())){
                    if (emailInputAdmin.getText().toString().isEmpty()){
                        emailInputAdmin.setError("Please Input a Valid Email");
                    }
                    if(!Patterns.EMAIL_ADDRESS.matcher(emailInputAdmin.getText().toString()).matches()){
                        emailInputAdmin.setError("Please Input a Valid Email");
                    }
                    if (passwordInputAdmin.getText().toString().isEmpty()){
                        passwordInputAdmin.setError("Password is Required");
                    }
                }else {
                    loading.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailAdminVal, passwordAdminVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified()){
                                    search();
                                }
                                else {
                                    user.sendEmailVerification();
                                    showMessage("Email Verification Required", "Please check your email to verify your account");
                                }
                            }
                            else {
                                showMessage("Something is Wrong!", "Please check your email and password");
                            }
                        }
                    });
                }
            }
        });
    }

    public void search(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = root.orderByChild(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    if (snapshot.exists()){
                        String access = snapshot.child(userId).child("Access").getValue(String.class);
                        if (access.equals("Admin")){
                            Intent intent = new Intent(adminLogIn.this, HomeScreenAdminResident.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            loading.setVisibility(View.GONE);
                            showMessage("Errors", "Error Logging as Admin");
                        }

                    }else {
                        loading.setVisibility(View.GONE);
                        showMessage("Error", "You are not a Admin");
                    }
                }catch (Exception exception){
                    loading.setVisibility(View.GONE);
                    showMessage("Error", "You are not a Admin");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}