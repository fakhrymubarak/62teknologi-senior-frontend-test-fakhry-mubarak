package com.fakhry.businessapp.core.utils.components

import android.content.Context
import android.widget.Toast


/**
 * Show error toast
 * */
fun Context.showToast(message: String) {
    Toast.makeText(this,  message, Toast.LENGTH_LONG).show()
}