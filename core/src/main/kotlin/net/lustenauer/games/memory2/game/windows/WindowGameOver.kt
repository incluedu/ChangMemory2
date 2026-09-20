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

class WindowGameOver(cmdListener: CommandListener?) : AbstractCommandWindow(cmdListener) {
    private lateinit var skinWindow: Skin
    private lateinit var lblScore: Label
    private lateinit var lblTime: Label
    private lateinit var lblCardFlippedCount: Label
    private lateinit var lblCardSolvedCount: Label
    private lateinit var lblLuckyStrikeCount: Label

    override fun init() {
        skinWindow = Assets.instance.skinWindow
        skin = skinWindow
        setBackground(Skins.BACKGROUND_3) // Nutzt das Alias!
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

    fun setScore(score: Int) = lblScore.setText(score.toString())
    fun setTime(totalTime: Float) = lblTime.setText(Time.formatSeconds(totalTime))
    fun setLuckStrikeCount(luckyStrikeCount: Int) = lblLuckyStrikeCount.setText(luckyStrikeCount.toString())
    fun setCardFlipCount(cardFlippedCount: Int) = lblCardFlippedCount.setText(cardFlippedCount.toString())
    fun setCardSolvedCount(cardSolvedCount: Int) = lblCardSolvedCount.setText(cardSolvedCount.toString())

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
