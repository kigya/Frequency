package com.example.frequency.utils

import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.frequency.R
import com.google.android.material.snackbar.Snackbar

fun <T> MutableLiveData<T>.share(): LiveData<T> = this

object SettingTags {
    const val AUTOLOGIN = "AUTOLOGIN"
    const val LANGUAGE = "LANGUAGE"
    const val NOTIFICATION_VOLUME = "NOTIFICATION_VOLUME"
    const val DISABLE_NOTIFICATIONS = "DISABLE_NOTIFICATIONS"

    // user
    const val USERNAME = "USERNAME"
    const val EMAIL = "EMAIL"
    const val ICON_URI = "ICON_URI"
    const val TOKEN = "TOKEN"
}

fun isValidEmail(email: String?): Boolean {
    email?.trim()
    return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun showSnackbar(view: View, message: String) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snackbar.view.setOnClickListener {
        snackbar.dismiss()
    }
    snackbar.show()
}

fun setUserImageByGlide(requireContext: Context, view: ImageView, uri: Uri?, timeout: Int = 0) {
    Glide.with(requireContext)
        .load(uri)
        .timeout(timeout)
        .error(R.drawable.ic_unknown_user_photo)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view)
}