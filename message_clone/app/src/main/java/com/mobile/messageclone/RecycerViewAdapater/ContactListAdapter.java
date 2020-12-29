package com.mobile.messageclone.RecycerViewAdapater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.Activity.ChatActivity;
import com.mobile.messageclone.Model.ContactAndSeenTime;
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.viewHolder>{


    Context context;
    Activity activity;
    ArrayList<ContactAndSeenTime> contactAndSeenTimeList=new ArrayList();
    RecyclerViewClickInterface recyclerViewClickInterface;

    public String stringOwnerID;




    public ContactListAdapter(Context context,ArrayList<ContactAndSeenTime>contactList,Activity activity)
    {
        this.activity=activity;
        this.contactAndSeenTimeList=contactList;
        this.context=context;
        stringOwnerID=null;

    }
    public void SetClickInterface(RecyclerViewClickInterface recyclerViewClickInterface)
    {
        this.recyclerViewClickInterface=recyclerViewClickInterface;
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

            if (stringOwnerID==null)
            {
                holder.displayAlterText.setVisibility(View.GONE);
            }
            else if (stringOwnerID.equals(contactAndSeenTimeList.get(position).contact.getUserIdContact()))
            {
                holder.displayAlterText.setVisibility(View.VISIBLE);
                holder.displayAlterText.setText("Owner");
            }
            else
            {
                holder.displayAlterText.setVisibility(View.GONE);
            }


            if (contactAndSeenTimeList.get(position).Status.equals(ChatActivity.STATUS_ONLINE)) {
                holder.Status.setText(contactAndSeenTimeList.get(position).Status+" ");
                holder.LastSeenTime.setText("");
            }
            else {
                holder.Status.setText("Last seen ");
                holder.LastSeenTime.setText(contactAndSeenTimeList.get(position).SeenTime);
            }
            if (contactAndSeenTimeList.get(position).imageUrl.isEmpty()==false)
            {
                Glide.with(context).load(contactAndSeenTimeList.get(position).imageUrl).into(holder.profilePicture);
            }
            else {
                if (contactAndSeenTimeList.get(position).contact.getLastNickName().isEmpty() == true) {

                    holder.profilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap(String.valueOf(ContactName.charAt(0)).toUpperCase(), 70, Color.WHITE));
                } else {

                    holder.profilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap((String.valueOf(ContactName.charAt(0)).toUpperCase() + (String.valueOf(contactAndSeenTimeList.get(position).contact.getLastNickName().charAt(0)).toUpperCase())), 70, Color.WHITE));
                }
            }
            }

    @Override
    public int getItemCount() {
        return contactAndSeenTimeList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private CircularImageView profilePicture;
        private TextView NameContact;
        private TextView LastSeenTime;
        private TextView Status;
        private TextView displayAlterText;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            NameContact=itemView.findViewById(R.id.displayUserName);
            profilePicture=itemView.findViewById(R.id.ProfilePicture);
            profilePicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            LastSeenTime=itemView.findViewById(R.id.displayLastSeen);
            Status=itemView.findViewById(R.id.displayStatus);
            displayAlterText=itemView.findViewById(R.id.displayAlterText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
