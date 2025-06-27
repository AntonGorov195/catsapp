package co.il.catsapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.room.Room
import co.il.catsapp.CatApplication
import co.il.catsapp.R
import co.il.catsapp.data.local_db.AppDatabase
import co.il.catsapp.data.models.Cat
import co.il.catsapp.ui.MainActivity
import co.il.catsapp.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val catId = intent.getStringExtra(Constants.CAT_BROADCAST_ID) ?: return
        val catName = intent.getStringExtra(Constants.CAT_BROADCAST_NAME) ?: return

        // TODO: Show a notification or start a service
        Toast.makeText(context, "Alarm for cat $catName!", Toast.LENGTH_LONG).show()
//
//        // Start music service
        val musicIntent = Intent(context, AlarmSoundService::class.java)
        context.startService(musicIntent)
        playAlarmMusic(context, catName, catId)

        // Build notification that navigates to CatFragment
        val navIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(Constants.CAT_BROADCAST_NAV, catId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            catId.hashCode(),
            navIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Alarm for ${catId}")
            .setContentText("Tap to stop the alarm")
            .setSmallIcon(R.drawable.cat_loading)
//            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(catId.hashCode(), notification)
    }

    private fun playAlarmMusic(context: Context, name: String, id: String) {
        val intent = Intent(context, AlarmSoundService::class.java).apply {
            putExtra(Constants.CAT_BROADCAST_ID, id)
            putExtra(Constants.CAT_BROADCAST_NAME, name)
        }
        ContextCompat.startForegroundService(context, intent)
    }
}