package arl.chronos.classes;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication get() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
