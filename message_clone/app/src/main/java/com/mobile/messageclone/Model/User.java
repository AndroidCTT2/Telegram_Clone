package com.mobile.messageclone.Model;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;

public class User {
    private String FirstName;
    private String LastName;
    private String PhoneNum;
    private String Bio;

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }
    public String getPhoneNum()
    {
        return PhoneNum;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public void setPhoneNum(String PhoneNum) {
        this.PhoneNum = PhoneNum;
    }
}
