package com.example.siddhant.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    String phone_string;
    EditText phone_no;
    MaterialButton bt_send_otp,bt_camera;
    Compressor compressedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone_no = findViewById(R.id.mob_no);
        bt_send_otp = findViewById(R.id.bt_vw_otp);
        bt_camera = findViewById(R.id.camera_act);

        loadImageFromStorage(Environment.getExternalStorageDirectory().getPath());
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Camera.class);
                startActivity(intent);
            }
        });


        bt_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_string = phone_no.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(),Otp.class);
                intent.putExtra("mobile",phone_string);
                startActivity(intent);
            }
        });
    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "photo.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = findViewById(R.id.imgview);
            img.setImageBitmap(b);
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }

    }
}
