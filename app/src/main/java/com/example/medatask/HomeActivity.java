package com.example.medatask;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mauth;
    private String user_id;
    private DatabaseReference mdatabaseref;
    private String status;
    private List<user> mlist;
    private user current_user;
    private Button google_map,signout;
    private ListView onlineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        google_map=findViewById(R.id.map);
        google_map=findViewById(R.id.map);
        signout=findViewById(R.id.logout);
        onlineList = findViewById(R.id.user_list);
        mauth = FirebaseAuth.getInstance();
        user_id = mauth.getCurrentUser().getUid();
       mdatabaseref=FirebaseDatabase.getInstance().getReference().child("Users");
        mlist=new ArrayList<>();
        mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postsnapshot:dataSnapshot.getChildren())
                {
                    user curr_user =postsnapshot.getValue(user.class);
                    if(curr_user.getStatus()=="online")
                    mlist.add(curr_user);
                }
                onlinearrayAdapter madapter=new onlinearrayAdapter(HomeActivity.this,R.layout.userdetails,mlist);
                onlineList.setAdapter(madapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });
         google_map.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                         Uri.parse("google.navigation:q=an+address+city"));
                 startActivity(intent);

             }
         });
    }
    public void userStatus(String status){
        DatabaseReference current_user_ref=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        current_user_ref.child("status").setValue(status);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userStatus("online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        userStatus("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        userStatus("online");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userStatus("offline");
    }
}
