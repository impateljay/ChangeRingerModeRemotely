package com.jay.changeringermoderemotely;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

/**
 * Created by jaypatel on 03/11/17
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Configuration dbConfiguration = new Configuration.Builder(this)
                .setDatabaseName("CRMR.db")
                .setDatabaseVersion(1)
                .addModelClass(Contact.class)
                .create();
        ActiveAndroid.initialize(dbConfiguration);
    }
}
