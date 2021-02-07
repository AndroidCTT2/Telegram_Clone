package com.mobile.messageclone.fragment.Chat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
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
import com.mobile.messageclone.Ulti.ActivityUlti;
import com.mobile.messageclone.Ulti.DateToString;
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;
import com.mobile.messageclone.ViewModel.ChatViewModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatProfileGroupFragmment extends Fragment {

    private ContactListAdapter contactListAdapter;

    private RecyclerView contactList;

    private String ContactID;
    private String UserID;

    private Boolean EditModeOn;

    private LinearLayout editMode;
    private LinearLayout viewMode;

    private FirebaseDatabase firebaseDatabase;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    private ChatViewModel chatViewModel;

    private ImageView profilePicture;

    private String ContactName;
    private ExtendedFloatingActionButton btnEditContact;

    private TextInputEditText editGroupName;
    private FloatingActionButton btnChoosePicture;
    private FloatingActionButton btnAddMember;


    private ArrayList<ContactAndSeenTime>contactAndSeenTimeArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null)
        {
            ContactID=getArguments().getString("ContactID");
            UserID=getArguments().getString("UserID");
            ContactName=getArguments().getString("ContactName");
        }
        firebaseDatabase=FirebaseDatabase.getInstance();

        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);

        chatViewModel.IsHideAppBar.setValue(true);
        chatViewModel.IsHideNavBar.setValue(true);

        contactAndSeenTimeArrayList=new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.chat_profile_group,container,false);

        viewMode=root.findViewById(R.id.ViewMode);
        editMode=root.findViewById(R.id.EditMode);

        appBarLayout=root.findViewById(R.id.appBarLayout);
        toolbar=root.findViewById(R.id.toolbar);
        btnChoosePicture=root.findViewById(R.id.btnChoosePicture);

        editGroupName=root.findViewById(R.id.editGroupName);

        profilePicture=root.findViewById(R.id.ProfilePicture);

        btnAddMember=root.findViewById(R.id.btnAddGroupMember);

        EditModeOn=false;

        btnEditContact=root.findViewById(R.id.editContactExtended);

        editMode.setVisibility(View.GONE);
        btnChoosePicture.setVisibility(View.GONE);






        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactList=view.findViewById(R.id.ListContact);
        contactListAdapter=new ContactListAdapter(getContext(),contactAndSeenTimeArrayList,getActivity());

        contactList.setAdapter(contactListAdapter);

        contactList.setLayoutManager(new LinearLayoutManager(getContext()));

        contactListAdapter.SetClickInterface(new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                return;
            }

            @Override
            public void onLongItemClick(int position) {
                return;
            }
        });


        if (isAdded()) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        }

        collapsingToolbarLayout=view.findViewById(R.id.collapsing);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());

                Log.d("Percent",String.valueOf(percentage));
                profilePicture.setScaleX(1-percentage);
                profilePicture.setScaleY(1-percentage);


            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    btnEditContact.shrink();


                }
                else
                {
                    //Expanded
                    btnEditContact.extend();

                }
            }
        });

        btnEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditModeOn==false)
                {
                    EditModeOn=true;
                    btnChoosePicture.setVisibility(View.VISIBLE);
                    editMode.setVisibility(View.VISIBLE);
                    viewMode.setVisibility(View.GONE);


                    collapsingToolbarLayout.setTitle("");
                    btnEditContact.setText("Done");
                    btnEditContact.setIcon(getActivity().getDrawable(R.drawable.ic_baseline_check_24));

                }
                else if(EditModeOn==true)
                {
                    if (editGroupName.getText().toString().isEmpty()==false)
                    {
                        EditModeOn=false;
                        btnEditContact.setText("Edit");
                        btnEditContact.setIcon(getActivity().getDrawable(R.drawable.ic_baseline_edit_24));
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("groupName",editGroupName.getText().toString().trim());
                        firebaseDatabase.getReference().child("GROUP_CHAT").child(ContactID).updateChildren(hashMap);
                        collapsingToolbarLayout.setTitle(editGroupName.getText().toString().trim());
                        btnEditContact.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        Toast.makeText(getContext(),"Require group name",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController= Navigation.findNavController(view);


                firebaseDatabase.getReference().child("GROUP_CHAT").child(ContactID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Group group=snapshot.getValue(Group.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("GroupKey",ContactID);
                        bundle.putStringArrayList("MemberList",group.getGroupMemberIdList());

                        navController.navigate(R.id.action_chatProfileGroupFragmment_to_editGroupMember,bundle);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });



        firebaseDatabase.getReference().child("GROUP_CHAT").child(ContactID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()==true)
                {
                    Group group=snapshot.getValue(Group.class);
                    if (group.getIdAdmin().equals(UserID))
                    {
                        btnEditContact.setVisibility(View.GONE);
                    }
                    if (group.getGroupImg().isEmpty()==false)
                    {
                        Glide.with(getContext()).load(group.getGroupImg()).into(profilePicture);
                    }
                    else
                    {
                        profilePicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        profilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap(String.valueOf(ContactName.charAt(0)).toUpperCase(),140, Color.WHITE));


                    }
                    contactListAdapter.stringOwnerID=group.getIdAdmin();
                    ArrayList<String>contactIDList=group.getGroupMemberIdList();
                    collapsingToolbarLayout.setTitle(group.getGroupName());
                    editGroupName.setText(group.getGroupName());
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







    }

    @Override
    public void onDetach() {
        super.onDetach();
        //firebaseDatabase.getReference().removeEventListener(contactListener);
        ((ActivityUlti)getActivity()).ReattachToolbar(ContactName);
    }
}
