package com.app.videonewsmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class profile extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth userd;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    TextView nametxt,emailtxt,phnotxt,locationtxt,videostxt,daystxt;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    String DateToday;
    int difference=0;
    LinearLayout btnbuy;
    private String udid,novideo,product_id,name,email,friends,location,created,phoneno,profilepic,license,licensedate,currdeviceuid;
    String video1txt, days1txt, video2txt, days2txt, video3txt, days3txt,price1txt,price2txt,price3txt;
    Bundle bundle;
    ImageView btnback;
    //ImageView btnback;
    int licensedays=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nametxt=findViewById(R.id.profname);
        emailtxt=findViewById(R.id.profemail);
        phnotxt=findViewById(R.id.profphoneno);
        locationtxt=findViewById(R.id.proflocation);
        videostxt=findViewById(R.id.profvideos);
        daystxt=findViewById(R.id.profdaysleft);
        btnbuy=findViewById(R.id.btn_buy_prof);
        btnback=findViewById(R.id.back_btn);
        bundle=getIntent().getExtras();

        updateData();
        try {
            license();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert bundle != null;
        nametxt.setText(bundle.getString("name"));
        emailtxt.setText(bundle.getString("email"));
        phnotxt.setText(bundle.getString("phno"));
        locationtxt.setText(bundle.getString("location"));
        videostxt.setText(bundle.getString("videos"));
        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd MM yyyy");
        DateToday = simpledateformat.format(calendar.getTime());
        String currdate=DateToday;
        String licensedatetxt=bundle.getString("licensedate");
        String daysleft;
        if(licensedatetxt!=null) {
        try {
            difference=differenceindays(licensedatetxt,currdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

            if (bundle.getString("license").equals("1")) {
                daysleft = String.valueOf(Integer.parseInt(bundle.getString("days1")) - difference);
            } else if (bundle.getString("license").equals("2")) {
                daysleft = String.valueOf(Integer.parseInt(bundle.getString("days2")) - difference);
            } else {
                daysleft = String.valueOf(Integer.parseInt(bundle.getString("days3")) - difference);
            }
            daystxt.setText(daysleft);
        }
        else{
            daystxt.setText("GET LICENSE");
        }

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(profile.this,dashboard.class);
                startActivity(intent);
            }
        });

        btnbuy.setOnClickListener(this);
        if(daystxt.getText()=="GET LICENSE"){
            btnbuy.setVisibility(View.VISIBLE);
        }
        else{btnbuy.setVisibility(View.GONE);}

    }
    int differenceindays(String before,String after) throws ParseException {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Date dateBefore = myFormat.parse(before);
        Date dateAfter = myFormat.parse(after);
        assert dateBefore != null;
        long difference = dateAfter.getTime() - dateBefore.getTime();
        float daysBetween = (difference / (1000*60*60*24));
        return (int)daysBetween;
    }
    void updateData(){
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
                        product_id = profileModel.getproduct_code();
                        license = profileModel.getlicense();
                        licensedate=profileModel.getlicensedate();


                    }

                    else {
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                }


            });



        }
    }
    private void license() throws NoSuchAlgorithmException {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("licence1/" + "days");
        DatabaseReference databaseReference2 = firebaseDatabase.getReference("licence1/" + "videos");
        DatabaseReference databaseReference3 = firebaseDatabase.getReference("licence2/" + "days");
        DatabaseReference databaseReference4 = firebaseDatabase.getReference("licence2/" + "videos");
        DatabaseReference databaseReference5 = firebaseDatabase.getReference("licence3/" + "days");
        DatabaseReference databaseReference6 = firebaseDatabase.getReference("licence3/" + "videos");
        DatabaseReference databaseReference7 = firebaseDatabase.getReference("licence1/" + "price");
        DatabaseReference databaseReference8 = firebaseDatabase.getReference("licence2/" + "price");
        DatabaseReference databaseReference9 = firebaseDatabase.getReference("licence3/" + "price");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    days1txt=dataSnapshot.getValue().toString();
                    System.out.println("HELLO: "+days1txt);
                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    video1txt=dataSnapshot.getValue().toString();

                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    days2txt=dataSnapshot.getValue().toString();

                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
        databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    video2txt=dataSnapshot.getValue().toString();

                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
        databaseReference5.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    days3txt=dataSnapshot.getValue().toString();

                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
        databaseReference6.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    video3txt=dataSnapshot.getValue().toString();

                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
        databaseReference7.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    price1txt=dataSnapshot.getValue().toString();

                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
        databaseReference8.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    price2txt=dataSnapshot.getValue().toString();

                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
        databaseReference9.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    price3txt=dataSnapshot.getValue().toString();

                }

                else {
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy_prof:

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
                String ulicense = license;
                String ulicensedate = licensedate;
                String video1 = video1txt;
                String days1 = days1txt;
                String video2 = video2txt;
                String days2 = days2txt;
                String video3 = video3txt;
                String days3 = days3txt;
                String price1 = price1txt;
                String price2 = price2txt;
                String price3 = price3txt;
                Intent intent = new Intent(profile.this, buy.class);
                intent.putExtra("name", username);
                intent.putExtra("phno", uphoneno);
                intent.putExtra("email", uemail);
                intent.putExtra("videos", uvideos);
                intent.putExtra("location", ulocation);
                intent.putExtra("friends", ufriends);
                intent.putExtra("created", ucreated);
                intent.putExtra("udid", uudid);
                intent.putExtra("product_id", uproduct_id);
                intent.putExtra("profilepic", uprofilepic);
                intent.putExtra("license", ulicense);
                intent.putExtra("licensedate", ulicensedate);
                intent.putExtra("video1", video1);
                intent.putExtra("days1", days1);
                intent.putExtra("video2", video2);
                intent.putExtra("days2", days2);
                intent.putExtra("video3", video3);
                intent.putExtra("days3", days3);
                intent.putExtra("price1", price1);
                intent.putExtra("price2", price2);
                intent.putExtra("price3", price3);
                startActivity(intent);

                break;
        }
    }
}
