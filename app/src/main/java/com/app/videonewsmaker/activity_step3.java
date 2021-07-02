package com.app.videonewsmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.videonewsmaker.Utility.ResourcesUtil;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class activity_step3 extends AppCompatActivity implements PickiTCallbacks {
    VideoView videoviewer;
    Uri videourl,selectedVideoUri,logodata,reporterphotodata;
    MediaController mediaC;
    private FirebaseAuth userd;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseUser user;
    LinearLayout removenoise,muteaudio,recaudio;
    Button promobtn;
    customization cust;
    private String outputfilePath,videopath,promovideopath;
    ProgressDialog pd;
    String mutedvideo,unmutedvideo;
    private static final String TAG = "VIDEO EDITOR";
    int remaudiomode=1;
    int remaudiotime=0;
    String fileloc;
    LinearLayout insertsound;
    int mode=1,promobtnmode=1;
    PickiT pickiT;
    int height, width,rotation;
    Button export,gotofolder;
    ImageView back;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String editedvideo,videowithpromo;
    private String udid,novideo,product_id,name,email,friends,location,created,phoneno,profilepic,license,licensedate,joindate;
    public static final int ACTIVITY_RECORD_SOUND = 0;
    ScrollView mainlayout,reclayout;
    String audiopath;
    TextView finalfilepath;
    Bundle bundle;
    int recaudiomode=0;
    LinearLayout main;
    RelativeLayout conf;
    int REQUEST_TAKE_AUDIO=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateData();
        pickiT = new PickiT(this,  this, this);
        setContentView(R.layout.activity_step3);
        export=findViewById(R.id.export);
        videoviewer = findViewById(R.id.videoviewer);
        mediaC=new MediaController(this);
        bundle = getIntent().getExtras();
        removenoise=findViewById(R.id.removenoise);
        mainlayout=findViewById(R.id.mainpanel);
        gotofolder=findViewById(R.id.gotofolder);
        cust = com.app.videonewsmaker.customization.getInstance();
        muteaudio=findViewById(R.id.muteaudio);
        recaudio=findViewById(R.id.recaudio);
        promobtn=findViewById(R.id.promobtn);
        finalfilepath=findViewById(R.id.fileloc);
        main=findViewById(R.id.mainActivity);
        conf=findViewById(R.id.confActivity);
        videopath=(String) bundle.get("videopath");
        insertsound=findViewById(R.id.insertsound);
        promovideopath=cust.getPromovideo();
        gotofolder.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            @Override
            public void onClick(View v) {
//                Uri selectedUri = Uri.parse("/storage/emulated/0/Movies");
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(selectedUri, "resource/folder");
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
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("/sdcard/Download/"+fileloc), "video/mp4");
                startActivity(intent);

            }
        });
        System.out.println("MAIN VIDEO: "+videopath);
        System.out.println("PROMO VIDEO: "+promovideopath);
        cust.setMainvideo((Uri) bundle.get("video"));
        cust.setCityname((String) bundle.get("city_name"));
        cust.setReporter_name((String) bundle.get("reporter_name"));
        cust.setLogo((Uri) bundle.get("logo"));
        final File cacheDirectory = getCacheDir();

        try {
            ResourcesUtil.rawResourceToFile(getResources(),R.raw.notodevnagari,new File(cacheDirectory,"notodevnagari.ttf"));
            final HashMap<String, String> fontNameMapping = new HashMap<>();
            fontNameMapping.put("MyFontName", "Noto Sans");
            Config.setFontDirectory(this,cacheDirectory.getAbsolutePath(), fontNameMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }

        cust.setReporterphoto((Uri) bundle.get("reporterphoto"));
        cust.setBr1((String) bundle.get("breakingnews1"));
        cust.setBr2((String) bundle.get("breakingnews2"));
        if(!new File("/sdcard/NotoSans-Black.ttf").exists())
            new DownloadFileFromURL().execute("https://firebasestorage.googleapis.com/v0/b/videoeditor-1f168.appspot.com/o/NotoSans-Black.ttf?alt=media&token=e6923b2b-5568-4482-a983-052f3b3c076f");
        else
        {

        }
        if(bundle.get("brstyle")!=null) {
            cust.setBrno((int) bundle.get("brstyle"));
        }
        selectedVideoUri=cust.getMainvideo();
        if(bundle.get("vidheight")!=null) {
            height = (int) bundle.get("vidheight");
            width = (int) bundle.get("vidwidth");
            rotation = (int) bundle.get("vidrotation");
        }
        logodata=cust.getLogo();
        if(promovideopath==null){
            mode=1;
        }
        else{
            mode=2;
            promobtn.setVisibility(View.VISIBLE);
        }

        muteaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(activity_step3.this);
                pd.setMessage("Processing");
                pd.setCancelable(false);
                pd.show();
                if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            removeaudio();
                            videourl = Uri.parse(outputfilePath);
                            videoviewer.setVideoURI(videourl);
                            videoviewer.setMediaController(mediaC);
                            mediaC.setAnchorView(videoviewer);
                            videoviewer.start();
                            pd.dismiss();
                        }
                    });
                }
                else {
                    removeaudioprocessing pros = new removeaudioprocessing();
                    pros.execute();
                }
            }
        });
        removenoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remaudiomode==2){Toast toast = Toast.makeText(getApplicationContext(),
                        "No audio to remove noise!",
                        Toast.LENGTH_SHORT);

                    toast.show();}
                else {
                    pd = new ProgressDialog(activity_step3.this);
                    pd.setMessage("Processing");
                    pd.setCancelable(false);
                    pd.show();
                    removenoiseprocessing pros = new removenoiseprocessing();
                    pros.execute();
                    removenoise.setBackgroundResource(R.drawable.btn_success);
                }
            }
        });
        promobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle.get("recaudiomode")!=null){
                    recaudiomode=bundle.getInt("recaudiomode");
                }
                if(promobtnmode==1){
                    promobtn.setText("Add Promo");
                    outputfilePath=editedvideo;
                    videourl = Uri.parse(outputfilePath);
                    videoviewer.setVideoURI(videourl);
                    videoviewer.setMediaController(mediaC);
                    mediaC.setAnchorView(videoviewer);
                    videoviewer.start();
                    promobtnmode--;
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Promo Video Removed!",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    muteaudio.setBackgroundResource(R.drawable.btn_background);
                    remaudiomode=1;
                    remaudiotime=0;
                }
                else{
                    pd = new ProgressDialog(activity_step3.this);
                    pd.setMessage("Processing");
                    pd.setCancelable(false);
                    pd.show();
                    promobtn.setText("Remove Promo");
                    addpromo ap=new addpromo();
                    ap.execute();
                    promobtnmode++;
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Promo Video Added!",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    remaudiotime=0;
                }
            }
        });
        if(bundle.get("mode")!=null) {
            if (bundle.get("mode").equals("process")) {
                pd = new ProgressDialog(this);
                pd.setMessage("Processing");
                pd.setCancelable(false);
                pd.show();
                processing pros = new processing();
                pros.execute();
            } else {
                videourl = Uri.parse(outputfilePath);
                videoviewer.setVideoURI(videourl);
                videoviewer.setMediaController(mediaC);
                mediaC.setAnchorView(videoviewer);
                videoviewer.start();
            }
        }
        else if(bundle.get("newaudiovideo")!=null){
            audiopath=bundle.getString("newaudiovideo");
            outputfilePath=bundle.getString("outputfileafterrec");
            cust.setMainvideo((Uri) bundle.get("video"));
            cust.setCityname((String) bundle.get("city_name"));
            cust.setReporter_name((String) bundle.get("reporter_name"));
            cust.setLogo((Uri) bundle.get("logo"));
            cust.setReporterphoto((Uri) bundle.get("reporterphoto"));
            cust.setBr1((String) bundle.get("breakingnews1"));
            cust.setBr2((String) bundle.get("breakingnews2"));
            cust.setBrno((int) bundle.get("brstyle"));
            height = (int) bundle.get("vidheight");
            width = (int) bundle.get("vidwidth");
            rotation = (int) bundle.get("vidrotation");
            editedvideo= bundle.getString("editedvideo");
            mutedvideo= bundle.getString("mutedvideo");
            unmutedvideo=bundle.getString("unmutedvideo");
            promovideopath= bundle.getString("promovideopath");
            videowithpromo=bundle.getString("videowithpromo");
            remaudiomode=bundle.getInt("remaudiomode");
            remaudiotime=bundle.getInt("remaudiotime");
            promobtnmode=bundle.getInt("promobtnmode");
            if(promobtnmode==0){
                promobtn.setText("ADD PROMO");
            }
            else{
                promobtn.setText("REMOVE PROMO");
            }
            if(remaudiomode==1){
                muteaudio.setBackgroundResource(R.drawable.btn_background);
            }
            else{
                muteaudio.setBackgroundResource(R.drawable.btn_success);
            }
            pd=new ProgressDialog(activity_step3.this);
            pd.setMessage("Processing");
            pd.setCancelable(false);
            pd.show();
            addrecorded pros = new addrecorded();
            pros.execute();
        }
        else{

        }

        export.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(activity_step3.this);
                pd.setMessage("PLEASE WAIT...");
                pd.setCancelable(false);
                pd.show();
                TestInternet test=new TestInternet();
                test.execute();

            }
        });

        recaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionListener=new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if(remaudiomode==0 || remaudiomode==1) {
                            Intent intent = new Intent(activity_step3.this, recaudio.class);
                            intent.putExtra("videopath", outputfilePath);
                            intent.putExtra("city_name", (String) bundle.get("city_name"));
                            intent.putExtra("logo", (Uri) bundle.get("logo"));
                            intent.putExtra("reporter_name", (String) bundle.get("reporter_name"));
                            //  intent.putExtra("reporterphoto", (String) bundle.get("reporterphoto"));
                            intent.putExtra("breakingnews1", (String) bundle.get("breakingnews1"));
                            intent.putExtra("breakingnews2", (String) bundle.get("breakingnews2"));
                            intent.putExtra("brstyle", (int) bundle.get("brstyle"));
                            intent.putExtra("vidheight", (int) bundle.get("vidheight"));
                            intent.putExtra("vidwidth", (int) bundle.get("vidwidth"));
                            intent.putExtra("vidrotation", (int) bundle.get("vidrotation"));
                            intent.putExtra("editedvideo", editedvideo);
                            intent.putExtra("mutedvideo", mutedvideo);
                            intent.putExtra("unmutedvideo", unmutedvideo);
                            intent.putExtra("remaudiomode", remaudiomode);
                            intent.putExtra("remaudiotime", remaudiotime);
                            intent.putExtra("promobtnmode", promobtnmode);
                            intent.putExtra("promovideopath", promovideopath);
                            intent.putExtra("videowithpromo", videowithpromo);
                            startActivity(intent);
                        }
                        else if(remaudiomode==2){
                            Toast.makeText(activity_step3.this, "PLEASE UNMUTE AND THEN RECORD!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(activity_step3.this,"Permission not given!",Toast.LENGTH_SHORT);
                    }
                };
                TedPermission.with(activity_step3.this).setPermissionListener(permissionListener).setPermissions(Manifest.permission.RECORD_AUDIO).check();


            }
        });
        insertsound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                    getPermission();
                else
                    uploadaudio();
            }
        });


    }
    public void copy(String x,String y) throws IOException {
        File src=new File(x);
        File dst=new File(y);
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_AUDIO) {
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(outputfilePath);
                    String secondsvideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int secondsvideolong = Integer.parseInt(secondsvideo);
                    retriever.setDataSource(this, Uri.parse(audiopath));
                    String secondsaudio = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int secondsaudiolong = Integer.parseInt(secondsaudio);
                    long minutes = (secondsvideolong / 1000) / 60;
                    long seconds = (secondsvideolong / 1000) % 60;
                    if (secondsaudiolong > secondsvideolong) {
                        Toast.makeText(activity_step3.this, "PLEASE SELECT A AUDIO WHOSE DURATION IS LESS THAN "+minutes+":"+seconds+" minutes!", Toast.LENGTH_SHORT).show();
                    } else {
                        pd = new ProgressDialog(activity_step3.this);
                        pd.setMessage("Processing");
                        pd.setCancelable(false);
                        pd.show();
                        addrecorded pros = new addrecorded();
                        pros.execute();
                    }
                }
                catch(Exception e){
                    Toast.makeText(activity_step3.this, "PLEASE SELECT SOME OTHER AUDIO FILE!", Toast.LENGTH_SHORT).show();
                }

            }
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
            ActivityCompat.requestPermissions(activity_step3.this,
                    params,
                    100);
        } else
            uploadaudio();
    }
    private void uploadaudio() {
        try {
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Photo"), REQUEST_TAKE_AUDIO);
        } catch (Exception e) {

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
                        joindate = profileModel.getJoindate();
                        novideo = profileModel.getnovideos();
                        product_id = profileModel.getproduct_code();
                        license=profileModel.getlicense();
                        licensedate=profileModel.getlicensedate();

                    }

                    else {
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(activity_step3.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

                }


            });



        }
    }
    private void execFFmpegBinary(final String[] command) {
        int rc=FFmpeg.execute(command);
        if(rc== RETURN_CODE_SUCCESS){
            Log.d(TAG, "SUCCESS");

        }
        else if(rc==RETURN_CODE_CANCEL){
            Log.d(TAG, "FAILED" );
        }
        else{
            Log.d(TAG, "Started command : ffmpeg " + command);
        }

    }
    private void addpromoagain(int height,int width,int rotation){
        System.out.println("LOGO INTEGRATION STARTED");
        Context context=this;
        File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String filePrefix = "sticker_video";
        String fileExtn = ".mp4";
        String yourRealPath = outputfilePath;
        File dest = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }
        outputfilePath = dest.getAbsolutePath();
        editedvideo=outputfilePath;
        String[] stickercommand = {
                "-i",
                yourRealPath,
                "-vf",
                "scale="+width+":"+height,
               "-vcodec", "libx264","-crf","27",
               "-preset","ultrafast",
                outputfilePath
        };
        String yourpath=outputfilePath;
        execFFmpegBinary(stickercommand);
        String promopath=promovideopath;
        String filePrefix2 = "promovideo";
        String fileExtn2 = ".mp4";
        File dest2 = new File(moviesDir, filePrefix2 + fileExtn2);
        int fileNo2 = 0;
        while (dest2.exists()) {
            fileNo2++;
            dest2 = new File(moviesDir, filePrefix2 + fileNo2 + fileExtn2);
        }
        promovideopath = dest2.getAbsolutePath();
        execFFmpegBinary(new String[]{
                "-i",
                promopath,
                "-vf",
                "scale="+width+":"+height,
               "-vcodec", "libx264","-crf","27",
               "-preset","ultrafast",
                promovideopath
        });
        System.out.println("INTERMEDIATE 3 STARTED");
        String filePrefix1 = "sticker_video";
        String fileExtn1 = ".mp4";
        File dest1 = new File(moviesDir, filePrefix1 + fileExtn1);
        int fileNo1 = 0;
        while (dest1.exists()) {
            fileNo1++;
            dest1 = new File(moviesDir, filePrefix1 + fileNo1 + fileExtn1);
        }
        outputfilePath = dest1.getAbsolutePath();
        videowithpromo=outputfilePath;
        String str1[]=new String[]{
                "-y",
                "-i",
                promovideopath,
                "-i",
                yourpath,
                "-filter_complex",
                "[0:v]setsar=1:1[v0];[1:v]scale="+width+":"+height+",setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1",
                outputfilePath
        };
        System.out.println(Arrays.toString(str1));
        execFFmpegBinary(str1);
        muteaudio.setBackgroundResource(R.drawable.btn_background);
        remaudiomode=1;
    }
    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }

    private void addlogo(int height,int width,int rotation){
        System.out.println("LOGO INTEGRATION STARTED");
        Context context=this;
        String[] stickercommand ={};
        File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String filePrefix = "sticker_video";
        String fileExtn = ".mp4";
        String yourRealPath = videopath;
        String stickerpath = "/sdcard/cache.png";
        File dest = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }
        outputfilePath = dest.getAbsolutePath();
        String yourpath=outputfilePath;
        editedvideo=outputfilePath;
        stickercommand = new String[]{"-i", "" + yourRealPath,"-i","" +stickerpath, "-filter_complex", "" +"overlay=0:0","-vcodec", "libx264","-crf","27", "-preset","ultrafast",""+ outputfilePath};
        System.out.println(Arrays.toString(stickercommand));

        if(mode==1) {
            execFFmpegBinary(stickercommand);
            if(bundle.getBoolean("br2switchval")) {

                moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                filePrefix = "sticker_video";
                fileExtn = ".mp4";
                yourRealPath = outputfilePath;
                stickerpath="/sdcard/cache3.png";
                dest = new File(moviesDir, filePrefix + fileExtn);
                fileNo = 0;
                while (dest.exists()) {
                    fileNo++;
                    dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
                }
                outputfilePath = dest.getAbsolutePath();
                editedvideo = outputfilePath;
                if (height <= 1080 && height > 720) {
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });
                } else if(height<=720 && height >480) {
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });

                }
                else if(height<=480 && height>400){
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
                else if(height<=400 && height>320){
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
                else{
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
            }
            if(bundle.getBoolean("br2switchval1")){

                moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                filePrefix = "sticker_video";
                fileExtn = ".mp4";
                yourRealPath = outputfilePath;
                stickerpath="/sdcard/cache2.png";
                dest = new File(moviesDir, filePrefix + fileExtn);
                fileNo = 0;
                while (dest.exists()) {
                    fileNo++;
                    dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
                }
                outputfilePath = dest.getAbsolutePath();
                editedvideo = outputfilePath;
                if (height <= 1080 && height > 720) {
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h",
                           "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });
                } else if(height<=720 && height >480) {
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });

                }
                else if(height<=480 && height>320){
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
                else{
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
            }
        }
            /*
            moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            filePrefix = "sticker_video";
            fileExtn = ".mp4";
            yourRealPath = outputfilePath;
            dest = new File(moviesDir, filePrefix + fileExtn);
            fileNo = 0;
            while (dest.exists()) {
                fileNo++;
                dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
            }
            outputfilePath = dest.getAbsolutePath();
            editedvideo=outputfilePath;
            if(height==1080) {
                execFFmpegBinary(new String[]{
                        "-i",
                        yourRealPath,
                        "-vf",
                        "drawtext=fontsize=75:fontfile=/system/fonts/DroidSans.ttf:text=" + cust.getBr1() + ":y=h-line_h-120:x=w-mod(max(t\\,0)*(w+tw)/7.5\\,(w+tw)):fontcolor=white",
                        editedvideo

                });
            }
            else{
                execFFmpegBinary(new String[]{
                        "-i",
                        yourRealPath,
                        "-vf",
                        "drawtext=fontsize=48:fontfile=/system/fonts/DroidSans.ttf:text=" + cust.getBr1() + ":y=h-line_h-60:x=w-mod(max(t\\,0)*(w+tw)/7.5\\,(w+tw)):fontcolor=white",
                        editedvideo

                });
            }

             */
        else if(mode==2){
            execFFmpegBinary(stickercommand);
            if(bundle.getBoolean("br2switchval")) {
                moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                filePrefix = "sticker_video";
                fileExtn = ".mp4";
                yourRealPath = outputfilePath;
                stickerpath="/sdcard/cache3.png";
                dest = new File(moviesDir, filePrefix + fileExtn);
                fileNo = 0;
                while (dest.exists()) {
                    fileNo++;
                    dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
                }
                outputfilePath = dest.getAbsolutePath();
                editedvideo = outputfilePath;
                if (height <= 1080 && height > 720) {
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });
                } else if(height<=720 && height >480) {
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });

                }
                else if(height<=480 && height>400){
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
                else if(height<=400 && height>320){
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
                else{
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
            }
            if(bundle.getBoolean("br2switchval1")){
                moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                filePrefix = "sticker_video";
                fileExtn = ".mp4";
                yourRealPath = outputfilePath;
                stickerpath="/sdcard/cache2.png";
                dest = new File(moviesDir, filePrefix + fileExtn);
                fileNo = 0;
                while (dest.exists()) {
                    fileNo++;
                    dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
                }
                outputfilePath = dest.getAbsolutePath();
                editedvideo = outputfilePath;
                if (height <= 1080 && height > 720) {
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });
                } else if(height<=720 && height >480) {
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });

                }
                else if(height<=480 && height>320){
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo

                    });
                }
                else{
                    execFFmpegBinary(new String[]{
                            "-i",
                            ""+yourRealPath,
                            "-i",""+stickerpath,
                            "-filter_complex",
                            "overlay=x='w-100*mod(t,(W+w)/100)':y=H-h",
                            "-vcodec", "libx264","-crf","27",
                            "-preset","ultrafast",
                            ""+ editedvideo
                    });
                }
            }
            String promopath=promovideopath;
            String filePrefix2 = "promovideo";
            String fileExtn2 = ".mp4";
            File dest2 = new File(moviesDir, filePrefix2 + fileExtn2);
            int fileNo2 = 0;
            while (dest2.exists()) {
                fileNo2++;
                dest2 = new File(moviesDir, filePrefix2 + fileNo2 + fileExtn2);
            }
            promovideopath = dest2.getAbsolutePath();
            System.out.println("PROMO VIDEO GETTING CREATED");
            execFFmpegBinary(new String[]{
                    "-i",
                    promopath,
                    "-vf",
                    "scale="+width+":"+height,
                   "-vcodec", "libx264","-crf","27",
                   "-preset","ultrafast",
                    promovideopath
            });
            System.out.println("INTERMEDIATE 3 STARTED");
            String filePrefix1 = "sticker_video";
            String fileExtn1 = ".mp4";
            File dest1 = new File(moviesDir, filePrefix1 + fileExtn1);
            int fileNo1 = 0;
            while (dest1.exists()) {
                fileNo1++;
                dest1 = new File(moviesDir, filePrefix1 + fileNo1 + fileExtn1);
            }
            outputfilePath = dest1.getAbsolutePath();
            videowithpromo=outputfilePath;
            String str1[]=new String[]{
                    "-y",
                    "-i",
                    promovideopath,
                    "-i",
                    editedvideo,
                    "-filter_complex",
                    "[0:v]setsar=1:1[v0];[1:v]scale="+width+":"+height+",setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1",
                    "-vcodec", "libx264","-r","25",
                    "-preset","ultrafast",
                    outputfilePath
            };
            System.out.println(Arrays.toString(str1));
            execFFmpegBinary(str1);}
    }

    private void removenoise(){
        System.out.println("REMOVE NOISE INTEGRATION STARTED");
        Context context=this;
        File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String outputfilePath1 = "/sdcard/cache.aac";
        String outputfilePath2 = "/sdcard/cache1.aac";
        String filePrefix = "sticker_video";
        String fileExtn = ".mp4";
        String yourRealPath = outputfilePath;
        String[] stickercommand1 = {"-i", "" + yourRealPath,"-vn","-acodec","copy","" + outputfilePath1};
        execFFmpegBinary(stickercommand1);
        System.out.println("REMOVE NOISE PHASE 2 STARTED");
        File dest = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }
        outputfilePath = dest.getAbsolutePath();

        String[] stickercommand2 = {"-i", "" + outputfilePath1,"-af","" + "highpass=f=200, lowpass=f=3000", "" + outputfilePath2};
        execFFmpegBinary(stickercommand2);
        System.out.println("REMOVE NOISE PHASE 3 STARTED");
        String[] stickercommand3= {"-i",""+yourRealPath,"-i",""+outputfilePath2,"-c:v","copy","-c:a","copy",""+outputfilePath};
        execFFmpegBinary(stickercommand3);
        File fdelete1= new File(outputfilePath1);
        if(fdelete1.exists()) {
            fdelete1.delete();
        }
        File fdelete2= new File(outputfilePath2);
        if(fdelete2.exists()) {
            fdelete2.delete();
        }
    }
    private void removeaudio(){
        System.out.println("UNMUTE VIDEO: "+mutedvideo);
        if(remaudiomode==1) {
            System.out.println("REMOVE AUDIO INTEGRATION STARTED");
            if(remaudiotime!=1) {
                Context context = this;
                File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String filePrefix = "sticker_video";
                String fileExtn = ".mp4";
                mutedvideo = outputfilePath;
                File dest2 = new File(moviesDir, filePrefix + fileExtn);
                int fileNo = 0;
                while (dest2.exists()) {
                    fileNo++;
                    dest2 = new File(moviesDir, filePrefix + fileNo + fileExtn);
                }
                outputfilePath = dest2.getAbsolutePath();
                execFFmpegBinary(new String[]{
                        "-f",
                        "lavfi",
                        "-i",
                        "aevalsrc=0",
                        "-i",
                        mutedvideo,
                        "-c:v",
                        "copy",
                        "-c:a",
                        "aac",
                        "-map",
                        "0",
                        "-map",
                        "1:v",
                        "-shortest",
                        outputfilePath
                });
                unmutedvideo=outputfilePath;
                System.out.println("MUTE VIDEO: " + outputfilePath);
                remaudiotime++;

                remaudiomode=2;
                muteaudio.setBackgroundResource(R.drawable.btn_success);
            }
            else{
                outputfilePath=unmutedvideo;
                System.out.println("OUTPUTSET TO: "+outputfilePath);
                remaudiomode=2;
                muteaudio.setBackgroundResource(R.drawable.btn_success);
            }
        }
        else if(remaudiomode==2){
            muteaudio.setBackgroundResource(R.drawable.btn_background);
            outputfilePath=mutedvideo;
            System.out.println("OUTPUTSET TO: "+outputfilePath);
            remaudiomode=1;
        }
    }


    public class addpromo extends AsyncTask<Integer, Void, String> {


        public addpromo() {

        }

        @Override
        protected String doInBackground(Integer... params) {
            if(recaudiomode==1){
                addpromoagain(height,width,rotation);
            }
            else if(remaudiomode==2){
                addpromoagain(height,width,rotation);

            }
            else if(remaudiomode==1){
                outputfilePath=videowithpromo;
            }



            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            videourl = Uri.parse(outputfilePath);
            videoviewer.setVideoURI(videourl);
            videoviewer.setMediaController(mediaC);
            mediaC.setAnchorView(videoviewer);
            videoviewer.start();
            pd.dismiss();
        }
    }

    public class processing extends AsyncTask<Integer, Void, String> {


        public processing() {

        }

        @Override
        protected String doInBackground(Integer... params) {
            addlogo(height,width,rotation);


            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            videourl = Uri.parse(outputfilePath);
            videoviewer.setVideoURI(videourl);
            videoviewer.setMediaController(mediaC);
            mediaC.setAnchorView(videoviewer);
            videoviewer.start();
            pd.dismiss();
        }
    }

    public class removeaudioprocessing extends AsyncTask<Integer, Void, String> {


        public removeaudioprocessing() {

        }

        @Override
        protected String doInBackground(Integer... params) {

            removeaudio();

            return "OK";
        }
        @Override
        protected void onPostExecute(String result) {
            videourl = Uri.parse(outputfilePath);
            videoviewer.setVideoURI(videourl);
            videoviewer.setMediaController(mediaC);
            mediaC.setAnchorView(videoviewer);
            videoviewer.start();
            pd.dismiss();

        }
    }
    public class removenoiseprocessing extends AsyncTask<Integer, Void, String> {


        public removenoiseprocessing() {

        }

        @Override
        protected String doInBackground(Integer... params) {

            removenoise();

            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            videourl = Uri.parse(outputfilePath);
            videoviewer.setVideoURI(videourl);
            videoviewer.setMediaController(mediaC);
            mediaC.setAnchorView(videoviewer);
            videoviewer.start();
            pd.dismiss();

        }
    }
    private String generateList(String[] inputs) {
        File list;
        Writer writer = null;
        try {
            File moviesDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            list = new File(moviesDir, "ffmpeg-list"+".txt");
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(list)));
            for (String input: inputs) {
                writer.write("file '" + input + "'\n");
                Log.d(TAG, "Writing to list file: file '" + input + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "/";
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        Log.d(TAG, "Wrote list file to " + list.getAbsolutePath());
        return list.getAbsolutePath();
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
        audiopath = path;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(activity_step3.this,MainActivity.class);
        startActivity(intent);
    }
    void recaudio(){
        Context context = this;
        File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String filePrefix = "sticker_video";
        String fileExtn = ".mp4";
        String yourfilepath = outputfilePath;
        File dest2 = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest2.exists()) {
            fileNo++;
            dest2 = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }
        outputfilePath = dest2.getAbsolutePath();
        execFFmpegBinary(new String[]{
                "-i",
                yourfilepath,
                "-i",
                audiopath,
                "-c",
                "copy",
                "-map",
                "0:0",
                "-map",
                "1:0",
                outputfilePath
        });
    }
    public class addrecorded extends AsyncTask<Integer, Void, String> {


        public addrecorded() {

        }

        @Override
        protected String doInBackground(Integer... params) {

            recaudio();

            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            videourl = Uri.parse(outputfilePath);
            videoviewer.setVideoURI(videourl);
            videoviewer.setMediaController(mediaC);
            mediaC.setAnchorView(videoviewer);
            videoviewer.start();
            pd.dismiss();

        }
    }
    class TestInternet extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url = new URL("https://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(3000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                }
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) { // code if not connected
                Context context = activity_step3.this;
                //File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                //deleteRecursive(moviesDir);
                pd.dismiss();
                Toast.makeText(activity_step3.this, "PLEASE CONNECT TO THE INTERENT!", Toast.LENGTH_SHORT).show();

            } else { // code if connected
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
                String ujoindate = joindate;
                String ulicensedate = licensedate;
                if (uproduct_id == null && Integer.parseInt(uvideos) >= 5) {
                    Context context = activity_step3.this;
                    File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    deleteRecursive(moviesDir);
                    pd.dismiss();
                    Toast.makeText(activity_step3.this, "EXCEEDED FREE VIDEOS LIMIT! BUY LICENSE TO GET MORE!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_step3.this, dashboard.class);
                    startActivity(intent);
                } else {
                    @SuppressLint("SdCardPath") String newfilepath = "/sdcard/Download";
                    String filePrefix = "news_video";
                    String fileExtn = ".mp4";
                    Context context = activity_step3.this;
                    File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File dest = new File(new File(newfilepath), filePrefix + fileExtn);
                    int fileNo = 0;
                    while (dest.exists()) {
                        fileNo++;
                        dest = new File(new File(newfilepath), filePrefix + fileNo + fileExtn);
                    }
                    File sourceLocation = new File(outputfilePath);
                    File targetLocation = dest;
                    try {
                        FileUtils.copyFile(sourceLocation, targetLocation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    deleteRecursive(moviesDir);
                    if(fileNo==0){
                        fileloc=filePrefix+".mp4";
                    }
                    else{
                        fileloc=filePrefix+String.valueOf(fileNo)+".mp4";
                    }
                    CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation, uprofilepic, ucreated, uudid, uproduct_id, String.valueOf(Integer.parseInt(uvideos) + 1), ulicense, ulicensedate,ujoindate);
                    DatabaseReference db = firebaseDatabase.getReference("users/");
                    db.child(user.getUid()).setValue(creatProfileModel);
                    pd.dismiss();
                    finalfilepath.setText("Download/" + filePrefix + fileNo);
                    main.setVisibility(View.GONE);
                    conf.setVisibility(View.VISIBLE);

                }
            }
        }
    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/sdcard/NotoSans-Black.ttf");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress(""+(int)((total*100)/lenghtOfFile));


                    output.write(data, 0, count);
                }


                output.flush();


                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }



    }

}
