package com.mobile.messageclone.Chat;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.DrawProfilePicture;
import com.mobile.messageclone.R;
import com.mobile.messageclone.SignIn.RecyclerViewClickInterface;

import java.util.ArrayList;

public class VoiceCallAdapter extends RecyclerView.Adapter<VoiceCallAdapter.ViewHolder> {
    private ArrayList<CallHistory> callHistoryList;
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView profilePicture;
        public TextView name;
        public TextView callDate;
        public ImageView arrow;
        public ImageButton btnCall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicture = (CircularImageView)itemView.findViewById(R.id.profilePictureVoiceCall);
            name = (TextView)itemView.findViewById(R.id.txtNameVoiceCall);
            callDate = (TextView)itemView.findViewById(R.id.txtCallDateVoiceCall);
            arrow = (ImageView)itemView.findViewById(R.id.imgArrowVoiceCall);
            btnCall = (ImageButton)itemView.findViewById(R.id.imgbtnCallVoiceCall);
            profilePicture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
    public VoiceCallAdapter(Context context, ArrayList<CallHistory> contactAndCallDateList) {
        this.context = context;
        this.callHistoryList = contactAndCallDateList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.one_row_voice_call, parent, false);
        return new ViewHolder(v);
    }

    public void setRecyclerViewClickInterface(RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(callHistoryList.get(position).getName());
        holder.callDate.setText(callHistoryList.get(position).getCallDate());
        holder.arrow.setRotation(callHistoryList.get(position).getRotation());
        if (callHistoryList.get(position).getName().isEmpty()==true)
        {
            holder.profilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap(String.valueOf(callHistoryList.get(position).getName().charAt(0)).toUpperCase(),70, Color.WHITE));
        }
        else {
            holder.profilePicture.setImageBitmap(DrawProfilePicture.textAsBitmap((String
                    .valueOf(callHistoryList.get(position).getName().charAt(0)).toUpperCase()+(String.valueOf(callHistoryList
                    .get(position).getName().charAt(0)).toUpperCase())),70, Color.WHITE));
        }
    }


    @Override
    public int getItemCount() {
        return callHistoryList.size();
    }


}
