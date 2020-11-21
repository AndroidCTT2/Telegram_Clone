package com.mobile.messageclone.Chat;

public class GenerateChatID {



    public static String GenerateKey(String id1,String id2)
    {
        String FirstString;
        int FirstStringLength;
        if (id1.compareTo(id2)<0)
        {
            FirstString=id1+id2;
            FirstStringLength=id1.length();
        }
        else
        {
            FirstString=id2+id1;
            FirstStringLength=id2.length();
        }

        return "PC-"+Integer.toBinaryString(FirstStringLength)+"-"+FirstString;
    }

}
