package com.mobile.messageclone.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.instacart.library.truetime.TrueTimeRx;
import com.mobile.messageclone.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactFragment extends Fragment {



    ;

    private FloatingActionButton btnAddContact;

    private NavController navController;

    private  ChatViewModel chatViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private RecyclerView RecyclerViewContact;

    private ArrayList<ContactAndSeenTime>contactAndSeenTimeArrayList;

    private ContactListAdapter contactListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
       contactAndSeenTimeArrayList=new ArrayList<>();



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_contact,container,false);

        ChatViewModel chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Contact");
        RecyclerViewContact=root.findViewById(R.id.ListContact);
        RecyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));

        contactListAdapter=new ContactListAdapter(getContext(),contactAndSeenTimeArrayList,getActivity());

        RecyclerViewContact.setAdapter(contactListAdapter);



        firebaseDatabase.getReference().child("CONTACT").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
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








        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);

        btnAddContact=view.findViewById(R.id.btnAddContact);

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    navController.navigate(R.id.action_fragment_contact_to_fragment_find_contact);
            }
        });



        firebaseDatabase.getReference().child("CONTACT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()==true)
                {

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



    }

    @Override
    public void onResume() {
        super.onResume();

    }
}