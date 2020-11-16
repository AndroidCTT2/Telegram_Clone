package com.mobile.messageclone.SignIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.instacart.library.truetime.TrueTimeRx;
import com.mobile.messageclone.R;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private NavGraph navGraph;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        TrueTimeRx.build()
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .subscribe(date -> {
                    Log.v("TAG", "TrueTime was initialized and we have a time: " + date);
                }, throwable -> {
                    throwable.printStackTrace();
                });

        navController=Navigation.findNavController(this,R.id.nav_host_fragment);
        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null)
        {
            navController.navigate(R.id.action_signIn_to_chatActivity);
            finish();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}