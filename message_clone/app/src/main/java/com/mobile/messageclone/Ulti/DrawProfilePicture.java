package com.mobile.messageclone.Ulti;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.mobile.messageclone.R;


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
    public static  Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}
