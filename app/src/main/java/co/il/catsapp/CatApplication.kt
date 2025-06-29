package co.il.catsapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import co.il.catsapp.utils.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CatApplication : Application()