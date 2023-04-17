package com.fakhry.businessapp.core.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*


private var serverDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

fun String.toDateAgo() : String {
    val date = serverDateFormat.parse(this)

    val niceDateStr = DateUtils.getRelativeTimeSpanString(
        date?.time ?: 0L,
        Calendar.getInstance().timeInMillis,
        DateUtils.MINUTE_IN_MILLIS
    )

    return niceDateStr.toString()
}
