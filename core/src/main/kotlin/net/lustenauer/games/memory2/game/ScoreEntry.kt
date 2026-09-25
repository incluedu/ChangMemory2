package net.lustenauer.games.memory2.game

import java.time.Instant

/**
 * Represents an immutable highscore entry within the game.
 * Implements [Comparable] to automatically sort scores in descending order (highest score first).
 *
 * @property name The profile name of the player. Defaults to [DEFAULT_NAME].
 * @property score The total point value achieved during the game session.
 * @property level The maximum level reached by the player.
 * @property time The total gameplay duration measured in elapsed seconds.
 * @property date The exact modern timestamp when this highscore entry was locked in.
 * @author Patric Hollenstein
 */
data class ScoreEntry(
    val name: String = DEFAULT_NAME,
    val score: Int,
    val level: Int,
    val time: Float,
    val date: Instant = Instant.now()
) : Comparable<ScoreEntry> {

    /**
     * Compares this highscore entry with another entry for sorting purposes.
     * Elements are ordered descending based on the numerical [score] value.
     * Uses zero-allocation primitive comparison for peak sorting performance.
     */
    override fun compareTo(other: ScoreEntry): Int {
        return other.score.compareTo(this.score)
    }

    companion object {
        /**
         * The default fallback name used when a player profile name is blank or missing.
         */
        const val DEFAULT_NAME = "Player"
    }
}
