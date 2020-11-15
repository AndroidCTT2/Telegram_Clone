package com.mobile.messageclone.SignIn;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignInViewModel extends ViewModel {

    public MutableLiveData<CountryToPhonePrefix> countryToPhonePrefixMutableLiveData;
    public MutableLiveData<String>phoneStringMutableLiveData;
    public MutableLiveData<String>countryCodeMutableLiveData;
    public MutableLiveData<String>countryNameMutableLiveData;

    public SignInViewModel()
    {
        countryToPhonePrefixMutableLiveData=new MutableLiveData<>();
        phoneStringMutableLiveData=new MutableLiveData<>();
        countryCodeMutableLiveData=new MutableLiveData<>();
        countryNameMutableLiveData=new MutableLiveData<>();

        phoneStringMutableLiveData.setValue("");
        countryCodeMutableLiveData.setValue("");
        countryNameMutableLiveData.setValue("");

    }
}
