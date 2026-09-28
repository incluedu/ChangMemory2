package net.lustenauer.games.memory2.ui.components

import com.badlogic.gdx.graphics.Color.RED
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable
import net.lustenauer.games.memory2.game.objects.FlashLabel
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_S

/**
 * Manages the Heads-Up Display (HUD) overlay layer for the gameplay screen.
 * Handles the initialization, positioning, formatting, and text updates of all
 * score, level, timer, and technical debug labels rendered on the [hudStage].
 *
 * Implements [com.badlogic.gdx.utils.Disposable] to ensure clean lifecycle destruction of attached stage resources.
 *
 * @author Patric Hollenstein
 * @property hudStage The dedicated scene2d rendering layer for user interface overlays.
 * @property skinWindow The central UI skin layout sheet configuration node.
 */
class GameHUD(
    val hudStage: Stage,
    private val skinWindow: Skin
) : Disposable {

    private val lblLevel: Label
    private val lblScore: Label
    private val lblTimeLeft: FlashLabel

    init {
        val hudStyle = skinWindow.get(FONT_S, LabelStyle::class.java)
        val timerStyle = skinWindow.get(FONT_M, LabelStyle::class.java)

        lblLevel = Label("LEVEL: 000", hudStyle).apply {
            setPosition(460f, 765f, Align.right)
        }

        lblScore = Label("SCORE: 0000000000", hudStyle).apply {
            setPosition(20f, 765f, Align.left)
        }

        lblTimeLeft = FlashLabel("TIME LEFT: 00:00:00", timerStyle).apply {
            setPosition(20f, 730f, Align.left)
        }

        hudStage.addActor(lblLevel)
        hudStage.addActor(lblScore)
        hudStage.addActor(lblTimeLeft)
    }

    /**
     * Updates the text properties of all active HUD labels based on the current game state variables.
     *
     * @param currentLevel The numerical index of the currently active level.
     * @param currentScore The current total point value achieved.
     * @param timeLeft The remaining gameplay duration value measured in seconds.
     * @param timeLeft30Seconds Trigger condition that activates the critical timer flickering effect.
     */
    fun update(currentLevel: Int, currentScore: Int, timeLeft: Float, timeLeft30Seconds: Boolean) {
        lblLevel.setText("LEVEL: $currentLevel")
        lblScore.setText("SCORE: $currentScore")

        if (timeLeft30Seconds) {
            lblTimeLeft.isFlashing = true
            lblTimeLeft.setColor(RED)
        } else {
            lblTimeLeft.isFlashing = false
            lblTimeLeft.setColor(WHITE)
        }
        lblTimeLeft.setText("TIME LEFT: ${timeLeft.toInt()}")
    }

    /**
     * Safely clears and releases all attached actor elements from the [hudStage].
     */
    override fun dispose() {
        hudStage.clear()
    }
}
