package ru.geekbrains.weather

import android.os.Build.VERSION.SDK_INT
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.load
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

fun View.createAndShow(
    text: String, actionText: String, action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

// SnackBar без action
fun View.showSnackBarWithoutAction(
    text: String,
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, text, length).show()
}

// SnackBar с текстом из ресурсов
fun View.showSnackBarWithResText(
    textFromResources: Int,
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(this, resources.getString(textFromResources), length).show()
}

fun ImageView.loadImageByUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadSVG(url: String) {
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            add(SvgDecoder(context))
        }
        .build()
    this.load(url, imageLoader)
}

fun ImageView.loadGIF(imageId: Int) {
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }
        .build()
    this.load(imageId, imageLoader)
}
