package com.example.abusufian.jobcueandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JobDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        TextView title = (TextView)findViewById(R.id.jobDetails_title);
        TextView description = (TextView)findViewById(R.id.jobDetails_description);
        Button jobapply=(Button)findViewById(R.id.jobapplication);


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
        final String id=array[2];
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
