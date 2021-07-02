package com.app.videonewsmaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.util.ArrayList;
import java.util.List;

public class activity_step1 extends AppCompatActivity implements PickiTCallbacks {
    Button next;
    ImageView back,reporter_photo,channel_logo;
    Bundle bundle;
    Uri fetchvideo,fetchpromo;
    LinearLayout uploadphoto,uploadlogo;
    EditText cityname,reportername;
    Uri logouri,reporterphotouri;

    PickiT pickiT;
    String city_name,reporter_name,logopath,reppath,videopath,promovideopath;
    private static final int REQUEST_TAKE_LOGO = 100;
    private static final int REQUEST_TAKE_PHOTO = 200;
    int choice=0;
    customization cust;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1);
        reporter_photo=findViewById(R.id.reporter_photo);
        channel_logo=findViewById(R.id.channel_logo);
        next=findViewById(R.id.next);
        back=findViewById(R.id.back_btn);
        uploadlogo = findViewById(R.id.uploadlogo);
        uploadphoto = findViewById(R.id.reporterphoto);
        cityname = findViewById(R.id.cityname);
        reportername=findViewById(R.id.reportername);
        System.out.println("UID: "+FirebaseAuth.getInstance().getUid());
        bundle = getIntent().getExtras();
        pickiT = new PickiT(this, this, this);
        fetchvideo= (Uri) bundle.get("video");
        fetchpromo = (Uri) bundle.get("promo");
        videopath= (String) bundle.get("videopath");
        promovideopath = (String) bundle.get("promopath");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity_step1.this,activity_step2.class);
                intent.putExtra("video",fetchvideo);
                intent.putExtra("promo",fetchpromo);
                intent.putExtra("city_name",cityname.getText().toString());
                intent.putExtra("reporter_name",reportername.getText().toString());
                intent.putExtra("logo",logouri);
                intent.putExtra("videopath",videopath);
                intent.putExtra("promopath",promovideopath);
                intent.putExtra("logopath",logopath);
                intent.putExtra("reppath",reppath);
                intent.putExtra("reporterphoto",reporterphotouri);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity_step1.this,MainActivity.class);
                startActivity(intent);
            }
        });
        uploadlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                    getPermissionLogo();
                else
                    uploadLogo();
            }
        });
        uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                    getPermissionreporter();
                else
                    uploadReporter();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_LOGO) {
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
                logouri = data.getData();
                Glide.with(getBaseContext()).load(logouri).into(channel_logo);
                uploadlogo.setBackgroundResource(R.drawable.btn_success);
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
                reporterphotouri=data.getData();
                Glide.with(getBaseContext()).load(reporterphotouri).into(reporter_photo);
                uploadphoto.setBackgroundResource(R.drawable.btn_success);
            }
        }
    }
    private void uploadLogo() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Photo"), REQUEST_TAKE_LOGO);
            choice=1;
        } catch (Exception e) {

        }
    }
    private void uploadReporter() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Photo"), REQUEST_TAKE_PHOTO);
            choice=2;
        } catch (Exception e) {

        }
    }
    private void getPermissionLogo() {
        String[] params = null;
        String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;

        int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, writeExternalStorage);
        int hasReadExternalStoragePermission = ActivityCompat.checkSelfPermission(this, readExternalStorage);
        List<String> permissions = new ArrayList<String>();

        if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(writeExternalStorage);
        if (hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(readExternalStorage);

        if (!permissions.isEmpty()) {
            params = permissions.toArray(new String[permissions.size()]);
        }
        if (params != null && params.length > 0) {
            ActivityCompat.requestPermissions(activity_step1.this,
                    params,
                    100);
        } else
            uploadLogo();
    }
    private void getPermissionreporter() {
        String[] params = null;
        String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;

        int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, writeExternalStorage);
        int hasReadExternalStoragePermission = ActivityCompat.checkSelfPermission(this, readExternalStorage);
        List<String> permissions = new ArrayList<String>();

        if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(writeExternalStorage);
        if (hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(readExternalStorage);

        if (!permissions.isEmpty()) {
            params = permissions.toArray(new String[permissions.size()]);
        }
        if (params != null && params.length > 0) {
            ActivityCompat.requestPermissions(activity_step1.this,
                    params,
                    100);
        } else
            uploadReporter();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(activity_step1.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        if(choice==1){
            logopath=path;
            System.out.print("CHECK: "+path);
        }
        else if(choice==2){
            reppath=path;
            System.out.print("CHECK: "+path);
        }
        else{

        }
    }

}
