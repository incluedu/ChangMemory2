package net.lustenauer.games.memory2.game.windows

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.actors.onClick
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants.Skins
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_MENU
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESTART
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SCORE
import net.lustenauer.gdx.scenes.scene2d.CommandListener
import net.lustenauer.gdx.scenes.scene2d.ui.AbstractCommandWindow
import net.lustenauer.utils.Time

/**
 * An overlay window displayed when the game finishes or the player loses.
 *
 * Displays final game statistics (score, time, cards flipped/solved, lucky strikes)
 * and provides navigation buttons to view scores, return to the menu, or restart.
 * Communicates actions back to the controller via a [CommandListener].
 *
 * @param cmdListener The listener that handles the fired [CommandListener.CommandEvent]s.
 */
class WindowGameOver(cmdListener: CommandListener?) : AbstractCommandWindow(cmdListener) {

    /** The texture skin resource container specifically assigned to window elements. */
    private lateinit var skinWindow: Skin

    /** Label displaying the player's final accumulated score. */
    private lateinit var lblScore: Label

    /** Label displaying the total chronological time spent in the match session. */
    private lateinit var lblTime: Label

    /** Label displaying the total number of manual card flips executed. */
    private lateinit var lblCardFlippedCount: Label

    /** Label displaying the absolute count of successfully matched card sets. */
    private lateinit var lblCardSolvedCount: Label

    /** Label displaying the count of lucky strikes triggered during the game. */
    private lateinit var lblLuckyStrikeCount: Label

    /**
     * Initializes the game over window layout, sets up action buttons,
     * and builds the statistics panel rows.
     */
    override fun init() {
        skinWindow = Assets.skinWindow
        skin = skinWindow
        setBackground(Skins.BACKGROUND_3)
        isVisible = false
        sizeBy(400f, 250f)

        addActor(Label(TEXT_TITLE, skinWindow, Skins.FONT_32, Color.RED).apply {
            setPosition(100f, 190f)
        })

        addActor(Button(skinWindow, Skins.BTN_BLUE).apply {
            add(TEXT_BTN_SCORES)
            setPosition(20f, 20f)
            onClick { fire(CommandListener.CommandEvent(CMD_SCORE)) }
        })

        addActor(Button(skinWindow, Skins.BTN_BLUE).apply {
            add(TEXT_BTN_MENU)
            setPosition(20f, 70f)
            onClick { fire(CommandListener.CommandEvent(CMD_MENU)) }
        })

        addActor(Button(skinWindow, Skins.BTN_BLUE).apply {
            add(TEXT_BTN_RESTART)
            setPosition(20f, 120f)
            onClick { fire(CommandListener.CommandEvent(CMD_RESTART)) }
        })

        buildStatRow(TEXT_STAT_SCORE, 140f, 140f) { lblScore = it }
        buildStatRow(TEXT_STAT_TIME, 140f, 120f) { lblTime = it }
        buildStatRow(TEXT_STAT_SOLVED, 140f, 100f) { lblCardSolvedCount = it }
        buildStatRow(TEXT_STAT_LUCKY, 140f, 80f) { lblLuckyStrikeCount = it }
        buildStatRow(TEXT_STAT_FLIPPED, 140f, 60f) { lblCardFlippedCount = it }
    }

    /**
     * Helper method to construct a standardized row containing a title label and a value label.
     *
     * @param title The text description of the statistic.
     * @param x The X position coordinates of the row container.
     * @param y The Y position coordinates of the row container.
     * @param assignTo Callback function passing the generated value [Label] to bind it to a class reference.
     */
    private fun buildStatRow(title: String, x: Float, y: Float, assignTo: (Label) -> Unit) {
        val grp = Group().apply { setPosition(x, y) }

        val lblTitle = Label(title, skinWindow, Skins.FONT_16, Color.BLACK).apply {
            setPosition(20f, 0f)
        }
        val valueLabel = Label("0", skinWindow, Skins.FONT_16, Color.BLACK).apply {
            setPosition(170f, 0f)
        }

        grp.addActor(lblTitle)
        grp.addActor(valueLabel)
        addActor(grp)

        assignTo(valueLabel)
    }

    /** Updates the displayed total score. */
    fun setScore(score: Int) = lblScore.setText(score.toString())

    /** Updates the displayed game duration, formatting seconds into a time string. */
    fun setTime(totalTime: Float) = lblTime.setText(Time.formatSeconds(totalTime))

    /** Updates the displayed number of consecutive correct card matches. */
    fun setLuckStrikeCount(luckyStrikeCount: Int) = lblLuckyStrikeCount.setText(luckyStrikeCount.toString())

    /** Updates the displayed total number of card flips performed by the player. */
    fun setCardFlipCount(cardFlippedCount: Int) = lblCardFlippedCount.setText(cardFlippedCount.toString())

    /** Updates the displayed total count of successfully matched card pairs. */
    fun setTotalCardSolvedCount(cardSolvedCount: Int) = lblCardSolvedCount.setText(cardSolvedCount.toString())

    companion object {
        /** Title banner text displayed at the top of the game over overlay. */
        private const val TEXT_TITLE = "G A M E   O V E R"

        /** Label text assigned to the online leaderboard navigation button. */
        private const val TEXT_BTN_SCORES = "SCORES"

        /** Label text assigned to the main menu navigation button. */
        private const val TEXT_BTN_MENU = "MENU"

        /** Label text assigned to the match restart option button. */
        private const val TEXT_BTN_RESTART = "RESTART"

        /** Label header description tracking the final score metric row. */
        private const val TEXT_STAT_SCORE = "TOTAL SCORE:"

        /** Label header description tracking the gameplay time metric row. */
        private const val TEXT_STAT_TIME = "TOTAL TIME:"

        /** Label header description tracking matched card pairs across levels. */
        private const val TEXT_STAT_SOLVED = "CARDS SOLVED:"

        /** Label header description tracking consecutive blind matches. */
        private const val TEXT_STAT_LUCKY = "LUCKY STRIKES:"

        /** Label header description tracking the total interactive flip count. */
        private const val TEXT_STAT_FLIPPED = "CARDS FLIPPED:"
    }
}
