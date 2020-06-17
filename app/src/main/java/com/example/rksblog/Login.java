package com.example.rksblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class Login extends AppCompatActivity {

    Button LoginIn;
    TextView signup;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup=findViewById(R.id.Signup);
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);

        LoginIn=findViewById(R.id.login);
        LoginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginIn.setClickable(false);
                final TextView m=findViewById(R.id.mail);
                final TextView p=findViewById(R.id.pass);
                String email=m.getText().toString();
                String password=p.getText().toString();

                if(password.trim().equals("")||email.trim().equals("")){

                    m.setTextColor(Color.rgb(255, 0, 0));
                    p.setTextColor(Color.rgb(255, 0, 0));
                    p.setText("");


                }else {
                    mAuth = FirebaseAuth.getInstance();

                    Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        currentUser = mAuth.getCurrentUser();
                                        Intent intent1 = new Intent(Login.this, FirstActivity.class);
                                        startActivity(intent1);
                                        String method="FirebaseAuthentication";
                                        Bundle bundle = new Bundle();
                                        bundle.putString(FirebaseAnalytics.Param.METHOD, method);
                                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                                        finish();
                                    } else {
                                        //if email and password are wrong
                                        m.setTextColor(Color.rgb(255, 0, 0));
                                        p.setTextColor(Color.rgb(255, 0, 0));
                                        p.setText("");
                                    }
                                }
                            });
                }


                LoginIn.setClickable(true);

            }


        });




    }


    public void click(View view) {

        Intent intent3=new Intent(this,signUp.class);
        startActivity(intent3);


    }
}
