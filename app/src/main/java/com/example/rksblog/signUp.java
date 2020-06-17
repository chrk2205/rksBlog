package com.example.rksblog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class signUp extends AppCompatActivity {


    private int GET_IMAGE=1;
    private Button button;

    private ImageButton img;
    private Uri ImageUri;
    private String name,email,password,pass1;

    private String downloadUrl;


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UploadTask uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();


        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_them();
                //if(password==pass1) {
                    Sign_Up_Fire();

            }


        });


    }

    private void Sign_Up_Fire() {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            into_Storage();
                        //Data To Firebase Analytics
                            FirebaseAnalytics mFirebaseAnalytics=FirebaseAnalytics.getInstance(getApplicationContext());
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.METHOD, "FirebaseSignUp");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
                        //USER signed up
                            Intent intent=new Intent(signUp.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(signUp.this,"Failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    private void Do_it_On_Database() {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("AU");


        Map<String,Object> map=new HashMap<>();
        map.put("Dp",downloadUrl);
        map.put("username",name);




        databaseReference.child(user.getUid()).setValue(map);
    }

    private void get_them() {
        TextView tname,temail,tpas,tpas2;
        tname=findViewById(R.id.editText);
        name=tname.getText().toString();
        temail=findViewById(R.id.editText2);
        email=temail.getText().toString();
        tpas=findViewById(R.id.editText3);
        password=tpas.getText().toString();
        tpas2=findViewById(R.id.editText4);
        pass1=tpas2.getText().toString();
    }

    private void image_chooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select Image"),GET_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_IMAGE){

            if(resultCode==RESULT_OK){
                if(data!=null){
                    ImageUri=data.getData();
                    Picasso.get().load(ImageUri).centerCrop().fit().into(img);
                }
            }
            else{
                //image_chooser();
            }
        }
    }

    private void into_Storage() {

         StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("AU");//.child(user.getUid());
         final StorageReference ref=storageReference.child(user.getUid()+".jpeg");

         uploadTask= ref.putFile(ImageUri);

        Task<Uri> url=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful())
                {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                    downloadUrl=task.getResult().toString();
                    Do_it_On_Database();
            }
        });

    }


}
