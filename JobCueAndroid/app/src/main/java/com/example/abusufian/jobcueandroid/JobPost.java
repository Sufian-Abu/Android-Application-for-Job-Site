package com.example.abusufian.jobcueandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;
import android.widget.Toast;

public class JobPost extends AppCompatActivity {


    EditText jtile;
    EditText jsalary;
    EditText jcity;
    EditText jstate;
    EditText jcontact;
    EditText jdescription;
    Button   jobpost;
    TextView errorMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);
        jtile=(EditText)findViewById(R.id.jobtitle);
        jsalary=(EditText)findViewById(R.id.salary);
        jcity=(EditText)findViewById(R.id.city);
        jstate=(EditText)findViewById(R.id.state);
        jcontact=(EditText)findViewById(R.id.contact);
        jdescription=(EditText)findViewById(R.id.description);
        jobpost=(Button)findViewById(R.id.jpost);



    }


    public void JobPost (View view)
    {
        Toast.makeText(getApplicationContext(), "Button clicked", Toast.LENGTH_LONG).show();
        String jobt=jtile.getText().toString();
        String jdes=jdescription.getText().toString();

        RequestParams params = new RequestParams();
        params.put("subject",jobt);
        params.put("description",jdes);
        params.put("lat","1");
        params.put("lon","2");
        invokeWS(params);

    }

    public void invokeWS(RequestParams params){

        Toast.makeText(getApplicationContext(), "I am here", Toast.LENGTH_LONG).show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NoYW9uLmF1dGgwLmNvbS8iLCJzdWIiOiJ0d2l0dGVyfDE5NjA4Njc5MCIsImF1ZCI6IlhEdnBjNXlSVzJxaFRyUkhXY0UwcTAyRnEza0xJbkNmIiwiZXhwIjoxNDYxNTM4MDgzLCJpYXQiOjE0NjE1MDIwODN9.zPHBb07MJEHrJFbb8ur3w6KqgWj_McFAeYOEYwKP4Tk");

        client.get("http://jobcue.herokuapp.com/jobs/", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            public void onSuccess(String response) {
                // Hide Progress Dialog
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("OK")) {
                        // Set Default Values for Edit View controls
                        setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else {
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();


                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void setDefaultValues(){
        jtile.setText("");
        jdescription.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.uprofile) {

            Intent i=new Intent(this,ProfileUpdate.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
