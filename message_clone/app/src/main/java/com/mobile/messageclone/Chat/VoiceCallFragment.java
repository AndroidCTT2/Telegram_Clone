package com.mobile.messageclone.Chat;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.messageclone.R;
import com.mobile.messageclone.SignIn.RecyclerViewClickInterface;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VoiceCallFragment extends Fragment implements RecyclerViewClickInterface {

    private FloatingActionButton btnNewCall;

    private NavController navController;

    private  ChatViewModel chatViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private RecyclerView recyclerViewVoiceCall;

    private ArrayList<CallHistory> callHistoryList;
    private VoiceCallAdapter voiceCallAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.chatViewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        chatViewModel.titleBar.setValue("Calls");
        return inflater.inflate(R.layout.fragment_voice_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.btnNewCall = (FloatingActionButton)view.findViewById(R.id.btnNewVoiceCall);
        this.navController = Navigation.findNavController(view);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.recyclerViewVoiceCall = (RecyclerView)view.findViewById(R.id.recyclerViewVoiceCall);
        this.recyclerViewVoiceCall.setLayoutManager(new LinearLayoutManager(getContext()));
        this.callHistoryList = new ArrayList<CallHistory>();
        this.voiceCallAdapter = new VoiceCallAdapter(getContext(), this.callHistoryList);
        this.recyclerViewVoiceCall.setAdapter(this.voiceCallAdapter);
        this.voiceCallAdapter.setRecyclerViewClickInterface(this);

        btnNewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_fragment_voice_call_to_fragment_new_voice_call);
            }
        });

        firebaseDatabase.getReference().child("CALL_HISTORY").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String remoteUserId = dataSnapshot.getKey();
                    final String[] name = {""};
                    firebaseDatabase.getReference().child("USER").child(remoteUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            name[0] = snapshot.child("firstName").getValue(String.class) + " "
                                    + snapshot.child("lastName").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    firebaseDatabase.getReference().child("CALL_HISTORY").child(firebaseAuth.getUid()).child(remoteUserId)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        CallHistory callHistory = new CallHistory();
                                        callHistory.setCallDate(DateToString.dateToString(dataSnapshot1.child("callDate").getValue(String.class)));
                                        callHistory.setName(name[0]);
                                        callHistory.setRemoteID(remoteUserId);
                                        callHistoryList.add(callHistory);
                                        if (firebaseAuth.getUid().equals(dataSnapshot1.child("callerID").getValue(String.class))) {
                                            callHistory.setRotation(45);
                                        }
                                        else {
                                            callHistory.setRotation(225);
                                        }
                                    }
                                    voiceCallAdapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(int position) {
        ((ChatActivity)getActivity()).callUser(this.callHistoryList.get(position).getRemoteID());
    }

    @Override
    public void onLongItemClick(int position) {

    }
}