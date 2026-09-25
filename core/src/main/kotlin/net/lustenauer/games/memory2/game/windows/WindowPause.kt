package net.lustenauer.games.memory2.game.windows

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.actors.onClick
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants.Skins
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_MENU
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESTART
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESUME
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SETTINGS
import net.lustenauer.gdx.scenes.scene2d.CommandListener
import net.lustenauer.gdx.scenes.scene2d.ui.AbstractCommandWindow

/**
 * An overlay window displayed when the game is paused.
 *
 * Provides UI controls for resuming, restarting, opening settings, or returning to the main menu.
 * Communicates actions back to the controller via a [CommandListener].
 *
 * @param cmdListener The listener that handles the fired [CommandListener.CommandEvent]s.
 */
class WindowPause(cmdListener: CommandListener?) : AbstractCommandWindow(cmdListener) {

    /** The texture skin resource container specifically assigned to window elements. */
    private lateinit var skinWindow: Skin

    /**
     * Initializes the pause window layout, visual assets, labels, and command buttons.
     */
    override fun init() {
        skinWindow = Assets.skinWindow
        skin = skinWindow
        setBackground(Skins.BACKGROUND_3)
        isVisible = false
        setPosition(40f, 220f)
        setSize(400f, 400f)

        addActor(Image(skinWindow, Skins.IMG_FROG).apply {
            setPosition(235f, 40f)
            setScale(0.7f)
            rotation = -10f
        })

        addActor(Image(skinWindow, Skins.IMG_BUTTERFLY).apply {
            setPosition(325f, 220f)
            setScale(0.35f)
            rotation = 30f
        })

        addActor(Label(TEXT_TITLE, skinWindow, Skins.FONT_32, Color.RED).apply {
            setPosition(20f, 325f)
        })

        addActor(Button(skinWindow, Skins.BTN_BLUE_BIG).apply {
            add(TEXT_BTN_RESUME)
            setPosition(20f, 200f)
            onClick { fire(CommandListener.CommandEvent(CMD_RESUME)) }
        })

        addActor(Button(skinWindow, Skins.BTN_BLUE).apply {
            add(TEXT_BTN_RESTART)
            setPosition(20f, 120f)
            onClick { fire(CommandListener.CommandEvent(CMD_RESTART)) }
        })

        addActor(Button(skinWindow, Skins.BTN_BLUE).apply {
            add(TEXT_BTN_MENU)
            setPosition(20f, 70f)
            onClick { fire(CommandListener.CommandEvent(CMD_MENU)) }
        })

        addActor(Button(skinWindow, Skins.BTN_BLUE).apply {
            add(TEXT_BTN_SETTINGS)
            setPosition(20f, 20f)
            onClick { fire(CommandListener.CommandEvent(CMD_SETTINGS)) }
        })
    }

    companion object {
        /** Title banner text displayed at the top of the pause overlay. */
        private const val TEXT_TITLE = "G A M E   P A U S E D"

        /** Label text assigned to the resume gameplay option button. */
        private const val TEXT_BTN_RESUME = "RESUME"

        /** Label text assigned to the match reset option button. */
        private const val TEXT_BTN_RESTART = "RESTART"

        /** Label text assigned to the main menu navigation button. */
        private const val TEXT_BTN_MENU = "MENU"

        /** Label text assigned to the game settings panel button. */
        private const val TEXT_BTN_SETTINGS = "SETTINGS"
    }
}
