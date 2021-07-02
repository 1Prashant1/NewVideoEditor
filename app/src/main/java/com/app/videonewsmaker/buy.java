package com.app.videonewsmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class buy extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth userd;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    private String udid, novideo, product_id, name, email, friends, location, created, phoneno, profilepic, license,licensedate,joindate;
    String video1txt, days1txt, video2txt, days2txt, video3txt, days3txt,buy1txt,buy2txt,buy3txt,price1txt,price2txt,price3txt;
    TextView video1, days1, video2, days2, video3, days3;
    Button buy1,buy2,buy3;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    String Date;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Bundle b=getIntent().getExtras();
        assert b != null;
        name=b.getString("name");
        phoneno=b.getString("phno");
        email=b.getString("email");
        friends=b.getString("friends");
        location=b.getString("location");
        created=b.getString("created");
        udid=b.getString("udid");
        product_id=b.getString("product_id");
        novideo=b.getString("videos");
        license=b.getString("license");
        licensedate=b.getString("licensedate");
        video1txt=b.getString("video1");
        days1txt=b.getString("days1");
        video2txt=b.getString("video2");
        days2txt=b.getString("days2");
        video3txt=b.getString("video3");
        days3txt=b.getString("days3");
        price1txt=b.getString("price1");
        price2txt=b.getString("price2");
        price3txt=b.getString("price3");
        buy1txt="₹ "+price1txt;
        buy2txt="₹ "+price2txt;
        buy3txt="₹ "+price3txt;

        video1 = findViewById(R.id.videos1);
        days1 = findViewById(R.id.days1);
        video2 = findViewById(R.id.videos2);
        days2 = findViewById(R.id.days2);
        video3 = findViewById(R.id.videos3);
        days3 = findViewById(R.id.days3);
        buy1=findViewById(R.id.buy1);
        buy2=findViewById(R.id.buy2);
        buy3=findViewById(R.id.buy3);
        video1.setText(video1txt);
        video2.setText(video2txt);
        video3.setText(video3txt);
        days1.setText(days1txt);
        days2.setText(days2txt);
        days3.setText(days3txt);
        buy1.setText(buy1txt);
        buy2.setText(buy2txt);
        buy3.setText(buy3txt);
        buy1.setOnClickListener(this);
        buy2.setOnClickListener(this);
        buy3.setOnClickListener(this);

    }


    void updateData() {
        userd = FirebaseAuth.getInstance();
        user = userd.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (userd != null) {
            assert user != null;
            databaseReference = firebaseDatabase.getReference("users/" + Objects.requireNonNull(user.getUid()));
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

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
                        joindate = profileModel.getJoindate();
                        product_id = profileModel.getproduct_code();
                        license = profileModel.getlicense();
                        licensedate = profileModel.getlicensedate();
                        joindate = profileModel.getJoindate();
                        try {
                            if (product_id == null) {

                            } else{
                                if (verifyprodkey(udid, user.getUid(), product_id)) {
                                    //Intent intent = new Intent(buy.this, dashboard.class);
                                   // startActivity(intent);
                                }
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(buy.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                }


            });


        }
    }

    boolean verifyprodkey(String udid, String uid, String currid) throws NoSuchAlgorithmException {
        if (currid.equals(getproductid(udid, uid))) {
            return true;
        } else {
            return false;
        }
    }

    private String getproductid(String udid, String uniqueid) throws NoSuchAlgorithmException {
        String stringToEncrypt = udid;
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(stringToEncrypt.getBytes());
        messageDigest.update(uniqueid.getBytes());
        byte[] digest = messageDigest.digest();
        System.out.println(digest);

        //Converting the byte array in to HexString format
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < digest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & digest[i]));
        }
        return (hexString.toString());

    }

    @SuppressLint("SimpleDateFormat")
    private void buybtn1() throws NoSuchAlgorithmException {
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd MM yyyy");
        Date = simpledateformat.format(calendar.getTime());
        String username = name;
        String uphoneno = phoneno;
        String uemail = email;
        String ufriends = friends;
        String ulocation = location;
        String ucreated = created;
        String uudid = udid;
        String uproduct_id = product_id;
        String ujoindate = joindate;
        String uvideos = novideo;
        String uprofilepic = profilepic;
        String ulicense = "1";
        String ulicensedate = Date;
        userd = FirebaseAuth.getInstance();
        user = userd.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (uproduct_id == null) {
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation, uprofilepic, ucreated, uudid, getproductid(uudid, user.getUid()), uvideos, ulicense,ulicensedate,ujoindate);
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(buy.this, "Congratulations! You have Bought the App!", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(buy.this,dashboard.class);
            startActivity(intent);
        } else {
            Toast.makeText(buy.this, "You have already Bought the App!", Toast.LENGTH_SHORT).show();
        }
    }
    private void buybtn2() throws NoSuchAlgorithmException {
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
        Date = simpledateformat.format(calendar.getTime());
        String username = name;
        String uphoneno = phoneno;
        String uemail = email;
        String ufriends = friends;
        String ulocation = location;
        String ucreated = created;
        String uudid = udid;
        String ujoindate = joindate;
        String uproduct_id = product_id;
        String uvideos = novideo;
        String uprofilepic = profilepic;
        String ulicense = "2";
        String ulicensedate = Date;
        userd = FirebaseAuth.getInstance();
        user = userd.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (uproduct_id == null) {
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation, uprofilepic, ucreated, uudid, getproductid(uudid, user.getUid()), uvideos, ulicense,ulicensedate,ujoindate);
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(buy.this, "Congratulations! You have Bought the App!", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(buy.this,dashboard.class);
            startActivity(intent);
        } else {
            Toast.makeText(buy.this, "You have already Bought the App!", Toast.LENGTH_SHORT).show();
        }
    }
    private void buybtn3() throws NoSuchAlgorithmException {
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
        Date = simpledateformat.format(calendar.getTime());
        String username = name;
        String uphoneno = phoneno;
        String uemail = email;
        String ufriends = friends;
        String ulocation = location;
        String ucreated = created;
        String uudid = udid;
        String uproduct_id = product_id;
        String ujoindate = joindate;
        String uvideos = novideo;
        String uprofilepic = profilepic;
        String ulicense = "3";
        String ulicensedate = Date;
        userd = FirebaseAuth.getInstance();
        user = userd.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (uproduct_id == null) {
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation, uprofilepic, ucreated, uudid, getproductid(uudid, user.getUid()), uvideos, ulicense,ulicensedate,ujoindate);
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(buy.this, "Congratulations! You have Bought the App!", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(buy.this,dashboard.class);
            startActivity(intent);
        } else {
            Toast.makeText(buy.this, "You have already Bought the App!", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buy1:

                int Money = Integer.parseInt(price1txt)*100;
                String Licensed = "1";
                Intent intent = new Intent(getApplicationContext(), PaymentScreen.class);
                intent.putExtra("name",name);
                intent.putExtra("phno",phoneno);
                intent.putExtra("email",email);
                intent.putExtra("videos",novideo);
                intent.putExtra("location",location);
                intent.putExtra("friends",friends);
                intent.putExtra("created",created);
                intent.putExtra("udid",udid);
                intent.putExtra("product_id",product_id);
                intent.putExtra("profilepic",profilepic);
                intent.putExtra("license",license);
                intent.putExtra("licensedate",licensedate);
                intent.putExtra("joindate",joindate);
                intent.putExtra("money", Money);
                intent.putExtra("lic", Licensed);
                startActivity(intent);
                break;

            case R.id.buy2:
                int Money2 = Integer.parseInt(price2txt)*100;
                String Licensed2 = "2";
                Intent intent2 = new Intent(getApplicationContext(), PaymentScreen.class);
                intent2.putExtra("name",name);
                intent2.putExtra("phno",phoneno);
                intent2.putExtra("email",email);
                intent2.putExtra("videos",novideo);
                intent2.putExtra("location",location);
                intent2.putExtra("friends",friends);
                intent2.putExtra("created",created);
                intent2.putExtra("udid",udid);
                intent2.putExtra("product_id",product_id);
                intent2.putExtra("profilepic",profilepic);
                intent2.putExtra("license",license);
                intent2.putExtra("licensedate",licensedate);
                intent2.putExtra("joindate",joindate);
                intent2.putExtra("money", Money2);
                intent2.putExtra("lic", Licensed2);
                startActivity(intent2);

                break;

            case R.id.buy3:

                int Money3 = Integer.parseInt(price3txt)*100;
                String Licensed3 = "3";
                Intent intent3 = new Intent(getApplicationContext(), PaymentScreen.class);
                intent3.putExtra("name",name);
                intent3.putExtra("phno",phoneno);
                intent3.putExtra("email",email);
                intent3.putExtra("videos",novideo);
                intent3.putExtra("location",location);
                intent3.putExtra("friends",friends);
                intent3.putExtra("created",created);
                intent3.putExtra("udid",udid);
                intent3.putExtra("product_id",product_id);
                intent3.putExtra("profilepic",profilepic);
                intent3.putExtra("license",license);
                intent3.putExtra("licensedate",licensedate);
                intent3.putExtra("joindate",joindate);
                intent3.putExtra("money", Money3);
                intent3.putExtra("lic",  Licensed3);
                startActivity(intent3);
                break;
        }

    }
}
