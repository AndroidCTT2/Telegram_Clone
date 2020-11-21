package com.mobile.messageclone.Chat;

public class GenerateChatID {

    public String ID1;
    public String ID2;
    public int lengthA;

    String FirstString;
    public GenerateChatID(String id1, String id2)
    {
        this.ID1=id1;
        this.ID2=id2;
        if (id1.compareTo(id2)<0)
        {
            FirstString=ID1+ID2;
            lengthA=id1.length();
        }
        else
        {
            FirstString=ID2+ID1;
            lengthA=id2.length();
        }
    }
    public String GenerateKey()
    {

        return "PC-"+Integer.toBinaryString(lengthA)+"-"+FirstString;
    }

}
