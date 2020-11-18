package com.mobile.messageclone.Chat;

public class IUser implements com.stfalcon.chatkit.commons.models.IUser {

    public String userId;
    public String userName;


    @Override
    public String getId() {
        return userId;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public String getAvatar() {
        return null;
    }
}
