package com.example.hw.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.hw.Actions
import com.example.hw.activity.PlayerActivity
import com.example.hw.R
import com.example.hw.Song
import com.example.hw.service.SongService


object PlayerNotification {
    private val CHANNEL_ID_1 = "channel1"

    fun createNotification(context: Context, song: Song?, icon : Int): Notification? {
        val channel = getNotificationChannel(context)
        val mediaSession = MediaSessionCompat(context, "player")
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationCompat.Builder(context, channel?.id ?: "")
        }
        else{
            NotificationCompat.Builder(context)
        }

        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .addAction(R.drawable.previous, "Previous", pIntent(context, Actions.PREVIOUS.name))
        .addAction(icon, "Play", pIntent(context, Actions.PLAY.name))
        .addAction(R.drawable.next, "Next", pIntent(context, Actions.NEXT.name))
            .addAction(R.drawable.stop, "Stop", pIntent(context, Actions.STOP.name))
            .setContentTitle(song?.singer)
            .setSmallIcon(R.drawable.ic_music_note)
            .setLargeIcon(song?.cover?.let { BitmapFactory.decodeResource(context.resources, it) })
            .setContentText(song?.name)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
            .setMediaSession(mediaSession.sessionToken))
            .setContentIntent(contentIntent(context))
            .build()

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run{
            notify(5, builder.build())
        }
        return builder.build();
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

    private fun pIntent(context: Context, action: String): PendingIntent? {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                context,
                34,
                Intent(context, SongService::class.java).setAction(action),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                context, 34,
                Intent(context, SongService::class.java).setAction(action),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun contentIntent(context: Context) = PendingIntent.getActivity(
        context,
        0,
        Intent(context, PlayerActivity::class.java).putExtra("action", "initViews"),
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}