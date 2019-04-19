package com.example.medatask;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity  {

    private EditText email,password,name;
    private Button registrationbtn,sign_inbtn;
    private FirebaseAuth mauth;
    private  FirebaseAuth.AuthStateListener firebaseauthlistener;
    private  String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth=FirebaseAuth.getInstance();
        firebaseauthlistener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        name=(EditText)findViewById(R.id.userName);
        email=(EditText)findViewById(R.id.user_email);
        password=(EditText)findViewById(R.id.user_password);
        registrationbtn=(Button)findViewById(R.id.register_btn);
        sign_inbtn=(Button)findViewById(R.id.signin_btn);

        registrationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email=email.getText().toString();
                final String Password=password.getText().toString();
                mauth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"Sign up Error",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            user_id=mauth.getCurrentUser().getUid();
                            DatabaseReference current_user_db=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                            user new_user=new user("online",name.getText().toString().trim());
                            current_user_db.setValue(new_user);
                           // current_user_db.setValue(true);
                        }
                    }
                });
            }
        });
        sign_inbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email=email.getText().toString();
                final String Password=password.getText().toString();
                mauth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"Sign in Error",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(firebaseauthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).setValue("offline");
        mauth.removeAuthStateListener(firebaseauthlistener);
    }
    public  void  userstatus(String status)
    {

    }
}
