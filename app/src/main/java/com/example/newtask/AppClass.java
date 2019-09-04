package com.example.newtask;

import android.app.Application;

public class AppClass extends Application {
    private static AppClass appClass;

    public static synchronized AppClass getObject() {
        return appClass;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appClass = this;
    }
}
