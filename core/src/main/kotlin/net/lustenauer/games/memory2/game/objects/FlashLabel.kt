package net.lustenauer.games.memory2.game.objects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions.color
import com.badlogic.gdx.scenes.scene2d.actions.Actions.forever
import com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence
import com.badlogic.gdx.scenes.scene2d.ui.Label

/**
 * A customized LibGDX [Label] that creates a continuous color-flashing animation effect.
 * Alternates visibility states or color steps seamlessly using repeating actions.
 *
 * @param text The initial character sequence to display inside the label payload.
 * @param style The design configuration styling profile assigning fonts and colors.
 */
class FlashLabel(text: CharSequence?, style: LabelStyle?) : Label(text, style) {

    /** Flag controlling whether the color flashing animation updates are actively processing. */
    var isFlashing: Boolean = false

    init {
        // Enqueue an infinite color shifting animation alternating between Yellow and Red
        addAction(forever(sequence(color(Color.YELLOW, 0.3f), color(Color.RED, 0.3f))))
    }

    /**
     * Updates the underlying actor state logic.
     * Animation steps only tick forward if [isFlashing] evaluates to true.
     *
     * @param delta The chronological time step increment value provided by the main frame loop.
     */
    override fun act(delta: Float) {
        if (isFlashing) super.act(delta)
    }
}
