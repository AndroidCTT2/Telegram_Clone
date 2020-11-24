package com.mobile.messageclone.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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


    private FloatingActionButton btnJumpToEnd;

    private MessagesListAdapter<LibMessage>messagesListAdapter;




    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-X");

    private ChatViewModel chatViewModel;

    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

                        ChatID=GenerateChatID.GenerateKey(UserID,ContactID,GenerateChatID.ID_CHAT_PERSONAL);
                        firebaseDatabase.getReference().child("CONVERSATION_ID").push().setValue(ChatID);
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("ContactID",ContactID);
                        hashMap.put("ChatID",ChatID);
                        firebaseDatabase.getReference().child("USER").child(UserID).child("ChatID").push().updateChildren(hashMap);
                        hashMap.clear();
                        hashMap.put("ContactID",UserID);
                        hashMap.put("ChatID",ChatID);
                        firebaseDatabase.getReference().child("USER").child(ContactID).child("ChatID").push().updateChildren(hashMap);
                    }

                    chatViewModel.ChatID.setValue(ChatID);
                }
                else
                {
                    ChatID=GenerateChatID.GenerateKey(UserID,ContactID,GenerateChatID.ID_CHAT_PERSONAL);
                    firebaseDatabase.getReference().child("CONVERSATION_ID").push().setValue(ChatID);


                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("ContactID",ContactID);
                    hashMap.put("ChatID",ChatID);
                    firebaseDatabase.getReference().child("USER").child(UserID).child("ChatID").push().updateChildren(hashMap);
                    hashMap.clear();
                    hashMap.put("ContactID",UserID);
                    hashMap.put("ChatID",ChatID);
                    firebaseDatabase.getReference().child("USER").child(ContactID).child("ChatID").push().updateChildren(hashMap);
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
        btnJumpToEnd=root.findViewById(R.id.jumpToEnd);
        btnJumpToEnd.setVisibility(View.GONE);




        MessageHolders holdersConfig=new MessageHolders().setOutcomingTextLayout(R.layout.one_row_message_sender).setOutcomingTextHolder(CustomOutComingMessageViewHolder.class,null);



        messagesListAdapter = new MessagesListAdapter<>(UserID,holdersConfig,null);
        messagesList.setAdapter(messagesListAdapter);

        btnJumpToEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesList.smoothScrollToPosition(0);
            }
        });


     messagesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
         @Override
         public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
             super.onScrollStateChanged(recyclerView, newState);
             if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
               //  btnJumpToEnd.setVisibility(View.GONE);
                 btnJumpToEnd.hide();
             }
         }

         @Override
         public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
             super.onScrolled(recyclerView, dx, dy);



                if (dy<0)
                {
                   // btnJumpToEnd.setVisibility(View.VISIBLE);
                    btnJumpToEnd.show();
                }

         }
     });




       messageInput.getInputEditText().setPadding(30,20,0,20);
       

        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Author author =new Author();
                author.userId=firebaseAuth.getCurrentUser().getUid();
                author.userName=firebaseAuth.getCurrentUser().getDisplayName();
                LibMessage message1=new LibMessage();
                if (TrueTimeRx.isInitialized()==true) {
                    message1.dateSend = TrueTimeRx.now();
                    Log.d("Time","TrueTime");
                }
                else
                {
                    Log.d("Time","Caledar");
                    message1.dateSend=Calendar.getInstance().getTime();
                }
                message1.iuser= author;
                message1.textMessage=input.toString().trim();

                UpdateMessageToServer();
                if (messagesListAdapter.getMessagesCount()==0)
                {

                }
                else {
                    messagesList.smoothScrollToPosition(0);
                }

                return true;
            }
        });


















        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<LibMessage> iMessageList = new ArrayList<>();

        chatViewModel.ChatID.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (chatViewModel.ChatID.getValue().isEmpty()==false) {
                    firebaseDatabase.getReference().child("MESSAGE").child(ChatID).orderByKey().addChildEventListener(UpdateMessage);

                }
            }
        });

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
        String ID=GenerateChatID.GenerateKey(this.UserID,this.ContactID,GenerateChatID.ID_CHAT_PERSONAL);
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
    private ChildEventListener UpdateMessage=new ChildEventListener() {
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
    };


}