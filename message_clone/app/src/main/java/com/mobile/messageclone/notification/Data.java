package com.mobile.messageclone.notification;

public class Data {
    private String user;
    private int icon;
    private String body;
    private String title;
    private String sented;

    public Data(String user, int icon, String body, String title, String sented){
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;

    }

    public int getIcon() {
        return icon;
    }

    public String getBody() {
        return body;
    }

    public String getSented() {
        return sented;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
