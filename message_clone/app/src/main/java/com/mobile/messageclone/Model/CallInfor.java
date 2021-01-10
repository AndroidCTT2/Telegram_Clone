package com.mobile.messageclone.Model;

public class CallInfor {

    private String callerID;
    private String receiverID;
    private Long callDuration;
    private String callDate;
    private Boolean reject;

    public CallInfor(String callerName, String receiverName, Long callDuration, String callDate) {
        this.callerID = callerName;
        this.receiverID = receiverName;
        this.callDuration = callDuration;
        this.callDate = callDate;
        this.reject = false;
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

    public Boolean getReject() {
        return reject;
    }

    public void setReject(Boolean reject) {
        reject = reject;
    }
}
