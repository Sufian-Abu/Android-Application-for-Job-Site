package com.example.abusufian.jobcueandroid;

import android.app.Application;

import com.auth0.core.Strategies;
import com.auth0.facebook.FacebookIdentityProvider;
import com.auth0.lock.Lock;
import com.auth0.lock.LockContext;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by abusufian on 5/10/2016.
 */
public class MyApplication extends Application {

    private String someVariable;
    private String JobApplicantId;

    public void onCreate() {
        super.onCreate();

        LockContext.configureLock(
                new Lock.Builder()
                        .loadFromApplication(this)
                        .withIdentityProvider(Strategies.Facebook, new FacebookIdentityProvider(this))
                        .closable(true)
        );
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);
    }

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }

    public String getJobApplicantId()
    {
        return JobApplicantId;
    }

    public void setJobApplicantId(String JobApplicantId) {
        this.JobApplicantId = JobApplicantId;
    }


}
