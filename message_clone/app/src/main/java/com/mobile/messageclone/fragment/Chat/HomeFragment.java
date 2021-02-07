package com.mobile.messageclone.fragment.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.Model.Group;
import com.mobile.messageclone.Model.Message;
import com.mobile.messageclone.R;
import com.mobile.messageclone.Model.ContactLastMessTime;
import com.mobile.messageclone.RecycerViewAdapater.ContactListHomeAdapter;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;
import com.mobile.messageclone.Model.Contact;
import com.mobile.messageclone.ViewModel.ChatViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class HomeFragment extends Fragment implements RecyclerViewClickInterface {

    private ChatViewModel chatViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private LinkedList<Contact> contactLinkedList;
    private RecyclerView HomeContactList;
    private ContactListHomeAdapter contactListHomeAdapter;
     private FloatingActionButton btnNewMessage;
    private NavController navController;
    private LinkedList<String> chatidlist=new LinkedList<String>();
    public LinkedList<ContactLastMessTime>contactLastMessTimeLinkedList=new LinkedList<>();
    private String LastChat="";
    private ChildEventListener childEventListener;
    private String thisUserID;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        contactLinkedList=new LinkedList<>();
        contactListHomeAdapter=new ContactListHomeAdapter(getActivity(),getContext());
        contactListHomeAdapter.contactLastMessTimeLinkedList=contactLastMessTimeLinkedList;
        //contactListHomeAdapter.SetUpContactLastMessTimeList(contactLastMessTimeLinkedList);
        contactListHomeAdapter.SetClickInterface(this);
        thisUserID = firebaseAuth.getCurrentUser().getUid();

   






        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("ChatID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()==true)
                {
                    for (DataSnapshot child1:snapshot.getChildren())
                    {
                        ContactLastMessTime contactLastMessTime=new ContactLastMessTime();
                        //contactLastMessTimeLinkedList.clear();
                        String id=child1.child("ChatID").getValue(String.class);
                        String contactID = child1.child("ContactID").getValue(String.class);
                        firebaseDatabase.getReference().child("CONTACT").child(thisUserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()==true){
                                    for(DataSnapshot child3:snapshot.getChildren()){
                                        Contact contact = child3.getValue(Contact.class);
                                        if(contact.getUserIdContact().equals(contactID)){
                                            Log.d("Message","Name - " + contact.getFirstNickName() + " " + contact.getLastNickName());
                                            contactLastMessTime.contact=contact;
                                            firebaseDatabase.getReference().child("USER").child(contactLastMessTime.contact.getUserIdContact()).child("ProfileImg").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()==true)
                                                    {
                                                        String url=snapshot.getValue(String.class);
                                                        contactLastMessTime.profileImg=url;
                                                        Log.d("Image",url);
                                                    }
                                                    else
                                                    {
                                                        contactLastMessTime.profileImg=null;
                                                        Log.d("Image","Khong vo day");
                                                    }
                                                    firebaseDatabase.getReference().child("MESSAGE").child(id).limitToLast(1).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()==true)
                                                            {

                                                                for (DataSnapshot child2:snapshot.getChildren())
                                                                {

                                                                    Message message=child2.getValue(Message.class);

                                                                    contactLastMessTime.LastMess=message;
                                                                    contactLastMessTime.type=ContactListHomeAdapter.CHAT_PERSONAL;

                                                                    contactLastMessTime.contact.setUserIdContact(contactID);
                                                                    if (contactLastMessTimeLinkedList.size()!=0) {

                                                                        for (int i = 0; i < contactLastMessTimeLinkedList.size(); i++) {
                                                                            if (contactLastMessTimeLinkedList.get(i).contact.getUserIdContact().equals(contactID)) {
                                                                                contactLastMessTimeLinkedList.get(i).LastMess = message;

                                                                                Log.d("Message", "updated");
                                                                                break;
                                                                            }
                                                                            if (i == contactLastMessTimeLinkedList.size() - 1) {
                                                                                contactLastMessTimeLinkedList.add(contactLastMessTime);
                                                                                Log.d("Message", "added");
                                                                            }
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        contactLastMessTimeLinkedList.add(contactLastMessTime);
                                                                    }



                                                                //    Log.d("Image",contactLastMessTime.profileImg);

                                                                    Log.d("Message","contactID - " + contactID);
                                                                    Log.d("Message",message.getMessage());
                                                                    Log.d("Message",message.getSenderID());
                                                                }


                                                                contactListHomeAdapter.contactLastMessTimeLinkedList=contactLastMessTimeLinkedList;
                                                                contactListHomeAdapter.SortTime();
                                                                contactListHomeAdapter.notifyDataSetChanged();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("GROUP").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()==true)
                {
                    for (DataSnapshot child1:snapshot.getChildren())
                    {
                        String groupChatId=child1.getValue(String.class);

                        firebaseDatabase.getReference().child("GROUP_CHAT").child(groupChatId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ContactLastMessTime contactLastMessTime=new ContactLastMessTime();
                                Group group=snapshot.getValue(Group.class);
                                contactLastMessTime.groupName=group.getGroupName();

                                firebaseDatabase.getReference().child("MESSAGE").child(groupChatId).limitToLast(1).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()==true)
                                            {
                                                final Contact[] contact = {new Contact()};
                                                for (DataSnapshot child2:snapshot.getChildren()) {


                                                    Message message = child2.getValue(Message.class);
                                                    Log.d("Group",message.getSenderID());
                                                    if (message.getSenderID().equals(thisUserID)==false) {
                                                        firebaseDatabase.getReference().child("CONTACT").child(thisUserID).child(message.getSenderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()==true)
                                                                {
                                                                    contact[0] =snapshot.getValue(Contact.class);
                                                                    contactLastMessTime.groupName=group.getGroupName();
                                                                    contactLastMessTime.contact=contact[0];
                                                                    contactLastMessTime.contact.setUserIdContact(groupChatId);
                                                                    contactLastMessTime.LastMess=message;
                                                                    contactLastMessTime.profileImg=null;
                                                                    contactLastMessTime.type=ContactListHomeAdapter.CHAT_GROUP;


                                                                    if (contactLastMessTimeLinkedList.size()!=0) {

                                                                        for (int i = 0; i < contactLastMessTimeLinkedList.size(); i++) {
                                                                            if (contactLastMessTimeLinkedList.get(i).contact.getUserIdContact().equals(groupChatId)) {
                                                                                contactLastMessTimeLinkedList.get(i).LastMess = message;

                                                                                Log.d("Message", "updated");
                                                                                break;
                                                                            }
                                                                            if (i == contactLastMessTimeLinkedList.size() - 1) {
                                                                                contactLastMessTimeLinkedList.add(contactLastMessTime);
                                                                                Log.d("Message", "added");
                                                                            }
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        contactLastMessTimeLinkedList.add(contactLastMessTime);
                                                                    }


                                                                    contactListHomeAdapter.contactLastMessTimeLinkedList=contactLastMessTimeLinkedList;
                                                                    contactListHomeAdapter.SortTime();
                                                                    contactListHomeAdapter.notifyDataSetChanged();
                                                                }
                                                                else
                                                                {
                                                                    Log.d("Group","Co vo day");
                                                                    firebaseDatabase.getReference().child("USER").child(message.getSenderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            contact[0].setLastNickName(snapshot.child("firstName").getValue(String.class));
                                                                            contact[0].setFirstNickName(snapshot.child("lastName").getValue(String.class));
                                                                            contactLastMessTime.profileImg=null;
                                                                            contact[0].setUserIdContact(groupChatId);
                                                                            ContactLastMessTime contactLastMessTime=new ContactLastMessTime();
                                                                            contactLastMessTime.contact=contact[0];
                                                                            contactLastMessTime.LastMess=message;
                                                                            contactLastMessTime.groupName=group.getGroupName();
                                                                            Log.d("Group",groupChatId);
                                                                            contactLastMessTime.type=ContactListHomeAdapter.CHAT_GROUP;

                                                                            if (contactLastMessTimeLinkedList.size()!=0) {

                                                                                for (int i = 0; i < contactLastMessTimeLinkedList.size(); i++) {
                                                                                    if (contactLastMessTimeLinkedList.get(i).contact.getUserIdContact().equals(groupChatId)) {
                                                                                        contactLastMessTimeLinkedList.get(i).LastMess = message;

                                                                                        Log.d("Message", "updated");
                                                                                        break;
                                                                                    }
                                                                                    if (i == contactLastMessTimeLinkedList.size() - 1) {
                                                                                        contactLastMessTimeLinkedList.add(contactLastMessTime);
                                                                                        Log.d("Message", "added");
                                                                                    }
                                                                                }
                                                                            }
                                                                            else
                                                                            {
                                                                                contactLastMessTimeLinkedList.add(contactLastMessTime);
                                                                            }


                                                                            contactListHomeAdapter.contactLastMessTimeLinkedList=contactLastMessTimeLinkedList;
                                                                            contactListHomeAdapter.SortTime();
                                                                            contactListHomeAdapter.notifyDataSetChanged();

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }


                                                            }





                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                    else
                                                    {
                                                        firebaseDatabase.getReference().child("USER").child(thisUserID).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {



                                                                contactLastMessTime.contact.setFirstNickName(snapshot.child("firstName").getValue(String.class));
                                                                contactLastMessTime.contact.setLastNickName(snapshot.child("lastName").getValue(String.class));
                                                                contactLastMessTime.contact.setUserIdContact(groupChatId);
                                                                contactLastMessTime.LastMess=message;
                                                                contactLastMessTime.profileImg=null;
                                                                contactLastMessTime.type=ContactListHomeAdapter.CHAT_GROUP;


                                                                if (contactLastMessTimeLinkedList.size()!=0) {

                                                                    for (int i = 0; i < contactLastMessTimeLinkedList.size(); i++) {
                                                                        if (contactLastMessTimeLinkedList.get(i).contact.getUserIdContact().equals(groupChatId)) {
                                                                            contactLastMessTimeLinkedList.get(i).LastMess = message;

                                                                            Log.d("Message", "updated");
                                                                            break;
                                                                        }
                                                                        if (i == contactLastMessTimeLinkedList.size() - 1) {
                                                                            contactLastMessTimeLinkedList.add(contactLastMessTime);
                                                                            Log.d("Message", "added");
                                                                        }
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    contactLastMessTimeLinkedList.add(contactLastMessTime);
                                                                }


                                                                contactListHomeAdapter.contactLastMessTimeLinkedList=contactLastMessTimeLinkedList;
                                                                contactListHomeAdapter.SortTime();
                                                                contactListHomeAdapter.notifyDataSetChanged();
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









    }




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btnNewMessage = root.findViewById(R.id.btnNewMessage);
        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Message");

        HomeContactList=root.findViewById(R.id.recyclerHomeContact);


        btnNewMessage = root.findViewById(R.id.btnNewMessage);

        HomeContactList.setAdapter(contactListHomeAdapter);
        HomeContactList.setLayoutManager(new LinearLayoutManager(getContext()));
        HomeContactList.addItemDecoration(new DividerItemDecoration(HomeContactList.getContext(),DividerItemDecoration.VERTICAL));




        return root;
    }



    @Override
    public void onResume() {
        super.onResume();
        contactListHomeAdapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(int position) {


        Bundle bundle=new Bundle();
        bundle.putString("UserID", firebaseAuth.getCurrentUser().getUid());
        bundle.putString("ContactID", contactLastMessTimeLinkedList.get(position).contact.getUserIdContact());
        bundle.putInt("Type",contactListHomeAdapter.contactLastMessTimeLinkedList.get(position).type);

        if (contactListHomeAdapter.contactLastMessTimeLinkedList.get(position).type==ContactListHomeAdapter.CHAT_GROUP)
        {


            bundle.putString("ContactName", contactLastMessTimeLinkedList.get(position).groupName);

        }
        else if (contactListHomeAdapter.contactLastMessTimeLinkedList.get(position).type==ContactListHomeAdapter.CHAT_PERSONAL) {

            bundle.putString("ContactName", contactLastMessTimeLinkedList.get(position).contact.getFirstNickName() + " " + contactLastMessTimeLinkedList.get(position).contact.getLastNickName());

        }
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_fragment_home_to_chat_fragment, bundle);
    }

    @Override
    public void onLongItemClick(int position) {

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);

        btnNewMessage=view.findViewById(R.id.btnNewMessage);

        btnNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragment_home_to_newChat);
            }
        });
    }
    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

}
