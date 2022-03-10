package com.example.frequency.utils

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frequency.R
import com.google.android.material.R.id
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView


fun <T> MutableLiveData<T>.share(): LiveData<T> = this

const val SUCCESS = "SUCCESS"
const val FAILURE = "FAILURE"
const val ERROR = "ERROR"

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

fun showSnackbar(view: View, message: String, iconPreset: String? = null) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    val snackView = snackbar.view
    snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.dark_slay_gray))
    snackbar.setTextColor(Color.WHITE)
    //snackbar.
    snackView.setOnClickListener {
        snackbar.dismiss()
    }
    snackView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    val textView = snackView.findViewById<View>(id.snackbar_text) as TextView

    if (!iconPreset.isNullOrBlank()) {
        when (iconPreset) {
            SUCCESS -> {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_selected_24, 0, 0, 0)
                textView.compoundDrawablePadding =
                    view.context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
            }
            FAILURE -> {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_failure, 0, 0, 0)
                textView.compoundDrawablePadding =
                    view.context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
            }
            ERROR -> {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning, 0, 0, 0)
                textView.compoundDrawablePadding =
                    view.context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
            }
        }
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

fun setToolbarUserIcon(
    requireContext: Context,
    item: MenuItem,
    uri: Uri?,
    timeout: Int = 0
) {
    val profileImage: CircleImageView =
        item.actionView.findViewById(R.id.toolbar_profile_image)
    setUserImageByGlide(requireContext, profileImage, uri, timeout)
}