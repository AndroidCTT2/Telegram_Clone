package com.mobile.messageclone.Chat;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

public class Message {

    enum STATUS
    {
        Delivered,
        Seen,
        Sending,
    }

    private String senderID;
    private String receiverID;
    private String message;
    private String sendTime;
    private STATUS Status;


    public String getSendTime() {
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

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setStatus(STATUS status) {
        Status = status;
    }

    public STATUS getStatus() {
        return Status;
    }
}
