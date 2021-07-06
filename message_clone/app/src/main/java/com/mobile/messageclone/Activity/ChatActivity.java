package com.mobile.messageclone.Activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.mobile.messageclone.Model.CallInfor;
import com.mobile.messageclone.R;
import com.mobile.messageclone.ViewModel.ChatViewModel;
import com.mobile.messageclone.fragment.Chat.OnGetDataListener;
import com.mobile.messageclone.Ulti.ActivityUlti;
import com.mobile.messageclone.fragment.VoiceCall.VoiceCallScreenFragment;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements ActivityUlti {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView displayUserName;
    private TextView displayUserPhoneNumber;
    private CircularImageView ProfilePicture;


    private Boolean HaveInternet;
    private boolean CheckInternetFlag=true;
    private Toolbar toolbar;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Task<String> firebaseMessaging;
    private String deviceToken;
    private ValueEventListener changeImageListener;
    private ChatViewModel chatViewModel;

    private Call call;
    private SinchClient sinchClient;
    private VoiceCallScreenFragment voiceCallScreenFragment;
    private String callerName;
    private String receiverName;
    private int isCaller;

    public static final String STATUS_OFFLINE="Offline";
    public static final String STATUS_ONLINE="Online";


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser()!=null)
        {
            UpdateStatus(STATUS_ONLINE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        setContentView(R.layout.activity_main_menu);

        chatViewModel =new ViewModelProvider(this).get(ChatViewModel.class);

        firebaseAuth=FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mAppBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawer).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        firebaseDatabase=FirebaseDatabase.getInstance();
        /*
        firebaseMessaging = FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if(task.isSuccessful()==false){
                        Log.d("Fetching FCM registration token failed", task.getException().toString());
                        return;
                    }
                    deviceToken=task.getResult();

                    Log.d("Token message: ", deviceToken);
                    Toast.makeText(ChatActivity.this, deviceToken, Toast.LENGTH_SHORT).show();
                    firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("DeviceToken").push().setValue(deviceToken);

                }
            });
        */


        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("CurrentChatID").setValue("");



        navigationView.getHeaderView(0);

        getSupportActionBar().setTitle(chatViewModel.titleBar.getValue());


        ProfilePicture=navigationView.getHeaderView(0).findViewById(R.id.ProfilePicture);

        ProfilePicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


        displayUserName=navigationView.getHeaderView(0).findViewById(R.id.displayUserName);
        displayUserPhoneNumber=navigationView.getHeaderView(0).findViewById(R.id.displayPhoneNumber);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        displayUserName.setText(user.getDisplayName());
        displayUserPhoneNumber.setText(user.getPhoneNumber());

        String displayName=user.getDisplayName();
        String firstname=String.valueOf(displayName.charAt(0));
        String lastname="";
        for (int i=displayName.length()-1;i>=0;i--)
        {
            if (displayName.charAt(i)!=' ') {
                continue;
            }
            else
            {
                lastname=String.valueOf(displayName.charAt(i+1));
                break;
            }

        }


        String finalLastname = lastname;

        changeImageListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()==true)
                {
                    String url=snapshot.getValue(String.class);
                    Log.d("Dowload",url);
                    ProfilePicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    Glide.with(getBaseContext()).load(url).into(ProfilePicture);
                    chatViewModel.UserProfileImageUrl.setValue(url);
                }
                else
                {
                    ProfilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap(firstname.toUpperCase()+ finalLastname.toUpperCase(),60,Color.WHITE));
                    Log.d("Dowload","NoImage");
                    chatViewModel.UserProfileImageUrl.setValue("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("ProfileImg").addValueEventListener(changeImageListener);

        chatViewModel.titleBar.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                getSupportActionBar().setTitle(chatViewModel.titleBar.getValue());
            }
        });

        chatViewModel.subtitleBar.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                getSupportActionBar().setSubtitle(chatViewModel.subtitleBar.getValue());
            }
        });


        ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragment_home_to_bio);
            }
        });

        chatViewModel.IsHideAppBar.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (chatViewModel.IsHideAppBar.getValue()==false)
                {

                }
                else
                {
                    getSupportActionBar().hide();
                    chatViewModel.IsHideAppBar.setValue(false);
                }
            }
        });

        chatViewModel.IsHideNavBar.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (chatViewModel.IsHideNavBar.getValue()==false)
                {

                }
                else
                {
                    mAppBarConfiguration.getOpenableLayout().close();
                }
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (CheckInternetFlag) {
                    try {
                        Thread.sleep(100);
                        HaveInternet=hasActiveInternetConnection(getApplicationContext());
                        if (HaveInternet==true)
                        {

                        }
                        else if (HaveInternet==false)
                        {


                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("STATUS").child("State").onDisconnect().setValue(STATUS_OFFLINE);
        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("STATUS").child("Time").onDisconnect().setValue(ServerValue.TIMESTAMP);



        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .applicationKey("6389d8f4-8a3f-4299-9b0c-883535d7688f")
                .applicationSecret("C3gOqAiVuUCMl0LaCaqxPA==")
                .environmentHost("clientapi.sinch.com")
                .userId(firebaseAuth.getUid())
                .build();
        // Specify the client capabilities.
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();

        sinchClient.start();
        CallClient callClient = sinchClient.getCallClient();
        callClient.addCallClientListener(new SinchCallClientListener());

        this.callerName = "Name";
        this.receiverName = "Name";
        this.voiceCallScreenFragment = new VoiceCallScreenFragment(callerName);

    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call inComingCall) {
            //Open call dialog
            call = inComingCall;
            AlertDialog alertDialog = new AlertDialog.Builder(ChatActivity.this).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setTitle(callerName + " is Calling");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(ChatActivity.this, "REJECT", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    call.addCallListener(new SinchCallListener());
                    call.hangup();
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Pick", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isCaller = 0; //Đánh dấu người này là người nhận cuộc gọi
                    Toast.makeText(ChatActivity.this, "PICK", Toast.LENGTH_SHORT).show();
                    call.addCallListener(new SinchCallListener());
                    call.answer();
                    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    //Open call dialog fragment
                    readData(firebaseDatabase.getReference().child("USER").child(inComingCall.getRemoteUserId()), new OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            String firstName = dataSnapshot.child("firstName").getValue(String.class);
                            String lastName = dataSnapshot.child("lastName").getValue(String.class);
                            callerName = firstName + " " + lastName;
                        }
                    });

                    voiceCallScreenFragment.setName(callerName);
                    voiceCallScreenFragment.show(getSupportFragmentManager(), "VoiceCallScreen");
                }
            });
            alertDialog.show();
        }

    }

    private class SinchCallListener implements CallListener {
        CallInfor callInfor = new CallInfor();
        @Override
        public void onCallProgressing(Call call) {
            callInfor.setReject(true);
            Toast.makeText(ChatActivity.this, "Ringing...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            callInfor.setReject(false);
            Toast.makeText(ChatActivity.this, "Call established", Toast.LENGTH_SHORT).show();
            FragmentManager fm = getSupportFragmentManager();
            VoiceCallScreenFragment voiceCallScreenFragment = (VoiceCallScreenFragment) fm.findFragmentByTag("VoiceCallScreen");
            Fragment prev = fm.findFragmentByTag("VoiceCallScreen");
            if (prev != null) {
                voiceCallScreenFragment.turnOnTheClock();
            }
        }

        @Override
        public void onCallEnded(Call endCall) {
            Toast.makeText(ChatActivity.this, "Call ended", Toast.LENGTH_SHORT).show();
            call = null;
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            FragmentManager fm = getSupportFragmentManager();
            VoiceCallScreenFragment voiceCallScreenFragment = (VoiceCallScreenFragment) fm.findFragmentByTag("VoiceCallScreen");
            Fragment prev = fm.findFragmentByTag("VoiceCallScreen");
            Long l = 0L; //Luu thoi gian cuoc goi
            if (prev != null) {
                voiceCallScreenFragment.turnOffTheClock();
                voiceCallScreenFragment.dismiss();
                l = voiceCallScreenFragment.getCallDuration();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-X");
//            callInfor.setReject(true);
            callInfor.setCallDuration(l);
            callInfor.setCallDate(simpleDateFormat.format(Calendar.getInstance().getTime()));
            if (isCaller == 0) { //Người nhận cuộc gọi
                callInfor.setReceiverID(firebaseAuth.getUid());
                callInfor.setCallerID(endCall.getRemoteUserId());
            }
            else if (isCaller == 1) {//Người gọi
                callInfor.setReceiverID(endCall.getRemoteUserId());
                callInfor.setCallerID(firebaseAuth.getUid());
            }
            firebaseDatabase.getReference().child("CALL_HISTORY")
                    .child(firebaseAuth.getUid()).child(endCall.getRemoteUserId()).push().setValue(callInfor);
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {
            Toast.makeText(getApplicationContext(), "Nofitication...", Toast.LENGTH_SHORT).show();
        }
    }
    public void callUser(String userId) {
        this.isCaller = 1; //Đánh dấu người này là người gọi
        if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if(call == null) {
                call = sinchClient.getCallClient().callUser(userId);
                setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                call.addCallListener(new SinchCallListener());
                //Open Voice Call Screen dialog fragment
                readData(firebaseDatabase.getReference().child("USER").child(userId), new OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        receiverName = firstName + " " + lastName;
                    }
                });
                voiceCallScreenFragment.setName(receiverName);
                voiceCallScreenFragment.show(getSupportFragmentManager(), "VoiceCallScreen");
            }
            else {
                call.hangup();
            }
        }
        else {
            ActivityCompat.requestPermissions(ChatActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 1);
        }
    }

    public void endVoiceCall() {
        if(this.call != null) {
            call.hangup();
        }
    }
    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }




    @Override
    public boolean onSupportNavigateUp() {
        CloseKeyBoard();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void CloseDrawer(boolean IsClose) {
        if (IsClose==true)
        {
            mAppBarConfiguration.getOpenableLayout().close();
        }
    }

    @Override
    public void UpdateStatus(String status) {


        HashMap<String,Object>onlineState=new HashMap<>();
        onlineState.put("Time",ServerValue.TIMESTAMP);
        onlineState.put("State",status);

        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("STATUS").updateChildren(onlineState);
    }

    @Override
    public void CloseKeyBoard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    private boolean isNetworkAvailable(Context context) {
        if (this!=null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
    public  boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.google.com/").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");

                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.d("Connect", "Error checking internet connection", e);
            }
        } else {
            Log.d("Connect", "No network available!");
        }
        return false;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void ReattachToolbar() {
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
       NavigationView navigationView = findViewById(R.id.nav_view);






       final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

       mAppBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawer).build();
       NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Message");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void ReattachToolbar(String titleName) {
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);




        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mAppBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawer).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().show();
        getSupportActionBar().setTitle(titleName);
    }


    @Override
    public void HideAppBar() {
        getSupportActionBar().hide();
    }


    @Override
    protected void onStop() {
        super.onStop();
        CheckInternetFlag=false;
        if (firebaseAuth.getCurrentUser()!=null)
        {
            UpdateStatus(STATUS_OFFLINE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatViewModel.subtitleBar.setValue("");


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("ProfileImg").addValueEventListener(changeImageListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){
            Log.d("NewIntent", "onNewIntent: ");
            Bundle bundle = intent.getExtras();

            String contactID = bundle.getString("userId");
            String contactName = bundle.getString("name");
            Bundle bundle1 = new Bundle();
            bundle1.putString("UserID",firebaseAuth.getCurrentUser().getUid());
            bundle1.putString("ContactID",contactID);
            bundle1.putString("ContactName",contactName);


            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = navHostFragment.getNavController();
            if  (navController.getBackStack()!=null)
            {
                navController.popBackStack(R.id.fragment_home,false);
            }

            //navController.getCurrentDestination()
            navController.navigate(R.id.action_fragment_home_to_chat_fragment,bundle1);
           // chat_fragment chatFragment = new chat_fragment();
         //   chatFragment.setArguments(bundle1);
          //  getSupportFragmentManager().beginTransaction().replace(android.R.id.content,chatFragment).addToBackStack(null).commit();

        }
        else
        {
            Log.d("NewIntent","Vo day ne");
        }
    }
}