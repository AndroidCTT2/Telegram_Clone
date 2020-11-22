package com.mobile.messageclone.Chat;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mobile.messageclone.R;
import com.stfalcon.chatkit.messages.MessageHolders;

public class CustomOutComingMessageViewHolder extends MessageHolders.OutcomingTextMessageViewHolder<LibMessage> {
    public CustomOutComingMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(LibMessage message) {
        Log.d("TAG","HUHU");
        super.onBind(message);

        ImageView tickIcon=itemView.findViewById(R.id.SeenStatus);


    }
}
