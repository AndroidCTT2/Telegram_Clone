package com.mobile.messageclone.Model;

import java.time.LocalDate;
import java.util.Date;

public class GroupMember  {

    public Contact contact;
    public long dayAdded;
    public Boolean isRemove;
    public Date dayRemove;

    public GroupMember()
    {
        contact=new Contact();
        dayAdded= LocalDate.now().toEpochDay();
        isRemove=false;
        dayRemove=null;
    }
}
