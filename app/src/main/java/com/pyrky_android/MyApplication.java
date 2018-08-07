package com.pyrky_android;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.pyrky_android.dagger.ApiComponent;
import com.pyrky_android.dagger.ApiModule;
import com.pyrky_android.dagger.AppModule;
import com.pyrky_android.dagger.DaggerApiComponent;
import io.fabric.sdk.android.Fabric;

/**
 * Created by thulirsoft on 7/2/18.
 */

public class MyApplication extends Application {
    Context context = this;
    private ApiComponent mApiComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(context);

        mApiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule("http://kavithaimazhai.com/kavithaimazhai/"))
                .build();
    }

    public ApiComponent getNetComponent(){
        return mApiComponent;
    }
}
