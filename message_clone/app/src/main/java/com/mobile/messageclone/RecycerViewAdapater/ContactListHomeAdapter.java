package com.mobile.messageclone.RecycerViewAdapater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.Model.ContactLastMessTime;
import com.mobile.messageclone.Ulti.DateToString;
import com.mobile.messageclone.Model.Message;
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class ContactListHomeAdapter extends RecyclerView.Adapter<ContactListHomeAdapter.viewHolder> {



    public LinkedList<ContactLastMessTime>contactLastMessTimeLinkedList=new LinkedList<>();

    Activity activity;
    Context context;
    RecyclerViewClickInterface recyclerViewClickInterface;


    public ContactListHomeAdapter(Activity activity, Context context){
        this.activity=activity;

        this.context=context;
    }

    public void SortTime()
    {
        Comparator<ContactLastMessTime> compareLastMessTime = new Comparator<ContactLastMessTime>() {
            @Override
            public int compare(ContactLastMessTime o1, ContactLastMessTime o2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-X");

                try {
                    Date firstDate = dateFormat.parse(o1.LastMess.getSendTime());
                    Date secondDate = dateFormat.parse(o2.LastMess.getSendTime());
                    return firstDate.compareTo(secondDate);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;

            }
        };
        contactLastMessTimeLinkedList.sort(compareLastMessTime.reversed());
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
        holder.SendTime.setText(DateToString.dateToString(contactLastMessTimeLinkedList.get(position).LastMess.getSendTime()));
        Message message=contactLastMessTimeLinkedList.get(position).LastMess;

        if (message.getStatus()!= Message.STATUS.Seen && message.getReceiverID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

            holder.LastMessage.setTypeface(Typeface.DEFAULT_BOLD);
            holder.ContactName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.SendTime.setTypeface(Typeface.DEFAULT);
            holder.LastMessage.setAlpha(1.0f);
        }
        else{
            holder.LastMessage.setAlpha(0.6f);
        }

        holder.LastMessage.setText(contactLastMessTimeLinkedList.get(position).LastMess.getMessage());

        if (contactLastMessTimeLinkedList.get(position).profileImg==null) {

            holder.ProfileImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            if (contactLastMessTimeLinkedList.get(position).contact.getLastNickName().isEmpty()==false)
            {
                holder.ProfileImageView.setImageBitmap(DrawProfilePicture.textAsBitmap((String.valueOf(name.charAt(0)).toUpperCase()+(String.valueOf(contactLastMessTimeLinkedList.get(position).contact.getLastNickName().charAt(0)).toUpperCase())),70, Color.WHITE));
            }
            else
            {
                holder.ProfileImageView.setImageBitmap(DrawProfilePicture.textAsBitmap((String.valueOf(name.charAt(0)).toUpperCase()),70,Color.WHITE));
            }

        }
        else
        {
           // Toast.makeText(context,contactLastMessTimeLinkedList.get(position).profileImg,Toast.LENGTH_SHORT).show();
            holder.ProfileImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(context).load(contactLastMessTimeLinkedList.get(position).profileImg).into(holder.ProfileImageView);
        }






    }

    @Override
    public int getItemCount() {
        return contactLastMessTimeLinkedList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private CircularImageView ProfileImageView;
        private TextView ContactName;
        private TextView LastMessage;
        private TextView SendTime;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ProfileImageView=itemView.findViewById(R.id.ProfilePicture);
            ContactName=itemView.findViewById(R.id.displayContactName);
            LastMessage=itemView.findViewById(R.id.displayLastMessage);
            SendTime=itemView.findViewById(R.id.sender_time);
            ProfileImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());

                }
            });

        }
    }
}
