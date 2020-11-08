package com.dp.agro.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import java.util.*

class ClassifyMediator(
    private val labeler: ImageLabeler,
    private val callback: (String) -> Unit
) {
    fun classify(bitmap: Bitmap) = classifyInputImage(InputImage.fromBitmap(bitmap, 0))

    fun classify(uri: Uri, context: Context) =
        classifyInputImage(InputImage.fromFilePath(context, uri))

    private fun classifyInputImage(inputImage: InputImage) {
        labeler.process(inputImage)
            .addOnSuccessListener {
                if (it.isNotEmpty()) {
                    val resultSlug = it[0].text
                        .trim()
                        .toLowerCase(Locale.ROOT)
                        .split(" ")
                        .joinToString("_")
                    callback.invoke(resultSlug)
                } else callback.invoke("unknown")
            }
    }
}