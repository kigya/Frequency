package com.example.frequency.utils

import android.Manifest
import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


object UtilPermission {

    val ALL_PERMISSIONS = arrayOf(
        INTERNET,
        ACCESS_NETWORK_STATE,
        READ_EXTERNAL_STORAGE,
    )

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

}