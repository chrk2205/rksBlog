package com.example.rksblog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class upload_Blog extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private FirebaseUser user;

    private String UserId;
    private String key;
    private Uri downloadurl;


    private int GET_IMAGE = 1;
    private Button post;
    private Uri ImageUri;
    private ImageView BlogImage;
    private UploadTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__blog);

        user=FirebaseAuth.getInstance().getCurrentUser();

        databaseReference=FirebaseDatabase.getInstance().getReference("AB");



        Select_Blog_image();


        BlogImage=findViewById(R.id.blogImage);
        BlogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select_Blog_image();
            }
        });


        post=findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post.setClickable(false);
                if(ImageUri!=null){
                    get_key();

                    getDownloadUri();

                }
                else{
                    Toast.makeText(upload_Blog.this, "Choose a Blog Image", Toast.LENGTH_LONG).show();
                }
          }
        });




        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));





    }
    private void get_key(){
        key=databaseReference.push().getKey();
    }

    private void upload_in_Database() {

        UserId=user.getUid();
        TextView p=findViewById(R.id.cap);


        blogGet b =new blogGet();
        b.setCaption(p.getText().toString());
        b.setBlogimage(downloadurl.toString());
        b.setUserid(UserId);
        databaseReference.child(key).setValue(b);
        post.setClickable(true);

        FirebaseAnalytics mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString("UserId",UserId);
        params.putInt(FirebaseAnalytics.Param.VALUE,5);
        mFirebaseAnalytics.logEvent("Upload_Blog", params);
        finish();
    }

    private void Select_Blog_image() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select Image"),GET_IMAGE);
    }

    private void getDownloadUri(){


        StorageReference storage=FirebaseStorage.getInstance().getReference().child("AB");
        final StorageReference ref=storage.child(key+".jpg");
        uploadTask=ref.putFile(ImageUri);


        Task<Uri> urlTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        downloadurl=task.getResult();
                        upload_in_Database();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        post.setClickable(true);
                        Toast.makeText(upload_Blog.this, "Not Posted", Toast.LENGTH_SHORT).show();
                    }
                });
   }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_IMAGE){

            if(resultCode==RESULT_OK){
                if(data!=null){
                    ImageUri=data.getData();
                    Picasso.get().
                            load(ImageUri)
                            .centerCrop()
                            .fit()
                            .into(BlogImage);

                }
            }
        }
    }
}

