package com.app.videonewsmaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PickiTCallbacks {
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    private static final int REQUEST_TAKE_GALLERY_PROMO = 200;
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 10;
    LinearLayout uploadVideo;
    private FirebaseDatabase firebaseDatabase;
    LinearLayout recordVideo;
    LinearLayout promoVideo;
    PickiT pickiT;
    String headertxt,bodytxt,footertxt;
    TextView header,body,footer;
    Button next;
    private Uri selectedVideoUri;
    private Uri selectedPromoUri;
    String videopath,promovideopath;
    int choice=0;
    customization cust;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        uploadVideo = findViewById(R.id.uploadVideo);
        recordVideo = findViewById(R.id.RecordVideo);
        promoVideo = findViewById(R.id.promoVideo);
        header=findViewById(R.id.headerbar);
        body=findViewById(R.id.bodybar);
        footer=findViewById(R.id.footerbar);
        next = findViewById(R.id.next);
        customization cust= com.app.videonewsmaker.customization.getInstance();
        headertxt=cust.getHeadertxt();
        footertxt=cust.getFootertxt();
        bodytxt=cust.getBodytxt();
        pickiT = new PickiT(this, this, this);
        System.out.println(headertxt);
        System.out.println(footertxt);
        System.out.println(bodytxt);
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice=1;
                if (Build.VERSION.SDK_INT >= 23)
                    getPermission();
                else
                    uploadVideo();

            }
        });
        recordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice=1;
                startCamera();
            }
        });
        promoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice=2;
                if (Build.VERSION.SDK_INT >= 23)
                    getPermissionpromo();
                else
                    uploadPromoVideo();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videopath!=null) {
                    Intent intent = new Intent(MainActivity.this, activity_step1.class);
                    intent.putExtra("video", selectedVideoUri);
                    intent.putExtra("videopath", videopath);
                    intent.putExtra("promopath", promovideopath);
                    intent.putExtra("promo", selectedVideoUri);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "PLEASE SELECT A VIDEO OR RECORD IT!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        header.setText(headertxt);
        body.setText(bodytxt);
        footer.setText(footertxt);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
                Log.d("DATA", String.valueOf(data));
                selectedVideoUri = data.getData();
                uploadVideo.setBackgroundResource(R.drawable.btn_success);
            } else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
                selectedVideoUri = data.getData();
                recordVideo.setBackgroundResource(R.drawable.btn_success);
            } else if (requestCode == REQUEST_TAKE_GALLERY_PROMO) {
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
                selectedPromoUri = data.getData();
                promoVideo.setBackgroundResource(R.drawable.btn_success);
            }
        }
    }
    public void startCamera()
    {
        //create Intent to record video and return it to the calling application*
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //start the video capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);


    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private void uploadVideo() {
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);

        } catch (Exception e) {

        }
    }
    private void uploadPromoVideo() {
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_PROMO);

        } catch (Exception e) {

        }
    }
    private void getPermission() {
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
            ActivityCompat.requestPermissions(MainActivity.this,
                    params,
                    100);
        } else
            uploadVideo();
    }
    private void getPermissionpromo() {
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
            ActivityCompat.requestPermissions(MainActivity.this,
                    params,
                    100);
        } else
            uploadPromoVideo();
    }
    private void openGallery() {
        //  first check if permissions was granted
        if (checkSelfPermission()) {
            Intent intent;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            } else {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            }
            //  In this example we will set the type to video
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra("return-data", true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
        }
    }
    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
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
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String reason) {
        if(choice==1) {
            videopath = path;

        }
        else if(choice==2){
            cust = com.app.videonewsmaker.customization.getInstance();
            cust.setPromovideo(path);
        }
    }

}
