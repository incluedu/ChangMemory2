package net.lustenauer.games.memory2.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.log.logger
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table
import net.lustenauer.games.memory2.utils.Constants.Skins
import net.lustenauer.games.memory2.utils.GamePreferences
import net.lustenauer.utils.Time
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import com.badlogic.gdx.utils.Array as GdxArray
import com.badlogic.gdx.scenes.scene2d.ui.Table



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
            val defaultStyle = Assets.skinWindow.get(Label.LabelStyle::class.java)
            val baseFont = defaultStyle.font

            val headerStyle = Label.LabelStyle(baseFont, Color.ORANGE)
            val rowStyle = Label.LabelStyle(baseFont, Color.WHITE)

            return scene2d.table {
                background = Assets.skinWindow.getDrawable(Skins.BACKGROUND_6)
                setSize(460f, 450f)
                setPosition(10f, 200f)
                align(Align.topLeft)
                pad(20f)

                label("Highscore") {
                    style = headerStyle
                }.cell(colspan = 5, padBottom = 25f)
                row()

                listOf("Rank", "Score", "Level", "Game-Time", "Date").forEach { header ->
                    label(header) {
                        style = headerStyle
                    }.cell(align = Align.left, padRight = 10f, padBottom = 15f)
                }
                row()

                scores.forEach { e ->
                    if (e.score > 0) {
                        val formattedDate = DATE_FORMATTER.format(e.date)

                        label("#${scores.indexOf(e, true) + 1}") { style = rowStyle }.cell(align = Align.left, padRight = 10f, padBottom = 5f)
                        label("${e.score}") { style = rowStyle }.cell(align = Align.left, padRight = 10f, padBottom = 5f)
                        label("${e.level}") { style = rowStyle }.cell(align = Align.left, padRight = 10f, padBottom = 5f)
                        label(Time.formatSeconds(e.time)) { style = rowStyle }.cell(align = Align.left, padRight = 10f, padBottom = 5f)
                        label(formattedDate) { style = rowStyle }.cell(align = Align.left, padRight = 10f, padBottom = 5f)
                        row()
                    }
                }
            }
        }

    /**
     * Commits all current active score entries back into the encrypted local preferences storage node.
     */
    fun save() {
        log.debug { "save()" }

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

        log.debug { "save() -> flush preferences success." }
    }
}
