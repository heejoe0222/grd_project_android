package org.grd_p.grd_project.Firebase;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.grd_p.grd_project.mainActivity;

public class NotificationManager {

    private Context mCtx;
    private static NotificationManager mInstance;

    private NotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized NotificationManager getInstance(Context context){
        if(mInstance == null) {
            mInstance = new NotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                .setSmallIcon(org.grd_p.grd_project.R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(body);

        Intent intent = new Intent(mCtx, mainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        android.app.NotificationManager mNotificationManager = (android.app.NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager != null){
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
