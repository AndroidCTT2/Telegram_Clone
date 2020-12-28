package com.mobile.messageclone.RecycerViewAdapater;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mobile.messageclone.Model.LibMessage;
import com.mobile.messageclone.R;
import com.stfalcon.chatkit.messages.MessageHolders;

public class CustomOutComingMessageViewHolder extends MessageHolders.OutcomingTextMessageViewHolder<LibMessage> {
    public CustomOutComingMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(LibMessage message) {

        super.onBind(message);

        ImageView tickIcon=itemView.findViewById(R.id.SeenStatus);
        ProgressBar progressBar=itemView.findViewById(R.id.ProgressBarSending);
        switch(message.Status)
        {
            case Seen:
            {
                tickIcon.setImageResource(R.drawable.ic_double_tick_indicator);
                tickIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
            }
            case Sending:
            {
                tickIcon.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            }
            case Delivered:
            {
                tickIcon.setImageResource(R.drawable.ic_baseline_check_24);
                progressBar.setVisibility(View.GONE);
                tickIcon.setVisibility(View.VISIBLE);
                break;
            }
        }

        //if (message.getIsSeen()==true)
        //{
       //     tickIcon.setImageResource(R.drawable.ic_double_tick_indicator);
       // }
       // else if (message.getIsSeen()==false)
       // {
        //    tickIcon.setImageResource(R.drawable.ic_baseline_check_24);
       // }

    }
}
