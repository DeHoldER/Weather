package ru.geekbrains.weather

import android.view.View
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