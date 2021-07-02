package com.app.videonewsmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class aboutus extends AppCompatActivity {
    TextView viewtoc,viewrefundpolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        viewtoc=findViewById(R.id.viewtoc);
        viewrefundpolicy=findViewById(R.id.viewrefundpolicy);
        viewtoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aboutus.this,termsandconditions.class));
            }
        });
        viewrefundpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aboutus.this,refundpolicypage.class));
            }
        });
    }
}
