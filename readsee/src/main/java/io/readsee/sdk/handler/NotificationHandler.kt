package io.readsee.sdk.handler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import io.readsee.sdk.R
import io.readsee.sdk.util.Constants
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun handle(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "Notification Title"
        val message = remoteMessage.notification?.body ?: "Notification Message"
        val iconResId = R.drawable.ic_stat_ic_notification // Or any other icon resource ID
        val url = remoteMessage.data["actionUrl"] ?: ""

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create the intent to open the web page
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        // Create a pending intent to launch the intent when the notification is clicked
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(iconResId)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationId = Random.nextInt()

        notificationManager.notify(notificationId, notification.build())
    }
}
