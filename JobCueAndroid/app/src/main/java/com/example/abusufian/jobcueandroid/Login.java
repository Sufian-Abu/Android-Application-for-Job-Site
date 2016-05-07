package com.example.abusufian.jobcueandroid;

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

import com.auth0.core.Token;
import com.auth0.lock.Lock;
import com.auth0.lock.LockActivity;

import static com.auth0.lock.Lock.AUTHENTICATION_ACTION;

public class Login extends AppCompatActivity {

    private LocalBroadcastManager broadcastManager;

    private BroadcastReceiver authenticationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Intent newIntent = new Intent(Login.this, ProfileActivity.class);
            UserProfile profile = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
            Token token = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_TOKEN_PARAMETER);
            newIntent.putExtras(intent);
            startActivity(newIntent);
        }
    };

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
