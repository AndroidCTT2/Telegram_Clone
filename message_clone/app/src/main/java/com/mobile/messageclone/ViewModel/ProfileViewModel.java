package com.mobile.messageclone.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String>UserId;
    public ProfileViewModel()
    {
        UserId=new MutableLiveData<>();

    }
}
