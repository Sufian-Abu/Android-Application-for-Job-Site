    package com.example.abusufian.jobcueandroid;

    import android.annotation.TargetApi;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Build;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;

    import com.loopj.android.http.AsyncHttpClient;
    import com.loopj.android.http.JsonHttpResponseHandler;

    import org.apache.http.Header;
    import org.apache.http.entity.ByteArrayEntity;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.io.UnsupportedEncodingException;
    import java.util.Arrays;

    import android.content.Context;
    import android.location.Location;
    import android.location.LocationListener;
    import android.location.LocationManager;

    public class JobPost extends AppCompatActivity implements LocationListener {

        String provider;
        public static String flag="false";
        public static String jobid="";



        EditText jtile;
        EditText jsalary;
        EditText jcity;
        EditText jstate;
        EditText jcontact;
        EditText jdescription;
        Button jobpost;

        TextView errorMsg;
        private double lat;
        private double longt;
        private static AsyncHttpClient client = new AsyncHttpClient();

        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location = null; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;


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




            //Location works starts here
            getCurrentLocation(getApplicationContext());

            lat = getLatitude();
            longt = getLongitude();

            String checktoken=getToken("Token");

            if(checktoken==null) {
                Intent i = new Intent(this, Login.class);
                startActivity(i);
            }
            else if (checktoken.equalsIgnoreCase("")) {
                Intent i = new Intent(this, Login.class);
                startActivity(i);
            }

            Bundle bundle=getIntent().getExtras();

            if(bundle!=null)
            {
                 flag=bundle.getString("Flag");
                 jobid=bundle.getString("jobid");
            }

            if(flag.equalsIgnoreCase("True"))
            {
                // Need to fetch data from server using job id
                //update function call
                //get Data from server
                //set data to editText
                //jtile.setText("");
                AsyncHttpClient client = new AsyncHttpClient();
                String token = getToken("Token");

                client.addHeader("Authorization", "Bearer " + token);

                Log.d("Empty", "Before Success " + token);

                client.get("http://jobcue.herokuapp.com/jobs/" + jobid , new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(JSONArray response) {
                        super.onSuccess(response);
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        Log.d("Hello",response.toString());
                        try {
                            Log.d("JOB_JSON", response.toString());
                               for (int i = 0; i < response.length(); i++) {
                                JSONObject jo = response.getJSONObject(i);
                                String sub=jo.getString("subject");
                                String title=jo.getString("description");
                                   Toast.makeText(getApplicationContext(), sub, Toast.LENGTH_LONG).show();
                                   Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
                                   jtile.setText(sub);
                                   jdescription.setText(title);



                            }

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

            jobpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(flag.equalsIgnoreCase("True"))
                    {
                        // Update er post e click korbo

                        UpdateJobPost();


                    }
                    else
                    {

                        JobPost();

                    }

                }
            });












        }


        @TargetApi(Build.VERSION_CODES.M)
        private void getCurrentLocation(Context mContext) {

            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        /**
         * Function to get latitude
         * */
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
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



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }


        public void JobPost(){
            Toast.makeText(getApplicationContext(), "Button clicked", Toast.LENGTH_LONG).show();
            String jobt = jtile.getText().toString();
            String jdes = jdescription.getText().toString();


            JSONObject sendData = new JSONObject();
            try {
                sendData.put("subject", jobt);
                sendData.put("description", jdes);

                JSONObject locationoObj = new JSONObject();
                locationoObj.put("lat", lat);
                locationoObj.put("lon", longt);

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


            Toast.makeText(getApplicationContext(), "I am here", Toast.LENGTH_LONG).show();
            // Make RESTful webservice call using AsyncHttpClient object
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
            //client.addHeader("Authorization","Bearer "+ "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NoYW9uLmF1dGgwLmNvbS8iLCJzdWIiOiJ0d2l0dGVyfDMwMjY4NDM0NDMiLCJhdWQiOiJYRHZwYzV5UlcycWhUclJIV2NFMHEwMkZxM2tMSW5DZiIsImV4cCI6MTQ2NTM2MTQyMiwiaWF0IjoxNDYyNzMxNjc2fQ.tQTJWLlWpB0Ihmk46FxpfyAk_Az01xy2X_IVVnkBRc4" );

            ByteArrayEntity entity = new ByteArrayEntity(params.toString().getBytes("UTF-8"));

            client.post(JobPost.this, "http://jobcue.herokuapp.com/jobs/", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);


                    Toast.makeText(JobPost.this, response.toString(), Toast.LENGTH_LONG).show();
                    try {
                        String Employerid=response.getString("EmployerId");
                        ((MyApplication) getApplication()).setSomeVariable(Employerid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }


                @Override
                public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                    super.onFailure(statusCode, e, errorResponse);
                    Toast.makeText(JobPost.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                }
            });





        }

        public void setDefaultValues() {
            jtile.setText("");
            jdescription.setText("");

        }

        public void UpdateJobPost()
        {
            Toast.makeText(getApplicationContext(), "Button clicked", Toast.LENGTH_LONG).show();
            String jobt = jtile.getText().toString();
            String jdes = jdescription.getText().toString();


            JSONObject sendData = new JSONObject();
            try {
                sendData.put("subject", jobt);
                sendData.put("description", jdes);

                JSONObject locationoObj = new JSONObject();
                locationoObj.put("lat", lat);
                locationoObj.put("lon", longt);

                sendData.put("location", locationoObj);

                String[] tags = {"tag1", "tag2"};
                JSONArray tagsJson = new JSONArray(Arrays.asList(tags));
                sendData.put("tags", tagsJson);
                UpdateJobPostData(sendData);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        public void UpdateJobPostData(JSONObject params) throws UnsupportedEncodingException {


            Toast.makeText(getApplicationContext(), "I am here", Toast.LENGTH_LONG).show();
            // Make RESTful webservice call using AsyncHttpClient object
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
            //client.addHeader("Authorization","Bearer "+ "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3NoYW9uLmF1dGgwLmNvbS8iLCJzdWIiOiJ0d2l0dGVyfDMwMjY4NDM0NDMiLCJhdWQiOiJYRHZwYzV5UlcycWhUclJIV2NFMHEwMkZxM2tMSW5DZiIsImV4cCI6MTQ2NTM2MTQyMiwiaWF0IjoxNDYyNzMxNjc2fQ.tQTJWLlWpB0Ihmk46FxpfyAk_Az01xy2X_IVVnkBRc4" );

            ByteArrayEntity entity = new ByteArrayEntity(params.toString().getBytes("UTF-8"));

            client.put(JobPost.this, "http://jobcue.herokuapp.com/jobs/" + jobid, entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);


                    Toast.makeText(JobPost.this, response.toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                    super.onFailure(statusCode, e, errorResponse);
                    Toast.makeText(JobPost.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                }
            });


        }


    }
