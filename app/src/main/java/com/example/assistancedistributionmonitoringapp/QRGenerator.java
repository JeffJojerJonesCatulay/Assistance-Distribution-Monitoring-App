package com.example.assistancedistributionmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

public class QRGenerator extends AppCompatActivity {
    // Firebase
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    // Authentication
    private FirebaseAuth mAuth;

    // ImageView
    ImageView qr;

    // TextView
    TextView regName, regEmail;

    // Strings
    String idUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_generator);

        // Authentication
        mAuth = FirebaseAuth.getInstance();

        // ImageView
        qr = findViewById(R.id.qr);

        // TextView
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);

        // On Create Function
        generateQrCode();
        search();
    }

    public void generateQrCode(){
        idUsers = FirebaseAuth.getInstance().getCurrentUser().getUid();
        MultiFormatWriter writer = new MultiFormatWriter();
        try{
            BitMatrix matrix = writer.encode(idUsers, BarcodeFormat.QR_CODE
                    , 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qr.setImageBitmap(bitmap);
        }catch (Exception exception){
            message("Sorry!", "Something is Wrong!");
        }
    }

    public void search(){
        try {
            Query query = root.orderByChild(idUsers);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try{
                        if (snapshot.exists()){
                            String fName = snapshot.child(idUsers).child("FirstName").getValue(String.class);
                            String LName = snapshot.child(idUsers).child("Surname").getValue(String.class);
                            String MName = snapshot.child(idUsers).child("MiddleName").getValue(String.class);
                            String email = snapshot.child(idUsers).child("Email").getValue(String.class);
                            String fullName = LName + ", " + fName + " " + MName + ".";
                            regName.setText(fullName);
                            regEmail.setText(email);

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
        }catch (Exception exception){

        }
    }

    public void message(String title, String message){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),
                HomeScreenUser.class));
        finish();
    }
}