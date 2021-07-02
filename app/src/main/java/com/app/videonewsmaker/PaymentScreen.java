package com.app.videonewsmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PaymentScreen extends AppCompatActivity implements PaymentResultListener {


    String TAG = "Payment Error";
    TextView textView;
    private int amounts;
    Button btn;
    String Date,lic;
    FirebaseUser user;
    private FirebaseAuth userd;

    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;

    private String names, lastname, mobile, clan, emails, wallet, Role, Joined, Played, Wins;
    private String udid, novideo, product_id, name, email, friends, location, created, phoneno, profilepic, license, licensedate,currdeviceuid,joindate;
    String video1txt, days1txt, video2txt, days2txt, video3txt, days3txt, buy1txt, buy2txt, buy3txt, price1txt, price2txt, price3txt;
    TextView video1, days1, video2, days2, video3, days3;
    Button buy1, buy2, buy3;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    private int Walletmoney;
    private int Walletmoney2;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
        Checkout.preload(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        currdeviceuid= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Bundle b = getIntent().getExtras();
        assert b != null;
        name = b.getString("name");
        phoneno = b.getString("phno");
        email = b.getString("email");
        friends = b.getString("friends");
        location = b.getString("location");
        created = b.getString("created");
        udid = b.getString("udid");
        product_id = b.getString("product_id");
        novideo = b.getString("videos");
        license = b.getString("license");
        licensedate = b.getString("licensedate");
        video1txt = b.getString("video1");
        days1txt = b.getString("days1");
        joindate = b.getString("joindate");
        video2txt = b.getString("video2");
        days2txt = b.getString("days2");
        video3txt = b.getString("video3");
        days3txt = b.getString("days3");
        price1txt = b.getString("price1");
        price2txt = b.getString("price2");
        price3txt = b.getString("price3");
        amounts = b.getInt("money");
        lic = b.getString("lic");

        startpayment();

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        //  databaseReference = FirebaseDatabase.getInstance().getReference("users");
        // FirebaseUser user = firebaseAuth.getCurrentUser();
        //  DatabaseReference databaseMyprofile = firebaseDatabase.getReference("users/" + Objects.requireNonNull(firebaseAuth.getUid()));
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

    }




    private void startpayment() {

        final Activity activity = this;
        Checkout checkout = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", name);

            options.put("currency", "INR");
            options.put("amount", amounts);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phoneno);

            checkout.open(activity, options);


        } catch (JSONException e) {
            Log.e(TAG, "Error starting RazorPay Checkout", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successfully Completed" + s, Toast.LENGTH_LONG).show();
        try {
            buybtn();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed " + s, Toast.LENGTH_LONG).show();
        onBackPressed();

    }


    private void buybtn() throws NoSuchAlgorithmException {
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
        String uvideos = novideo;
        String ujoindate = joindate;
        String uprofilepic = profilepic;
        String ulicense = lic;
        String ulicensedate = Date;
        userd = FirebaseAuth.getInstance();
        user = userd.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (uproduct_id == null) {
            CreatProfileModel creatProfileModel = new CreatProfileModel(username, uphoneno, uemail, ufriends, ulocation, uprofilepic, ucreated, currdeviceuid, getproductid(currdeviceuid, user.getUid()), uvideos, ulicense,ulicensedate,ujoindate);
            DatabaseReference db = firebaseDatabase.getReference("users/");
            db.child(user.getUid()).setValue(creatProfileModel);
            Toast.makeText(PaymentScreen.this, "Congratulations! You have Bought the App!", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(PaymentScreen.this,dashboard.class);
            startActivity(intent);
        } else {
            Toast.makeText(PaymentScreen.this, "You have already Bought the App!", Toast.LENGTH_SHORT).show();
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
}
