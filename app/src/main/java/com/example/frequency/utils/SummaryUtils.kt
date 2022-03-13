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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.frequency.R
import com.google.android.material.R.id
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView

object SummaryUtils {

    const val SUCCESS = 0
    const val FAILURE = 1
    const val ERROR = 2
    const val ALERT = 3

    fun isValidEmail(email: String?): Boolean {
        email?.trim()
        return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun showSnackbar(
        view: View,
        message: String,
        iconPreset: Int? = null,
        elevation: Float? = null
    ) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackView = snackbar.view
        snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.dark_slay_gray))
        snackbar.setTextColor(Color.WHITE)
        //snackbar.
        snackView.setOnClickListener {
            snackbar.dismiss()
        }
        if (elevation != null) {
            snackView.elevation = 1f
        }
        snackView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val textView = snackView.findViewById<View>(id.snackbar_text) as TextView

        if (iconPreset != null) {
            when (iconPreset) {
                SUCCESS -> {
                    textView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_selected_24,
                        0,
                        0,
                        0
                    )
                    textView.compoundDrawablePadding =
                        view.context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
                }
                FAILURE -> {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_failure, 0, 0, 0)
                    textView.compoundDrawablePadding =
                        view.context.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
                }
                ALERT -> {
                    textView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_crisis_alert,
                        0,
                        0,
                        0
                    )
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
}

