package com.example.rksblog;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class unandimg {
    DatabaseReference databaseReference;
    String Dp;
    String username;

    public String getDp() {
        return Dp;
    }

    public void setDp(String dp) {
        Dp = dp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public unandimg() {
    }

    public unandimg(String userid){



    }




}
