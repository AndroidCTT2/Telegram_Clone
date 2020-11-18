package com.mobile.messageclone.Chat;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.messageclone.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.SignIn.RecyclerViewClickInterface;

import java.util.LinkedList;

public class ContactListHomeAdapter extends RecyclerView.Adapter<ContactListHomeAdapter.viewHolder> {

    LinkedList<Contact> contacts=new LinkedList<>();
    Activity activity;
    Context context;
    RecyclerViewClickInterface recyclerViewClickInterface;


    public ContactListHomeAdapter(LinkedList<Contact>contacts,Activity activity,Context context){
        this.activity=activity;
        this.contacts=contacts;
        this.context=context;
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
        String name=contacts.get(position).getFirstNickName()+" "+contacts.get(position).getLastNickName();
        holder.ContactName.setText(name);
        holder.ProfileImageView.setImageDrawable(DrawProfilePicture.drawProfileDynamicPicture(String.valueOf(name.charAt(0)),activity,context));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private ImageView ProfileImageView;
        private TextView ContactName;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ProfileImageView=itemView.findViewById(R.id.ProfilePicture);
            ContactName=itemView.findViewById(R.id.displayContactName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
