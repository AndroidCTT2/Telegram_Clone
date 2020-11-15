package com.mobile.messageclone.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobile.messageclone.R;

public class NewGroupFragment extends Fragment {

    private ChatViewModel chatViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_new_group, container, false);
        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("New group");

        return root;
    }
}