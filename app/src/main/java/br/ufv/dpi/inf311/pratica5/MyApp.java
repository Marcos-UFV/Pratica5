package br.ufv.dpi.inf311.pratica5;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Context context;
    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return MyApp.context;
    }
}
