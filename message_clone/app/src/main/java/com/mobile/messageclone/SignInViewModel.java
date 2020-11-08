package com.mobile.messageclone;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SignInViewModel extends ViewModel {

    public MutableLiveData<CountryToPhonePrefix> countryToPhonePrefixMutableLiveData;

    public SignInViewModel()
    {
        countryToPhonePrefixMutableLiveData=new MutableLiveData<>();

    }
}
