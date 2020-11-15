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
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.viewHolder>{

    Context context;
    Activity activity;
    List<Contact> contactList=new ArrayList<>();

    public ContactListAdapter(Context context,ArrayList<Contact>contactList,Activity activity)
    {
        this.activity=activity;
        this.contactList=contactList;
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
            String ContactName=contactList.get(position).getFirstNickName()+contactList.get(position).getLastNickName();
            holder.NameContact.setText(ContactName);
            holder.profilePicture.setImageDrawable(DrawProfilePicture.drawProfilePicture(String.valueOf(ContactName.charAt(0)),activity));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePicture;
        private TextView NameContact;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            NameContact=itemView.findViewById(R.id.displayUserName);
            profilePicture=itemView.findViewById(R.id.ProfilePicture);
        }
    }
}
