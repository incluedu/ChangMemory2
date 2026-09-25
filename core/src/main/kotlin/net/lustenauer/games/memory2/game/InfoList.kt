package net.lustenauer.games.memory2.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import net.lustenauer.games.memory2.utils.Constants.Skins
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * A singleton container that holds floating or flashing text elements (Labels).
 * Collects message entries and compiles them into an animated, scrolling libGDX [Table] overlay.
 *
 * @author Patric Hollenstein
 */
class InfoList private constructor() {

    /**
     * The internal collection holding all queued message [Actor] items before layout compilation.
     */
    private val list = GdxArray<Actor?>()

    /**
     * Appends a new text message to the floating layout pool.
     * Uses Kotlin default parameters to replace six heavily overloaded legacy Java methods.
     *
     * @param text The string characters to display inside the label.
     * @param flash If true, forces the label to cycle through an animated light/dark color loop.
     * @param size The targeted font sizing constant from the [Companion] object.
     * @param color The font base tinting structure. Overridden by [Color.WHITE] if flashing is active.
     */
    fun add(
        text: String?,
        flash: Boolean = false,
        size: Int = SIZE_DEFAULT,
        color: Color? = null
    ) {
        list.add(buildLabel(text, flash, size, color))
    }

    /**
     * Compiles all active message elements into a single scrolling [Table] container, centers it,
     * injects a fade-out movement animation sequence, and attaches the overlay onto the game stage.
     * Automatically clears the item pool upon execution.
     *
     * @param stage The active libGDX rendering platform [Stage] context to attach the overlay onto.
     * @return The fully built, animated [Table] actor, or null if no message elements were queued.
     */
    fun addInfoTable(stage: Stage): Actor? {
        if (list.isEmpty) return null

        val table = Table().apply {
            for (actor in list) {
                add(actor).row()
            }

            pack()

            setPosition(
                stage.width / 2f - (width / 2f),
                stage.height / 2f - (height / 2f)
            )

            addAction(
                Actions.sequence(
                    Actions.parallel(
                        Actions.moveBy(0f, 350f, 2f),
                        Actions.fadeOut(2f)
                    ),
                    Actions.removeActor()
                )
            )
        }

        stage.addActor(table)
        list.clear()

        return table
    }

    /**
     * Helper factory that instantiates a libGDX [Label] with specified styling rules
     * and optional endless flickering effects.
     */
    private fun buildLabel(text: String?, flash: Boolean, size: Int, color: Color?): Label {
        val baseColor = if (flash) Color.WHITE else (color ?: Color.WHITE)

        val styleName = when (size) {
            SIZE_XS -> Skins.FONT_16
            SIZE_S -> Skins.FONT_16
            SIZE_M -> Skins.FONT_24
            SIZE_L -> Skins.FONT_32
            SIZE_XL -> Skins.FONT_48
            SIZE_XXL -> Skins.FONT_56
            SIZE_XXXL -> Skins.FONT_72
            else -> Skins.DEFAULT_FONT
        }

        return Label(text, Assets.skinWindow, styleName, baseColor).apply {
            if (flash) {
                addAction(
                    Actions.forever(
                        Actions.sequence(
                            Actions.color(Color.YELLOW, FLASHTIME),
                            Actions.color(Color.DARK_GRAY, FLASHTIME)
                        )
                    )
                )
            }
        }
    }

    companion object {
        /**
         * Floating speed constant determining how fast flash color changes loop.
         */
        private const val FLASHTIME = 0.4f

        /**
         * The central global singleton access node.
         */
        val instance = InfoList()

        const val SIZE_XS = 7000
        const val SIZE_S = 7001
        const val SIZE_M = 7002
        const val SIZE_L = 7003
        const val SIZE_XL = 7004
        const val SIZE_XXL = 7005
        const val SIZE_XXXL = 7006
        private const val SIZE_DEFAULT = 0
    }
}
