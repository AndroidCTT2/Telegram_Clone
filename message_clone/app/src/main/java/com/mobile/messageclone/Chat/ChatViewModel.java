package com.mobile.messageclone.Chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    public MutableLiveData<String> titleBar;
    public MutableLiveData<String> ChatID;

    public MutableLiveData<Boolean>IsDeleteListContactSeenTimeList;


    public ChatViewModel()
    {
        titleBar=new MutableLiveData<>();
        ChatID=new MutableLiveData<>();
        IsDeleteListContactSeenTimeList=new MutableLiveData<>();
        //titleBar.setValue("Message");
    }
}
