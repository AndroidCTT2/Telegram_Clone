package com.mobile.messageclone.fragment.VoiceCall;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.R;
import com.mobile.messageclone.RecycerViewAdapater.NewVoiceCallAdapter;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;
import com.mobile.messageclone.Model.Contact;
import com.mobile.messageclone.Activity.ChatActivity;
import com.mobile.messageclone.ViewModel.ChatViewModel;
import com.mobile.messageclone.Model.ContactAndSeenTime;
import com.mobile.messageclone.Ulti.DateToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewVoiceCallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewVoiceCallFragment extends Fragment implements RecyclerViewClickInterface {

    public RecyclerView recyclerView;
    public NewVoiceCallAdapter newVoiceCallAdapter;
    public ArrayList<ContactAndSeenTime> contactAndSeenTimeArrayList;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseAuth firebaseAuth;
    public ChatViewModel chatViewModel;


    public static NewVoiceCallFragment newInstance(String param1, String param2) {
        NewVoiceCallFragment fragment = new NewVoiceCallFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.contactAndSeenTimeArrayList = new ArrayList<ContactAndSeenTime>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatViewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Select Contact");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_voice_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewNewVoiceCall);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newVoiceCallAdapter = new NewVoiceCallAdapter(getContext(), contactAndSeenTimeArrayList, getActivity());
        newVoiceCallAdapter.setClickInterface(this);
        recyclerView.setAdapter(newVoiceCallAdapter);

        firebaseDatabase.getReference().child("CONTACT").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot childSnapShot : snapshot.getChildren()) {
                    if (childSnapShot.exists() == true){
                        Contact contact = new Contact();
                        contact = childSnapShot.getValue(Contact.class);

                        Contact finalContact1 = contact;
                        firebaseDatabase.getReference().child("USER").child(contact.getUserIdContact()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists() == true) {

                                    long  timeStamp=snapshot.child("STATUS").child("Time").getValue(Long.class);
                                    String status=snapshot.child("STATUS").child("State").getValue(String.class);
                                    Instant instant=Instant.ofEpochMilli(timeStamp);
                                    Date date=Date.from(instant);
                                    ContactAndSeenTime contactAndSeenTime1 = new ContactAndSeenTime();
                                    contactAndSeenTime1.Status = status;
                                    contactAndSeenTime1.imageUrl = snapshot.child("ProfileImg").getValue(String.class);

                                    contactAndSeenTime1.SeenTime= DateToString.LastSeenString(date);
                                    contactAndSeenTime1.contact = finalContact1;


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
                                    newVoiceCallAdapter.notifyDataSetChanged();
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
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(getContext(), "Calling " + this.contactAndSeenTimeArrayList.get(position).contact.getFirstNickName()
//                +" "+this.contactAndSeenTimeArrayList.get(position).contact.getLastNickName(), Toast.LENGTH_SHORT).show();
        ((ChatActivity)getActivity()).callUser(this.contactAndSeenTimeArrayList.get(position).contact.getUserIdContact());
    }

    @Override
    public void onLongItemClick(int position) {

    }
}