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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.core.UserProfile;
import com.auth0.lock.Lock;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommonProfile extends AppCompatActivity {


    //Posted Job
    public static String[] subject;
    public static String[] description;
    public static String[] id_job;

    //FoundJobs
    public static String[] fsubject;
    public static String[] fdescription;
    public static String[] femployerid;


    private static String[] get_id_job = null;
    public static String[] firstname;
    public static String[] lastname;
    private static ArrayList<String[]> add_data = new ArrayList<String[]>();


    public static final String KEY_SUB = "subject";
    public static final String KEY_DES = "description";
    public static final String KEY_ID = "id";
    public static final String KEY_EMID = "EmployerId";

    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_FIRSTNAME = "firstName";

    private Handler handler = new Handler();
    private  Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_common_profile);
        UserProfile profile = getIntent().getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
        ImageView profileImageView = (ImageView) findViewById(R.id.profile_image);
        if (profile.getPictureURL() != null) {
            ImageLoader.getInstance().displayImage(profile.getPictureURL(), profileImageView);
        }

        intent = new Intent(this, JobDetails.class);
        final LinearLayout fjobs = (LinearLayout) findViewById(R.id.foundjobs);
        final LinearLayout pjobs = (LinearLayout) findViewById(R.id.postedjobs);

        Button foundjobs = (Button) findViewById(R.id.fjobs);
        Button postnewjobs = (Button) findViewById(R.id.postnewjob);
        Button findnewjob=(Button)findViewById(R.id.findnewjob);
        final Intent i = new Intent(this, JobPost.class);
        final Intent findnewerjob = new Intent(this, JobSearch.class);

        postnewjobs.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               startActivity(i);

                                           }
                                       }
        );

        findnewjob.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               startActivity(findnewerjob);

                                           }
                                       }
        );


        foundjobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fjobs.setVisibility(View.VISIBLE);
                pjobs.setVisibility(View.GONE);
                FoundJob();

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

        Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_LONG).show();

        client.addHeader("Authorization", "Bearer " + token);
        String s = ((MyApplication)this.getApplication()).getSomeVariable();
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


        //Log.d("Foo", s);


        client.get("http://jobcue.herokuapp.com/users/"+s+"/employer", new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(JSONArray response) {
                super.onSuccess(response);
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                Log.d("Employer", response.toString());
                try {
                    Log.d("JSONArray", String.valueOf(response));


                    subject = new String[response.length()];
                    description = new String[response.length()];
                    id_job = new String[response.length()];



                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jo= response.getJSONObject(i);



                        subject[i] = jo.getString(KEY_SUB);
                        description[i] = jo.getString(KEY_DES);
                        id_job[i] = jo.getString(KEY_ID);
                    }

                    if (subject.length == 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
                    } else {

                        ListView listView = (ListView) findViewById(R.id.posted_job);
                        ListViewAdapterUserProfile lv_adapter = new ListViewAdapterUserProfile
                                (CommonProfile.this, R.layout.listiteam2, R.id.usertext, subject);
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

    private void FoundJob() {
        AsyncHttpClient client = new AsyncHttpClient();
        String token = getToken("Token");

        Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_LONG).show();

        client.addHeader("Authorization", "Bearer " + token);
        String s = ((MyApplication)this.getApplication()).getJobApplicantId();
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


        //Log.d("Foo", s);


        client.get("http://jobcue.herokuapp.com/users/" + s + "/employee", new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(JSONArray response) {
                super.onSuccess(response);
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();


                try {
                    Log.d("JSONArray", response.toString());


                    fsubject = new String[response.length()];
                    fdescription = new String[response.length()];
                    femployerid = new String[response.length()];


                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jo = response.getJSONObject(i);


                        fsubject[i] = jo.getString(KEY_SUB);
                        fdescription[i] = jo.getString(KEY_DES);
                        femployerid[i] = jo.getString(KEY_EMID);
                    }



                        ListView listView = (ListView) findViewById(R.id.current_jobs);
                        ListViewAdapterForFoundJobs lv_adapter = new ListViewAdapterForFoundJobs
                                (CommonProfile.this, R.layout.foundjobslistiteam, R.id.usertile, fsubject);
                        listView.setAdapter(lv_adapter);

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

    public void JobDes(View view) {

        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        String[] array = new String[4];

        array[0] = fsubject[position];
        array[1] = fdescription[position];
        array[2] = femployerid[position];
        array[3] = "found";


        Intent intent = new Intent(this, JobDetails.class);
        intent.putExtra("job", array);
        startActivity(intent);

    }
    public void Employer(View view) {

        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        String id=femployerid[position];
        Intent intent = new Intent(this,  UserProfileView.class);
        intent.putExtra("job", id);
        startActivity(intent);

    }






}
