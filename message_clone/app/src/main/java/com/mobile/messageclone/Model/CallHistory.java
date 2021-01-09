package com.mobile.messageclone.Model;

public class CallHistory {
    private String remoteID;
    private String name;
    private String callDate;
    private int rotation;
    private String imageUrl;
    private Boolean reject;

    public CallHistory() {
        this.name = "";
        this.callDate = "";
        this.imageUrl = "";
        this.reject = true;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getReject() {
        return this.reject;
    }

    public void setReject(Boolean reject) {
        this.reject = reject;
    }
}
