package com.mobile.messageclone;

import android.app.Activity;

import com.amulyakhare.textdrawable.TextDrawable;

public class DrawProfilePicture {




    public static TextDrawable drawProfilePicture(String str,Activity activity)
    {

        TextDrawable textDrawable=TextDrawable.builder().beginConfig().bold().fontSize(55).toUpperCase().endConfig().buildRound(str,activity.getResources().getColor(R.color.colorPrimary));
        return textDrawable;
    }
}
