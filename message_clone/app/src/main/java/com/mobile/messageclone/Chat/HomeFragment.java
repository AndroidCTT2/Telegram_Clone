package com.mobile.messageclone.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mobile.messageclone.R;

import java.util.LinkedList;

public class HomeFragment extends Fragment {

    private ChatViewModel chatViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private LinkedList<Contact> contactLinkedList;
    private RecyclerView HomeContactList;
    private ContactListHomeAdapter contactListHomeAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        contactLinkedList=new LinkedList<>();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Message");

        HomeContactList=root.findViewById(R.id.recyclerHomeContact);

        contactListHomeAdapter=new ContactListHomeAdapter(contactLinkedList,getActivity(),getContext());

        HomeContactList.setAdapter(contactListHomeAdapter);
        HomeContactList.setLayoutManager(new LinearLayoutManager(getContext()));

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


                        contactListHomeAdapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();

    }
}