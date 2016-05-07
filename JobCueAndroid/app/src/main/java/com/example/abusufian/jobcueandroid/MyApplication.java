package com.example.abusufian.jobcueandroid;

import android.app.Application;

import com.auth0.core.Strategies;
import com.auth0.facebook.FacebookIdentityProvider;
import com.auth0.googleplus.GooglePlusIdentityProvider;
import com.auth0.lock.LockContext;
import com.auth0.lock.LockProvider;

import java.util.concurrent.locks.Lock;

/**
 * Created by abusufian on 5/4/2016.
 */
public class MyApplication extends Application implements LockProvider {

    private Lock lock;

    public void onCreate() {
        super.onCreate();
        LockContext.configureLock(
                new com.auth0.lock.Lock.Builder()
                        .loadFromApplication(this)

                        .closable(true)
        );
    }

    @Override
    public com.auth0.lock.Lock getLock() {
        return (com.auth0.lock.Lock) lock;
    }
}