package org.grd_p.grd_project.Firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class NotificationService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("haeyoon", "entered");
//        Notification notification = new NotificationCompat.Builder(this)
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody())
//                .setSmallIcon(R.drawable.app_icon)
//                .build();
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        org.grd_p.grd_project.Firebase.NotificationManager.getInstance(getApplicationContext())
                .displayNotification(title, body);
    }

}