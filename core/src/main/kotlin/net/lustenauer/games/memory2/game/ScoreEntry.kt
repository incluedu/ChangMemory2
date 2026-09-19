package net.lustenauer.games.memory2.game

import java.util.Date

data class ScoreEntry(
    val name: String?,
    val score: Int,
    val level: Int,
    val time: Float,
    val date: Date?
) : Comparable<ScoreEntry> {

    override fun compareTo(other: ScoreEntry): Int {
        return other.score.compareTo(this.score)
    }
}
