package com.mobile.messageclone.fragment.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.Model.Contact;
import com.mobile.messageclone.Model.ContactAndSeenTime;
import com.mobile.messageclone.Model.Group;
import com.mobile.messageclone.R;
import com.mobile.messageclone.RecycerViewAdapater.ContactListAdapter;
import com.mobile.messageclone.Ulti.DateToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class ChatProfileGroupFragmment extends Fragment {

    private ContactListAdapter contactListAdapter;

    private RecyclerView contactList;

    private String ContactID;
    private String UserID;

    private FirebaseDatabase firebaseDatabase;

    private ArrayList<ContactAndSeenTime>contactAndSeenTimeArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null)
        {
            ContactID=getArguments().getString("ContactID");
            UserID=getArguments().getString("UserID");
        }
        firebaseDatabase=FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.chat_profile_group,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactList=view.findViewById(R.id.ListContact);

        firebaseDatabase.getReference().child("GROUP_CHAT").child(ContactID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()==true)
                {
                    Group group=snapshot.getValue(Group.class);
                    ArrayList<String>contactIDList=group.getGroupMemberIdList();
                    for (int i=0;i<contactIDList.size();i++)
                    {

                        String contactId=contactIDList.get(i);
                        firebaseDatabase.getReference().child("CONTACT").child(UserID).child(contactId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()==true)
                                {

                                    Contact contact =snapshot.getValue(Contact.class);
                                    firebaseDatabase.getReference().child("USER").child(contact.getUserIdContact()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists() == true) {

                                                long  timeStamp=snapshot.child("STATUS").child("Time").getValue(Long.class);
                                                String status=snapshot.child("STATUS").child("State").getValue(String.class);
                                                String profileImg=snapshot.child("ProfileImg").getValue(String.class);
                                                if (profileImg==null)
                                                {
                                                    profileImg="";
                                                }
                                                Instant instant=Instant.ofEpochMilli(timeStamp);
                                                Date date=Date.from(instant);
                                                ContactAndSeenTime contactAndSeenTime1 = new ContactAndSeenTime();
                                                contactAndSeenTime1.Status = status;

                                                contactAndSeenTime1.SeenTime= DateToString.LastSeenString(date);
                                                contactAndSeenTime1.contact = contact;
                                                contactAndSeenTime1.imageUrl=profileImg;



                                                if (contactAndSeenTimeArrayList.size()!=0) {
                                                    for (int i = 0; i < contactAndSeenTimeArrayList.size(); i++) {
                                                        if (contactAndSeenTime1.contact.getUserIdContact().equals(contactAndSeenTimeArrayList.get(i).contact.getUserIdContact())) {
                                                            contactAndSeenTimeArrayList.get(i).Status = contactAndSeenTime1.Status;
                                                            contactAndSeenTimeArrayList.get(i).SeenTime = contactAndSeenTime1.SeenTime;
                                                            break;
                                                        }
                                                        if (i == contactAndSeenTimeArrayList.size() - 1) {
                                                            contactAndSeenTimeArrayList.add(contactAndSeenTime1);
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    contactAndSeenTimeArrayList.add(contactAndSeenTime1);
                                                }

                                                contactListAdapter.notifyDataSetChanged();


                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                else
                                {
                                    firebaseDatabase.getReference().child("USER").child(contactId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Contact contact=new Contact();
                                            contact.setFirstNickName(snapshot.child("firstName").getValue(String.class));
                                            contact.setLastNickName(snapshot.child("lastName").getValue(String.class));
                                            contact.setUserIdContact(contactId);

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
               
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        firebaseDatabase.getReference().child("CONTACT").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() == true) {

                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {

                        Contact contact = new Contact();
                        contact = childSnapShot.getValue(Contact.class);




                        Contact finalContact1 = contact;
                        firebaseDatabase.getReference().child("USER").child(contact.getUserIdContact()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists() == true) {

                                    long  timeStamp=snapshot.child("STATUS").child("Time").getValue(Long.class);
                                    String status=snapshot.child("STATUS").child("State").getValue(String.class);
                                    String profileImg=snapshot.child("ProfileImg").getValue(String.class);
                                    if (profileImg==null)
                                    {
                                        profileImg="";
                                    }
                                    Instant instant=Instant.ofEpochMilli(timeStamp);
                                    Date date=Date.from(instant);
                                    ContactAndSeenTime contactAndSeenTime1 = new ContactAndSeenTime();
                                    contactAndSeenTime1.Status = status;

                                    contactAndSeenTime1.SeenTime= DateToString.LastSeenString(date);
                                    contactAndSeenTime1.contact = finalContact1;
                                    contactAndSeenTime1.imageUrl=profileImg;



                                    if (contactAndSeenTimeArrayList.size()!=0) {
                                        for (int i = 0; i < contactAndSeenTimeArrayList.size(); i++) {
                                            if (contactAndSeenTime1.contact.getUserIdContact().equals(contactAndSeenTimeArrayList.get(i).contact.getUserIdContact())) {
                                                contactAndSeenTimeArrayList.get(i).Status = contactAndSeenTime1.Status;
                                                contactAndSeenTimeArrayList.get(i).SeenTime = contactAndSeenTime1.SeenTime;
                                                break;
                                            }
                                            if (i == contactAndSeenTimeArrayList.size() - 1) {
                                                contactAndSeenTimeArrayList.add(contactAndSeenTime1);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        contactAndSeenTimeArrayList.add(contactAndSeenTime1);
                                    }

                                    contactListAdapter.notifyDataSetChanged();


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
        });


        */





    }
}
