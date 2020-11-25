package com.mobile.messageclone.Chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String>UserId;
    public ProfileViewModel()
    {
        UserId=new MutableLiveData<>();

    }
}
