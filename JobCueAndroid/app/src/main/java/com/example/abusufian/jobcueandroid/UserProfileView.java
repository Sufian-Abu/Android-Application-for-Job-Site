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
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

public class UserProfileView extends AppCompatActivity {

    private static  String id=null;
    private static  String id_job=null;
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
            //id_job=bundle.get("Hello").toString();
            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Information", Toast.LENGTH_LONG).show();
        }
        UserProfile();

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                String rate = (String.valueOf(rating));
                try {
                    RatingSend(rate,id);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });





    }

    public void UserProfile()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        String token = getToken("Token");

        client.addHeader("Authorization", "Bearer " + token);

        Log.d("Empty", "Before Success " + token);
        Toast.makeText(getApplicationContext(), "I AM HERE", Toast.LENGTH_LONG).show();

        client.get("http://jobcue.herokuapp.com/users/" + id, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.d("FALTU",response.toString());
                try {
                    Log.d("AALBAAL", response.toString());

                    String demo="";
                    String city="";
                    String state="";
                    String aboutme="";
                    String birth="";


                    for (int i = 0; i < response.length(); i++) {

                        String lastName = response.getString("lastName");
                        String firstName = response.getString("firstName");
                        demo=firstName+" "+lastName;
                        city=response.getString("city");
                        state=response.getString("state");
                        aboutme=response.getString("aboutMe");
                        birth=response.getString("dateOfBirth");
                        Log.d("FLNAME",demo);
                        Log.d("FLNAME",aboutme);
                        Log.d("FLNAME",birth);
                        Log.d("FLNAME",city);

                    }

                    //todo
                    name.setText(demo);
                    exp.setText(aboutme);
                    age.setText(birth);






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

    public void RatingSend(String rate,String userid) throws UnsupportedEncodingException
    {
        Toast.makeText(getApplicationContext(),"Rating"+rate+" "+"UserID"+userid,Toast.LENGTH_LONG).show();
        JSONObject sendData = new JSONObject();
        try {
            sendData.put("UserId", userid);
            sendData.put("rating", 10);
            sendData.put("feedback", "Good");
            sendData.put("JobId", "2");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String token = getToken("Token");

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

        ByteArrayEntity entity = new ByteArrayEntity(sendData.toString().getBytes("UTF-8"));

        Toast.makeText(getApplicationContext(),"Entity Printing",Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),entity.toString(),Toast.LENGTH_LONG).show();

        client.post(getApplicationContext(), "http://jobcue.herokuapp.com/users/" + userid + "/reviews", entity, "application/json", new JsonHttpResponseHandler() {
            // client.put(getApplicationContext(),"http://jobcue.herokuapp.com/jobs/6/applications/25",entity,"application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {
                super.onSuccess(response);
                Log.d("Ratingjaena", response.toString());

                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                super.onFailure(statusCode, e, errorResponse);
                Toast.makeText(getApplicationContext(), errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });





    }





}
