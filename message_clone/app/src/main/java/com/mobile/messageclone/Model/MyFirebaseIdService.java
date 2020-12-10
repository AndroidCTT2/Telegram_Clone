package com.mobile.messageclone.Model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Demo",refreshToken);
       // String refreshToken=firebaseUser.getIdToken(true);
        if(firebaseUser != null){
           updateToken(refreshToken);
        }
    }
    private void updateToken(String refreshToken){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("USER").child(firebaseUser.getUid()).child("DeviceToken").setValue(refreshToken);
    }
}
