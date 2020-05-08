package com.app.videoeditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.videoeditor.Utility.ImageUtility;
import com.bumptech.glide.Glide;
import com.facebook.login.Login;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.provider.CalendarContract.CalendarCache.URI;

public class Signup extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    public static String fileName;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Bitmap imageBitmap;
    private Uri fileUri;
    private File actual_size;
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    String ImageDecode = "";

    Bitmap compressedImages;

    EditText et_names, et_com_mobiles, et_citys;
    private int PICK_IMAGE_GALLARY_PERMISSION = 120;
    private static int IMG_RESULT = 1;

    TextView et_emails, tv_select_location, change, select_camera, open_images;
    Button submit;
    ImageView civ_user, logout;

    private Marker marker;
    Button btnButton;
    private FirebaseAuth userd;
    private boolean isclickones = true;

    private String aAddress;
    private BottomSheetBehavior bottomSheetBehavior;
    private CoordinatorLayout coordinatorLayout;


    private String generatedFilePath;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    UploadTask uploadimage;


    Button regsiter;
    EditText firstnaem, lastname, email, phone, pass, cnfrmpass;
    CheckBox check;

    private static String emailPattern = "[a-zA-Z+{n}0-9._-]+@[a-z]+\\.+[a-z]+";
    private static String passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,}";
    private Uri imageUri;
    private String profilepic;
    private String Imagecode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
        userd = FirebaseAuth.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            //    finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser users = firebaseAuth.getCurrentUser();
        DatabaseReference databaseMyprofile = firebaseDatabase.getReference("users/" + Objects.requireNonNull(firebaseAuth.getUid()));

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        databaseMyprofile.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null) {
                //    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                }else {
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Signup.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }


        });


        //  StorageReference storageRef = firebaseStorage.getReference().child("profilePic/" + Objects.requireNonNull(firebaseAuth.getUid()));//reach out to your photo file hierarchically

        findViewById();
        setOnclicklistner();
        locationEnabled();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location mCurrentLocation = locationResult.getLastLocation();
                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                String cityName = getCityName(myCoordinates);
                // Toast.makeText(UserDetail.this, aAddress, Toast.LENGTH_SHORT).show();
                pass.setText(aAddress);


            }
        };

        FirebaseUser user = userd.getCurrentUser();
        if (userd != null) {
            assert user != null;

            firstnaem.setText(user.getDisplayName());
            email.setText(user.getEmail());

        }

        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("mylog", "Getting Location Permission");
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mylog", "Not granted");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
                requestLocation();
        } else
            requestLocation();
    }




    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);
        Log.d("mylog", "In Requesting Location");
        if (location != null && (System.currentTimeMillis() - location.getTime()) <= 1000 * 2) {
            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            String cityName = getCityName(myCoordinates);
            Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog", "Last location too old getting new location!");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    }

    private void setOnclicklistner(){
        regsiter.setOnClickListener(this);
        change.setOnClickListener(this);
        select_camera.setOnClickListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the user is not logged in
        //opening the login activity
        if (userd.getCurrentUser() == null) {
            //  finish();
            startActivity(new Intent(this, Login.class));
        }

    }




    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(Signup.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            aAddress = address;
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
            //   Toast.makeText(UserDetail.this, addresses.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }




    private void findViewById() {
        firstnaem = findViewById(R.id.et_Firstname);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        change = findViewById(R.id.change);
        email = findViewById(R.id.et_email);
        pass = findViewById(R.id.et_map);
        phone = findViewById(R.id.et_number);
        regsiter = findViewById(R.id.btn_register);
        select_camera = findViewById(R.id.select_camera);

    }



    private void locationEnabled() {

        LocationManager lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(Signup.this)
                    .setCancelable(false)
                    .setMessage("Turn On your Location Service")
                    .setPositiveButton("Settings", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));



                                }
                            })
                    .setNegativeButton( "" , null )
                    .show() ;

        }
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                if(firstnaem.getText().toString().isEmpty()) {
                    firstnaem.setHint("First name is required");
                    firstnaem.setBackgroundResource(R.xml.btn_delete_card);
                    firstnaem.setHintTextColor(Color.RED);
                    firstnaem.requestFocus();
                    firstnaem.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            firstnaem.setHintTextColor(R.color.colorPrimary);
                            firstnaem.setBackgroundResource(R.xml.edt_text_gray_border);
                            firstnaem.setHint("First Name");

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });
                    return;
                }else if (email.getText().toString().isEmpty()){


                    email.setHint("Email is required");
                    email.setBackgroundResource(R.xml.btn_delete_card);
                    email.setHintTextColor(Color.RED);
                    email.requestFocus();
                    email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            email.setHintTextColor(R.color.colorPrimary);
                            email.setBackgroundResource(R.xml.edt_text_gray_border);
                            email.setHint("Enter Email here");

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });
                    return;
                }else if (!email.getText().toString().matches(emailPattern)) {


                    email.setHint("Invalid Email");
                    email.setBackgroundResource(R.xml.btn_delete_card);
                    email.setHintTextColor(Color.RED);
                    email.requestFocus();
                    email.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            email.setHintTextColor(R.color.colorPrimary);
                            email.setBackgroundResource(R.xml.edt_text_gray_border);
                            email.setHint("Enter Email here");

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });
                    return;
                }else if(phone.getText().toString().isEmpty()){

                    phone.setHint("Phone Number is required");
                    phone.setBackgroundResource(R.xml.btn_delete_card);
                    phone.setHintTextColor(Color.RED);
                    phone.requestFocus();
                    phone.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            phone.setHintTextColor(R.color.colorPrimary);
                            phone.setBackgroundResource(R.xml.edt_text_gray_border);
                            phone.setHint("Enter Phone Number");

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });
                    return;

                }else if (phone.getText().toString().length()<10){
                    phone.setHint("Invalid Phone number");
                    phone.setBackgroundResource(R.xml.btn_delete_card);
                    phone.setHintTextColor(Color.RED);
                    phone.requestFocus();
                    phone.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            phone.setHintTextColor(R.color.colorPrimary);
                            phone.setBackgroundResource(R.xml.edt_text_gray_border);
                            phone.setHint("Enter Phone Number");

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });
                    return;
                }else if(pass.getText().toString().isEmpty()){
                    pass.setHint("Location is required");
                    pass.setBackgroundResource(R.xml.btn_delete_card);
                    pass.setHintTextColor(Color.RED);
                    pass.requestFocus();
                    pass.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            pass.setHintTextColor(R.color.colorPrimary);
                            pass.setBackgroundResource(R.xml.edt_text_gray_border);
                            pass.setHint("Enter Location here");

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                        }
                    });
                    return;


                }else {
                    if (URI == null) {
                        Drawable drawable = this.getResources().getDrawable(R.drawable.logo);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                        // openSelectProfilePictureDialog();
                        Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG).show();
                        uploadImage();
                        //   userInformation();
                        // sendUserData();
                        // finish();
                        // onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG).show();
                        uploadImage();
                        //  userInformation();
                        //   sendUserData();
                        //  finish();
                        //  onBackPressed();
                    }
                    break;
                }
            case R.id.change:
                if (isclickones) {
                    isclickones = false;
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    openBottomSheet();
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    isclickones = true;
                }
                break;
            case R.id.open_images:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    opengallery();
                } else {
                    Toast.makeText(this, "VERSION ISSUE", Toast.LENGTH_SHORT).show();
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.select_camera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    opencamera();
                } else {
                    Toast.makeText(this, "VERSION ISSUE", Toast.LENGTH_SHORT).show();
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;



        }


    }



    private void UploadData() {

    }


    private void uploadImage() {
        userInformation();
        //String slot = categories_type.getText().toString();
        if(fileUri != null)
        {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImages.compress(Bitmap.CompressFormat.JPEG,50,baos);
            byte[] final_image = baos.toByteArray();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("profilePic/" + Objects.requireNonNull(firebaseAuth.getUid()));
            final UploadTask uploadTasks = ref.putBytes(final_image);

            uploadTasks.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();

                    Toast.makeText(Signup.this, "Image Uploaded Successfully" , Toast.LENGTH_LONG).show();
                    getcurrentpicture();

                    progressDialog.dismiss();


                    if(downloadUri.isSuccessful()){
                        generatedFilePath = downloadUri.getResult().toString();
                        System.out.println("## Stored path is "+generatedFilePath);
                        //  Toast.makeText(EditProfile.this, "Image Uploaded Successfully" +generatedFilePath , Toast.LENGTH_LONG).show();

                    }}
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");

                        }
                    });
        }else {

        }
    }

    private void userInformation(){

        String name = firstnaem.getText().toString();
        String phoneno = phone.getText().toString();
        String emails = email.getText().toString();
        String friends = "0";
        String location = pass.getText().toString();
        String created = "yes";




        CreatProfileModel creatProfileModel = new CreatProfileModel(name,phoneno,emails,friends,location,profilepic,created);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(creatProfileModel);


    }

    private void getcurrentpicture() {
        StorageReference storageRef = firebaseStorage.getReference().child("profilePic/"+Objects.requireNonNull(firebaseAuth.getUid()));//reach out to your photo file hierarchically
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("URI", uri.toString()); //check path is correct or not ?
                Picasso.get().load(uri.toString()).into(civ_user);
                profilepic = uri.toString();
                userInformation();
          //      startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle errors
            }
        });
    }


    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Get "User UID" from Firebase > Authentification > Users.
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic"); //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(URI);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup.this, "Error: Uploading profile picture", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Signup.this, "Profile picture uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void opencamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE
                }, PICK_IMAGE_GALLARY_PERMISSION);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

                            /*fileName = System.currentTimeMillis() + ".jpg";
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, IMG_RESULT);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);*/
                // }
            }
        }



    }

    private void opengallery() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PICK_IMAGE_GALLARY_PERMISSION);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, IMG_RESULT);

            }
        } else {

        }
    }

    public String getCompresssedImages(String imagePath) {
//        Bitmap beforeBitmap = BitmapFactory.decodeFile(imagePath);
//        System.out.println("Before Compress Dimension" + beforeBitmap.getWidth() + "-" + beforeBitmap.getHeight());
//        Log.i("Before Compress Dimension", beforeBitmap.getWidth()+"-"+beforeBitmap.getHeight());

        Bitmap afterBitmap = ImageUtility.getInstant().getCompressedBitmap(imagePath);
//        Log.i("After Compress Dimension", afterBitmap.getWidth() + "-" + afterBitmap.getHeight());
        System.out.println("After Compress Dimension" + afterBitmap.getWidth() + "-" + afterBitmap.getHeight());


        return getBitmapImagePath(afterBitmap);
    }


    private String getBitmapImagePath(Bitmap finalBitmap) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = timeStamp + "image.png";
            File sd = getCacheDir();
            File dest = new File(sd, filename);

            FileOutputStream out = new FileOutputStream(dest);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
            out.flush();
            out.close();
            return dest.getAbsolutePath();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private void openBottomSheet () {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            try {

                if (requestCode == IMG_RESULT && resultCode == RESULT_OK) {
                    fileUri = data.getData();
                    String[] FILE = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(fileUri,
                            FILE, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(FILE[0]);
                    ImageDecode = cursor.getString(columnIndex);

                    System.out.println("Image===" + ImageDecode);

                    cursor.close();
                    Imagecode = getCompresssedImages(ImageDecode);
                    civ_user.setImageBitmap(BitmapFactory.decodeFile(Imagecode));
                    compressedImages = (BitmapFactory.decodeFile(Imagecode));
                    System.out.println("ImageAfterCompress===" + ImageDecode);
                    actual_size = new File(fileUri.getPath());

                    super.onActivityResult(requestCode, resultCode, data);
                }
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    ImageDecode = getBitmapImagePath(imageBitmap);

                    civ_user.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));
                    compressedImages = (BitmapFactory.decodeFile(ImageDecode));

                    civ_user.setImageBitmap(imageBitmap);
                }
            } catch (Exception e) {

            }
        }
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
}
