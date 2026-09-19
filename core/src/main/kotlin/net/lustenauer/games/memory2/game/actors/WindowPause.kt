package net.lustenauer.games.memory2.game.actors

import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_MENU
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESTART
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESUME
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SETTINGS
import net.lustenauer.gdx.scenes.scene2d.CommandListener
import net.lustenauer.gdx.scenes.scene2d.ui.AbstractCommandWindow
import net.lustenauer.gdx.scenes.scene2d.util.Build

class WindowPause(cmdListener: CommandListener?) : AbstractCommandWindow(cmdListener) {
    private lateinit var skinWindow: Skin

    override fun init() {
        skinWindow = Assets.instance.skinWindow!!

        skin = skinWindow
        isVisible = false
        setPosition(40F, 220F)
        setSize(400F, 400F)

        addActor(Build.img(skinWindow, "frog", 235F, 40F, 0.7f, -10f, null))
        addActor(Build.img(skinWindow, "butterfly", 325F, 220F, 0.35f, 30f, null))

        addActor(Build.lbl("G A M E   P A U S E D", skinWindow, "font32", com.badlogic.gdx.graphics.Color.RED, 20F, 325F))

        addActor(Build.btn("RESUME", skinWindow, "blueBig", 20F, 200F, object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                fire(CommandListener.CommandEvent(CMD_RESUME))
                return super.touchDown(event, x, y, pointer, button)
            }
        }))
        //		addActor (Build.img(skinWindow, "btnResume", 10, 220, 2f, 0, new InputListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//				fire(new CommandListener.CommandEvent(CMD_RESUME));
//				return super.touchDown(event, x, y, pointer, button);
//			}
//		}));
        addActor(Build.btn("RESTART", skinWindow, "blue", 20F, 120F, object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                fire(CommandListener.CommandEvent(CMD_RESTART))
                return super.touchDown(event, x, y, pointer, button)
            }
        }))
        addActor(Build.btn("MENU", skinWindow, "blue", 20F, 70F, object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                fire(CommandListener.CommandEvent(CMD_MENU))
                return super.touchDown(event, x, y, pointer, button)
            }
        }))
        addActor(Build.btn("SETTINGS", skinWindow, "blue", 20F, 20F, object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                fire(CommandListener.CommandEvent(CMD_SETTINGS))
                return super.touchDown(event, x, y, pointer, button)
            }
        }))
    }
}
