    package com.example.abusufian.jobcueandroid;

    import android.app.DatePickerDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.support.v4.app.DialogFragment;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.EditText;
    import android.widget.Toast;

    import org.apache.http.entity.ByteArrayEntity;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
    import android.widget.DatePicker;
    import android.support.v4.app.FragmentActivity;
    import android.app.Dialog;
    import android.app.DatePickerDialog;

    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.JsonHttpResponseHandler;

    import java.io.UnsupportedEncodingException;
    import java.util.Arrays;
    import java.util.Calendar;

    public class ProfileUpdate extends AppCompatActivity
             {

        EditText fname;
        EditText lname;
        EditText cnumber;
        EditText address;
        EditText city;
        EditText state;
        EditText experience;
        Button update;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile_update);
            String checktoken=getToken("Token");

            if(checktoken==null) {
                Intent i = new Intent(this, Login.class);
                startActivity(i);
            }
            else if (checktoken.equalsIgnoreCase("")) {
                Intent i = new Intent(this, Login.class);
                startActivity(i);
            }
            fname = (EditText) findViewById(R.id.fname);
            lname = (EditText) findViewById(R.id.lname);
            cnumber = (EditText) findViewById(R.id.cnumber);
            address = (EditText) findViewById(R.id.address);
            city = (EditText) findViewById(R.id.city);
            state = (EditText) findViewById(R.id.state);
            experience = (EditText) findViewById(R.id.experience);
            update = (Button) findViewById(R.id.update);




        }

        public void ProUpdate(View view) {
            Toast.makeText(getApplicationContext(), "Button clicked", Toast.LENGTH_LONG).show();
            String userfirstname = fname.getText().toString();
            String userlastname = lname.getText().toString();
            String usercontact = cnumber.getText().toString();
            String useraddress = address.getText().toString();
            String usercity = city.getText().toString();
            String userstate = state.getText().toString();
            String userexpereince = experience.getText().toString();
            JSONObject sendData = new JSONObject();
            try {
                sendData.put("lastName", userlastname);
                sendData.put("firstName", userfirstname);
                sendData.put("streetAddress", useraddress);
                sendData.put("aboutMe", userexpereince);


                JSONObject BirthObj = new JSONObject();
                BirthObj.put("day", 1);
                BirthObj.put("month", 8);
                BirthObj.put("year", 1994);
                sendData.put("dateOfBirth", BirthObj);
                invokeWS(sendData);







            } catch (JSONException e) {
                e.printStackTrace();
           }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


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

                 public void invokeWS(JSONObject params) throws UnsupportedEncodingException {


                     Toast.makeText(getApplicationContext(),params.toString() , Toast.LENGTH_LONG).show();
                     // Make RESTful webservice call using AsyncHttpClient object
                     AsyncHttpClient client = new AsyncHttpClient();

                     String token = getToken("Token");
                     Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();

                         client.addHeader("Authorization", "Bearer " + token);

                     //client.addHeader("Authorization","Bearer "+ "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NoYW9uLmF1dGgwLmNvbS8iLCJzdWIiOiJ0d2l0dGVyfDMwMjY4NDM0NDMiLCJhdWQiOiJYRHZwYzV5UlcycWhUclJIV2NFMHEwMkZxM2tMSW5DZiIsImV4cCI6MTQ2NTM2MTQyMiwiaWF0IjoxNDYyNzMxNjc2fQ.tQTJWLlWpB0Ihmk46FxpfyAk_Az01xy2X_IVVnkBRc4" );

                     ByteArrayEntity entity = new ByteArrayEntity(params.toString().getBytes("UTF-8"));


                     client.put(getApplicationContext(), "http://jobcue.herokuapp.com/users/me", entity, "application/json", new JsonHttpResponseHandler() {
                         @Override
                         public void onSuccess(JSONObject response) {
                             super.onSuccess(response);


                             Toast.makeText(ProfileUpdate.this, response.toString(), Toast.LENGTH_LONG).show();
                         }

                         @Override
                         public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                             super.onFailure(statusCode, e, errorResponse);
                             Toast.makeText(ProfileUpdate.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                         }
                     });


                 }




    }
