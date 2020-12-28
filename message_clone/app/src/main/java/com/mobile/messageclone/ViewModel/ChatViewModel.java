package com.mobile.messageclone.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatViewModel extends ViewModel {

    public final static int LOAD_GROUP=0;
    public final static int LOAD_COMPLETE_GROUP=1;
    public final static int LOCK_LOAD_GROUP=2;

    public MutableLiveData<String> titleBar;
    public MutableLiveData<String>subtitleBar;
    public MutableLiveData<String> ChatID;
    public MutableLiveData<Boolean>IsScrollingMutableLiveData;
    public MutableLiveData<Boolean>IsHideAppBar;
    public MutableLiveData<Boolean>IsHideNavBar;
    public MutableLiveData<String>UserProfileImageUrl;


    public MutableLiveData<Integer>CompleteLoadGroup;



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

        CompleteLoadGroup=new MutableLiveData<>();
        //titleBar.setValue("Message");
    }
}
