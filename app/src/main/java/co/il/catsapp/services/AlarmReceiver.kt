package co.il.catsapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import co.il.catsapp.R
import co.il.catsapp.utils.Constants

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val catId = intent.getStringExtra(Constants.CAT_BROADCAST_ID) ?: return
        val catName = intent.getStringExtra(Constants.CAT_BROADCAST_NAME) ?: return

        Toast.makeText(context,
            context.getString(R.string.alarm_for_cat, catName), Toast.LENGTH_LONG).show()
        // Start music service
        val musicIntent = Intent(context, AlarmSoundService::class.java)
        context.startService(musicIntent)
        playAlarmMusic(context, catName, catId)
    }

    private fun playAlarmMusic(context: Context, name: String, id: String) {
        val intent = Intent(context, AlarmSoundService::class.java).apply {
            putExtra(Constants.CAT_BROADCAST_ID, id)
            putExtra(Constants.CAT_BROADCAST_NAME, name)
        }
        ContextCompat.startForegroundService(context, intent)
    }
}