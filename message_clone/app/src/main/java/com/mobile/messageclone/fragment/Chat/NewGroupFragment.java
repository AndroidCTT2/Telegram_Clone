package com.mobile.messageclone.fragment.Chat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.Model.ContactAndSeenTime;
import com.mobile.messageclone.Model.GroupMember;
import com.mobile.messageclone.R;
import com.mobile.messageclone.RecycerViewAdapater.ContactListNewGroupChatAdapter;
import com.mobile.messageclone.Ulti.RecyclerCheckBoxClick;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;
import com.mobile.messageclone.Model.Contact;
import com.mobile.messageclone.Ulti.DateToString;
import com.mobile.messageclone.ViewModel.ChatViewModel;
import com.mobile.messageclone.ViewModel.NewGroupViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Predicate;

public class NewGroupFragment extends Fragment implements RecyclerViewClickInterface, RecyclerCheckBoxClick
{



    private String contactGroupMember;
    private SparseBooleanArray sparseBooleanArray;

    private FloatingActionButton btnAddContact;

    private NavController navController;

    private ChatViewModel chatViewModel;
    private NewGroupViewModel newGroupViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private RecyclerView RecyclerViewContact;

    private ArrayList<ContactAndSeenTime>contactAndSeenTimeArrayList;

    private ContactListNewGroupChatAdapter contactListAdapter;

    private ValueEventListener valueEventListener;

    private TextView displayAddedUser;
    private FloatingActionButton btnNext;
    private ArrayList<ContactNameAndPos>contactnameList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        contactAndSeenTimeArrayList=new ArrayList<>();
        setHasOptionsMenu(true);
        contactnameList=new ArrayList<>();
        sparseBooleanArray=new SparseBooleanArray();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_new_group,container,false);

        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        newGroupViewModel=new ViewModelProvider(getActivity()).get(NewGroupViewModel.class);

        chatViewModel.titleBar.setValue("New group");
        RecyclerViewContact=root.findViewById(R.id.ListContact);
        RecyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));

        contactListAdapter=new ContactListNewGroupChatAdapter(getContext(),contactAndSeenTimeArrayList,getActivity());
        contactListAdapter.contactKeyMatch=null;
        contactListAdapter.SetClickInterface(this);
        contactListAdapter.SetCheckBoxInterface(this::CheckBoxClick);

        RecyclerViewContact.setAdapter(contactListAdapter);


        chatViewModel.IsDeleteListContactSeenTimeList.setValue(false);

        displayAddedUser=root.findViewById(R.id.textDisplayContactAdded);
        contactListAdapter.itemStateArray=sparseBooleanArray;
        for (int i=0;i<contactnameList.size();i++)
        {
            displayAddedUser.append(contactnameList.get(i).contactName+", ");

        }

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


        btnNext=root.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sparseBooleanArray.size()==0)
                {
                    Toast.makeText(getContext(),"Please choose at lease one member",Toast.LENGTH_SHORT).show();
                    return;
                }

                NavController navController=Navigation.findNavController(root);
                Bundle bundle=new Bundle();
                ArrayList<ContactAndSeenTime>contactAndSeenTimes=new ArrayList<>();
                ArrayList<String>contactName=new ArrayList<>();
                ArrayList<String>imgUrl=new ArrayList<>();
                ArrayList<GroupMember>groupMembers=new ArrayList<>();
                for (int i=0;i<contactListAdapter.contactAndSeenTimeList.size();i++)
                {
                    if (contactListAdapter.itemStateArray.get(i)==true)
                    {


                        contactName.add(contactListAdapter.contactAndSeenTimeList.get(i).contact.getFirstNickName()+" "+contactListAdapter.
                                contactAndSeenTimeList.get(i).contact.getLastNickName());
                        imgUrl.add(contactListAdapter.contactAndSeenTimeList.get(i).imageUrl);
                        contactAndSeenTimes.add(contactListAdapter.contactAndSeenTimeList.get(i));


                    }
                }
                newGroupViewModel.arrayListMutableLiveData.setValue(contactAndSeenTimes);

                bundle.putStringArrayList("contactName",contactName);
                bundle.putStringArrayList("imgUrl",imgUrl);
                navController.navigate(R.id.action_newGroupFragment_to_nameNewGroupFragment,bundle);
            }
        });












        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);

        btnAddContact=view.findViewById(R.id.btnAddContact);









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
        //bundle.putString("ContactName",contactAndSeenTimeArrayList.get(position).contact.getFirstNickName()+" "+contactAndSeenTimeArrayList.get(position).contact.getLastNickName());

       // NavController navController= Navigation.findNavController(getView());
//        navController.navigate(R.id.action_fragment_contact_to_chat_fragment,bundle);
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void CheckBoxClick(int position,boolean check) {



        if (check==true)
        {

            ContactNameAndPos contactNameAndPos=new ContactNameAndPos();
            contactNameAndPos.contactName=contactAndSeenTimeArrayList.get(position).contact.getFirstNickName()+" "+contactAndSeenTimeArrayList.get(position).contact.getLastNickName();
            contactNameAndPos.Pos=position;
            contactGroupMember=contactGroupMember+contactNameAndPos.contactName;
            contactnameList.add(contactNameAndPos);

            sparseBooleanArray.append(position,check);


        }
        else
        {
            Predicate<ContactNameAndPos>contactNameAndPosPredicate=new Predicate<ContactNameAndPos>() {
                @Override
                public boolean test(ContactNameAndPos contactNameAndPos) {
                    if (contactNameAndPos.Pos==position)
                    {
                        return true;
                    }
                    return false;
                }
            };

            contactnameList.removeIf(contactNameAndPosPredicate);
        }

        displayAddedUser.setText("");
        for (int i=0;i<contactnameList.size();i++)
        {
            displayAddedUser.append(contactnameList.get(i).contactName+", ");

        }
        contactListAdapter.itemStateArray=sparseBooleanArray;



    }


    private class ContactNameAndPos
    {
        public String contactName;
        public int Pos;
    }
}