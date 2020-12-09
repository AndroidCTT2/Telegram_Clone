package com.mobile.messageclone.Chat;

import com.mobile.messageclone.SignIn.RecyclerViewClickInterface;

public class CallHistory {
    private String remoteID;
    private String name;
    private String callDate;
    private int rotation;

    public CallHistory() {
        this.name = "";
        this.callDate = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getRemoteID() {
        return remoteID;
    }

    public void setRemoteID(String remoteID) {
        this.remoteID = remoteID;
    }
}
