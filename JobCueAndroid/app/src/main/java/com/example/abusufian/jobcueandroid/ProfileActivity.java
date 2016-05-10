package com.example.abusufian.jobcueandroid;

/**
 * Created by abusufian on 5/4/2016.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.auth0.core.UserProfile;
import com.auth0.lock.Lock;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ProfileActivity extends AppCompatActivity {

    private static final String SAMPLE_API_URL = "http://jobcue.herokuapp.com/callback";
    private static final String TAG = ProfileActivity.class.getName();

    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
//        UserProfile profile = getIntent().getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
//        TextView greetingTextView = (TextView) findViewById(R.id.welcome_message);
//        greetingTextView.setText("Welcome " + profile.getName());
//        ImageView profileImageView = (ImageView) findViewById(R.id.profile_image);
//        if (profile.getPictureURL() != null) {
//            ImageLoader.getInstance().displayImage(profile.getPictureURL(), profileImageView);
//        }
//
//        client = new AsyncHttpClient();
//        client.setMaxRetriesAndTimeout(0, 5000);
//        Button callAPIButton = (Button) findViewById(R.id.call_api_button);
//        callAPIButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callAPI();
//            }
//        });
    }

//    private void callAPI() {
//        client.get(this, SAMPLE_API_URL, new AsyncHttpResponseHandler() {
//
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                Log.v(TAG, "We got the secured data successfully");
//                showAlertDialog(ProfileActivity.this, "We got the secured data successfully");
//            }
//
//
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                Log.e(TAG, "Failed to contact API", error);
//                showAlertDialog(ProfileActivity.this, "Please download the API seed so that you can call it.");
//            }
//        });
//    }
//
//    public static AlertDialog showAlertDialog(Context context, String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder
//                .setMessage(message)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//        return builder.show();
//    }
}
