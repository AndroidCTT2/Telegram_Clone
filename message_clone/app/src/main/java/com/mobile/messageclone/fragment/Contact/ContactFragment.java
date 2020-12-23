package com.mobile.messageclone.fragment.Contact;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.R;
import com.mobile.messageclone.RecycerViewAdapater.ContactListAdapter;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;
import com.mobile.messageclone.ViewModel.ChatViewModel;
import com.mobile.messageclone.Model.ContactAndSeenTime;
import com.mobile.messageclone.Ulti.DateToString;
import com.mobile.messageclone.Model.Contact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class ContactFragment extends Fragment implements RecyclerViewClickInterface {





    private FloatingActionButton btnAddContact;

    private NavController navController;

    private ChatViewModel chatViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private RecyclerView RecyclerViewContact;

    private ArrayList<ContactAndSeenTime>contactAndSeenTimeArrayList;

    private ContactListAdapter contactListAdapter;

    private ValueEventListener valueEventListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
       contactAndSeenTimeArrayList=new ArrayList<>();
        setHasOptionsMenu(true);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Contact");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_contact,container,false);


        RecyclerViewContact=root.findViewById(R.id.ListContact);
        RecyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));

        contactListAdapter=new ContactListAdapter(getContext(),contactAndSeenTimeArrayList,getActivity());
        contactListAdapter.SetClickInterface(this);
        RecyclerViewContact.setAdapter(contactListAdapter);


        chatViewModel.IsDeleteListContactSeenTimeList.setValue(false);



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







    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (firebaseDatabase!=null && valueEventListener!=null)
        {
            firebaseDatabase.getReference().removeEventListener(valueEventListener);
        }
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("UserID",firebaseAuth.getCurrentUser().getUid());
        bundle.putString("ContactID",contactAndSeenTimeArrayList.get(position).contact.getUserIdContact());
        bundle.putString("ContactName",contactAndSeenTimeArrayList.get(position).contact.getFirstNickName()+" "+contactAndSeenTimeArrayList.get(position).contact.getLastNickName());

        NavController navController= Navigation.findNavController(getView());
        navController.navigate(R.id.action_fragment_contact_to_chat_fragment,bundle);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.contact_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.btnSortTime)
        {
            
        }

        return super.onOptionsItemSelected(item);
    }
}