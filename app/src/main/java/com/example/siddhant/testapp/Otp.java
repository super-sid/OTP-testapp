package com.example.siddhant.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class Otp extends AppCompatActivity {

    private MaterialButton send_otp;
    CoordinatorLayout coordinatorLayout;
    private TextView user_phone_no, user_phone_2,sus_pic_url,tv1,tv2;
    private ProgressBar progressBar;
    private EditText otp_field;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String user_id;
    StorageReference storageRef;
    private int btnType = 0;
    String parent_key, pkey;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        user_phone_no = findViewById(R.id.phone_no);
        progressBar = findViewById(R.id.progressBar);
        otp_field = findViewById(R.id.otp_enter);
        send_otp = findViewById(R.id.s_otp);
        user_phone_2 = findViewById(R.id.up2);
        sus_pic_url = findViewById(R.id.picURL);
        tv1 = findViewById(R.id.purl);
        tv2 = findViewById(R.id.mobtv);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        Intent i = getIntent();
        final String phone_num = i.getStringExtra("mobile");

        user_phone_no.setText(phone_num);
        mDatabase = FirebaseDatabase.getInstance().getReference("suspicious_users").push();
        pkey = mDatabase.getKey();
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnType == 0){
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone_num,
                        60,
                        TimeUnit.SECONDS,
                        Otp.this,
                        mCallbacks
                );
                }
                else {
                    String ver_code = otp_field.getText().toString().trim();
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId,ver_code);
                    signInWithPhoneAuthCredential(phoneAuthCredential,phone_num);
                }
                mDatabase = FirebaseDatabase.getInstance().getReference("suspicious_users").child(pkey);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Phone").exists()){
                            String phone_sus = dataSnapshot.child("Phone").getValue().toString();
                            user_phone_2.setText(phone_sus);
                            user_phone_2.setVisibility(View.VISIBLE);
                            //tv2.setVisibility(View.VISIBLE);
                            String pic_URL = dataSnapshot.child("photoURL").getValue().toString();
                            sus_pic_url.setText(pic_URL);
                            sus_pic_url.setVisibility(View.VISIBLE);
                            //tv1.setVisibility(View.VISIBLE);
                        }else {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential,phone_num);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mDatabase = FirebaseDatabase.getInstance().getReference("suspicious_users").child(pkey);
                Toast.makeText(getApplicationContext(),"OTP not Verified",Toast.LENGTH_SHORT).show();
                HashMap<String,String> userMap = new HashMap<>();
                userMap.put("Phone",phone_num);
                mDatabase.setValue(userMap);
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                btnType = 1;

                send_otp.setText("Verify OTP");
            }

        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential,final String phone_nu) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            String id_user = user.getUid();
                            Toast.makeText(getApplicationContext(),id_user,Toast.LENGTH_SHORT).show();
                            mDatabase = FirebaseDatabase.getInstance().getReference("visitors").child(id_user);
                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("Phone",phone_nu);
                            String url = uploadFile(phone_nu);
                            userMap.put("photoURL",url);
                            mDatabase.setValue(userMap);
                            mDatabase.child("visit_count").setValue(1);
                            Intent intent = new Intent(getApplicationContext(),User_Activity.class);
                            intent.putExtra("mobile",phone_nu);
                            startActivity(intent);
                        } else {
                            mDatabase = FirebaseDatabase.getInstance().getReference("suspicious_users").child(pkey);
                            HashMap<String,String> userMap = new HashMap<>();
                            userMap.put("Phone",phone_nu);
                            String url = uploadFile(phone_nu);
                            userMap.put("photoURL",url);
                            mDatabase.setValue(userMap);
                            Snackbar.make(findViewById(android.R.id.content),"OTP not Verified",Snackbar.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private String uploadFile(String phone) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"photo.jpg"));
        final StorageReference riversRef = storageRef.child("images"+ phone + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Snackbar.make(findViewById(android.R.id.content),"Error Uploading Photo",Snackbar.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Snackbar.make(findViewById(android.R.id.content),"Photo Uploaded to API",Snackbar.LENGTH_LONG).show();
            }
        });
        String url = riversRef.getDownloadUrl().toString();
        return url;
    }
}
