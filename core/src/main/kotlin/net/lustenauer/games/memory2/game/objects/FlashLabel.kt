package net.lustenauer.games.memory2.game.objects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label

class FlashLabel(text: CharSequence?, style: LabelStyle?) : Label(text, style) {
    var isFlashing: Boolean = false

    init {
        addAction(Actions.forever(Actions.sequence(Actions.color(Color.YELLOW, 0.3f), Actions.color(Color.RED, 0.3f))))
    }

    override fun act(delta: Float) {
        if (this.isFlashing) super.act(delta)
    }
}
