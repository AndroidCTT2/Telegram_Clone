package com.mobile.messageclone.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactFragment extends Fragment {


    private ArrayList<Contact>contactArrayList;
    private ArrayList<String>LastSeenArrayList;

    private FloatingActionButton btnAddContact;

    private NavController navController;

    private  ChatViewModel chatViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private RecyclerView RecyclerViewContact;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        contactArrayList=new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_contact,container,false);

        ChatViewModel chatViewModel =new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Contact");
        RecyclerViewContact=root.findViewById(R.id.ListContact);
        firebaseDatabase.getReference().child("CONTACT").orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()==true)
                {


                    for (DataSnapshot childSnapShot:snapshot.getChildren())
                    {

                        Contact contact=childSnapShot.getValue(Contact.class);

                        contactArrayList.add(contact);
                    }
                    firebaseDatabase.getReference().child("USER").child(firebaseAuth.getCurrentUser().getUid()).child("STATUS").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()==true)
                            {
                                String a="";
                                for (DataSnapshot childData:snapshot.getChildren())
                                {
                                    a= childData.child("Date").getValue(String.class);
                                }
                                Log.d("Phone",a);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    ContactListAdapter contactListAdapter=new ContactListAdapter(getContext(),contactArrayList,getActivity());
                    RecyclerViewContact.setAdapter(contactListAdapter);
                    RecyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));

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