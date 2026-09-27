package net.lustenauer.games.memory2.extensions

import java.time.Duration
import java.util.Locale

/**
 * Formats a gameplay duration value measured in seconds into a standardized,
 * internationalization-safe time clock string representation (HH:MM:SS).
 */
val Float.toTimeString: String
    get() {
        val duration = Duration.ofSeconds(this.toLong())
        return String.format(
            Locale.ENGLISH,
            "%02d:%02d:%02d",
            duration.toHours(),
            duration.toMinutesPart(),
            duration.toSecondsPart()
        )
    }
