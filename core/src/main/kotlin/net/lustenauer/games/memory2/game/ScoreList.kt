package net.lustenauer.games.memory2.game

import com.badlogic.gdx.graphics.Color.ORANGE
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Align.left
import ktx.log.logger
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_S
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XS
import net.lustenauer.games.memory2.utils.Constants.Skins.BACKGROUND_6
import net.lustenauer.games.memory2.utils.GamePreferences
import net.lustenauer.games.memory2.utils.toTimeString
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * Manages the highscore list of the game. Handles loading, saving, and
 * ranking calculations of highscore records using modern time structures.
 *
 * @author Patric Hollenstein
 */
object ScoreList {

    private val log = logger<ScoreList>()

    private const val FALLBACK_NAME = "Changnoi"

    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm", Locale.ENGLISH)
        .withZone(ZoneId.systemDefault())

    /**
     * The continuous container storing the top ranking score records.
     */
    private val scores = GdxArray<ScoreEntry>()

    /**
     * Initializes the score list by reading saved entries from the local game preferences.
     * Automatically populates fallback configurations if profile configurations are empty.
     */
    fun init() {
        log.debug { "init()" }
        scores.clear()

        val prefs = GamePreferences.instance.prefs

        for (i in 0..9) {
            val name = prefs.getString("Rank${i + 1}.Name", FALLBACK_NAME)
            val score = prefs.getInteger("Rank${i + 1}.Score", 0)
            val level = prefs.getInteger("Rank${i + 1}.Level", 0)
            val time = prefs.getFloat("Rank${i + 1}.Time", 0f)
            val dateString = prefs.getString("Rank${i + 1}.Date", "07-Apr-2015 07:44")

            val date = try {
                Instant.from(DATE_FORMATTER.parse(dateString))
            } catch (_: Exception) {
                Instant.now()
            }

            scores.add(ScoreEntry(name, score, level, time, date))
        }
    }

    /**
     * Appends a newly achieved gameplay record onto the ranking array, sorts the pool,
     * and limits entries strictly down to the top 10 positions.
     *
     * @param score The point value achieved.
     * @param level The milestone level index reached.
     * @param time The elapsed game timer value.
     * @return The absolute leaderboard ranking index (1-10), or -1 if outside the top tier.
     */
    fun addScore(score: Int, level: Int, time: Float): Int {
        log.debug { "addScore($score)" }

        val safeName = GamePreferences.instance.userName ?: ScoreEntry.DEFAULT_NAME
        val entry = ScoreEntry(safeName, score, level, time, Instant.now())

        scores.add(entry)
        scores.sort()

        while (scores.size > 10) {
            scores.removeIndex(10)
        }

        var rank = scores.indexOf(entry, true)
        if (rank != -1) rank++

        return rank
    }

    /**
     * Compiles and layout-structures the highscore leaderboard visual representation
     * into a scene2d [Table] actor utilizing the type-safe KTX Scene2D DSL.
     */
    val scorePane: Actor
        get() {
            val mainHeaderStyle = Label.LabelStyle(Assets.skinWindow.getFont(FONT_S), ORANGE)
            val columnHeaderStyle = Label.LabelStyle(Assets.skinWindow.getFont(FONT_XS), ORANGE)
            val rowStyle = Label.LabelStyle(Assets.skinWindow.getFont(FONT_XS), WHITE)

            return scene2d.table {
                background = Assets.skinWindow.getDrawable(BACKGROUND_6)
                setSize(460f, 450f)
                setPosition(10f, 200f)
                align(Align.topLeft)
                pad(25f, 20f, 20f, 20f)

                label("Highscore") {
                    style = mainHeaderStyle
                }.cell(colspan = 5, padBottom = 20f, align = Align.center)
                row()

                listOf("Rank", "Score", "Level", "Game-Time", "Date").forEach { header ->
                    label(header) {
                        style = columnHeaderStyle
                    }.cell(align = left, padRight = 10f, padBottom = 12f)
                }
                row()

                scores.forEach { e ->
                    if (e.score > 0) {
                        val formattedDate = DATE_FORMATTER.format(e.date)

                        label("#${scores.indexOf(e, true) + 1}") { style = rowStyle }.cell(
                            align = left,
                            padRight = 10f,
                            padBottom = 4f
                        )
                        label("${e.score}") { style = rowStyle }.cell(
                            align = left,
                            padRight = 10f,
                            padBottom = 4f
                        )
                        label("${e.level}") { style = rowStyle }.cell(
                            align = left,
                            padRight = 10f,
                            padBottom = 4f
                        )
                        label(e.time.toTimeString) { style = rowStyle }.cell(
                            align = left,
                            padRight = 10f,
                            padBottom = 4f
                        )
                        label(formattedDate) { style = rowStyle }.cell(
                            align = left,
                            padRight = 10f,
                            padBottom = 4f
                        )
                        row()
                    }
                }
            }
        }

    /**
     * Commits all current active score entries back into the encrypted local preferences storage node.
     */
    fun save() {
        val prefs = GamePreferences.instance.prefs

        for (i in 0 until scores.size) {
            val entry = scores[i]
            prefs.putString("Rank${i + 1}.Name", entry.name)
            prefs.putInteger("Rank${i + 1}.Score", entry.score)
            prefs.putInteger("Rank${i + 1}.Level", entry.level)
            prefs.putFloat("Rank${i + 1}.Time", entry.time)
            prefs.putString("Rank${i + 1}.Date", DATE_FORMATTER.format(entry.date))
        }
        prefs.flush()

        log.info { "Successfully persisted ${scores.size} highscore records to local preferences storage." }
    }

}
