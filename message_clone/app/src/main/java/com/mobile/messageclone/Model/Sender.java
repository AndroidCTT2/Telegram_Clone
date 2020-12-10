package com.mobile.messageclone.Model;

public class Sender {
    public Data data;
    public String to;
    public String priority;
    public Sender(Data data1, String to1){
        this.data = data1;
        this.to = to1;
        this.priority="high";
    }

}
