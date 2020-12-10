package com.mobile.messageclone.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.messageclone.Model.CountryToPhonePrefix;

public class SignInViewModel extends ViewModel {

    public MutableLiveData<CountryToPhonePrefix> countryToPhonePrefixMutableLiveData;
    public MutableLiveData<String>phoneStringMutableLiveData;
    public MutableLiveData<String>countryCodeMutableLiveData;
    public MutableLiveData<String>countryNameMutableLiveData;
    public MutableLiveData<String>ISOCNameMutableLiveData;

    public SignInViewModel()
    {
        countryToPhonePrefixMutableLiveData=new MutableLiveData<>();
        phoneStringMutableLiveData=new MutableLiveData<>();
        countryCodeMutableLiveData=new MutableLiveData<>();
        countryNameMutableLiveData=new MutableLiveData<>();
        ISOCNameMutableLiveData=new MutableLiveData<>();

        phoneStringMutableLiveData.setValue("");
        countryCodeMutableLiveData.setValue("");
        countryNameMutableLiveData.setValue("");
        ISOCNameMutableLiveData.setValue("");

    }
}
