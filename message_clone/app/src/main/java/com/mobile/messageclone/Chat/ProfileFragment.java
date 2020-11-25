package com.mobile.messageclone.Chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.R;
import com.mobile.messageclone.SignIn.MainActivity;
import com.mobile.messageclone.SignIn.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NavController navController;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ChatViewModel chatViewModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView displayPhoneNum;
    private TextView displayName;
    private TextView displayBio;


    private String UserId;
    private LinearLayout ChangeName;
    private LinearLayout ChangeBio;



    private ValueEventListener InfoListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bio.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        UserId=firebaseAuth.getCurrentUser().getUid();


        setHasOptionsMenu(true);
        ((CloseDrawer)getActivity()).CloseDrawer(true);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_bio,container,false);
        Toolbar toolbar=root.findViewById(R.id.toolbar);

       ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);




        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Your profile");
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.profile_menu,menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController=Navigation.findNavController(view);
        collapsingToolbarLayout=view.findViewById(R.id.collapsing);
        displayName=view.findViewById(R.id.displayUserName);
        displayBio=view.findViewById(R.id.displayBio);
        displayPhoneNum=view.findViewById(R.id.displayPhoneNumber);
        ChangeName=view.findViewById(R.id.ChangeName);
        ChangeBio=view.findViewById(R.id.ChangeBio);

        collapsingToolbarLayout.setTitle(firebaseAuth.getCurrentUser().getDisplayName());


        InfoListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    String firstName=snapshot.child("firstName").getValue(String.class);
                    String lastName=snapshot.child("lastName").getValue(String.class);
                    String bio=snapshot.child("bio").getValue(String.class);
                    String phone=snapshot.child("phoneNum").getValue(String.class);
                    displayName.setText(firstName+" "+lastName);
                    if (bio.isEmpty()==false) {
                        displayBio.setText(bio);
                    }
                    else
                    {
                        displayBio.setText("Bio");
                    }
                    displayPhoneNum.setText(phone);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(InfoListener);



        ChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fragment_change_name fragment_change_name1=new fragment_change_name();
                    fragment_change_name1.show(getParentFragmentManager(),null);
            }
        });
        ChangeBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BioChangeDialogFragment bioChangeDialogFragment=new BioChangeDialogFragment();
                bioChangeDialogFragment.show(getParentFragmentManager(),null);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.btnLogOut)
        {
            ((CloseDrawer)getActivity()).UpdateStatus(ChatActivity.STATUS_OFFLINE);
            firebaseAuth.signOut();
            Intent intent=new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((CloseDrawer)getActivity()).ReattachToolbar();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseDatabase.getReference().removeEventListener(InfoListener);
    }
}