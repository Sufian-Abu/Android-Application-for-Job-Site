package com.example.abusufian.jobcueandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    public static String[] subject;
    public static String[] description;
    public static String[] id_job;
    private static String[] get_id_job = null;
    public static String[] firstname;
    public static String[] lastname;
    private static ArrayList<String[]> add_data = new ArrayList<String[]>();


    public static final String KEY_SUB = "subject";
    public static final String KEY_DES = "description";
    public static final String KEY_ID = "id";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_FIRSTNAME = "firstName";

    private Handler handler = new Handler();
    private  Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        intent = new Intent(this, JobDetails.class);
        final LinearLayout fjobs = (LinearLayout) findViewById(R.id.foundjobs);
        final LinearLayout pjobs = (LinearLayout) findViewById(R.id.postedjobs);

        Button foundjobs = (Button) findViewById(R.id.fjobs);
        Button postnewjobs = (Button) findViewById(R.id.postnewjob);
        final Intent i = new Intent(this, JobPost.class);

        postnewjobs.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   startActivity(i);

               }
           }
        );


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
                PostedJob();
            }
        });
    }

    public String getToken(String token) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        ;
        String fetch_token = preferences.getString(token, "");
        if (!fetch_token.equalsIgnoreCase("")) {
            return fetch_token;
        } else {
            return null;
        }
    }

    private void PostedJob() {
        AsyncHttpClient client = new AsyncHttpClient();
        String token = getToken("Token");

        client.addHeader("Authorization", "Bearer " + token);

        client.get("http://jobcue.herokuapp.com/jobs/", new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(JSONArray response) {
                super.onSuccess(response);
                //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                try {
                    Log.d("JSONArray", response.toString());

                    subject = new String[response.length()];
                    description = new String[response.length()];
                    id_job = new String[response.length()];

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jo = response.getJSONObject(i);

                        subject[i] = jo.getString(KEY_SUB);
                        description[i] = jo.getString(KEY_DES);
                        id_job[i] = jo.getString(KEY_ID);
                    }

                    if (subject.length == 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
                    } else {

                        ListView listView = (ListView) findViewById(R.id.posted_job);
                        ListViewAdapterUserProfile lv_adapter = new ListViewAdapterUserProfile
                                (UserProfile.this, R.layout.listiteam2, R.id.usertext, subject);
                        listView.setAdapter(lv_adapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                //Toast.makeText(getContext(), "Error " + statusCode + " " + responseBody, Toast.LENGTH_LONG).show();
                Log.d("erro22r", "failed");
            }
        });
    }

    public void click_button(View view) {

        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        String[] array = new String[4];

        array[0] = subject[position];
        array[1] = description[position];
        array[2] = id_job[position];
        array[3] = "True";

        Intent intent = new Intent(this, JobDetails.class);
        intent.putExtra("job", array);
        startActivity(intent);

    }



}
