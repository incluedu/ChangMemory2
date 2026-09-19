package net.lustenauer.utils

object Time {
    fun formatMillis(milliseconds: Long): String {
        val second = (milliseconds / 1000) % 60
        val minute = (milliseconds / (1000 * 60)) % 60
        val hour = (milliseconds / (1000 * 60 * 60))

        return String.format("%02d:%02d:%02d", hour, minute, second)
    }

    fun formatSeconds(seconds: Float): String {
        return formatSeconds(seconds.toLong())
    }

    fun formatSeconds(seconds: Long): String {
        val second = (seconds % 60)
        val minute = (seconds / 60) % 60
        val hour = (seconds / (60 * 60))

        return String.format("%02d:%02d:%02d", hour, minute, second)
    }
}
