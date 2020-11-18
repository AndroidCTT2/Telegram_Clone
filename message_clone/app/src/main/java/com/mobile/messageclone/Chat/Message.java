package com.mobile.messageclone.Chat;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Message  {

    private String senderID;
    private String receiverID;
    private String message;
    private Date sendTime;

    public Date getSendTime() {
        return sendTime;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
