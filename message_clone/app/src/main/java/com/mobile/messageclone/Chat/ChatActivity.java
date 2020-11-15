package com.mobile.messageclone.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.messageclone.R;

public class ChatActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView displayUserName;
    private TextView displayUserPhoneNumber;
    private ImageView ProfilePicture;



    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final ChatViewModel chatViewModel =new ViewModelProvider(this).get(ChatViewModel.class);

        firebaseAuth=FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        mAppBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawer).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);






        navigationView.getHeaderView(0);

        getSupportActionBar().setTitle(chatViewModel.titleBar.getValue());


        ProfilePicture=navigationView.getHeaderView(0).findViewById(R.id.ProfilePicture);

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




        TextDrawable profileDrawable= TextDrawable.builder().beginConfig().fontSize(50).toUpperCase().bold().endConfig().buildRound(firstname+lastname, Color.CYAN);
        ProfilePicture.setImageDrawable(profileDrawable);

        chatViewModel.titleBar.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                getSupportActionBar().setTitle(chatViewModel.titleBar.getValue());
            }
        });




    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}