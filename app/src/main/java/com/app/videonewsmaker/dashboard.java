package com.app.videonewsmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.videonewsmaker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class dashboard extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth userd;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    public LinearLayout buy;
    FirebaseUser user;
    String headertxt,bodytxt,footertxt;
    private String udid,novideo,product_id,name,email,friends,location,created,phoneno,profilepic,license,licensedate,currdeviceuid,joindate;
    public Button dltprodcode,dltvdo;
    public TextView productcode;
    videoallow vidcnt = com.app.videonewsmaker.videoallow.getInstance();
    LinearLayout video,myprofile,shareapp,yourvideo;
    String video1txt, days1txt, video2txt, days2txt, video3txt, days3txt,price1txt,price2txt,price3txt;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    String DateToday;
    int datedifference;
    String maxvids;
    customization cust;
    Button learn;

    String daysleft;
    LinearLayout aboutus;
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


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        buy = findViewById(R.id.btn_buy);
        yourvideo=findViewById(R.id.openfolder);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        video=findViewById(R.id.btn_makevideo);
        productcode = findViewById(R.id.product_code);
        shareapp=findViewById(R.id.shareapp);
        dltprodcode = findViewById(R.id.btn_dltprodcode);
        dltvdo = findViewById(R.id.btn_remvdohist);
        myprofile=findViewById(R.id.btn_myprofile);
        aboutus=findViewById(R.id.btn_aboutus);
        learn=findViewById(R.id.learnapp);
        buy.setOnClickListener(this);
        video.setOnClickListener(this);
        dltprodcode.setOnClickListener(this);
        dltvdo.setOnClickListener(this);
        myprofile.setOnClickListener(this);
        updateData();
        try {
            license();
            getinformation();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        vidcnt = com.app.videonewsmaker.videoallow.getInstance();
        if(vidcnt.getText()==1){
            Intent intent=new Intent(dashboard.this,MainActivity.class);
            startActivity(intent);
        }
        yourvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("SdCardPath") File file=new File("/sdcard/Dashboard");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "*/*");
                startActivity(Intent.createChooser(intent, "Open with a file Manager"));
