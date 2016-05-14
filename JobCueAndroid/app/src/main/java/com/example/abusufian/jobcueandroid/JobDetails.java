package com.example.abusufian.jobcueandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JobDetails extends AppCompatActivity {

    private static String [] name;
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_FIRSTNAME = "firstName";
    private static String job_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        TextView title = (TextView)findViewById(R.id.jobDetails_title);
        TextView description = (TextView)findViewById(R.id.jobDetails_description);
        Button jobapply=(Button)findViewById(R.id.jobapplication);
        Button remove_job = (Button)findViewById(R.id.delete_job);
        LinearLayout layout_request_job = (LinearLayout)findViewById(R.id.layout_request_job);


        Intent i = getIntent();
        Bundle bundle = i.getExtras();
         String []array=null;

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
        final String id = array[2];
        final String flag = array[3];

        job_id = id;

        if(flag.equalsIgnoreCase("True"))
        {
            //Delete button Visible
            Log.d("Empty" , flag + "");
            jobapply.setVisibility(View.GONE);
            remove_job.setVisibility(View.VISIBLE);
            layout_request_job.setVisibility(View.VISIBLE);
            RequestForJob();

        }
        else
        {
            //JobApply Button Visible
            jobapply.setVisibility(View.VISIBLE);
            remove_job.setVisibility(View.GONE);
            layout_request_job.setVisibility(View.GONE);
        }


        jobapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String token=getToken("Token");
            if(token != null) {

                try {
                    invokeWS(id,token);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Token not fetched", Toast.LENGTH_SHORT).show();
            }
            }
        });



    }


    public void RequestForJob()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        String token = getToken("Token");

        client.addHeader("Authorization", "Bearer " + token);

        Log.d("Empty", "Before Success " + token);

        client.get("http://jobcue.herokuapp.com/jobs/" + job_id + "/applications", new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(JSONArray response) {
                super.onSuccess(response);
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                try {
                    Log.d("JOB_JSON", response.toString());

//                    if (response.toString().length() != 0) { //TODO

                        name = new String[10];

                        for (int i = 0; i < name.length; i++) {
//                            JSONObject jo = response.getJSONObject(i);
//
//                            String lastName = jo.getString(KEY_LASTNAME);
//                            String firstName = jo.getString(KEY_FIRSTNAME);
                            name[i] =  " apple ";
                        }

                        name[0] = "Apple";
                        name[1] = "Mango";
                        name[2] = "Strawberry";
                        name[3] = "Jackfruit";

                        String temp = "one ";
                        for(int i = 0; i < name.length; i++)
                        {
                            temp +=  " " + name[i];
                        }

                        Log.d("Empty", temp);

                        ListView listView = (ListView) findViewById(R.id.request_job);//change
                        RequestAdapterClass lv_adapter = new RequestAdapterClass(JobDetails.this,
                                R.layout.list_item_request_job,
                                R.id.usertext, name);
                        listView.setAdapter(lv_adapter);
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "CHETER BAAL ", Toast.LENGTH_LONG).show();
//                    }
                } catch (Exception e) {
                    Log.d("Error", e + "");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                Log.d("erro22r", "failed");
            }
        });

    }



    public void check_user(View view)
    {
        Toast.makeText(getApplicationContext(), "HEllo description", Toast.LENGTH_LONG).show();

//        View parentRow = (View) view.getParent();

//        ListView listView = (ListView) parentRow.getParent();
//        final int position = listView.getPositionForView(parentRow);
//
//        String[] array = new String[4];
//
//        array[0] = subject[position];
//        array[1] = description[position];
//        array[2] = id_job[position];
//        array[3] = "True";
//
//        Intent intent = new Intent(this, JobDetails.class);
//        intent.putExtra("job", array);
//        startActivity(intent);

    }




    public String getToken(String token)
    {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);;
        String fetch_token = preferences.getString(token, "");
        if(!fetch_token.equalsIgnoreCase(""))
        {
            return fetch_token;
        }
        else
        {
            return null;
        }
    }

    public void invokeWS(String id,String new_token) throws UnsupportedEncodingException {


        Toast.makeText(getApplicationContext(), "I am here", Toast.LENGTH_LONG).show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        client.addHeader("Authorization","Bearer "+ new_token );
        Toast.makeText(JobDetails.this, id, Toast.LENGTH_LONG).show();
        Toast.makeText(JobDetails.this, new_token, Toast.LENGTH_LONG).show();


        //client.addHeader("Authorization","Bearer "+ "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NoYW9uLmF1dGgwLmNvbS8iLCJzdWIiOiJ0d2l0dGVyfDMwMjY4NDM0NDMiLCJhdWQiOiJYRHZwYzV5UlcycWhUclJIV2NFMHEwMkZxM2tMSW5DZiIsImV4cCI6MTQ2NTM2MTQyMiwiaWF0IjoxNDYyNzMxNjc2fQ.tQTJWLlWpB0Ihmk46FxpfyAk_Az01xy2X_IVVnkBRc4" );

        ByteArrayEntity entity = new ByteArrayEntity(id.getBytes("UTF-8"));




        client.post("http://jobcue.herokuapp.com/jobs/"+id+"/applications",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);


                Toast.makeText(JobDetails.this, response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                super.onFailure(statusCode, e, errorResponse);
                Toast.makeText(JobDetails.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }


}
