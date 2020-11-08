package com.dp.agro.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.dp.agro.R
import java.io.File
import java.io.FileOutputStream

fun Activity.allPermissionsGranted() =
    arrayOf(
        Manifest.permission.CAMERA
    ).all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

fun changeTheme(theme: String) {
    when (theme) {
        "LIGHT_THEME" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        "DARK_THEME" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}

fun Activity.getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else filesDir
}

fun saveScreenshotFromView(view: View, file: File): File? {
    val rootView: View = view.rootView
    rootView.isDrawingCacheEnabled = true
    val fos: FileOutputStream
    return try {
        fos = FileOutputStream(file)
        rootView.drawingCache.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        file
    } catch (e: Exception) {
        null
    }
}

fun nonFallbackTryCatch(f: () -> Unit) {
    try {
        f.invoke()
    } catch (e: Exception) {
    }
}