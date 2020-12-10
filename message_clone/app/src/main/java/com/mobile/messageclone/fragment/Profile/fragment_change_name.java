package com.mobile.messageclone.fragment.Profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.messageclone.R;

import java.util.HashMap;

public class fragment_change_name extends DialogFragment {

    private TextInputEditText inputFirstName;
    private TextInputEditText inputLastName;
    private MaterialButton btnCancel;
    private MaterialButton btnUpdateName;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String UserId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        UserId=firebaseAuth.getCurrentUser().getUid();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_change_name,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputFirstName=view.findViewById(R.id.inputFirstName);
        inputLastName=view.findViewById(R.id.inputLastName);
        btnCancel=view.findViewById(R.id.btnCancel);
        btnUpdateName=view.findViewById(R.id.btnUpdateName);


        btnUpdateName.setEnabled(false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object>hashMap=new HashMap<>();

                String FirstName=inputFirstName.getText().toString().trim();
                String LastName;
                if (inputLastName.getText().toString().isEmpty()==true)
                {
                    LastName="";
                }
                else
                {
                    LastName=inputLastName.getText().toString().trim();
                }
               // hashMap.put("firstName",FirstName);
                //hashMap.put("lastName",LastName);


                firebaseDatabase.getReference().child("USER").child(UserId).child("firstName").setValue(FirstName);
                firebaseDatabase.getReference().child("USER").child(UserId).child("lastName").setValue(LastName);

                UserProfileChangeRequest userProfileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(FirstName+" "+LastName).build();
                firebaseAuth.getCurrentUser().updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Update successful",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
            }
        });

        inputFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()==false)
                {
                    btnUpdateName.setEnabled(true);
                }
                else
                {
                    btnUpdateName.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
