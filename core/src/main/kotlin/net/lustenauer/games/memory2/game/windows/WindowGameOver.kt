package net.lustenauer.games.memory2.game.windows

import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.Color.RED
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onClick
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.ui.CommandListener
import net.lustenauer.games.memory2.ui.CommandListener.CommandEvent
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XS
import net.lustenauer.games.memory2.utils.Constants.Skins.BACKGROUND_3
import net.lustenauer.games.memory2.utils.Constants.Skins.BTN_BLUE
import net.lustenauer.games.memory2.utils.GameCommand.MENU
import net.lustenauer.games.memory2.utils.GameCommand.RESTART
import net.lustenauer.games.memory2.utils.GameCommand.SCORE
import net.lustenauer.games.memory2.utils.toTimeString

/**
 * An overlay window displayed when the game finishes or the player loses.
 *
 * Displays final game statistics (score, time, cards flipped/solved, lucky strikes)
 * and provides navigation buttons to view scores, return to the menu, or restart.
 * Communicates actions back to the controller via a [CommandListener].
 *
 * @author Patric Hollenstein
 */
class WindowGameOver(cmdListener: CommandListener?) : Table(Assets.skinWindow) {

    private val lblScore: Label
    private var lblTime: Label
    private val lblCardFlippedCount: Label
    private val lblCardSolvedCount: Label
    private val lblLuckyStrikeCount: Label

    init {
        if (cmdListener != null) {
            addListener(cmdListener)
        }

        background = skin.getDrawable(BACKGROUND_3)
        isVisible = false
        setSize(400f, 250f)

        addActor(Label(TEXT_TITLE, skin, FONT_M, RED).apply {
            setPosition(100f, 190f)
        })

        addActor(Button(skin, BTN_BLUE).apply {
            add(TEXT_BTN_SCORES)
            setPosition(20f, 20f)
            onClick { fire(CommandEvent(SCORE)) }
        })

        addActor(Button(skin, BTN_BLUE).apply {
            add(TEXT_BTN_MENU)
            setPosition(20f, 70f)
            onClick { fire(CommandEvent(MENU)) }
        })

        addActor(Button(skin, BTN_BLUE).apply {
            add(TEXT_BTN_RESTART)
            setPosition(20f, 120f)
            onClick { fire(CommandEvent(RESTART)) }
        })

        lblScore = buildStatRow(TEXT_STAT_SCORE, 140f, 140f)
        lblTime = buildStatRow(TEXT_STAT_TIME, 140f, 120f)
        lblCardSolvedCount = buildStatRow(TEXT_STAT_SOLVED, 140f, 100f)
        lblLuckyStrikeCount = buildStatRow(TEXT_STAT_LUCKY, 140f, 80f)
        lblCardFlippedCount = buildStatRow(TEXT_STAT_FLIPPED, 140f, 60f)
    }

    /**
     * Helper method to construct a standardized row containing a title label and a value label.
     *
     * @param title The text description of the statistic.
     * @param x The X position coordinates of the row container.
     * @param y The Y position coordinates of the row container.
     * @return The freshly generated value [Label] reference.
     */
    private fun buildStatRow(title: String, x: Float, y: Float): Label {
        val grp = Group().apply { setPosition(x, y) }

        val lblTitle = Label(title, skin, FONT_XS, BLACK).apply {
            setPosition(20f, 0f)
        }
        val valueLabel = Label("0", skin, FONT_XS, BLACK).apply {
            setPosition(170f, 0f)
        }

        grp.addActor(lblTitle)
        grp.addActor(valueLabel)
        addActor(grp)

        return valueLabel
    }

    /** Updates the displayed total score. */
    fun setScore(score: Int) = lblScore.setText(score.toString())

    /** Updates the displayed game duration, formatting seconds into a time string. */
    fun setTime(totalTime: Float) = lblTime.setText(totalTime.toTimeString)

    /** Updates the displayed number of consecutive correct card matches. */
    fun setLuckStrikeCount(luckyStrikeCount: Int) = lblLuckyStrikeCount.setText(luckyStrikeCount.toString())

    /** Updates the displayed total number of card flips performed by the player. */
    fun setCardFlipCount(cardFlippedCount: Int) = lblCardFlippedCount.setText(cardFlippedCount.toString())

    /** Updates the displayed total count of successfully matched card pairs. */
    fun setTotalCardSolvedCount(cardSolvedCount: Int) = lblCardSolvedCount.setText(cardSolvedCount.toString())

    companion object {
        private const val TEXT_TITLE = "G A M E   O V E R"
        private const val TEXT_BTN_SCORES = "SCORES"
        private const val TEXT_BTN_MENU = "MENU"
        private const val TEXT_BTN_RESTART = "RESTART"
        private const val TEXT_STAT_SCORE = "TOTAL SCORE:"
        private const val TEXT_STAT_TIME = "TOTAL TIME:"
        private const val TEXT_STAT_SOLVED = "CARDS SOLVED:"
        private const val TEXT_STAT_LUCKY = "LUCKY STRIKES:"
        private const val TEXT_STAT_FLIPPED = "CARDS FLIPPED:"
    }
}
