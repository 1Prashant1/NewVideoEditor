package com.app.videonewsmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class activity_step2 extends AppCompatActivity implements View.OnClickListener {
    ImageView back,reporterphoto,logoimage;
    Button next,btnprev,btnnext;
    Bundle bundle,bundle2;
    Uri fetchvideo,fetchpromo,fetchlogouri,fetchreporteruri;
    EditText br1,br2;
    RelativeLayout brimg1,city_name;
    RelativeLayout mobile1,mobile2;
    TextView brt1,nbrt1,nbrt2,brt2,styleid,reporter_name,brt11,brt22,city_text;
    TextView brt111,brt222,city_text2,reporter_name2;
    RelativeLayout brimg11,main_template2,reptemp2,city_name2;
    int designbatch=1;
    ImageView logoimage2,reporterphoto2;
    RelativeLayout bitmaptext2;
    HorizontalScrollView bitmaptext,bitmaptext1;
    LinearLayout toplayout,news1layout;
    ImageView brimg,brimg2;
    String cityname,reportername;
    RelativeLayout main_template,reptemp;
    String temppath,videopath,logopath,reppath,promovideopath;
    private String udid,novideo,product_id,name,email,friends,location,created,phoneno,profilepic,license,licensedate,currdeviceuid;
    int brno=0,height,width,rotation;
    private FirebaseAuth userd;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    Switch br2switch,br2switch2,br2switch1;
    Boolean switchval=false;
    Boolean switchval1=false;
    private static final String TAG = "VIDEO EDITOR";


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);
        updateData();
        currdeviceuid= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        back=findViewById(R.id.back_btn);
        next=findViewById(R.id.next);
        btnprev=findViewById(R.id.btnprev);
        mobile1=findViewById(R.id.mobile1);
        mobile2=findViewById(R.id.mobile2);
        br2switch=findViewById(R.id.br2scroll);
        br2switch1=findViewById(R.id.br2scroll1);
        br2switch2=findViewById(R.id.br2scroll2);
        brimg2=findViewById(R.id.news_style2);
        btnnext=findViewById(R.id.btnnext);
