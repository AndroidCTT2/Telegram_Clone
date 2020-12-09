package com.mobile.messageclone.Chat;

public class CallInfor {

    private String callerID;
    private String receiverID;
    private Long callDuration;
    private String callDate;

    public CallInfor(String callerName, String receiverName, Long callDuration, String callDate) {
        this.callerID = callerName;
        this.receiverID = receiverName;
        this.callDuration = callDuration;
        this.callDate = callDate;
    }

    public CallInfor() {

    }

    public String getCallerID() {
        return callerID;
    }

    public void setCallerID(String callerId) {
        this.callerID = callerId;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverId) {
        this.receiverID = receiverId;
    }

    public Long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(Long callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }



}
