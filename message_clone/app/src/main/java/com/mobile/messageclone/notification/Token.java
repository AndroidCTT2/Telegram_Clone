package com.mobile.messageclone.notification;

public class Token {
    private String token;

    public Token(String tokenStr){
        this.token = tokenStr;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