//                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
//                {
//                    startActivity(intent);
//                }
//                else
//                {
//                    intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    Uri uri = Uri.parse("/storage/emulated/0/Movies");
//                    intent.setDataAndType(uri, "text/csv");
//                    startActivity(Intent.createChooser(intent, "Open folder"));
//                }
            }
        });
        currdeviceuid=Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dashboard.this,aboutus.class);
                startActivity(intent);
            }
        });
        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Download VIDEO NEWS MAKER NOW AND CREATE NEWS VIDEOS EASILY!");
                String app_url = "Download VIDEO NEWS MAKER NOW AND CREATE NEWS VIDEOS EASILY! https://play.google.com/store/apps/details?id=com.fragron.bobooperator";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this,learnapp.class));
            }
        });
    }
    boolean verifyprodkey(String udid,String uid,String currid) throws NoSuchAlgorithmException {
        if(currid.equals(getproductid(udid,uid))){return true;}
        else{return false;}
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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(dashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });
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
                        joindate=profileModel.getlicensedate();

                        try {
                            if (product_id == null) {
                                productcode.setText("PRODUCT NOT BOUGHT!");
                            } else if (verifyprodkey(udid, user.getUid(),product_id)) {
                                buy.setVisibility(View.GONE);
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
                String ujoindate = joindate;
                String video1= video1txt;
                String days1= days1txt;
                String video2= video2txt;
                String days2= days2txt;
                String video3= video3txt;
                String days3=days3txt;
                String price1=price1txt;
                String price2=price2txt;
                String price3=price3txt;
                Intent intent=new Intent(dashboard.this,buy.class);
                intent.putExtra("name",username);
                intent.putExtra("phno",uphoneno);
                intent.putExtra("email",uemail);
                intent.putExtra("videos",uvideos);
                intent.putExtra("location",ulocation);
                intent.putExtra("friends",ufriends);
                intent.putExtra("created",ucreated);
                intent.putExtra("udid",uudid);
                intent.putExtra("product_id",uproduct_id);
                intent.putExtra("profilepic",uprofilepic);
                intent.putExtra("license",ulicense);
                intent.putExtra("licensedate",ulicensedate);
                intent.putExtra("joindate",ujoindate);
                intent.putExtra("video1",video1);
                intent.putExtra("days1",days1);
                intent.putExtra("video2",video2);
                intent.putExtra("days2",days2);
                intent.putExtra("video3",video3);
                intent.putExtra("days3",days3);
                intent.putExtra("price1",price1);
                intent.putExtra("price2",price2);
                intent.putExtra("price3",price3);
                startActivity(intent);

                break;
            case R.id.btn_makevideo:
                try{
                    makevid();

                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.btn_dltprodcode:
                deleteprodcode();
                break;
            case R.id.btn_remvdohist:
                deletevdo();
                break;
            case R.id.btn_myprofile:
                myprofilefn();
                break;
        }
    }

    private void myprofilefn(){
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
        String ujoindate = joindate;
        String video1= video1txt;
        String days1= days1txt;
        String video2= video2txt;
        String days2= days2txt;
        String video3= video3txt;
        String days3=days3txt;
        String price1=price1txt;
        String price2=price2txt;
        String price3=price3txt;
        Intent intent=new Intent(dashboard.this,profile.class);
        intent.putExtra("name",username);
        intent.putExtra("phno",uphoneno);
        intent.putExtra("email",uemail);
        intent.putExtra("videos",uvideos);
        intent.putExtra("location",ulocation);
        intent.putExtra("license",ulicense);
        intent.putExtra("licensedate",ulicensedate);
        intent.putExtra("joindate",ujoindate);
        intent.putExtra("video1",video1);
        intent.putExtra("days1",days1);
        intent.putExtra("video2",video2);
        intent.putExtra("days2",days2);
        intent.putExtra("video3",video3);
        intent.putExtra("days3",days3);
        intent.putExtra("price1",price1);
        intent.putExtra("price2",price2);
        intent.putExtra("price3",price3);
        startActivity(intent);
    }
    private void deletevdo() {
        String username = name;
        String uphoneno = phoneno;
        String uemail = email;
        String ufriends = friends;
        String ulocation = location;
        String ucreated = created;
        String uudid = udid;
        String uproduct_id = product_id;
        String uvideos = novideo;
        String ujoindate = joindate;
        String uprofilepic = profilepic;
        String ulicense = license;
        String ulicensedate = licensedate;
        if(novideo!="0"){
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation,uprofilepic, ucreated, uudid, uproduct_id, "0",ulicense,ulicensedate,ujoindate);
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(dashboard.this, "VIDEO HISTORY DELETED!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(dashboard.this, "CREATE SOME VIDEOS TO DELETE THE VIDEO HISTORY!", Toast.LENGTH_SHORT).show();

        }
    }

    private void deleteprodcode() {
        userd = FirebaseAuth.getInstance();
        user = userd.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
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
        String ulicense = license;
        String ulicensedate = licensedate;
        if(product_id!=null){
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation,uprofilepic, ucreated, uudid, null, "0",null,null,ujoindate);
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(dashboard.this, "LICENSE EXPIRED!", Toast.LENGTH_SHORT).show();
            buy.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(dashboard.this, "NO PRODUCT CODE FOUND!", Toast.LENGTH_SHORT).show();

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
        String ujoindate = joindate;
        String ulicense=license;
        String ulicensedate=licensedate;
        if(product_id!=null) {
            if (license.equals("1")) {
                maxvids = video1txt;
                daysleft= days1txt;
            } else if (license.equals("2")) {
                maxvids = video2txt;
                daysleft = days2txt;
            } else {
                maxvids = video3txt;
                daysleft = days3txt;
            }
            calendar = Calendar.getInstance();
            simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
            DateToday = simpledateformat.format(calendar.getTime());
            String currdate = DateToday;
            String datelicense = licensedate;
            try {
                datedifference = differenceindays(licensedate, currdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((Integer.parseInt(daysleft)-datedifference) == 0) {
                deleteprodcode();
            } else if (Integer.parseInt(novideo) > Integer.parseInt(maxvids)) {
                deleteprodcode();
            }
        }
        Toast.makeText(dashboard.this, "Welcome!", Toast.LENGTH_SHORT).show();
        vidcnt.setText(1);
        cust=com.app.videonewsmaker.customization.getInstance();
        cust.setBodytxt(bodytxt);
        cust.setFootertxt(footertxt);
        cust.setHeadertxt(headertxt);
        Intent intent=new Intent(dashboard.this,MainActivity.class);
        startActivity(intent);
    }

    private void buybtn() throws NoSuchAlgorithmException {
        String username = name;
        String uphoneno = phoneno;
        String uemail = email;
        String ufriends = friends;
        String ulocation = location;
        String ucreated = created;
        String ujoindate = joindate;
        String uudid = udid;
        String uproduct_id = product_id;
        String uvideos = novideo;
        String uprofilepic = profilepic;
        String ulicense = license;
        String ulicensedate = licensedate;
        if (uproduct_id == null ) {
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation,uprofilepic, ucreated, uudid, getproductid(uudid,user.getUid()), uvideos,ulicense,ulicensedate,ujoindate);
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(dashboard.this, "Congratulations! You have Bought the App!", Toast.LENGTH_SHORT).show();
            vidcnt.setText(1);
        }  else {
            Toast.makeText(dashboard.this, "You have already Bought the App!", Toast.LENGTH_SHORT).show();
            vidcnt.setText(0);
        }
    }
    private void getinformation() throws NoSuchAlgorithmException {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("information/" + "body");
        DatabaseReference databaseReference2 = firebaseDatabase.getReference("information/" + "footer");
        DatabaseReference databaseReference3 = firebaseDatabase.getReference("information/" + "heading");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    bodytxt=dataSnapshot.getValue().toString();
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
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    footertxt=dataSnapshot.getValue().toString();

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
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    headertxt=dataSnapshot.getValue().toString();

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


