package com.mobile.messageclone.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobile.messageclone.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ContactFragment extends Fragment {

    private ChatViewModel chatViewModel;
    private FloatingActionButton btnAddContact;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_contact,container,false);

        final ChatViewModel chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Contact");


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);

        btnAddContact=view.findViewById(R.id.btnAddContact);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    navController.navigate(R.id.action_fragment_contact_to_fragment_find_contact);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}