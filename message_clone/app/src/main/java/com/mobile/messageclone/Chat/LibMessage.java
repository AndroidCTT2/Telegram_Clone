package com.mobile.messageclone.Chat;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class LibMessage implements IMessage {

    public String id;
    public IUser iuser;
    public Date dateSend;
    public String textMessage;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return textMessage;
    }

    @Override
    public IUser getUser() {
        return iuser;
    }

    @Override
    public Date getCreatedAt() {
        return dateSend;
    }
}
