package com.example.rksblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            //Opening FirstPage i.e., View all Blog
            User_Verified();

        } else {

            SignInPage();

        }
            }
        },1500);




    }



    private void SignInPage() {
        Intent intent=new Intent(MainActivity.this,Login.class);
        startActivity(intent);
        finish();
    }

    private void User_Verified() {
        Intent intent1=new Intent(MainActivity.this,FirstActivity.class);
        startActivity(intent1);
        finish();
    }





}
