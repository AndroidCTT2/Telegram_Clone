package com.mobile.messageclone.Ulti;

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
        FirebaseDatabase.getInstance().getReference().child("USER").keepSynced(true);

        Runnable runnablen=new Runnable() {
            @Override
            public void run() {
                TrueTimeRx.build()
                        .initializeRx("time.google.com")
                        .subscribeOn(Schedulers.io())
                        .subscribe(date -> {
                            Log.v("TAG", "TrueTime was initialized and we have a time: " + date);
                            return;
                        }, throwable -> {
                            throwable.printStackTrace();
                        });
            }
        };
        Thread thread=new Thread(runnablen);
        thread.start();



    }
}
