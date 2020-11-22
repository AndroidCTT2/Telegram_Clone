package com.mobile.messageclone;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;

import com.amulyakhare.textdrawable.TextDrawable;


public class DrawProfilePicture {


    public static TextDrawable drawProfileDynamicPicture(String str,Activity activity,Context context)
    {
        TextDrawable textDrawable=TextDrawable.builder().beginConfig().textColor(Color.WHITE).toUpperCase().bold().fontSize(50).endConfig().buildRound(str,activity.getResources().getColor(R.color.colorPrimary,context.getTheme()));
        return textDrawable;
    }



    public static TextDrawableForStaticImage drawProfilePicture(String str, Activity activity, Context context) {

        TextDrawableForStaticImage textDrawableForStaticImage = new TextDrawableForStaticImage(context);
        textDrawableForStaticImage.setText(str);
        textDrawableForStaticImage.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        textDrawableForStaticImage.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        textDrawableForStaticImage.setTextColor(Color.WHITE);

        return textDrawableForStaticImage;
    }
}
