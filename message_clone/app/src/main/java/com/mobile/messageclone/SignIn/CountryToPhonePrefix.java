package com.mobile.messageclone.SignIn;

public class CountryToPhonePrefix {
    public String Countryname;
    public String Code;
    public String ISOCountry;
    public CountryToPhonePrefix(String a, String b,String isoCountry)
    {
        Countryname=a;
        Code="+"+b;
        ISOCountry=isoCountry;
    }
}
