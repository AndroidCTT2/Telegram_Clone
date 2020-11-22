package com.mobile.messageclone.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.R;
import com.mobile.messageclone.SignIn.RecyclerViewClickInterface;

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
    private LinkedList<ContactLastMessTime>contactLastMessTimeLinkedList=new LinkedList<>();
    private String LastChat="";
    private ChildEventListener childEventListener;





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        contactLinkedList=new LinkedList<>();
        contactListHomeAdapter=new ContactListHomeAdapter(contactLinkedList,getActivity(),getContext());
        contactListHomeAdapter.SetUpContactLastMessTimeList(contactLastMessTimeLinkedList);
        contactListHomeAdapter.SetClickInterface(this);
        firebaseDatabase.getReference().child("CONTACT").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()==true) {

                    contactLinkedList.clear();
                    for (DataSnapshot child1 : snapshot.getChildren())
                    {

                        Contact contact=new Contact();
                        contact= child1.getValue(Contact.class);
                        contactLinkedList.add(contact);
                    }
                    contactListHomeAdapter.contacts=contactLinkedList;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("ChatID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot:snapshot.getChildren())
                {
                    String id=childSnapshot.child("ChatID").getValue(String.class);
                    String idContact=childSnapshot.child("ContactID").getValue(String.class);
                    Log.d("Phone",id);
                    Log.d("Phone",idContact);
                    chatidlist.add(id);
                    childEventListener=firebaseDatabase.getReference().child("MESSAGE").child(id).limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            contactLastMessTimeLinkedList.clear();
                            Message message;
                            message=snapshot.getValue(Message.class);
                            ContactLastMessTime contactLastMessTime=new ContactLastMessTime();
                            contactLastMessTime.LastMess=message.getMessage();
                            contactLastMessTime.contact=new Contact();
                            contactLastMessTime.contact.setUserIdContact(message.getReceiverID());
                            contactLastMessTime.LastSendTime=message.getSendTime();
                            contactLastMessTimeLinkedList.add(contactLastMessTime);
                            contactListHomeAdapter.SetUpContactLastMessTimeList(contactLastMessTimeLinkedList);
                            contactListHomeAdapter.notifyDataSetChanged();
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




        return root;
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("UserID",firebaseAuth.getCurrentUser().getUid());
        bundle.putString("ContactID",contactLinkedList.get(position).getUserIdContact());
        bundle.putString("ContactName",contactLinkedList.get(position).getFirstNickName()+" "+contactLinkedList.get(position).getLastNickName());

        NavController navController= Navigation.findNavController(getView());
        navController.navigate(R.id.action_fragment_home_to_chat_fragment,bundle);
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
        firebaseDatabase.getReference().removeEventListener(childEventListener);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseDatabase.getReference().removeEventListener(childEventListener);
    }

}
