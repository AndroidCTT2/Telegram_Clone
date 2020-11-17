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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.viewHolder>{


    Context context;
    Activity activity;
    ArrayList<ContactAndSeenTime>contactAndSeenTimeList=new ArrayList();



    public ContactListAdapter(Context context,ArrayList<ContactAndSeenTime>contactList,Activity activity)
    {
        this.activity=activity;
        this.contactAndSeenTimeList=contactList;
        this.context=context;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.one_row_contact,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            String ContactName=contactAndSeenTimeList.get(position).contact.getFirstNickName()+" "+contactAndSeenTimeList.get(position).contact.getLastNickName();


            holder.NameContact.setText(ContactName);
            if (contactAndSeenTimeList.get(position).Status.equals("ONLINE")) {
                holder.Status.setText(contactAndSeenTimeList.get(position).Status+" ");
                holder.LastSeenTime.setText("");
            }
            else {
                holder.LastSeenTime.setText(contactAndSeenTimeList.get(position).SeenTime);
            }
            holder.profilePicture.setImageDrawable(DrawProfilePicture.drawProfileDynamicPicture(String.valueOf(ContactName.charAt(0)+String.valueOf(contactAndSeenTimeList.get(position).contact.getLastNickName().charAt(0))),activity,context));
    }

    @Override
    public int getItemCount() {
        return contactAndSeenTimeList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePicture;
        private TextView NameContact;
        private TextView LastSeenTime;
        private TextView Status;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            NameContact=itemView.findViewById(R.id.displayUserName);
            profilePicture=itemView.findViewById(R.id.ProfilePicture);
            LastSeenTime=itemView.findViewById(R.id.displayLastSeen);
            Status=itemView.findViewById(R.id.displayStatus);
        }
    }
}
