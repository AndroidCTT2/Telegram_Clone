package com.mobile.messageclone.RecycerViewAdapater;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.messageclone.Model.Message;
import com.mobile.messageclone.R;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {


    private static final int UI_SENDER=1;
    private static final int UI_RECEIVER=2;

    public String SenderUID;


    LinkedList<Message>   messageLinkedList;

    public Activity activity;
    public Context context;

    public MessageAdapter(LinkedList<Message>messageLinkedList,Activity activity,Context context,String UID)
    {
        this.messageLinkedList=new LinkedList<>();
        this.messageLinkedList=messageLinkedList;
        this.activity=activity;
        this.context=context;
        this.SenderUID=UID;
    }

    @NonNull
    @Override
    public MessageAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {




            LayoutInflater layoutInflater=LayoutInflater.from(context);
            View root=layoutInflater.inflate(R.layout.one_row_message_sender,parent,false);
            return new viewHolder((root));




    }

    @Override
    public int getItemViewType(int position) {

       return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.viewHolder holder, int position) {

            Log.d("Phone",messageLinkedList.get(position).getSenderID());
            holder.textMessage.setText(messageLinkedList.get(position).getMessage());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd,MM,yyyy");
          //  String date = simpleDateFormat.format(messageLinkedList.get(position).getSendTime());
            //holder.timeSend.setText(date);


    }

    @Override
    public int getItemCount() {
        return messageLinkedList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView textMessage;
        private TextView timeSend;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

                textMessage=itemView.findViewById(R.id.sender_message);
                timeSend=itemView.findViewById(R.id.sender_time);



        }
    }
}
