package com.example.print.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.print.MainActivity;
import com.example.print.R;

public class NotificationHelper {
    private static final String CHANNEL_ID = "print_orders_channel";
    private static final String CHANNEL_NAME = "Order Updates";
    private static final String PROMO_CHANNEL_ID = "print_promo_channel";
    private static final String PROMO_CHANNEL_NAME = "Promotions & Offers";

    public static void showNotification(Context context, String title, String message) {
        showNotification(context, title, message, CHANNEL_ID, CHANNEL_NAME);
    }

    public static void showPromoNotification(Context context, String title, String message) {
        showNotification(context, title, message, PROMO_CHANNEL_ID, PROMO_CHANNEL_NAME);
    }

    private static void showNotification(Context context, String title, String message, String channelId, String channelName) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
