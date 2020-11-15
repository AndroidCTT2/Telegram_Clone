package com.mobile.messageclone.Chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatViewModel extends ViewModel {

    public MutableLiveData<String> titleBar;

    public ChatViewModel()
    {
        titleBar=new MutableLiveData<>();
        titleBar.setValue("Message");
    }
}
