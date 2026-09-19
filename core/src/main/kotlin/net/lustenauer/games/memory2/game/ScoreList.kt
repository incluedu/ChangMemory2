package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import net.lustenauer.games.memory2.utils.GamePreferences
import net.lustenauer.utils.Time
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScoreList  // constructor for singleton
private constructor() {
    val TAG: String = this.javaClass.getName()

    private var scores: Array<ScoreEntry>? = null

    fun init() {
        Gdx.app.debug(TAG, "init()")

        scores = Array<ScoreEntry>()
        val prefs: Preferences = GamePreferences.instance.prefs

        for (i in 0..9) {
            try {
                val name = prefs.getString("Rank" + (i + 1) + ".Name", "Changnoi")
                val score = prefs.getInteger("Rank" + (i + 1) + ".Score", 0)
                val level = prefs.getInteger("Rank" + (i + 1) + ".Level", 0)
                val time = prefs.getFloat("Rank" + (i + 1) + ".Time", 0f)
                val dateString = prefs.getString("Rank" + (i + 1) + ".Date", "07-Apr-2015 07:44")

                val format: DateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH)

                val date: Date? = format.parse(dateString)

                scores!!.add(ScoreEntry(name, score, level, time, date))
            } catch (e: ParseException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    /**
     * Add an entry to the score list and remove the last entry.
     *
     * @param score the score as an int
     * @return Returns the ranking of the user or -1 when the user is not under top 10 ranking
     */
    fun addScore(score: Int, level: Int, time: Float): Int {
        Gdx.app.debug(TAG, " addScore($score) ")

        val date = Date()

        val entry = ScoreEntry(GamePreferences.instance.userName, score, level, time, date)

        scores!!.add(entry)
        scores!!.sort()

        while (scores!!.size > 10) {
            scores!!.removeIndex(10)
        }

        var rank = scores!!.indexOf(entry, true)

        if (rank != -1) rank++ // add one because the array index start at 0


        return rank
    }

    val scorePane: Actor
        get() {
            val skinWindow: Skin = Assets.instance.skinWindow
            val tbl = Table(skinWindow)
            tbl.setBackground("background6")
            tbl.setSize(460f, 450f)
            tbl.setPosition(10f, 200f)
            tbl.align(Align.topLeft)
            tbl.pad(20f)

            tbl.add(
                Label(
                    "Highscore",
                    skinWindow,
                    "font32",
                    Color.ORANGE
                )
            ).colspan(5).padBottom(25f).row()

            tbl.add(
                Label(
                    "Rank",
                    skinWindow,
                    "font16",
                    Color.ORANGE
                )
            ).left().pad(0f, 0f, 15f, 10f)
            tbl.add(
                Label(
                    "Score",
                    skinWindow,
                    "font16",
                    Color.ORANGE
                )
            ).left().pad(0f, 0f, 15f, 10f)
            tbl.add(
                Label(
                    "Level",
                    skinWindow,
                    "font16",
                    Color.ORANGE
                )
            ).left().pad(0f, 0f, 15f, 10f)
            tbl.add(
                Label(
                    "Gametime",
                    skinWindow,
                    "font16",
                    Color.ORANGE
                )
            ).left().pad(0f, 0f, 15f, 10f)
            tbl.add(
                Label(
                    "Date",
                    skinWindow,
                    "font16",
                    Color.ORANGE
                )
            ).left().pad(0f, 0f, 15f, 10f).row()

            for (i in 0..<scores!!.size) {
                val e = scores!!.get(i)
                if (e.score > 0) {
                    val df: DateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH)

                    tbl.add(
                        Label(
                            "#" + (i + 1),
                            skinWindow,
                            "font16",
                            Color.WHITE
                        )
                    ).left().pad(0f, 0f, 5f, 10f)
                    tbl.add(
                        Label(
                            "" + e.score,
                            skinWindow,
                            "font16",
                            Color.WHITE
                        )
                    ).left().pad(0f, 0f, 5f, 10f)
                    tbl.add(
                        Label(
                            "" + e.level,
                            skinWindow,
                            "font16",
                            Color.WHITE
                        )
                    ).left().pad(0f, 0f, 5f, 10f)
                    tbl.add(
                        Label(
                            "" + Time.formatSeconds(
                                e.time
                            ), skinWindow, "font16", Color.WHITE
                        )
                    ).left().pad(0f, 0f, 5f, 10f)
                    tbl.add(
                        Label(
                            df.format(
                                e.date
                            ), skinWindow, "font16", Color.WHITE
                        )
                    ).left().pad(0f, 0f, 5f, 10f).row()
                }
            }

            return tbl
        }

    fun save() {
        Gdx.app.debug(TAG, "save()")

        val prefs: Preferences = GamePreferences.instance.prefs
        val format: DateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH)

        for ((index, entry) in scores!!.withIndex()) {
            prefs.putString("Rank${index + 1}.Name", entry.name)
            prefs.putInteger("Rank${index + 1}.Score", entry.score)
            prefs.putInteger("Rank${index + 1}.Level", entry.level)
            prefs.putFloat("Rank${index + 1}.Time", entry.time)
            prefs.putString("Rank${index + 1}.Date", format.format(entry.date))
        }
    }

    companion object {
        val instance: ScoreList = ScoreList() // Initialize class as a singleton
    }
}

