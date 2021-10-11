package com.lazydev.realtime_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    private EditText username, phone, email, password;
    private Button Signup;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        username = findViewById(R.id.userName);
        Bundle i = getIntent().getExtras();
        String Phn = i.getString("phone");
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+Phn);
        Signup = findViewById(R.id.signup);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, phn, pass, mail;
                user = username.getText().toString();
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("username", user);
                hashMap.put("token","bUOIEAbfoBAOCBOAWbwcoaefklabnaobdfOHCWoadoAwd");
                databaseReference.setValue(hashMap);
                username.setText("");
                Toast.makeText(getApplicationContext(),"username added successfully ",Toast.LENGTH_LONG).show();
                System.out.println(FirebaseDatabase.getInstance().getReference("helllo"));



            }
        });
             



    }
}