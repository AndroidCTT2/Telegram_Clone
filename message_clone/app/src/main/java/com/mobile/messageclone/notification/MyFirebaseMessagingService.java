package com.mobile.messageclone.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobile.messageclone.Activity.ChatActivity;

import java.util.LinkedList;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public static LinkedList<Message> ListMessage;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null && sented.equals(firebaseUser.getUid())){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            {
                sendOreoNotification(remoteMessage);
            }
            else {
                sendNotification(remoteMessage);
            }
        }
    }
    private void sendOreoNotification(RemoteMessage remoteMessage)
    {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String body = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");
        String sendted=remoteMessage.getData().get("sented");
        String iconUrl=remoteMessage.getData().get("iconUrl");
        Log.d("sendted",icon);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, ChatActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putString("userId", user);
        bundle.putString("name",title);

        intent.putExtras(bundle);
        //intent.putExtras("NewMessage",bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        /*String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String body = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);*/

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Glide.with(this)
                .asBitmap().circleCrop()
                .load(icon).addListener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {


                Person sender=new Person.Builder().setIcon(null).setName(title).setKey(user).build();


                // Person sender=new Person.Builder().setIcon(null).setName(title).setKey(user).build();
                NotificationCompat.MessagingStyle style= new NotificationCompat.MessagingStyle(sender);
                style.addMessage(new NotificationCompat.MessagingStyle.Message(body,System.currentTimeMillis(),sender));
                OreoNotification oreoNotification=new OreoNotification(getApplicationContext());
                NotificationCompat.Builder builder=oreoNotification.getOreoNotification(title,body,pendingIntent,defaultSound,icon,style);
                oreoNotification.getManager().notify(0,builder.build());
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                return false;
            }
        })
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        IconCompat iconCompat=IconCompat.createWithBitmap(resource);
                        Person sender=new Person.Builder().setIcon(iconCompat).setName(title).setKey(user).build();


                        // Person sender=new Person.Builder().setIcon(null).setName(title).setKey(user).build();
                        NotificationCompat.MessagingStyle style= new NotificationCompat.MessagingStyle(sender);
                        style.addMessage(new NotificationCompat.MessagingStyle.Message(body,System.currentTimeMillis(),sender));
                        OreoNotification oreoNotification=new OreoNotification(getApplicationContext());
                        NotificationCompat.Builder builder=oreoNotification.getOreoNotification(title,body,pendingIntent,defaultSound,icon,style);
                        oreoNotification.getManager().notify(0,builder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });



    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String body = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Bundle bundle = new Bundle();
        bundle.putString("userId", user);
        bundle.putString("name",title);
        intent.putExtra("NewMessage",bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(Integer.parseInt(user),builder.build());

    }


}
