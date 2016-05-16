package com.example.abusufian.jobcueandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class UserProfileView extends AppCompatActivity {

    private static  String id=null;
    ImageView image;
    TextView  name;
    TextView  age;
    RatingBar rating;
    TextView  exp;
    TextView  jobdone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);

         image=(ImageView)findViewById(R.id.image);
         name=(TextView)findViewById(R.id.name);
          age=(TextView)findViewById(R.id.Age);
          rating=(RatingBar)findViewById(R.id.rating);
         exp=(TextView)findViewById(R.id.exp);
         jobdone=(TextView)findViewById(R.id.jobdone);



        Intent i = getIntent();
        Bundle bundle = i.getExtras();


        if(bundle != null)
        {
            id = bundle.get("job").toString();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Information", Toast.LENGTH_LONG).show();
        }
        UserProfile();


    }

    public void UserProfile()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        String token = getToken("Token");

        client.addHeader("Authorization", "Bearer " + token);

        Log.d("Empty", "Before Success " + token);

        client.get("http://jobcue.herokuapp.com/jobs/" + id + "/applications", new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(JSONArray response) {
                super.onSuccess(response);
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.d("Hello",response.toString());
                try {
                    Log.d("JOB_JSON", response.toString());

                    //todo
                   // name.setText("");






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





}
