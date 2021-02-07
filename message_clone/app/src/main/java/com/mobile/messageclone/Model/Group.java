package com.mobile.messageclone.Model;

import android.provider.ContactsContract;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private ArrayList<String>groupMemberIdList;

    private ArrayList<GroupMember>groupMemberList;


    private String idAdmin;
    private String groupID;
    private String GroupImg;
    public Group()
    {
        groupName="";
        idAdmin="";
        groupID="";
        GroupImg="";
    }


    public ArrayList<GroupMember> getGroupMemberList()
    {
        return groupMemberList;
    }

    public void setGroupMemberList(ArrayList<GroupMember> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }

    public String getGroupImg() {
        return GroupImg;
    }

    public void setGroupImg(String groupImg) {
        GroupImg = groupImg;
    }

    public ArrayList<String> getGroupMemberIdList() {
        return groupMemberIdList;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setGroupMemberIdList(ArrayList<String> groupMemberIdList) {
        this.groupMemberIdList = groupMemberIdList;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
