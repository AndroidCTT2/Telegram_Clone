package com.mobile.messageclone;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.instacart.library.truetime.TrueTime;
import com.instacart.library.truetime.TrueTimeRx;

import java.io.IOException;
import java.util.concurrent.Executor;

import io.reactivex.schedulers.Schedulers;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }
}
