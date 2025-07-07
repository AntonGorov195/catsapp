package co.il.catsapp.utils

import android.content.Context

private const val PREF_NAME = "cats_settings"
private const val NUM_CATS = "cats_count"

fun getCatCount(context: Context): Int {
    val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return pref.getInt(NUM_CATS, 10)
}

fun setCatCount(context: Context, count: Int) {
    val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
    editor.putInt(NUM_CATS, count)
    editor.apply()
}