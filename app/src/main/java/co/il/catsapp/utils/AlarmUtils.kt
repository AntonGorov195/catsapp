package co.il.catsapp.utils

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import co.il.catsapp.R
import co.il.catsapp.data.models.Cat
import co.il.catsapp.services.AlarmReceiver

object CatAlarmHelper {

    fun setAlarm(context: Context, cat: Cat, triggerAtMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(Constants.CAT_BROADCAST_ID, cat.id)
            putExtra(Constants.CAT_BROADCAST_NAME, cat.name)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            cat.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, catId: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            catId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    // show the state in a toast
    fun makeToast(context: Context?, message: String? = "toast!") {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

//    fun notify(
//        context: Context,
//        title: String,
//        msg: String,
//        iconRes: Int = R.drawable.ic_cat_foreground,
//    ) {
//        val build = NotificationCompat.Builder(
//            context,
//            Constants.NOTIFICATION_CHANNEL_ID
//        )
//            .setContentTitle(title)
//            .setContentText(msg)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//        build.setSmallIcon(iconRes)
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            makeToast(context, "posting notification permission denied")
//            return
//        }
//
//        NotificationManagerCompat.from(context)
//            .notify(Constants.NOTIFICATION_ID, build.build())
//    }
}
