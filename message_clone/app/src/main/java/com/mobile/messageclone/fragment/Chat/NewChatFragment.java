package com.mobile.messageclone.fragment.Chat;

import android.content.Context;
import android.icu.util.Calendar;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instacart.library.truetime.TrueTimeRx;
import com.mobile.messageclone.Model.ContactAndSeenTime;
import com.mobile.messageclone.R;
import com.mobile.messageclone.RecycerViewAdapater.ContactListAdapter;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;
import com.mobile.messageclone.Model.Contact;
import com.mobile.messageclone.ViewModel.ChatViewModel;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class NewChatFragment extends Fragment implements RecyclerViewClickInterface {
    @Nullable


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private LinkedList<Contact> contactLinkedList;
    private ChatViewModel chatViewModel;

    private RecyclerView RecyclerViewContact;
    private ArrayList<ContactAndSeenTime> contactAndSeenTimeArrayList;
    private ContactListAdapter contactListAdapter;
    private ValueEventListener valueEventListener;
    //private RecyclerViewClickInterface recyclerViewClickInterface;
    //private ContactListHomeAdapter contactListHomeAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ChatViewModel chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("New chat");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        contactAndSeenTimeArrayList=new ArrayList<>();
        //contactListAdapter = new ContactListAdapter(getContext(), contactAndSeenTimeArrayList,getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_chat, container, false);

        RecyclerViewContact=root.findViewById(R.id.ListContact);
        RecyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));

        contactListAdapter=new ContactListAdapter(getContext(),contactAndSeenTimeArrayList,getActivity());
        contactListAdapter.SetClickInterface(this);
        RecyclerViewContact.setAdapter(contactListAdapter);
        //RecyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));
        //chatViewModel.IsDeleteListContactSeenTimeList.setValue(false);

        /*firebaseDatabase.getReference().child("CONTACT").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()==true)
                {

                    for (DataSnapshot childSnapShot:snapshot.getChildren())
                    {

                        Contact contact=new Contact();

                        for (DataSnapshot child2:childSnapShot.getChildren()) {
                            contact = child2.getValue(Contact.class);



                            Contact finalContact1 = contact;
                            firebaseDatabase.getReference().child("USER").child(contact.getUserIdContact()).child("STATUS").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists() == true) {
                                        String DbDate;
                                        String DbTime;
                                        String status;
                                        ContactAndSeenTime contactAndSeenTime = null;
                                        DbDate = snapshot.child("Date").getValue(String.class);
                                        DbTime = snapshot.child("Time").getValue(String.class);
                                        status=snapshot.child("State").getValue(String.class);
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy,X-HH-mm-ss");
                                        DbDate = DbDate + "-" + DbTime;

                                        Date date = new Date();
                                        try {
                                            date = dateFormat.parse(DbDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Date todayDate = TrueTimeRx.now();
                                        LocalDateTime fromDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                        LocalDateTime toDateTime = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                                        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

                                        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
                                        tempDateTime = tempDateTime.plusYears(years);

                                        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
                                        tempDateTime = tempDateTime.plusMonths(months);

                                        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
                                        tempDateTime = tempDateTime.plusDays(days);


                                        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
                                        tempDateTime = tempDateTime.plusHours(hours);

                                        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
                                        tempDateTime = tempDateTime.plusMinutes(minutes);

                                        long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);
                                        ContactAndSeenTime contactAndSeenTime1=new ContactAndSeenTime();
                                        contactAndSeenTime1.Status=status;
                                        Log.d("Phone",contactAndSeenTime1.Status);
                                        Log.d("Phone", finalContact1.getFirstNickName());
                                        contactAndSeenTime1.contact=finalContact1;



                                        if (days>=1 && days<=2) {
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                            Log.d("Phone","yesterday at " + simpleDateFormat.format(date));
                                            contactAndSeenTime1.SeenTime="yesterday at " + simpleDateFormat.format(date);
                                        } else if (days < 1 && hours>=1) {

                                            Log.d("Phone", "at " + hours + " hours ago");
                                            contactAndSeenTime1.SeenTime="at " + hours + " hours ago";
                                        }
                                        else if (days<1 && hours<1) {
                                            Log.d("Phone", +minutes + " minutes ago");
                                            contactAndSeenTime1.SeenTime = "at " + minutes + " minutes ago";
                                        }

                                        else {
                                            contactAndSeenTime1.SeenTime="at " + fromDateTime.getDayOfMonth() + "-" + fromDateTime.getMonthValue() + "-" + fromDateTime.getYear();
                                            Log.d("Phone",  "yesterday at " + fromDateTime.getDayOfMonth() + "-" + fromDateTime.getMonthValue() + "-" + fromDateTime.getYear());
                                        }

                                        contactAndSeenTimeArrayList.clear();
                                        contactAndSeenTimeArrayList.add(contactAndSeenTime1);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        */
        firebaseDatabase.getReference().child("CONTACT").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() == true) {

                    for (DataSnapshot childSnapShot : snapshot.getChildren()) {

                        Contact contact = new Contact();
                        contact = childSnapShot.getValue(Contact.class);




                        Contact finalContact1 = contact;
                        firebaseDatabase.getReference().child("USER").child(contact.getUserIdContact()).child("STATUS").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists() == true) {
                                    String DbDate;
                                    String DbTime;
                                    String status;
                                    ContactAndSeenTime contactAndSeenTime = null;

                                    long  timeStamp=snapshot.child("Time").getValue(Long.class);
                                    Instant instant=Instant.ofEpochMilli(timeStamp);
                                    Date date=Date.from(instant);
                                    status = snapshot.child("State").getValue(String.class);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy,X-HH-mm-ss");

                                    Date todayDate;
                                    if (TrueTimeRx.isInitialized() == true){
                                       todayDate = TrueTimeRx.now();
                                    }
                                    else{
                                        todayDate = Calendar.getInstance().getTime();
                                    }

                                    LocalDateTime fromDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                    LocalDateTime toDateTime = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                                    LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

                                    long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
                                    tempDateTime = tempDateTime.plusYears(years);

                                    long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
                                    tempDateTime = tempDateTime.plusMonths(months);

                                    long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
                                    tempDateTime = tempDateTime.plusDays(days);


                                    long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
                                    tempDateTime = tempDateTime.plusHours(hours);

                                    long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
                                    tempDateTime = tempDateTime.plusMinutes(minutes);

                                    long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);
                                    ContactAndSeenTime contactAndSeenTime1 = new ContactAndSeenTime();
                                    contactAndSeenTime1.Status = status;
                                    Log.d("Phone", contactAndSeenTime1.Status);
                                    Log.d("Phone", finalContact1.getFirstNickName());
                                    contactAndSeenTime1.contact = finalContact1;


                                    if (days >= 1 && days <= 2) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                        Log.d("Phone", "yesterday at " + simpleDateFormat.format(date));
                                        contactAndSeenTime1.SeenTime = "yesterday at " + simpleDateFormat.format(date);
                                    } else if (days < 1 && hours >= 1) {

                                        Log.d("Phone", "at " + hours + " hours ago");
                                        contactAndSeenTime1.SeenTime = "at " + hours + " hours ago";
                                    } else if (days < 1 && hours < 1) {
                                        Log.d("Phone", +minutes + " minutes ago");
                                        contactAndSeenTime1.SeenTime = "at " + minutes + " minutes ago";
                                    } else {
                                        contactAndSeenTime1.SeenTime = "at " + fromDateTime.getDayOfMonth() + "-" + fromDateTime.getMonthValue() + "-" + fromDateTime.getYear();
                                        Log.d("Phone", "yesterday at " + fromDateTime.getDayOfMonth() + "-" + fromDateTime.getMonthValue() + "-" + fromDateTime.getYear());
                                    }
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




        return root;

        //chatViewModel.titleBar.setValue("Contact");
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("UserID",firebaseAuth.getCurrentUser().getUid());
        bundle.putString("ContactID",contactAndSeenTimeArrayList.get(position).contact.getUserIdContact());
        bundle.putString("ContactName",contactAndSeenTimeArrayList.get(position).contact.getFirstNickName()+" "+contactAndSeenTimeArrayList.get(position).contact.getLastNickName());

        NavController navController= Navigation.findNavController(getView());
        navController.navigate(R.id.action_fragment_newChat_to_chat_fragment,bundle);
    }

    @Override
    public void onLongItemClick(int position) {

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
