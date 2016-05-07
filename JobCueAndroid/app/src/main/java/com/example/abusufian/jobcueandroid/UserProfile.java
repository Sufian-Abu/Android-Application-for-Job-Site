package com.example.abusufian.jobcueandroid;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final LinearLayout fjobs = (LinearLayout)findViewById(R.id.foundjobs);
        final LinearLayout pjobs=  (LinearLayout)findViewById(R.id.postedjobs);

        Button foundjobs = (Button) findViewById(R.id.fjobs);


        foundjobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fjobs.setVisibility(View.VISIBLE);
                pjobs.setVisibility(View.GONE);
            }
        });


        Button postedjobs = (Button) findViewById(R.id.pjobs);

        postedjobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fjobs.setVisibility(View.GONE);
                pjobs.setVisibility(View.VISIBLE);
            }
        });
    }



}
