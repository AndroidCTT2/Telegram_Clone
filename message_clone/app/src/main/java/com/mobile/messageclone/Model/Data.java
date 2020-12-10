package com.mobile.messageclone.Model;

public class Data {
    private String user;
    private String icon;
    private String body;
    private String title;
    private String sented;
    private String iconUrl;

    public Data(String user, String icon, String body, String title, String sented){
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;

    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIcon() {
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

    public void setIcon(String icon) {
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
