package com.mobile.messageclone.Model;

import com.stfalcon.chatkit.commons.models.IUser;

public class Author implements IUser {

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
