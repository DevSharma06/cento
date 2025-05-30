package com.example.cento.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

@BindingAdapter("formattedDate")
fun formattedDate(textView: TextView, timestamp: Long) {
    textView.text = DateUtils.convertIntoDate(timestamp)
}

@BindingAdapter("android:text")
fun setDoubleText(view: TextInputEditText, value: Double?) {
    val newValue = value ?: ""
    val currentText = view.text.toString()
    val formatted = DecimalFormat("#.##").format(newValue)
    if (currentText != formatted) {
        view.setText(formatted)
    }
}

@InverseBindingAdapter(attribute = "android:text")
fun getDoubleFromText(view: TextInputEditText): Double {
    return view.text.toString().toDoubleOrNull() ?: 0.0
}