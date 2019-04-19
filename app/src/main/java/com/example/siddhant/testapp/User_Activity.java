package com.example.siddhant.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class User_Activity extends AppCompatActivity {

    private TextView user_phone_no,url_tv;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    private MaterialButton bt_signOut;
    private String user_id;
    private String phone_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        user_phone_no = findViewById(R.id.ver_phn);
        bt_signOut = findViewById(R.id.sign_out_btn);
        url_tv = findViewById(R.id.url_pic);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        user_id = firebaseUser.getUid();

        Intent i = getIntent();
        phone_no = i.getStringExtra("mobile");
        bt_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference("visitors").child(user_id);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Phone").exists()){
                            Toast.makeText(getApplicationContext(),"User Registered",Toast.LENGTH_SHORT).show();
                            phone_no = dataSnapshot.child("Phone").getValue().toString();
                            user_phone_no.setText(phone_no);
                            user_phone_no.setVisibility(View.VISIBLE);
                            String photoURL = dataSnapshot.child("photoURL").getValue().toString();
                            url_tv.setText(photoURL);
                            url_tv.setVisibility(View.VISIBLE);
                        }
                        else{
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

    }
}
