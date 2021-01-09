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
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.Ulti.RecyclerViewClickInterface;
import com.mobile.messageclone.Activity.ChatActivity;
import com.mobile.messageclone.Model.ContactAndSeenTime;

import java.util.ArrayList;

public class NewVoiceCallAdapter extends RecyclerView.Adapter<NewVoiceCallAdapter.ViewHolder> {
    Context context;
    ArrayList<ContactAndSeenTime> contactAndSeenTimeList;
    Activity activity;
    RecyclerViewClickInterface recyclerViewClickInterface;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView lastSeen;
        CircularImageView profilePicture;
        ImageView statusSpark;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.txtNameNewVoiceCall);
            lastSeen = (TextView)itemView.findViewById(R.id.txtLastSeenTimeNewVoiceCall);
            profilePicture = (CircularImageView)itemView.findViewById(R.id.profilePictureNewVoiceCall);
            profilePicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            statusSpark = (ImageView)itemView.findViewById(R.id.imgStatusSpark);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewClickInterface.onItemClick(getAdapterPosition());
        }
    }


    public NewVoiceCallAdapter(Context context, ArrayList<ContactAndSeenTime> contactAndSeenTimeList, Activity activity) {
        this.context = context;
        this.contactAndSeenTimeList = contactAndSeenTimeList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.one_row_new_voice_call, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ContactName=contactAndSeenTimeList.get(position).contact.getFirstNickName()+" "+contactAndSeenTimeList.get(position).contact.getLastNickName();

        holder.name.setText(ContactName);
        if (contactAndSeenTimeList.get(position).Status.equals(ChatActivity.STATUS_ONLINE)) {
            holder.lastSeen.setText("Online");
            holder.statusSpark.setImageResource(R.drawable.ic_baseline_greenspark_1_24);
        }
        else {
            holder.lastSeen.setText("Last seen: " + contactAndSeenTimeList.get(position).SeenTime);
            holder.statusSpark.setImageResource(R.drawable.ic_baseline_brightness_1_24);
        }
        if (contactAndSeenTimeList.get(position).contact.getLastNickName().isEmpty()==true)
        {
            holder.profilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap(String.valueOf(ContactName.charAt(0)).toUpperCase(),70, Color.WHITE));
        }
        else {
            Glide.with(context).load(contactAndSeenTimeList.get(position).imageUrl).into(holder.profilePicture);
            //holder.profilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap((String.valueOf(ContactName.charAt(0)).toUpperCase()+(String.valueOf(contactAndSeenTimeList.get(position).contact.getLastNickName().charAt(0)).toUpperCase())),70, Color.WHITE));
        }
    }

    @Override
    public int getItemCount() {
        return this.contactAndSeenTimeList.size();
    }

    public void setClickInterface(RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

}
