package com.mobile.messageclone.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.TextDrawableForStaticImage;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements CloseDrawer  {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView displayUserName;
    private TextView displayUserPhoneNumber;
    private CircularImageView ProfilePicture;

    private Toolbar toolbar;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Task<String> firebaseMessaging;
    private String deviceToken;
    private ValueEventListener changeImageListener;


    public static final String STATUS_OFFLINE="OFFLINE";
    public static final String STATUS_ONLINE="ONLINE";


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

        final ChatViewModel chatViewModel =new ViewModelProvider(this).get(ChatViewModel.class);

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
                }
                else
                {
                    ProfilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap(firstname.toUpperCase()+ finalLastname.toUpperCase(),60,Color.WHITE));
                    Log.d("Dowload","NoImage");
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
        String CurrentDate;
        String CurrentTime;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy,X");
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH-mm-ss");




        CurrentDate=dateFormat.format(calendar.getTime());
        CurrentTime=timeFormat.format(calendar.getTime());

        HashMap<String,Object>onlineState=new HashMap<>();
        onlineState.put("Date",CurrentDate);
        onlineState.put("Time",CurrentTime);
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




    @Override
    public void HideAppBar() {
        getSupportActionBar().hide();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuth.getCurrentUser()!=null)
        {
            UpdateStatus(STATUS_OFFLINE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


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
}