//        ImageView imageView = findViewById(R.id.comingsoon);
//        Glide.with(this).load(R.drawable.comingsoon).into(imageView);
        br1=findViewById(R.id.brn1);
        br2=findViewById(R.id.brn2);
        bitmaptext=findViewById(R.id.bitmapnews2);
        bitmaptext2=findViewById(R.id.bitmap2news);
        bitmaptext1=findViewById(R.id.bitmapnews);
        brt1=findViewById(R.id.news1_text);
        nbrt1=findViewById(R.id.news1_text2);
        brt11=findViewById(R.id.news1text);
        toplayout=findViewById(R.id.toplayout);
        news1layout=findViewById(R.id.news1layout);
        brt111=findViewById(R.id.news1text2);
        brt222=findViewById(R.id.news2text2);
        city_text2=findViewById(R.id.city_txt2);
        reporter_name2=findViewById(R.id.reporter_name2);
        main_template2=findViewById(R.id.main_template2);
        reptemp2=findViewById(R.id.reptemp2);
        reporterphoto2=findViewById(R.id.reporterphoto2);
        logoimage2=findViewById(R.id.logoimage2);
        brimg11=findViewById(R.id.newsstyle2);
        city_name2=findViewById(R.id.city_name2);
        brt2=findViewById(R.id.news2_text);
        nbrt2=findViewById(R.id.news2_text2);
        city_text=findViewById(R.id.city_txt);
        brt22=findViewById(R.id.news2text);
        styleid=findViewById(R.id.styleid);
        brimg=findViewById(R.id.news_style);
        brimg1=findViewById(R.id.newsstyle);
        reporterphoto=findViewById(R.id.reporterphoto);
        reporter_name=findViewById(R.id.reporter_name);
        main_template=findViewById(R.id.main_template);
        reptemp=findViewById(R.id.reptemp);
        city_name=findViewById(R.id.city_name);
        logoimage=findViewById(R.id.logoimage);
        brimg.setBackgroundResource(R.mipmap.breaking1);
        brimg1.setBackgroundResource(R.mipmap.breaking1);
        bundle = getIntent().getExtras();
        br2switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchval=true;
                }
                else{
                    switchval=false;

                }
            }
        });
        br2switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchval1=true;

                }
                else{
                    switchval1=false;

                }
            }
        });
        fetchvideo= (Uri) bundle.get("video");
        System.out.println("VIDEO PATH: "+fetchvideo);
        fetchpromo= (Uri) bundle.get("promo");
        fetchlogouri = (Uri) bundle.get("logo");
        videopath=bundle.getString("videopath");
        logopath=(String) bundle.get("logopath");
        reppath=(String) bundle.get("reppath");
        fetchreporteruri = (Uri) bundle.get("reporterphoto");
        cityname = bundle.getString("city_name");
        System.out.println("CITY NAME: "+cityname);
        reportername= bundle.getString("reporter_name");
        System.out.println("REP NAME: "+reportername);
        city_text.setText(cityname);
        city_text2.setText(cityname);
        reporter_name.setText(reportername);
        reporter_name2.setText(reportername);
        reporterphoto.setImageURI(fetchreporteruri);
        reporterphoto2.setImageURI(fetchreporteruri);
        promovideopath=(String) bundle.get("promopath");
        logoimage.setImageURI(fetchlogouri);
        logoimage2.setImageURI(fetchlogouri);
        String resolution="";
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videopath);
        width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        String metaRotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
        if(rotation==90 || rotation==270){
            int temp=width;
        width=height;
        height=temp;
      }
       System.out.println("VIDEO HEIGHT: "+height);
        System.out.println("VIDEO WIDTH: "+width);
        if(height>2000 || width>2000){
            Intent intent=new Intent(activity_step2.this,dashboard.class);
            startActivity(intent);
            Toast.makeText(activity_step2.this,"PLEASE CHOOSE VIDEOS UPTO 1080p RESOLUTION!", Toast.LENGTH_SHORT).show();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity_step2.this,MainActivity.class);
                startActivity(intent);
            }
        });
       //resize(height,width);
        textresize(height,width);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(videopath);
                width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                String metaRotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
                rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
                if(rotation==90 || rotation==270){
                    int temp=width;
                    width=height;
                    height=temp;
                }
                textresize(height,width);
                if(switchval){
                    saveToImage2(bitmaptext1);
                    brt11.setText("");
                }
                if(switchval1){
                    saveToImage(bitmaptext);
                    brt22.setText("");
                    brt222.setText("");
                }

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
                String ulicense=license;
                String ulicensedate=licensedate;
                try {
                    if (uproduct_id == null && Integer.parseInt(uvideos) >= 5) {
                        Toast.makeText(activity_step2.this, "You have made all your videos Buy to make more", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(activity_step2.this,dashboard.class);
                        startActivity(intent);
                    } else if (uproduct_id == null && Integer.parseInt(uvideos) <= 5) {
                        Bitmap bitmap = getBitmapFromView(main_template);
                        if(designbatch==1) {
                            bitmap = getBitmapFromView(main_template);
                        }
                        else if(designbatch==2){
                            bitmap = getBitmapFromView(main_template2);

                        }
                        File file = new File("/sdcard/" + "cache.png");
                        try {
                            if (!file.exists()) {
                                System.out.println("HELLO: "+"FILE SAVED!!!!");
                                file.createNewFile();
                            }
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                            ostream.close();
                            temppath=file.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                        }
                        Intent intent=new Intent(activity_step2.this,activity_step3.class);
                        intent.putExtra("video",fetchvideo);
                        intent.putExtra("promo",fetchpromo);
                        intent.putExtra("city_name",cityname);
                        intent.putExtra("reporter_name",reportername);
                        intent.putExtra("logo",fetchlogouri);
                        intent.putExtra("vidheight",height);
                        intent.putExtra("vidwidth",width);
                        intent.putExtra("vidrotation",rotation);
                        intent.putExtra("reporterphoto",fetchreporteruri);
                        intent.putExtra("videopath",videopath);
                        intent.putExtra("promopath",promovideopath);
                        intent.putExtra("designbatch",designbatch);
                        intent.putExtra("breakingnews1",br1.getText().toString());
                        intent.putExtra("breakingnews2",br2.getText().toString());
                        intent.putExtra("brstyle", brno);
                        intent.putExtra("temppath",temppath);
                        intent.putExtra("br2switchval",switchval);
                        intent.putExtra("br2switchval1",switchval1);
                        intent.putExtra("mode","process");
                        startActivity(intent);
                    } else if (verifyprodkey(currdeviceuid, user.getUid(), product_id)) {
                        Bitmap bitmap = getBitmapFromView(main_template);
                        if(designbatch==1) {
                            bitmap = getBitmapFromView(main_template);

                        }
                        else if(designbatch==2){
                            bitmap = getBitmapFromView(main_template2);

                        }
                        File file = new File("/sdcard/" + "cache.png");
                        try {
                            if (!file.exists()) {
                                System.out.println("HELLO: "+"FILE SAVED!!!!");
                                file.createNewFile();
                            }
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                            ostream.close();
                            temppath=file.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                        }
                        Intent intent=new Intent(activity_step2.this,activity_step3.class);
                        intent.putExtra("video",fetchvideo);
                        intent.putExtra("promo",fetchpromo);
                        intent.putExtra("city_name",cityname);
                        intent.putExtra("reporter_name",reportername);
                        intent.putExtra("logo",fetchlogouri);
                        intent.putExtra("vidheight",height);
                        intent.putExtra("vidwidth",width);
                        intent.putExtra("vidrotation",rotation);
                        intent.putExtra("reporterphoto",fetchreporteruri);
                        intent.putExtra("videopath",videopath);
                        intent.putExtra("promopath",promovideopath);
                        intent.putExtra("designbatch",designbatch);
                        intent.putExtra("breakingnews1",br1.getText().toString());
                        intent.putExtra("breakingnews2",br2.getText().toString());
                        intent.putExtra("brstyle", brno);
                        intent.putExtra("temppath",temppath);
                        intent.putExtra("br2switchval",switchval);
                        intent.putExtra("br2switchval1",switchval1);
                        intent.putExtra("mode","process");
                        startActivity(intent);

                    } else {
                        Toast.makeText(activity_step2.this, "DEVICE CHANGED CANNOT MAKE VIDEO!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(activity_step2.this,dashboard.class);
                        startActivity(intent);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        br1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                brt1.setText(null);
                brt11.setText(null);
                brt111.setText(null);
                nbrt1.setText(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                brt1.setText(s);
                brt11.setText(s);
                brt111.setText(s);
                nbrt1.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                brt1.setText(s);
                brt11.setText(s);
                brt111.setText(s);
                nbrt1.setText(s);
            }
        });
        br2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                brt2.setText(null);
                brt22.setText(null);
                brt222.setText(null);
                nbrt2.setText(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                brt2.setText(s);
                brt22.setText(s);
                brt222.setText(s);
                nbrt2.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                brt2.setText(s);
                brt22.setText(s);
                brt222.setText(s);
                nbrt2.setText(s);

            }
        });
        btnprev.setOnClickListener(this);
        btnnext.setOnClickListener(this);


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
                    Toast.makeText(activity_step2.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    boolean verifyprodkey(String udid,String uid,String currid) throws NoSuchAlgorithmException {
        if(currid.equals(getproductid(udid,uid))){return true;}
        else{return false;}
    }
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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnprev:
                if(brno>1 && brno<23){
                    brno=brno-1;
                    styleid.setText("Style "+ brno);
                    setbrnviewer();
                }
                break;
            case R.id.btnnext:
                if(brno>=0 && brno<22){
                    brno=brno+1;
                    styleid.setText("Style "+ brno);
                    setbrnviewer();
                }
                break;
        }

    }
    private void saveGIF(int x)
    {
        try
        {
            File file = new File("/sdcard", "currgif" + ".gif");

            long startTime = System.currentTimeMillis();

            Log.d(TAG, "on do in background, url open connection");

            InputStream is = getResources().openRawResource(x);
            Log.d(TAG, "on do in background, url get input stream");
            BufferedInputStream bis = new BufferedInputStream(is);
            Log.d(TAG, "on do in background, create buffered input stream");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Log.d(TAG, "on do in background, create buffered array output stream");

            byte[] img = new byte[1024];

            int current = 0;

            Log.d(TAG, "on do in background, write byte to baos");
            while ((current = bis.read()) != -1) {
                baos.write(current);
            }


            Log.d(TAG, "on do in background, done write");

            Log.d(TAG, "on do in background, create fos");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());

            Log.d(TAG, "on do in background, write to fos");
            fos.flush();

            fos.close();
            is.close();
            Log.d(TAG, "on do in background, done write to fos");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("ResourceAsColor")
    void setbrnviewer(){
        if (brno == 0) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking1);
            brimg1.setBackgroundResource(R.mipmap.breaking1);
        } else if (brno == 1) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking1);
            brimg1.setBackgroundResource(R.mipmap.breaking1);
        } else if (brno == 2) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking2);
            brimg1.setBackgroundResource(R.mipmap.breaking2);
        } else if (brno == 3) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking3);
            brimg1.setBackgroundResource(R.mipmap.breaking3);
        } else if (brno == 4) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking4);
            brimg1.setBackgroundResource(R.mipmap.breaking4);
        } else if (brno == 5) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking5);
            brimg1.setBackgroundResource(R.mipmap.breaking5);
        } else if (brno == 6) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking6);
            brimg1.setBackgroundResource(R.mipmap.breaking6);
        } else if (brno == 7) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking7);
            brimg1.setBackgroundResource(R.mipmap.breaking7);
        } else if (brno == 8) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking8);
            brimg1.setBackgroundResource(R.mipmap.breaking8);
        } else if (brno == 9) {
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking9);
            brimg1.setBackgroundResource(R.mipmap.breaking9);
        } else if (brno == 10) {
            if(br2switch1.isChecked()){
                br2switch1.setChecked(true);
            }

            br2switch1.setClickable(true);
            designbatch=1;
            ImageView imageView = findViewById(R.id.news_style);
            ImageView imageView2 = findViewById(R.id.news_style2);
            mobile2.setVisibility(View.GONE);
            mobile1.setVisibility(View.VISIBLE);
            brimg.setBackgroundResource(R.mipmap.breaking10);
            brimg1.setBackgroundResource(R.mipmap.breaking10);
        }else if (brno == 11) {
            br2switch1.setChecked(false);
            br2switch1.setClickable(false);
            designbatch=2;
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            txtView2.setBackgroundResource(R.color.blue);
            news1layout.setBackgroundResource(R.color.blue);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(R.mipmap.breaking11);
            brimg11.setBackgroundResource(R.mipmap.breaking11);
        }else if (brno == 12) {
            designbatch=2;
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            txtView2.setBackgroundResource(R.color.light_green);
            news1layout.setBackgroundResource(R.color.light_green);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(R.mipmap.breaking12);
            brimg11.setBackgroundResource(R.mipmap.breaking12);
        }else if (brno == 13) {
            designbatch=2;
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            txtView2.setBackgroundResource(R.color.light_purple);
            news1layout.setBackgroundResource(R.color.light_purple);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(R.mipmap.breaking13);
            brimg11.setBackgroundResource(R.mipmap.breaking13);
        }else if (brno == 14) {
            designbatch=2;
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            txtView2.setBackgroundResource(R.color.text_light_gray);
            news1layout.setBackgroundResource(R.color.text_light_gray);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(R.mipmap.breaking14);
            brimg11.setBackgroundResource(R.mipmap.breaking14);
        }else if (brno == 15) {
            designbatch=2;
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            txtView2.setBackgroundResource(R.color.Red);
            news1layout.setBackgroundResource(R.color.Red);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(R.mipmap.breaking15);
            brimg11.setBackgroundResource(R.mipmap.breaking15);
        }else if (brno == 16) {
            designbatch=2;
            toplayout.setVisibility(View.VISIBLE);
            reptemp2.setVisibility(View.VISIBLE);
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            mobile2.setBackgroundResource(R.drawable.mobile2);
            txtView2.setBackgroundResource(R.color.orange_peel);
            txtView2.setVisibility(View.VISIBLE);
            news1layout.setBackgroundResource(R.color.orange_peel);
            city_name2.setVisibility(View.VISIBLE);
            mobile1.setVisibility(View.GONE);
            news1layout.setVisibility(View.VISIBLE);
            brimg2.setBackgroundResource(R.mipmap.breaking16);
            brimg11.setBackgroundResource(R.mipmap.breaking16);
        }
        else if (brno == 17) {
            designbatch=2;
            city_name2.setVisibility(View.INVISIBLE);
            reptemp2.setVisibility(View.GONE);
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            mobile2.setBackgroundResource(R.drawable.mobile3);
            txtView2.setBackgroundResource(R.color.orange_peel);
            news1layout.setVisibility(View.INVISIBLE);
            txtView2.setVisibility(View.GONE);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(R.mipmap.breaking17);
            brimg11.setBackgroundResource(R.mipmap.breaking17);
        }
        else if (brno == 18) {
            designbatch=2;
            TextView txtView2=findViewById(R.id.news1_text2);
            txtView2.setVisibility(View.GONE);
            mobile2.setVisibility(View.VISIBLE);
            mobile2.setBackgroundResource(R.drawable.mobile3);
            txtView2.setBackgroundResource(R.color.orange_peel);
            news1layout.setVisibility(View.INVISIBLE);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(R.mipmap.breaking18);
            brimg11.setBackgroundResource(R.mipmap.breaking18);
        }
        else if (brno == 19) {
            designbatch=2;
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            mobile2.setBackgroundResource(R.drawable.mobile3);
            RelativeLayout rl=findViewById(R.id.bottombar);
            rl.setVisibility(View.VISIBLE);
            txtView2.setBackgroundResource(R.color.orange_peel);
            news1layout.setVisibility(View.INVISIBLE);
            ImageView imageView2 = findViewById(R.id.news_style2);
            imageView2.setVisibility(View.VISIBLE);
            txtView2.setVisibility(View.GONE);
            news1layout.setBackgroundResource(R.color.orange_peel);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(View.VISIBLE);
            brimg11.setBackgroundResource(View.VISIBLE);
            brimg2.setBackgroundResource(R.mipmap.breaking19);
            brimg11.setBackgroundResource(R.mipmap.breaking19);
        }
        else if (brno == 20) {
            designbatch=2;
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            mobile2.setBackgroundResource(R.drawable.mobile3);
            TextView tv=findViewById(R.id.city_namemob2);
            tv.setVisibility(View.VISIBLE);
            txtView2.setBackgroundResource(R.color.orange_peel);
            news1layout.setVisibility(View.INVISIBLE);
            ImageView imageView2 = findViewById(R.id.news_style2);
            imageView2.setVisibility(View.VISIBLE);
            txtView2.setVisibility(View.GONE);
            news1layout.setBackgroundResource(R.color.orange_peel);
            mobile1.setVisibility(View.GONE);
            brimg2.setVisibility(View.VISIBLE);
            brimg11.setVisibility(View.VISIBLE);
            brimg2.setBackgroundResource(0);
            brimg11.setBackgroundResource(0);
        }
        else if (brno == 21) {
            designbatch=2;
            city_name2.setVisibility(View.GONE);
            reptemp2.setVisibility(View.GONE);
            RelativeLayout rl=findViewById(R.id.bottombar);
            rl.setVisibility(View.GONE);
            TextView tv=findViewById(R.id.city_namemob2);
            tv.setVisibility(View.GONE);
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            mobile2.setBackgroundResource(R.drawable.mobile3);
            txtView2.setBackgroundResource(R.color.orange_peel);
            news1layout.setBackgroundResource(R.color.orange_peel);
            news1layout.setVisibility(View.VISIBLE);
            txtView2.setVisibility(View.VISIBLE);
            mobile1.setVisibility(View.GONE);
            brimg2.setVisibility(View.GONE);
            brimg11.setVisibility(View.GONE);
        }
        else if (brno == 22) {
            designbatch=2;
            city_name2.setVisibility(View.GONE);
            reptemp2.setVisibility(View.GONE);
            RelativeLayout rl=findViewById(R.id.bottombar);
            rl.setVisibility(View.GONE);
            TextView tv=findViewById(R.id.city_namemob2);
            tv.setVisibility(View.GONE);
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            mobile2.setBackgroundResource(R.drawable.mobile3);
            txtView2.setBackgroundResource(R.color.red);
            news1layout.setBackgroundResource(R.color.red);
            news1layout.setVisibility(View.VISIBLE);
            txtView2.setVisibility(View.VISIBLE);
            mobile1.setVisibility(View.GONE);
            brimg2.setVisibility(View.GONE);
            brimg11.setVisibility(View.GONE);
        }
        else {
            designbatch=2;
            city_name2.setVisibility(View.INVISIBLE);
            reptemp2.setVisibility(View.GONE);
            TextView txtView2=findViewById(R.id.news1_text2);
            mobile2.setVisibility(View.VISIBLE);
            mobile2.setBackgroundResource(R.drawable.mobile3);
            txtView2.setBackgroundResource(R.color.light_green);
            news1layout.setVisibility(View.VISIBLE);
            txtView2.setVisibility(View.GONE);
            mobile1.setVisibility(View.GONE);
            brimg2.setBackgroundResource(View.GONE);
            brimg11.setBackgroundResource(View.GONE);
        }
    }
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    void resize(int height,int width){
        main_template.getLayoutParams().height=height;
        main_template.getLayoutParams().width=width;
        main_template.setBackgroundColor(0);
        reptemp.getLayoutParams().height=height/3;
        reptemp.getLayoutParams().width=height/4;
        reporterphoto.getLayoutParams().height=height/5;
        reporter_name.getLayoutParams().height=height/20;
        logoimage.getLayoutParams().height=height/5;
        logoimage.getLayoutParams().width=width/5;
        brimg1.getLayoutParams().height=height/4;
        city_name.getLayoutParams().height=height/20;
        city_name.getLayoutParams().width=width/4;

    }
    void textresize(int height,int width){
        reporter_name.getLayoutParams().width = reporterphoto.getLayoutParams().width;
        reporter_name2.getLayoutParams().width = reporterphoto2.getLayoutParams().width;
        if(height>=1080 && width>=1920 || height>=1920 && width>=1080) {
            if(brno==21 || brno==22) {
                brt11.setTextSize(27);
                brt22.setTextSize(25);
                city_text.setTextSize(14);
                reporter_name.setTextSize(14);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 5;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 8;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = width / 7;

                brt111.setTextSize(27);
                brt222.setTextSize(23);
                city_text2.setTextSize(14);
                reporter_name2.setTextSize(14);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 5;
                reporterphoto2.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 9;
                toplayout.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = width / 7;
            }
            else{
                brt11.setTextSize(25);
                brt22.setTextSize(23);
                city_text.setTextSize(14);
                reporter_name.setTextSize(14);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 5;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 9;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = width / 7;

                brt111.setTextSize(27);
                brt222.setTextSize(23);
                city_text2.setTextSize(14);
                reporter_name2.setTextSize(14);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 5;
                reporterphoto2.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 9;
                toplayout.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = width / 7;
            }
        }
        else if(height==352 && width==640) {
            if(brno==21 || brno==22) {
                brt11.setTextSize(11);
                brt22.setTextSize(9);
                city_text.setTextSize(5);
                reporter_name.setTextSize(5);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 6;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(11);
                brt222.setTextSize(9);
                city_text2.setTextSize(5);
                reporter_name2.setTextSize(5);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 6;
                reporterphoto2.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 18;
                toplayout.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }
            else{
                brt11.setTextSize(9);
                brt22.setTextSize(7);
                city_text.setTextSize(5);
                reporter_name.setTextSize(5);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 6;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(9);
                brt222.setTextSize(7);
                city_text2.setTextSize(5);
                reporter_name2.setTextSize(5);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 6;
                reporterphoto2.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 18;
                toplayout.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }
        }
        else if(height==480 && width==720) {
            if(brno==21 || brno==22) {
                brt11.setTextSize(14);
                brt22.setTextSize(12);
                city_text.setTextSize(6);
                reporter_name.setTextSize(6);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 6;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(14);
                brt222.setTextSize(12);
                city_text2.setTextSize(6);
                reporter_name2.setTextSize(6);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 6;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 6;
//                reporter_name2.getLayoutParams().height = height / 18;
                logoimage2.getLayoutParams().height = height / 6;
                logoimage2.getLayoutParams().width = width / 6;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }
            else{
                brt11.setTextSize(12);
                brt22.setTextSize(10);
                city_text.setTextSize(6);
                reporter_name.setTextSize(6);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 6;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(12);
                brt222.setTextSize(10);
                city_text2.setTextSize(6);
                reporter_name2.setTextSize(6);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 6;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 6;
//                reporter_name2.getLayoutParams().height = height / 18;
                logoimage2.getLayoutParams().height = height / 6;
                logoimage2.getLayoutParams().width = width / 6;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }

        }
        else if((height==720 && width==1280) || (height==1280 && width==720)){
            if(brno==21 || brno==22) {
                brt11.setTextSize(20);
                brt22.setTextSize(18);
                city_text.setTextSize(12);
                reporter_name.setTextSize(12);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 5;
                reporterphoto.getLayoutParams().height = height / 5;

//                reporter_name.getLayoutParams().height = height / 9;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = width / 7;

                brt111.setTextSize(20);
                brt222.setTextSize(18);
                city_text2.setTextSize(12);
                reporter_name2.setTextSize(12);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 5;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 9;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = width / 7;
            }
            else{
                brt11.setTextSize(18);
                brt22.setTextSize(16);
                city_text.setTextSize(12);
                reporter_name.setTextSize(12);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 5;
                reporterphoto.getLayoutParams().height = height / 5;

//                reporter_name.getLayoutParams().height = height / 9;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = width / 7;

                brt111.setTextSize(18);
                brt222.setTextSize(16);
                city_text2.setTextSize(12);
                reporter_name2.setTextSize(12);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 5;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 9;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = width / 7;
            }
        }
        else if((height<1080 && width<1920)&&(height>=854 && width>=480) || (height<1920 && width<1080) && (height>=480 && width>=854)){
            if(brno==21 || brno==22) {
                brt11.setTextSize(20);
                brt22.setTextSize(18);
                city_text.setTextSize(13);
                reporter_name.setTextSize(13);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 4;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(20);
                brt222.setTextSize(18);
                city_text2.setTextSize(13);
                reporter_name2.setTextSize(13);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 4;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 18;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }
            else{
                brt11.setTextSize(18);
                brt22.setTextSize(16);
                city_text.setTextSize(13);
                reporter_name.setTextSize(13);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 4;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(18);
                brt222.setTextSize(16);
                city_text2.setTextSize(13);
                reporter_name2.setTextSize(13);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 4;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 18;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }
        }
        else if((height<854 && width<480) && (height>750 && width>400) || (height<480 && width<854)&&(height>400 && width>750)){
            if(brno==21 || brno==22) {
                brt11.setTextSize(17);
                brt22.setTextSize(14);
                city_text.setTextSize(13);
                reporter_name.setTextSize(13);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 4;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(17);
                brt222.setTextSize(14);
                city_text2.setTextSize(13);
                reporter_name2.setTextSize(13);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 4;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 18;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }
            else{
                brt11.setTextSize(15);
                brt22.setTextSize(12);
                city_text.setTextSize(13);
                reporter_name.setTextSize(13);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 4;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(15);
                brt222.setTextSize(12);
                city_text2.setTextSize(13);
                reporter_name2.setTextSize(13);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 4;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 18;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }
        }
        else{
            /*
            Toast.makeText(this,"Videos having resolution lesser than 480p and greater than 1080p are not currently supported!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MainActivity.class));
             */
            if(brno==21 || brno==22) {
                brt11.setTextSize(10);
                brt22.setTextSize(9);
                city_text.setTextSize(5);
                reporter_name.setTextSize(5);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 4;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(10);
                brt222.setTextSize(9);
                city_text2.setTextSize(5);
                reporter_name2.setTextSize(5);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 4;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 18;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }
            else{
                brt11.setTextSize(8);
                brt22.setTextSize(7);
                city_text.setTextSize(5);
                reporter_name.setTextSize(5);
                main_template.getLayoutParams().height = height;
                main_template.getLayoutParams().width = width;
                main_template.setBackgroundColor(0);
                reptemp.getLayoutParams().height = height / 3;
                reptemp.getLayoutParams().width = width / 4;
                reporterphoto.getLayoutParams().height = height / 5;
//                reporter_name.getLayoutParams().height = height / 18;
                logoimage.getLayoutParams().height = height / 5;
                logoimage.getLayoutParams().width = width / 5;
                brimg1.getLayoutParams().height = height / 4;
                city_name.getLayoutParams().height = height / 16;
                city_name.getLayoutParams().width = height / 4;

                brt111.setTextSize(8);
                brt222.setTextSize(7);
                city_text2.setTextSize(5);
                reporter_name2.setTextSize(5);
                main_template2.getLayoutParams().height = height;
                main_template2.getLayoutParams().width = width;
                main_template2.setBackgroundColor(0);
                reptemp2.getLayoutParams().height = height / 3;
                reptemp2.getLayoutParams().width = width / 4;
                reporterphoto2.getLayoutParams().height = height / 5;
                toplayout.getLayoutParams().height = height / 5;
//                reporter_name2.getLayoutParams().height = height / 18;
                logoimage2.getLayoutParams().height = height / 5;
                logoimage2.getLayoutParams().width = width / 5;
                brimg11.getLayoutParams().height = height / 5;
                city_name2.getLayoutParams().height = height / 16;
                city_name2.getLayoutParams().width = height / 4;
            }

        }
    }

    private String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri.
     */
    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(activity_step2.this,MainActivity.class);
        startActivity(intent);
    }

    public void saveToImage(HorizontalScrollView content){

        Bitmap bitmap = Bitmap.createBitmap(content.getChildAt(0).getWidth(), content.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        content.layout(0, 0, content.getLayoutParams().width, content.getLayoutParams().height);
        content.draw(c);
        File file = new File("/sdcard/" + "cache2.png");
        try {
            if (!file.exists()) {
                System.out.println("HELLO: "+"FILE SAVED!!!!");
                file.createNewFile();
            }
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            ostream.close();
            temppath=file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    public void saveToImage2(HorizontalScrollView content){

        Bitmap bitmap = Bitmap.createBitmap(content.getChildAt(0).getWidth(), content.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        content.layout(0, 0, content.getLayoutParams().width, content.getLayoutParams().height);
        content.draw(c);
        File file = new File("/sdcard/" + "cache3.png");
        try {
            if (!file.exists()) {
                System.out.println("HELLO: "+"FILE SAVED!!!!");
                file.createNewFile();
            }
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            ostream.close();
            temppath=file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }



}
