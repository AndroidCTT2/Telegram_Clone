package com.mobile.messageclone.Chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.messageclone.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.SignIn.RecyclerViewClickInterface;

import java.util.LinkedList;

public class ContactListHomeAdapter extends RecyclerView.Adapter<ContactListHomeAdapter.viewHolder> {

    LinkedList<Contact> contacts=new LinkedList<>();

    LinkedList<ContactLastMessTime>contactLastMessTimeLinkedList=new LinkedList<>();

    Activity activity;
    Context context;
    RecyclerViewClickInterface recyclerViewClickInterface;


    public ContactListHomeAdapter(LinkedList<Contact> contacts, Activity activity, Context context){
        this.activity=activity;
        this.contacts=contacts;
        this.context=context;
    }

    public void SetUpContactLastMessTimeList(LinkedList<ContactLastMessTime>contactLastMessTimes)
    {
        contactLastMessTimeLinkedList=contactLastMessTimes;
        for (int i=0;i<contacts.size();i++)
        {
            contactLastMessTimeLinkedList.get(i).contact.setFirstNickName(contacts.get(i).getFirstNickName());
            contactLastMessTimeLinkedList.get(i).contact.setLastNickName(contacts.get(i).getLastNickName());
        }
    }

    public void SetClickInterface(RecyclerViewClickInterface recyclerViewClickInterface)
    {
        this.recyclerViewClickInterface=recyclerViewClickInterface;
    }


    @NonNull
    @Override
    public ContactListHomeAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View root=layoutInflater.inflate(R.layout.one_row_main_message,parent,false);
        return  new viewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListHomeAdapter.viewHolder holder, int position) {
        String name=contactLastMessTimeLinkedList.get(position).contact.getFirstNickName()+" "+contactLastMessTimeLinkedList.get(position).contact.getLastNickName();
        holder.ContactName.setText(name);
        holder.LastMessage.setText(contactLastMessTimeLinkedList.get(position).LastMess);
        holder.ProfileImageView.setImageDrawable(DrawProfilePicture.drawProfileDynamicPicture(String.valueOf(name.charAt(0)),activity,context));

        
        holder.SendTime.setText();
    }

    @Override
    public int getItemCount() {
        return contactLastMessTimeLinkedList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView ProfileImageView;
        private TextView ContactName;
        private TextView LastMessage;
        private TextView SendTime;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ProfileImageView=itemView.findViewById(R.id.ProfilePicture);
            ContactName=itemView.findViewById(R.id.displayContactName);
            LastMessage=itemView.findViewById(R.id.displayLastMessage);
            SendTime=itemView.findViewById(R.id.sender_time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());

                }
            });

        }
    }
}
