package co.il.catsapp.utils

import android.graphics.drawable.Animatable
import android.view.View
import android.widget.ImageView

fun animateCatIcon(icon: ImageView) {
    val drawable = icon.drawable
    if (drawable is Animatable) {
        drawable.start()
    }
}

fun animateRotation(view: View, deg: Float, totalDur: Long) {
    val segDur = totalDur / 3
    view.animate().rotation(deg)
        .setDuration(segDur).withEndAction {
            view.animate().rotation(-deg)
                .setDuration(segDur).withEndAction {
                    view.animate()
                        .rotation(0f)
                        .setDuration(segDur).start()
                }.start()
        }.start()
}

fun animateOffsetX(view: View, x: Float, totalDur: Long) {
    val segDur = totalDur / 3
    view.animate().translationX(x)
        .setDuration(segDur).withEndAction {
            view.animate().translationX(-x)
                .setDuration(segDur).withEndAction {
                    view.animate()
                        .translationX(0f)
                        .setDuration(segDur).start()
                }.start()
        }.start()
}

fun animateScale(view: View, scale: Float, totalDur: Long) {
    val segDur = totalDur / 2
    view.animate().scaleX(scale).scaleY(scale)
        .setDuration(segDur).withEndAction {
            view.animate().scaleX(1f).scaleY(1f)
                .setDuration(segDur).start()
        }.start()
}

fun animateRandom(view: View, deg: Float, x: Float, scale: Float, totalDur: Long) {
    val anim = (0 until 3).random()
    when (anim) {
        0 -> animateOffsetX(view, x, totalDur)
        1 -> animateScale(view, scale, totalDur)
        2 -> animateRotation(view, deg, totalDur)
    }
}