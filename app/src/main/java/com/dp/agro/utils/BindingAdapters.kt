package com.dp.agro.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("sourceText")
fun setSourceText(view: TextView, text: String?) {
    if (text.isNullOrEmpty()) return
    view.text = "Source: $text"
}