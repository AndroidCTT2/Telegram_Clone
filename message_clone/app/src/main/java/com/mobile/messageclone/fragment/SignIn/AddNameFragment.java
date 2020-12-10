package com.mobile.messageclone.fragment.SignIn;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.messageclone.Model.User;
import com.mobile.messageclone.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextInputEditText inputFirstName;
    private TextInputEditText inputLastName;

    private FloatingActionButton btnComplete;

    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;

    private Uri ProfileImageUri;

    private NavController navController;

    public AddNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNameFragment newInstance(String param1, String param2) {
        AddNameFragment fragment = new AddNameFragment();
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
         firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        navController= Navigation.findNavController(view);

        inputFirstName=view.findViewById(R.id.inputFirstName);
        inputLastName=view.findViewById(R.id.inputLastName);
        btnComplete=view.findViewById(R.id.btnComplete);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputFirstName.getText().toString().isEmpty()==true)
                {

                }
                else {
                    String FirstName="";
                    String LastName="";
                    FirstName=inputFirstName.getText().toString().trim();
                    if (inputLastName.getText().toString().isEmpty()==false) {
                        LastName=inputLastName.getText().toString().trim();
                    }
                    final User user=new User();
                    user.setFirstName(FirstName);
                    user.setLastName(LastName);
                    user.setPhoneNum(firebaseAuth.getCurrentUser().getPhoneNumber());
                    user.setBio("");


                    UserProfileChangeRequest userProfileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(FirstName+" "+LastName).build();
                    firebaseAuth.getCurrentUser().updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {



                            firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                            navController.navigate(R.id.action_registerFragment_to_chatActivity2);
                        }


                    });
                }

            }
        });


    }

}