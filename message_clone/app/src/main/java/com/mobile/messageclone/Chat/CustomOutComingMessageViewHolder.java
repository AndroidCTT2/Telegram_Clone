package com.mobile.messageclone.Chat;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
        if (message.getIsSeen()==true)
        {
            progressBar.setVisibility(View.GONE);
            Log.d("TAG","HUHU");
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
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
