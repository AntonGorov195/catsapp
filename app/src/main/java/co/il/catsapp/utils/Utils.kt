package co.il.catsapp.utils

import android.view.View
import android.widget.ImageView
import co.il.catsapp.R
import com.bumptech.glide.Glide

fun setGlideImage(root: View, imageView:ImageView, url: String) {
    Glide.with(root).load(url).placeholder(R.mipmap.ic_cat).error(R.drawable.error).into(imageView)
}