package com.mobile.messageclone.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobile.messageclone.R;

public class HomeFragment extends Fragment {

    private ChatViewModel chatViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Message");
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}