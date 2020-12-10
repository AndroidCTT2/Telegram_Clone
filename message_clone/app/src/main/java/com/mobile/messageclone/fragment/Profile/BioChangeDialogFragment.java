package com.mobile.messageclone.fragment.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.messageclone.R;

import java.util.HashMap;

public class BioChangeDialogFragment extends DialogFragment {


    private MaterialButton btnUpdate;
    private MaterialButton btnCancel;
    private TextInputEditText inputBio;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        userId=firebaseAuth.getCurrentUser().getUid();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_change_bio,container,false);

        btnUpdate=view.findViewById(R.id.btnUpdateBio);
        btnCancel=view.findViewById(R.id.btnCancel);

        inputBio=view.findViewById(R.id.inputBio);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bio;
                if (inputBio.getText().toString().isEmpty()==false) {
                     bio = inputBio.getText().toString().trim();
                }
                else
                {
                    bio="";
                }
                HashMap<String,Object>hashMap=new HashMap<>();
                hashMap.put("bio",bio);
                firebaseDatabase.getReference().child("USER").child(userId).updateChildren(hashMap);
                dismiss();
            }
        });
    }
}
