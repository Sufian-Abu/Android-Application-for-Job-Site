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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class JobDetails extends AppCompatActivity {

    private static String [] name;
    private static String []userid;
    private static String []jobid;
//    public static final String KEY_LASTNAME = "lastName";
//    public static final String KEY_FIRSTNAME = "firstName";
//    public static final String KEY_ID="id";

    public static String job_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        TextView title = (TextView)findViewById(R.id.jobDetails_title);
        TextView description = (TextView)findViewById(R.id.jobDetails_description);
        Button jobapply=(Button)findViewById(R.id.jobapplication);
        Button remove_job = (Button)findViewById(R.id.delete_job);
        Button update_job=(Button)findViewById(R.id.update_job);
        LinearLayout layout_request_job = (LinearLayout)findViewById(R.id.layout_request_job);



        Intent i = getIntent();
        Bundle bundle = i.getExtras();
         String []array=null;
        String  []foundjobs=null;

        //Problem is here for foundJobs

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
            update_job.setVisibility(View.VISIBLE);
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

    public void RemoveJob(View view)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        String token = getToken("Token");
        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();

        if(token != null) {
            //String new_token = token;
            String new_token=token;
            client.addHeader("Authorization","Bearer "+ new_token );
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Token not fetched", Toast.LENGTH_SHORT).show();
        }

        client.delete(getApplicationContext(),"http://jobcue.herokuapp.com/jobs/"+job_id, new JsonHttpResponseHandler() {
        @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);


                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                super.onFailure(statusCode, e, errorResponse);
                Toast.makeText(getApplicationContext(), errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });



    }

    public void UpdateJob(View view)
    {
        Intent i=new Intent(this,JobPost.class);
        i.putExtra("Flag","True");
        i.putExtra("jobid",job_id);
        startActivity(i);




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
                Log.d("Hello",response.toString());
                try {
                    Log.d("JOB_JSON", response.toString());

//                    if (response.toString().length() != 0) { //TODO

                        name = new String[response.length()];
                       userid= new String[response.length()];
                       jobid=  new String[response.length()];

                        for (int i = 0; i < name.length; i++) {
                            JSONObject jo = response.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jo.toString(),Toast.LENGTH_LONG).show();

                            JSONObject ob=jo.getJSONObject("User");

                            String lastName = ob.getString("lastName");
                            String firstName = ob.getString("firstName");
                            String demo=lastName+firstName;
                            name[i]=demo;
                            userid[i]=ob.getString("id");
                            jobid[i]=jo.getString("JobId");

                        }







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


        View parentRow = (View) view.getParent();

        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        String id=userid[position];
        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();


        Intent intent = new Intent(this, UserProfileView.class);
        intent.putExtra("job", id);
        startActivity(intent);

    }

    public void check_reject(View view) throws UnsupportedEncodingException {
        Toast.makeText(getApplicationContext(), "HEllo Reject", Toast.LENGTH_LONG).show();

        View parentRow = (View) view.getParent();

        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        String user_id=userid[position];
        String job_id=jobid[position];
//        Toast.makeText(getApplicationContext(),user_id , Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), job_id, Toast.LENGTH_LONG).show();
        boolean test=false;
        try {
            CheckStatusJob(job_id,user_id,test);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }




    }
    public void check_accept(View view) throws UnsupportedEncodingException {


        View parentRow = (View) view.getParent();

        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        String user_id=userid[position];
        String job_id=jobid[position];
//        Toast.makeText(getApplicationContext(),user_id , Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), job_id, Toast.LENGTH_LONG).show();


        boolean test=true;
        try {
            CheckStatusJob(job_id,user_id,test);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void CheckStatusJob(String jobid,String userid,boolean test) throws UnsupportedEncodingException {
        Toast.makeText(getApplicationContext(), jobid, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_SHORT).show();

        String status=null;
        if(test)
        {
            status="accepted";
        }
        else
        {

            status="rejected";

        }

        JSONObject sendData = new JSONObject();
        try {
            sendData.put("status", status);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();

        String token = getToken("Token");
      // Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
        Log.d("TokenShow",token);

        if(token != null) {
            //String new_token = token;
            String new_token=token;
            client.addHeader("Authorization","Bearer "+ new_token );
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Token not fetched", Toast.LENGTH_SHORT).show();
        }
        //client.addHeader("Authorization","Bearer "+ "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NoYW9uLmF1dGgwLmNvbS8iLCJzdWIiOiJ0d2l0dGVyfDMwMjY4NDM0NDMiLCJhdWQiOiJYRHZwYzV5UlcycWhUclJIV2NFMHEwMkZxM2tMSW5DZiIsImV4cCI6MTQ2NTM2MTQyMiwiaWF0IjoxNDYyNzMxNjc2fQ.tQTJWLlWpB0Ihmk46FxpfyAk_Az01xy2X_IVVnkBRc4" );

        ByteArrayEntity entity = new ByteArrayEntity(sendData.toString().getBytes("UTF-8"));

        //Toast.makeText(getApplicationContext(),sendData.toString(),Toast.LENGTH_LONG).show();

        client.put(getApplicationContext(),"http://jobcue.herokuapp.com/jobs/" + jobid + "/applications/" + userid, entity, "application/json", new JsonHttpResponseHandler() {
       // client.put(getApplicationContext(),"http://jobcue.herokuapp.com/jobs/1/applications/3",entity,"application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);


                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                super.onFailure(statusCode, e, errorResponse);
                Toast.makeText(getApplicationContext(), errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });






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
                try {
                    String UserId=response.getString("UserId");
                    ((MyApplication) getApplication()).setJobApplicantId(UserId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                super.onFailure(statusCode, e, errorResponse);
                Toast.makeText(JobDetails.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }


}
