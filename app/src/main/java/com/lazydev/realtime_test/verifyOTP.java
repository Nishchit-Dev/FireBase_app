package com.lazydev.realtime_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class verifyOTP extends AppCompatActivity {
    private Button verfying;
    private EditText otp ;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        otp = findViewById(R.id.Otp);
        verfying = findViewById(R.id.verify);

        verfying.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle i = getIntent().getExtras();
                String code = otp.getText().toString();
                String phoneno = i.getString("phn");
                String verification = i.getString("verificationId");
                if(verification != null ) {
                    PhoneAuthCredential cred = PhoneAuthProvider.getCredential(verification, code);
                    FirebaseAuth.getInstance().signInWithCredential(cred)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"User is verified ",Toast.LENGTH_SHORT).show();
                                        databaseReference = FirebaseDatabase.getInstance().getReference("users");
                                        databaseReference.child(phoneno);
                                        Intent i = new Intent(verifyOTP.this,Signup.class);
                                        i.putExtra("phone",phoneno);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"verification Failed",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            }
        });
    }
}