package com.mobile.messageclone.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instacart.library.truetime.TrueTime;
import com.instacart.library.truetime.TrueTimeRx;
import com.mobile.messageclone.R;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private String ContactID;
    private String ContactName;



    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMM yyyy HH:mm:ss X");

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




        firebaseDatabase.getReference().child("CONVERSATION_ID").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()==true) {
                    if (CheckExistID(snapshot)==false)
                    {
                        firebaseDatabase.getReference().child("CONVERSATION_ID").push().setValue(GenerateChatID.GenerateKey(UserID,ContactID));
                    }
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
        MessagesListAdapter<IMessage> adapter = new MessagesListAdapter<>(UserID,null);
        messagesList.setAdapter(adapter);

       List<IMessage> iMessageList=new ArrayList<>();




       /*IUser iUser=new IUser();
        iUser.userId="123";
        iUser.userName="Nguyễn";


       IMessage iMessage=new IMessage();
      iMessage.textMessage="Demo1";
       iMessage.id=UserID;iMessage.iuser=iUser;
       iMessage.dateSend= Calendar.getInstance().getTime();
        iMessageList.add(iMessage);

        iMessage=new IMessage();
       iMessage.id=UserID;iMessage.textMessage="Demo2";
        iMessage.iuser=iUser;
        iMessage.dateSend= Calendar.getInstance().getTime();
        iMessageList.add(iMessage);

        IUser iUser1=new IUser();
        iUser1.userId=UserID;
        iUser1.userName="Nguyễn";
        iMessage=new IMessage();
        iMessage.textMessage="Demo2";
        iMessage.iuser=iUser1;
        iMessage.dateSend=Calendar.getInstance().getTime();
        iMessageList.add(iMessage);*/



      adapter.addToEnd(iMessageList,true);


        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                IUser iUser=new IUser();
                iUser.userId=firebaseAuth.getCurrentUser().getUid();
                iUser.userName=firebaseAuth.getCurrentUser().getDisplayName();
                IMessage message1=new IMessage();
                message1.dateSend= TrueTime.now();
                message1.iuser=iUser;
                message1.textMessage=input.toString();
                UpdateMessageToServer();
                adapter.addToStart(message1,true);
                return true;
            }
        });




        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        message.setMessage(messageInput.getInputEditText().getText().toString());
        firebaseDatabase.getReference().child("MESSAGE").child(this.UserID).child(this.ContactID).push().setValue(message);

    }

    private boolean CheckExistID(DataSnapshot dataSnapshot)
    {
        String ID=GenerateChatID.GenerateKey(this.UserID,this.ContactID);
        for (DataSnapshot child:dataSnapshot.getChildren())
        {
            String IDInDB=child.getValue(String.class);
            if (IDInDB.equals(ID)==true)
            {
                return true;
            }
        }
        return false;
    }
}