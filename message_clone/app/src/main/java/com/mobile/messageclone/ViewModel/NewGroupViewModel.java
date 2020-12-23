package com.mobile.messageclone.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.messageclone.Model.ContactAndSeenTime;

import java.util.ArrayList;

public class NewGroupViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ContactAndSeenTime>>arrayListMutableLiveData;
    public NewGroupViewModel()
    {
        arrayListMutableLiveData=new MutableLiveData<>();
    }

}
