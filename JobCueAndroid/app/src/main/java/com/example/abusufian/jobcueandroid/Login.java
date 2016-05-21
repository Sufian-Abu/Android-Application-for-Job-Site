package com.example.abusufian.jobcueandroid;

import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.auth0.core.Token;
import com.auth0.core.UserProfile;
import com.auth0.lock.Lock;
import com.auth0.lock.LockActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import static com.auth0.lock.Lock.AUTHENTICATION_ACTION;

public class Login extends AppCompatActivity {

    private LocalBroadcastManager broadcastManager;

    private BroadcastReceiver authenticationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Intent newIntent = new Intent(Login.this, CommonProfile.class);
            UserProfile profile = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
            Token token = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_TOKEN_PARAMETER);




            String toAddToken =  token.getIdToken();

            Log.d("Token", token+"");
            Log.d("Token", toAddToken);

            if(addToken(toAddToken))
            {
                Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_LONG).show();
            }


            newIntent.putExtras(intent);
            startActivity(newIntent);
        }
    };

    private boolean addToken(String token)
    {
        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Token", token);
        Log.d("Token", token);
        boolean flag = editor.commit();

        if(flag == true)
        {
            return true;
        }
        else {
            return false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, LockActivity.class));
            }
        });
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(authenticationReceiver, new IntentFilter(AUTHENTICATION_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(authenticationReceiver);
    }

}