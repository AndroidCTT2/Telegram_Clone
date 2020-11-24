package com.mobile.messageclone.Chat;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

public class LibMessage implements IMessage, MessageContentType.Image {

    public String id;
    public IUser iuser;
    public Date dateSend;
    public String textMessage;

    public Message.STATUS Status;


    public Message.STATUS getStatus() {
        return Status;
    }

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

    @Nullable
    @Override
    public String getImageUrl() {
        return null;
    }
}
