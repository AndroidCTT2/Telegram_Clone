package com.mobile.messageclone.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.mobile.messageclone.R;

public class OreoNotification extends ContextWrapper {
    public OreoNotification(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createChannel();
        }
    }

    private static  final String Channel_ID="com.mobile.messageclone";
    private static final String Channel_NAME="chatapp";
    private NotificationManager notificationManager;

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel()
    {
        NotificationChannel channel=new NotificationChannel(Channel_ID,Channel_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager()
    {
        if (notificationManager==null)
        {
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public NotificationCompat.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent, Uri uri, String icon, NotificationCompat.MessagingStyle messagingStyle)
    {
        return new  NotificationCompat.Builder(getApplicationContext(),Channel_ID).setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(messagingStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(uri)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_DEFAULT);
    }



}
