package com.mobile.messageclone.fragment.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.messageclone.Model.Contact;
import com.mobile.messageclone.Model.ContactAndSeenTime;
import com.mobile.messageclone.Model.Group;
import com.mobile.messageclone.Model.Message;
import com.mobile.messageclone.R;
import com.mobile.messageclone.RecycerViewAdapater.ContactListAdapter;
import com.mobile.messageclone.RecycerViewAdapater.ContactListHomeAdapter;
import com.mobile.messageclone.Ulti.GenerateChatID;
import com.mobile.messageclone.ViewModel.NewGroupViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class NameNewGroupFragment extends Fragment {

    private NewGroupViewModel newGroupViewModel;
    private RecyclerView listGroupContact;
    private ContactListAdapter contactListAdapter;
    private FloatingActionButton btnComplete;
    private TextInputEditText inputGroupName;

    private NavController navController;

    private FirebaseDatabase firebaseDatabase;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newGroupViewModel=new ViewModelProvider(getActivity()).get(NewGroupViewModel.class);
        firebaseDatabase=FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_name_new_group,container,false);
        listGroupContact=root.findViewById(R.id.ListContact);
        contactListAdapter=new ContactListAdapter(getContext(),newGroupViewModel.arrayListMutableLiveData.getValue(),getActivity());
        listGroupContact.setAdapter(contactListAdapter);
        listGroupContact.setLayoutManager(new LinearLayoutManager(getContext()));




        inputGroupName=root.findViewById(R.id.inputGroupName);
        btnComplete=root.findViewById(R.id.btnComplete);

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= Navigation.findNavController(view);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputGroupName.getText().toString().isEmpty()==true)
                {
                    Toast.makeText(getContext(),"Please provide group name",Toast.LENGTH_SHORT).show();
                    return;
                }
                String randomKey=firebaseDatabase.getReference().child("GROUP_CHAT").push().getKey();
                String key=GenerateChatID.GenerateKey(randomKey,"",GenerateChatID.ID_CHAT_GROUP);
                ArrayList<String>memberID=new ArrayList<>();

                for (int i=0;i<newGroupViewModel.arrayListMutableLiveData.getValue().size();i++)
                {
                    memberID.add(newGroupViewModel.arrayListMutableLiveData.getValue().get(i).contact.getUserIdContact());

                    firebaseDatabase.getReference().child("USER").child(memberID.get(i)).child("GROUP").push().setValue(key);

                }
                memberID.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                firebaseDatabase.getReference().child("USER").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GROUP").push().setValue(key);




                Group group=new Group();

                group.setGroupImg("");
                group.setGroupMemberIdList(memberID);
                group.setGroupName(inputGroupName.getText().toString().trim());
                group.setGroupID(key);

                group.setIdAdmin(FirebaseAuth.getInstance().getCurrentUser().getUid());
                firebaseDatabase.getReference().child("CONVERSATION_ID").push().setValue(group.getGroupID());
                firebaseDatabase.getReference().child("GROUP_CHAT").child(key).setValue(group);


                Message message=new Message();
                message.setMessage("Your new group chat is ready");
                message.setSenderID("ADMIN");
                message.setReceiverID(key);

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-X");

                message.setSendTime(simpleDateFormat.format(Calendar.getInstance().getTime()));
                firebaseDatabase.getReference().child("MESSAGE").child(key).push().setValue(message);



                Bundle bundle=new Bundle();
                bundle.putString("UserID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                bundle.putString("ContactID",key);
                bundle.putInt("Type",ContactListHomeAdapter.CHAT_GROUP);
                bundle.putString("ContactName", inputGroupName.getText().toString().trim());



                navController.navigate(R.id.action_nameNewGroupFragment_to_fragment_home);





            }
        });
    }
}

