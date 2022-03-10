package com.example.frequency.preferences.custom_preference

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.ImageView
import androidx.preference.Preference


class CircleImageViewPreference(context: Context, attrs: AttributeSet?) : Preference(context, attrs) {

    private var imageView: ImageView? = null
    private var imageBitmap: Bitmap? = null

    /*var imageClickListener = object: View. {

    }View.OnClickListener? = null

    //onBindViewHolder() will be called after we call setImageClickListener() and setBitmap() from SettingsFragment
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        imageView = holder.findViewById(R.id.icon) as ImageView
        imageView!!.setOnClickListener(imageClickListener)
        imageView!!.setImageBitmap(imageBitmap)
    }

    fun setImageClickListener(onClickListener: View.OnClickListener?) {
        imageClickListener = onClickListener
    }

    fun setBitmap(bitmap: Bitmap?) {
        imageBitmap = bitmap
    }*/
}