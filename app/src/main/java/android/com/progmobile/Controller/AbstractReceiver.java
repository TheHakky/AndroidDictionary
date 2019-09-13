package android.com.progmobile.Controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.com.progmobile.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public abstract class AbstractReceiver extends BroadcastReceiver {

    protected String mTextToShow = "";

    protected void setTextToShow(String text) {
        this.mTextToShow = text;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    protected void showNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 31, intent, 0);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            mNotificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context, notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        builder = builder
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.add_word_icon)
                .setContentTitle("My dictionary quiz")
                .setContentText(mTextToShow)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        mNotificationManager.notify(31, builder.build());
    }
}
