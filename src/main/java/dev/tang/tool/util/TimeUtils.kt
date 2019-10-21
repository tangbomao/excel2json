package dev.tang.tool.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    /**
     * Get current time
     */
    val currentTime: String
        get() {
            val now = System.currentTimeMillis()
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(Date(now))
        }
}
