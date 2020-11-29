package com.example.hw

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class NotificationReceiver : BroadcastReceiver() {
    val ACTION_NEXT = "NEXT"
    val ACTION_PREV = "PREVIOUS"
    val ACTION_PLAY = "PLAY"
    val ACTION_STOP = "STOP"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(contxt: Context?, intent: Intent?) {
        val intent1 = Intent(contxt, SongService::class.java).apply {
            putExtra("actionName", intent?.action)
        }
        when (intent?.action) {
            ACTION_NEXT -> contxt?.startService(intent1)
            ACTION_PREV -> contxt?.startService(intent1)
            ACTION_PLAY -> contxt?.startService(intent1)
            ACTION_STOP -> contxt?.startService(intent1)
        }
    }
}