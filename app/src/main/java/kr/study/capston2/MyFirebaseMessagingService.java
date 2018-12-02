package kr.study.capston2;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.android.volley.VolleyLog.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String title =remoteMessage.getData().get("title").toString();
            String text =remoteMessage.getData().get("text").toString();
            String what =remoteMessage.getData().get("what").toString();
            String room_name =remoteMessage.getData().get("room_name").toString();


            sendNotification(title,text,what,room_name);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String title, String text,String what, String room_name) {
        String channelId = "channel";
        String channelName = "Channel Name";

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferences message = getSharedPreferences("setting", Activity.MODE_PRIVATE);

        ////// 팝업 , 진동 없이 알림만 상태바에 표시////////////////////////////////////////////
        if(message.getString("message","true").equals("true")) {
            channelId = "message_true";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                int importance;

                    importance = NotificationManager.IMPORTANCE_LOW;

                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);

                notificationManager.createNotificationChannel(mChannel);
            }

        }


        //////////// 팝업 없이 진동과 알림 표시////////////////////////////////////////////
        if(message.getString("message","true").equals("true") && message.getString("vibrate","true").equals("true")) {
            channelId = "vibrate_true";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                int importance;

                importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);

                notificationManager.createNotificationChannel(mChannel);
            }

        }


        //////////// 진동 없이 팝업과 알림 표시////////////////////////////////////////////
        if(message.getString("message","true").equals("true") && message.getString("popup","true").equals("true") && message.getString("vibrate","true").equals("false")) {

            channelId = "popup_true";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                int importance;

                importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);

                mChannel.setSound(null, null);
                mChannel.enableVibration(false);
                notificationManager.createNotificationChannel(mChannel);
            }

        }


        //////////// 팝업 , 진동과  함 께 알림 표시////////////////////////////////////////////
        if(message.getString("message","true").equals("true") && message.getString("popup","true").equals("true") && message.getString("vibrate","true").equals("true")) {
            channelId = "all_true";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                int importance;

                importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);


                notificationManager.createNotificationChannel(mChannel);
            }

        }


        /////////////////전체 알림 거 부 일 경우///////////////////////////////////////
        if (message.getString("message", "true").equals("false")) {
            channelId = "message_false";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                int importance;

                importance = NotificationManager.IMPORTANCE_NONE;

                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, importance);

                notificationManager.createNotificationChannel(mChannel);
            }

        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), channelId);

        Intent intent = new Intent(this, ChickenChatActivity.class);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        intent.putExtra("userID", auto.getString("inputId", null));
        intent.putExtra("what",what);
        intent.putExtra("room_name",room_name);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        int requestID = (int) System.currentTimeMillis();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID/* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);



       notificationBuilder
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.capston_logo_round)
                        .setColor(0x0368f5)
                        .setContentIntent(pendingIntent)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setPriority(Notification.PRIORITY_HIGH);


        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}


