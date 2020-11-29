package com.example.hw

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.NotificationManager.IMPORTANCE_MIN
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat


object PlayerNotification {
    val CHANNEL_ID_1 = "channel1"
    val ACTION_NEXT = "NEXT"
    val ACTION_PREV = "PREVIOUS"
    val ACTION_PLAY = "PLAY"
    val ACTION_STOP = "STOP"

    @SuppressLint("WrongConstant")
    fun createNotification(context: Context, cover: Int, singer: String?, name: String?, icon : Int): NotificationManager {
        val channel = getNotificationChannel(context)
        val mediaSession = MediaSessionCompat(context, "player")
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationCompat.Builder(context, channel?.id ?: "")
        }
        else{
            NotificationCompat.Builder(context)
        }

        builder.setVisibility(Notification.VISIBILITY_PUBLIC)
        .addAction(R.drawable.previous, "Previous", pIntent(context, ACTION_PREV))
        .addAction(icon, "Play", pIntent(context, ACTION_PLAY))
        .addAction(R.drawable.next, "Next", pIntent(context, ACTION_NEXT))
            .addAction(R.drawable.stop, "Stop", pIntent(context, ACTION_STOP))
            .setContentTitle(singer)
            .setSmallIcon(R.drawable.ic_music_note)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, cover))
            .setContentText(name)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
            .setMediaSession(mediaSession.sessionToken))
            .setContentIntent(contentIntent(context))
            .build()

        val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(5, builder.build())
        return notificationManager
    }

    private fun getNotificationChannel(context: Context): NotificationChannel? {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.getNotificationChannel(CHANNEL_ID_1) ?: run {

                val new = NotificationChannel(
                    CHANNEL_ID_1,
                    "channel(1)",
                    NotificationManager.IMPORTANCE_LOW

                )
                notificationManager.createNotificationChannel(new)
                new
            }
        } else null
    }

    private fun pIntent(context: Context, action: String) = PendingIntent.getBroadcast(
        context,
        34,
        Intent(context, NotificationReceiver::class.java).setAction(action),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun contentIntent(context: Context) = PendingIntent.getActivity(
        context,
        0,
        Intent(context, PlayerActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}