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

class WindowPause(cmdListener: CommandListener?) : AbstractCommandWindow(cmdListener) {
    private lateinit var skinWindow: Skin

    override fun init() {
        skinWindow = Assets.instance.skinWindow
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
        private const val TEXT_TITLE = "G A M E   P A U S E D"
        private const val TEXT_BTN_RESUME = "RESUME"
        private const val TEXT_BTN_RESTART = "RESTART"
        private const val TEXT_BTN_MENU = "MENU"
        private const val TEXT_BTN_SETTINGS = "SETTINGS"
    }
}
