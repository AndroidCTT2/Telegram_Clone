package com.mobile.messageclone.RecycerViewAdapater;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobile.messageclone.Model.GroupImgUrl;
import com.mobile.messageclone.Model.LibMessage;
import com.mobile.messageclone.R;
import com.mobile.messageclone.Ulti.DrawProfilePicture;
import com.stfalcon.chatkit.messages.MessageHolders;

public class CustomIntComingMessageViewHolder extends MessageHolders.IncomingTextMessageViewHolder<LibMessage> {
    public CustomIntComingMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(LibMessage message) {
        super.onBind(message);

        Context context=(Context)payload;

        CircularImageView groupIcon=itemView.findViewById(R.id.messageImg);

        groupIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        if (message.iuser.getName()!=null)
        { groupIcon.setImageBitmap(DrawProfilePicture.textAsBitmap(message.iuser.getName(),70, Color.WHITE));
        }
        else
        {
            Glide.with(context).load(message.imgUrl).into(groupIcon);
        }

        TextView textView=itemView.findViewById(R.id.displayContactName);

        if (message.SenderName!=null) {
            textView.setText(message.SenderName);
        }

    }
}
