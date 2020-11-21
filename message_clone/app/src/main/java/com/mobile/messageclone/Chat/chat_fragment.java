package com.mobile.messageclone.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instacart.library.truetime.TrueTime;
import com.instacart.library.truetime.TrueTimeRx;
import com.mobile.messageclone.R;
import com.mobile.messageclone.SignIn.User;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class chat_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MessagesList messagesList;
    private LinkedList<Message>messageLinkedList;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private MessageInput messageInput;
    private MessageAdapter messageAdapter;


    private String UserID;
    private  String ContactID;
    private String ContactName;
    private  String ChatID="";

    private MessagesListAdapter<LibMessage>messagesListAdapter;




    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-X");

    private ChatViewModel chatViewModel;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserID=getArguments().getString("UserID");
            ContactID=getArguments().getString("ContactID");
            ContactName=getArguments().getString("ContactName");

        }

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        messageLinkedList=new LinkedList<>();
        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue(ContactName);
        chatViewModel.ChatID.setValue(ChatID);







        firebaseDatabase.getReference().child("CONVERSATION_ID").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()==true) {
                    if (CheckExistID(snapshot)==false)
                    {
                        firebaseDatabase.getReference().child("CONVERSATION_ID").push().setValue(GenerateChatID.GenerateKey(UserID,ContactID));

                    }
                    else
                    {
                        ChatID=GenerateChatID.GenerateKey(UserID,ContactID);
                    }
                    chatViewModel.ChatID.setValue(ChatID);
                }
                else
                {
                    ChatID=GenerateChatID.GenerateKey(UserID,ContactID);
                    firebaseDatabase.getReference().child("CONVERSATION_ID").push().setValue(GenerateChatID.GenerateKey(UserID,ContactID));
                    chatViewModel.ChatID.setValue(ChatID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_chat_fragment,container,false);


        messageInput=root.findViewById(R.id.inputMessage);
        messagesList=root.findViewById(R.id.messageList);


      //  messageAdapter=new MessageAdapter(messageLinkedList,getActivity(),getContext(),firebaseAuth.getCurrentUser().getUid());
      //  messagesList.setAdapter(messageAdapter);
      //  messagesList.setLayoutManager(new LinearLayoutManager(getContext()));
        messagesListAdapter = new MessagesListAdapter<>(UserID,null);
        messagesList.setAdapter(messagesListAdapter);







       messageInput.getInputEditText().setPadding(30,20,0,20);
       

        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Author author =new Author();
                author.userId=firebaseAuth.getCurrentUser().getUid();
                author.userName=firebaseAuth.getCurrentUser().getDisplayName();
                LibMessage message1=new LibMessage();
                message1.dateSend= TrueTime.now();
                message1.iuser= author;
                message1.textMessage=input.toString().trim();

                UpdateMessageToServer();
                return true;
            }
        });


















        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<LibMessage> iMessageList=new ArrayList<>();
        firebaseDatabase.getReference().child("MESSAGE").child(GenerateChatID.GenerateKey(UserID,ContactID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null)
                {
                    Log.d("Sender","true");
                    for (DataSnapshot childSnapshot:snapshot.getChildren())
                    {
                        String messageKey=childSnapshot.getKey();

                        Message message=childSnapshot.getValue(Message.class);

                        if (message.getSenderID().equals(UserID))
                        {

                            LibMessage iMessage=new LibMessage();
                            User user=new User();

                            Date date= null;
                            try {
                                 date=simpleDateFormat.parse(message.getSendTime());
                                iMessage=ConvertMessageToIMessage(message,user,UserID,messageKey,date);
                                iMessageList.add(iMessage);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                        else if (message.getSenderID().equals(ContactID))
                        {

                            LibMessage iMessage=new LibMessage();
                            User user=new User();

                            Date date= null;
                            try {
                                date = simpleDateFormat.parse(message.getSendTime());
                                iMessage=ConvertMessageToIMessage(message,user,ContactID,messageKey,date);
                                iMessageList.add(iMessage);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }



                        }

                    }
                    messagesListAdapter.addToEnd(iMessageList,true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
      /*  firebaseDatabase.getReference().child("MESSAGE").child(GenerateChatID.GenerateKey(UserID,ContactID)).orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()==true) {
                    String messageKey = snapshot.getKey();

                    Message message = snapshot.getValue(Message.class);


                    if (message.getSenderID().equals(UserID) == true) {
                        LibMessage iMessage = new LibMessage();
                        Log.d("Sender", UserID);

                        try {
                            Author author =new Author();
                            author.userId=UserID;
                            Date date=simpleDateFormat.parse(message.getSendTime());
                            iMessage.iuser= author;
                            iMessage.id=messageKey;
                            iMessage.textMessage=message.getMessage();
                            iMessage.dateSend=date;
                            messagesListAdapter.addToStart(iMessage,true);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                    } else if (message.getSenderID().equals(ContactID)==true) {

                        LibMessage iMessage=new LibMessage();

                        Date date= null;
                        try {
                            Log.d("Sender", message.getMessage());
                            Author author =new Author();
                            author.userId=ContactID;
                            date = simpleDateFormat.parse(message.getSendTime());
                            iMessage.iuser= author;
                            iMessage.textMessage=message.getMessage();
                            iMessage.dateSend=date;
                            Log.d("Sender", iMessage.getText());
                            messagesListAdapter.addToStart(iMessage,true);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }


                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    }

    public void UpdateMessageToServer()
    {
        Message message=new Message();
        message.setSenderID(this.UserID);
        message.setReceiverID(this.ContactID);

        if (TrueTimeRx.isInitialized()==true)
        {
            message.setSendTime(simpleDateFormat.format(TrueTimeRx.now()));
        }
        else
        {
            message.setSendTime(simpleDateFormat.format(Calendar.getInstance().getTime()));
        }
        message.setMessage(messageInput.getInputEditText().getText().toString().trim());
        firebaseDatabase.getReference().child("MESSAGE").child(ChatID).push().setValue(message);

    }

    private boolean CheckExistID(DataSnapshot dataSnapshot)
    {
        String ID=GenerateChatID.GenerateKey(this.UserID,this.ContactID);
        for (DataSnapshot child:dataSnapshot.getChildren())
        {
            String IDInDB=child.getValue(String.class);
            if (IDInDB.equals(ID)==true)
            {
                ChatID=IDInDB;
                return true;
            }
        }
        return false;
    }

    private Author ConvertUserToIUser(User user, String userID)
    {
        Author author =new Author();
        author.userId=userID;
        return author;
    }

    private LibMessage ConvertMessageToIMessage(Message message,User user,String UserId,String messageId,Date date)  {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat();
        LibMessage iMessage=new LibMessage();
        iMessage.dateSend=date;
        Author author =new Author();
        author.userId=UserId;
        iMessage.iuser= author;
        iMessage.textMessage=message.getMessage();
        iMessage.id=messageId;
        return iMessage;

    }
}