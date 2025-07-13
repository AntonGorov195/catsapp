package co.il.catsapp.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import co.il.catsapp.R
import co.il.catsapp.utils.Constants

class AlarmSoundService : Service() {
    private var player: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.alarm_ringing))
            .setSmallIcon(R.mipmap.ic_cat)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(1, notification)

        stopMusicIfPlaying()
        player = MediaPlayer.create(this, R.raw.meow_alarm)
        player?.isLooping = true
        player?.start()
        return START_STICKY
    }

    override fun onDestroy() {
        stopMusicIfPlaying()
        super.onDestroy()
    }

    private fun stopMusicIfPlaying() {
        player?.let {
            if (it.isPlaying) it.stop()
            it.release()
        }
        player = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}