package com.app.videoeditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class dashboard extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth userd;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    public Button buy;
    FirebaseUser user;
    private String udid,novideo,product_id,name,email,friends,location,created,phoneno,profilepic;
    public Button video;
    public TextView productcode;


   private String getproductid(String udid,String uniqueid) throws NoSuchAlgorithmException {
        String stringToEncrypt = udid;
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
        return (hexString.toString());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        buy = findViewById(R.id.btn_buy);
        video=findViewById(R.id.btn_makevideo);
        productcode = findViewById(R.id.product_code);
        buy.setOnClickListener(this);
        video.setOnClickListener(this);
        updateData();




    }
  boolean verifyprodkey(String udid,String uid,String currid) throws NoSuchAlgorithmException {
       if(currid.equals(getproductid(udid,uid))){return true;}
       else{return false;}
  }
    void updateData(){
        userd = FirebaseAuth.getInstance();
        user = userd.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (userd != null) {
            assert user != null;
            databaseReference = firebaseDatabase.getReference("users/" + Objects.requireNonNull(user.getUid()));
            databaseReference.addValueEventListener(new ValueEventListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        CreatProfileModel profileModel = dataSnapshot.getValue(CreatProfileModel.class);
                        name = profileModel.getUserName();
                        created = profileModel.getCreated();
                        friends = profileModel.getFriends();
                        location = profileModel.getLocation();
                        phoneno = profileModel.getUserPhoneno();
                        email = profileModel.getUserEmail();
                        profilepic = null;
                        udid = profileModel.getudid();
                        novideo = profileModel.getnovideos();
                        product_id = profileModel.getproduct_code();
                        try {
                            if (product_id == null) {
                                productcode.setText("PRODUCT NOT BOUGHT!");
                            } else if (verifyprodkey(udid, user.getUid(),product_id)) {
                                productcode.setText("PRODUCT CODE: " + product_id);
                            } else {
                                productcode.setText("DEVICE CHANGED PRODUCT KEY NOT VALID!");
                            }
                        }
                        catch(Exception e){

                        }
                    }

                        else {
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                }


            });



        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_buy:
                try {
                    buybtn();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_makevideo:
                try {
                    makevid();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void makevid() throws NoSuchAlgorithmException {
        String username = name;
        String uphoneno = phoneno;
        String uemail = email;
        String ufriends = friends;
        String ulocation = location;
        String ucreated = created;
        String uudid = udid;
        String uproduct_id = product_id;
        String uvideos = novideo;
        String uprofilepic = profilepic;

        if (uproduct_id == null && Integer.parseInt(uvideos) > 2 ) {
            Toast.makeText(dashboard.this, "You have made all your videos Buy to make more", Toast.LENGTH_SHORT).show();
        } else if (uproduct_id == null && Integer.parseInt(uvideos) <= 2) {
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation,uprofilepic, ucreated, uudid, uproduct_id, String.valueOf(Integer.parseInt(uvideos) + 1));
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(dashboard.this, "You have made " + String.valueOf(Integer.parseInt(uvideos)+1) + " video", Toast.LENGTH_SHORT).show();

        } else if(verifyprodkey(udid, user.getUid(),product_id)){
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation,uprofilepic, ucreated, uudid, uproduct_id, String.valueOf(Integer.parseInt(uvideos) + 1));
           DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(dashboard.this, "You have made " + String.valueOf(Integer.parseInt(uvideos)+1) + " video", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(dashboard.this, "DEVICE CHANGED CANNOT MAKE VIDEO!", Toast.LENGTH_SHORT).show();
        }
    }

    private void buybtn() throws NoSuchAlgorithmException {
        String username = name;
        String uphoneno = phoneno;
        String uemail = email;
        String ufriends = friends;
        String ulocation = location;
        String ucreated = created;
        String uudid = udid;
        String uproduct_id = product_id;
        String uvideos = novideo;
        String uprofilepic = profilepic;

        if (uproduct_id == null) {
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation,uprofilepic, ucreated, uudid, getproductid(uudid,user.getUid()), uvideos);
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(dashboard.this, "Congratulations! You have Bought the App!", Toast.LENGTH_SHORT).show();
        }  else {
            Toast.makeText(dashboard.this, "You have already Bought the App!", Toast.LENGTH_SHORT).show();
        }
    }

    }


