package com.example.cento.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("formattedDate")
fun formattedDate(textView: TextView, timestamp: Long) {
    textView.text = DateUtils.convertIntoDate(timestamp)
}