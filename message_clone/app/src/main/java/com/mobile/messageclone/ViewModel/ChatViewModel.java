package com.mobile.messageclone.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatViewModel extends ViewModel {

    public MutableLiveData<String> titleBar;
    public MutableLiveData<String>subtitleBar;
    public MutableLiveData<String> ChatID;
    public MutableLiveData<Boolean>IsScrollingMutableLiveData;
    public MutableLiveData<Boolean>IsHideAppBar;
    public MutableLiveData<Boolean>IsHideNavBar;
    public MutableLiveData<String>UserProfileImageUrl;

    public MutableLiveData<Boolean>IsDeleteListContactSeenTimeList;


    public ChatViewModel()
    {
        titleBar=new MutableLiveData<>();
        ChatID=new MutableLiveData<>();
        IsDeleteListContactSeenTimeList=new MutableLiveData<>();
        IsScrollingMutableLiveData=new MutableLiveData<>();
        IsHideAppBar=new MutableLiveData<>();
        IsHideNavBar=new MutableLiveData<>();
        UserProfileImageUrl=new MutableLiveData<>();
        subtitleBar=new MutableLiveData<>();
        //titleBar.setValue("Message");
    }
}
