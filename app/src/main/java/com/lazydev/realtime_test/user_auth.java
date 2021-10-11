package com.lazydev.realtime_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;

public class user_auth extends AppCompatActivity {
    private Button send_otp;
    private EditText user_Phn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_auth);
        send_otp = findViewById(R.id.send_Otp);
        user_Phn = findViewById(R.id.user_phn);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
        }
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "user Exits", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "user does not Exits", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("users/9624211194").addValueEventListener(eventListener);

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = user_Phn.getText().toString();
                if (!phone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "hello succesfully ", Toast.LENGTH_SHORT).show();

                    int time_out = 60;
                    FirebaseAuth firebaseAuth;
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                                    .setPhoneNumber("+91" + phone)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(user_auth.this)                 // Activity (for callback binding)
                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            Toast.makeText(getApplicationContext(), "verifyed succesfully ", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            super.onCodeSent(s, forceResendingToken);
                                            Toast.makeText(getApplicationContext(), "Code Sent Successfully ", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(user_auth.this, verifyOTP.class);
                                            i.putExtra("verificationId", s);
                                            i.putExtra("phn", phone);
                                            startActivity(i);

                                        }
                                    })          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0 ){
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                // here app request the contentResolver from os
                ContentResolver contentResolver = getContentResolver();
                // uri says path of the thing that app want to access from other app
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                // querying the data using uri
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                System.out.println(cursor.getCount());

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String contact = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("phn no :", contact);
                    }
                }
            }
        }

    }
}