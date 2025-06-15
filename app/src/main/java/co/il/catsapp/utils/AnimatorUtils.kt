package co.il.catsapp.utils

import android.graphics.drawable.Animatable
import android.widget.ImageView

fun AnimateCatIcon(icon: ImageView) {
    val drawable = icon.drawable
    if (drawable is Animatable) {
        drawable.start()
    }
}