package com.mobile.messageclone.Ulti;

public class GenerateChatID {

    public static final String ID_CHAT_PERSONAL="PC-";
    public static final String ID_CHAT_GROUP="GC:";

    public static String GenerateKey(String id1,String id2,String Type)
    {
        if (Type.equals(ID_CHAT_PERSONAL)) {
            String FirstString;
            int FirstStringLength;
            if (id1.compareTo(id2) < 0) {
                FirstString = id1 + id2;
                FirstStringLength = id1.length();
            } else {
                FirstString = id2 + id1;
                FirstStringLength = id2.length();
            }

            return Type + Integer.toBinaryString(FirstStringLength) + "-" + FirstString;
        }
        else if (Type.equals(ID_CHAT_GROUP))
        {
            return ID_CHAT_GROUP+id1;
        }
        return "";
    }




    public static String SplitID (String userID, String chatID){
        String contactID ="";

        return contactID;
    }
}
