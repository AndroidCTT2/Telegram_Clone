package com.mobile.messageclone.Model;



public class Contact {

    public static final int IN_CONTACT=1;
    public static final int NOT_IN_CONTACT=0;

    private String UserIdContact="";
    private String FirstNickName="";
    private String LastNickName="";
    private int ContactStatus;


    public void setContactStatus(int contactStatus) {
        ContactStatus = contactStatus;
    }

    public int getContactStatus() {
        return ContactStatus;
    }

    public void setFirstNickName(String firstNickName) {
        FirstNickName = firstNickName;
    }

    public void setLastNickName(String lastNickName) {
        LastNickName = lastNickName;
    }

    public void setUserIdContact(String userIdContact) {
        UserIdContact = userIdContact;
    }

    public String getFirstNickName() {
        return FirstNickName;
    }

    public String getLastNickName()
    {
        return LastNickName;
    }

    public String getUserIdContact()
    {
        return UserIdContact;
    }

}
