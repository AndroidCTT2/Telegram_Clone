package com.mobile.messageclone.Chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ChatViewModel extends ViewModel {

    public MutableLiveData<String> titleBar;
    public MutableLiveData<String> ChatID;


    public ChatViewModel()
    {
        titleBar=new MutableLiveData<>();
        ChatID=new MutableLiveData<>();

        titleBar.setValue("Message");
    }
}
