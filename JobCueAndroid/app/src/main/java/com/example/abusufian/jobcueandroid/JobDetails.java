package com.example.abusufian.jobcueandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class JobDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        TextView title = (TextView)findViewById(R.id.jobDetails_title);
        TextView description = (TextView)findViewById(R.id.jobDetails_description);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        String []array;

        if(bundle != null)
        {
            array = (String[]) bundle.get("job");
            title.setText(array[0]);
            description.setText(array[1]);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Information", Toast.LENGTH_LONG).show();
        }





    }
}
