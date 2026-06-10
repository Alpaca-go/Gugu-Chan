package com.guguchan.app.utils

object PrivacyMaskUtils {
    fun maskConsumerId(raw: String, mode: String): String {
        return when (mode) {
            "partial" -> {
                if (raw.length <= 6) raw else raw.take(2) + "****" + raw.takeLast(2)
            }
            "short" -> raw.takeLast(6)
            else -> raw
        }
    }
}
