package com.example.abusufian.jobcueandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class JobPost extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;


    EditText jtile;
    EditText jsalary;
    EditText jcity;
    EditText jstate;
    EditText jcontact;
    EditText jdescription;
    Button jobpost;
    TextView errorMsg;
    private double currentLatitude;
    private double currentLongitude;
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);


        jtile = (EditText) findViewById(R.id.jobtitle);
        jsalary = (EditText) findViewById(R.id.salary);
        jcity = (EditText) findViewById(R.id.city);
        jstate = (EditText) findViewById(R.id.state);
        jcontact = (EditText) findViewById(R.id.contact);
        jdescription = (EditText) findViewById(R.id.description);
        jobpost = (Button) findViewById(R.id.jpost);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            locationManager.requestLocationUpdates(provider, 20000, 1, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_job, menu);
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

            Intent i = new Intent(this,ProfileUpdate.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }


    public void JobPost(View view) {
        Toast.makeText(getApplicationContext(), "Button clicked", Toast.LENGTH_LONG).show();
        String jobt = jtile.getText().toString();
        String jdes = jdescription.getText().toString();


        JSONObject sendData = new JSONObject();
        try {
            sendData.put("subject", jobt);
            sendData.put("description", jdes);

            JSONObject locationoObj = new JSONObject();
            locationoObj.put("lat", currentLatitude);
            locationoObj.put("lon", currentLongitude);

            sendData.put("location", locationoObj);

            String[] tags = {"tag1", "tag2"};
            JSONArray tagsJson = new JSONArray(Arrays.asList(tags));
            sendData.put("tags", tagsJson);
            invokeWS(sendData);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void invokeWS(JSONObject params) throws UnsupportedEncodingException {


        Toast.makeText(getApplicationContext(), "I am here", Toast.LENGTH_LONG).show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NoYW9uLmF1dGgwLmNvbS8iLCJzdWIiOiJ0d2l0dGVyfDE5NjA4Njc5MCIsImF1ZCI6IlhEdnBjNXlSVzJxaFRyUkhXY0UwcTAyRnEza0xJbkNmIiwiZXhwIjoxNDYxNjQxMjQzLCJpYXQiOjE0NjE2MDUyNDN9.6ZKIchXKW4g7LfWzZx89MhKN2WXhS9GnuptfUk5RtI0");

        ByteArrayEntity entity = new ByteArrayEntity(params.toString().getBytes("UTF-8"));

        client.post(JobPost.this, "http://jobcue.herokuapp.com/jobs/", entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);

                //Response ta jsonobject hoye asbe
                // Mobile e test kore dekbo?
                Toast.makeText(JobPost.this, response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                super.onFailure(statusCode, e, errorResponse);
            }
        });


    }

    public void setDefaultValues() {
        jtile.setText("");
        jdescription.setText("");

    }
}
