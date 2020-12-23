package com.mobile.messageclone.Model;

import com.mobile.messageclone.Model.Contact;

public class ContactAndSeenTime {
    public Contact contact;
    public String SeenTime;
    public String Status;
    public String imageUrl;


    public ContactAndSeenTime()
    {
        contact=new Contact();
        SeenTime="";
        Status="";
        imageUrl="";

    }
}
