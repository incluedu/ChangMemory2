package net.lustenauer.games.memory2.game.windows

import com.badlogic.gdx.graphics.Color.RED
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onClick
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Skins.BACKGROUND_3
import net.lustenauer.games.memory2.utils.Constants.Skins.BTN_BLUE
import net.lustenauer.games.memory2.utils.Constants.Skins.BTN_BLUE_BIG
import net.lustenauer.games.memory2.utils.Constants.Skins.IMG_BUTTERFLY
import net.lustenauer.games.memory2.utils.Constants.Skins.IMG_FROG
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_MENU
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESTART
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESUME
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SETTINGS
import net.lustenauer.gdx.scenes.scene2d.CommandListener
import net.lustenauer.gdx.scenes.scene2d.CommandListener.CommandEvent

/**
 * An overlay window displayed when the game is paused.
 *
 * Provides UI controls for resuming, restarting, opening settings, or returning to the main menu.
 * Communicates actions back to the controller via a [CommandListener].
 *
 * @author Patric Hollenstein
 */
class WindowPause(cmdListener: CommandListener?) : Table(Assets.skinWindow) {

    init {
        if (cmdListener != null) {
            addListener(cmdListener)
        }

        background = skin.getDrawable(BACKGROUND_3)

        isVisible = false
        setPosition(40f, 220f)
        setSize(400f, 400f)

        addActor(Image(skin, IMG_FROG).apply {
            setPosition(235f, 40f)
            scaleX = 0.7f
            scaleY = 0.7f
            rotation = -10f
        })

        addActor(Image(skin, IMG_BUTTERFLY).apply {
            setPosition(325f, 220f)
            scaleX = 0.35f
            scaleY = 0.35f
            rotation = 30f
        })

        addActor(Label(TEXT_TITLE, skin, FONT_M, RED).apply {
            setPosition(20f, 325f)
        })

        addActor(Button(skin, BTN_BLUE_BIG).apply {
            add(TEXT_BTN_RESUME)
            setPosition(20f, 200f)
            onClick { fire(CommandEvent(CMD_RESUME)) }
        })

        addActor(Button(skin, BTN_BLUE).apply {
            add(TEXT_BTN_RESTART)
            setPosition(20f, 120f)
            onClick { fire(CommandEvent(CMD_RESTART)) }
        })

        addActor(Button(skin, BTN_BLUE).apply {
            add(TEXT_BTN_MENU)
            setPosition(20f, 70f)
            onClick { fire(CommandEvent(CMD_MENU)) }
        })

        addActor(Button(skin, BTN_BLUE).apply {
            add(TEXT_BTN_SETTINGS)
            setPosition(20f, 20f)
            onClick { fire(CommandEvent(CMD_SETTINGS)) }
        })
    }

    companion object {
        private const val TEXT_TITLE = "G A M E   P A U S E D"
        private const val TEXT_BTN_RESUME = "RESUME"
        private const val TEXT_BTN_RESTART = "RESTART"
        private const val TEXT_BTN_MENU = "MENU"
        private const val TEXT_BTN_SETTINGS = "SETTINGS"
    }
}
