package com.app.videoeditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.videoeditor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class dashboard extends AppCompatActivity {
    private FirebaseAuth userd;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;


   private String getproductid(String uid,String uniqueid) throws NoSuchAlgorithmException {
        String stringToEncrypt = uid;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(stringToEncrypt.getBytes());
        messageDigest.update(uniqueid.getBytes());
        byte[] digest = messageDigest.digest();
        System.out.println(digest);

        //Converting the byte array in to HexString format
        StringBuffer hexString = new StringBuffer();

        for (int i = 0;i<digest.length;i++) {
            hexString.append(Integer.toHexString(0xFF & digest[i]));
        }
        return ("Hex format : " + hexString.toString());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        userd = FirebaseAuth.getInstance();
        FirebaseUser user = userd.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (userd != null) {
            assert user != null;
            databaseReference = firebaseDatabase.getReference("users/" + Objects.requireNonNull(user.getUid()));



        }
    }
}
