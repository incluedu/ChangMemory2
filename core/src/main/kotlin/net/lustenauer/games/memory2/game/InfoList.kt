package net.lustenauer.games.memory2.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_DEFAULT
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XS
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_S
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_L
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XL
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XXL
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XXXL
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * A native Kotlin singleton object container that holds floating or flashing text elements (Labels).
 * Collects message entries and compiles them into an animated, scrolling libGDX [Table] overlay.
 *
 * @author Patric Hollenstein
 */
object InfoList {

    /**
     * The internal collection holding all queued message [Actor] items before layout compilation.
     */
    private val list = GdxArray<Actor>()

    /**
     * Appends a new text message to the floating layout pool.
     * Uses Kotlin default parameters to replace multiple overloaded legacy method definitions.
     *
     * @param text The string characters to display inside the label.
     * @param flash If true, forces the label to cycle through an animated light/dark color loop.
     * @param size The targeted font sizing constant from the constants block.
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
            for (i in 0 until list.size) {
                add(list[i]).row()
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
            SIZE_XS   -> FONT_XS
            SIZE_S    -> FONT_S
            SIZE_M    -> FONT_M
            SIZE_L    -> FONT_L
            SIZE_XL   -> FONT_XL
            SIZE_XXL  -> FONT_XXL
            SIZE_XXXL -> FONT_XXXL
            else      -> FONT_DEFAULT
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

    // =================
    // CONSTANTS & SIZES
    // =================

    private const val FLASHTIME = 0.4f

    const val SIZE_XS = 7000
    const val SIZE_S = 7001
    const val SIZE_M = 7002
    const val SIZE_L = 7003
    const val SIZE_XL = 7004
    const val SIZE_XXL = 7005
    const val SIZE_XXXL = 7006
    private const val SIZE_DEFAULT = 0
}
