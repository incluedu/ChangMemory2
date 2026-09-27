package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.graphics.Color.GREEN
import com.badlogic.gdx.graphics.Color.RED
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.graphics.Color.YELLOW
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_L
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XL
import net.lustenauer.games.memory2.utils.Constants.Skins.BTN_BLUE
import net.lustenauer.games.memory2.utils.Constants.Skins.BTN_BLUE_BIG
import net.lustenauer.games.memory2.utils.Constants.Skins.IMG_GOOGLE_PLAY

/**
 * Technical UI layout container for the main menu.
 * Separates graphic structure and button alignments from the screen lifecycle.
 *
 * @author Patric Hollenstein
 */
class MenuLayout(actionsHandler: MenuActionHandler) : Table(Assets.skinWindow) {

    /**
     * Callback interface to route UI clicks safely back to the screen manager.
     */
    interface MenuActionHandler {
        fun onPlaySelected()
        fun onScoresSelected()
        fun onSettingsSelected()
        fun onCreditsSelected()
        fun onExitSelected()
    }

    init {
        setFillParent(true)
        center()

        val logoTable = Table().apply {
            add(Label("CHANG MEMORY II", Assets.skinWindow, FONT_XL, YELLOW)).row()
            add(Label("(c) 2015 - 2026 BY lustenauer.net", Assets.skinWindow, FONT_M, YELLOW))
        }
        add(logoTable).padTop(40f).padBottom(30f)
        row()

        val controlsTable = Table(Assets.skinWindow).apply {
            val btnStart = Button(Assets.skinWindow, BTN_BLUE_BIG).apply {
                val lbl = Label("PLAY", Assets.skinWindow, FONT_L, WHITE).apply {
                    setupPulsingColorAction()
                }
                add(lbl)
                onClick { actionsHandler.onPlaySelected() }
            }

            val btnScore = Button(Assets.skinWindow, BTN_BLUE).apply {
                add("SCORES")
                onClick { actionsHandler.onScoresSelected() }
            }

            val btnSettings = Button(Assets.skinWindow, BTN_BLUE).apply {
                add("SETTINGS")
                onClick { actionsHandler.onSettingsSelected() }
            }

            val btnCredits = Button(Assets.skinWindow, BTN_BLUE).apply {
                add("CREDITS")
                onClick { actionsHandler.onCreditsSelected() }
            }

            val btnExit = Button(Assets.skinWindow, BTN_BLUE).apply {
                add("EXIT")
                onClick { actionsHandler.onExitSelected() }
            }

            add(btnStart).pad(0f, 0f, 60f, 0f).colspan(2).row()
            add(btnScore).pad(0f, 0f, 20f, 20f).width(150f).height(60f)
            add(btnSettings).pad(0f, 0f, 20f, 0f).width(150f).height(60f).row()
            add(btnCredits).pad(0f, 0f, 20f, 20f).width(150f).height(60f)
            add(btnExit).pad(0f, 0f, 20f, 0f).width(150f).height(60f).row()
        }
        add(controlsTable).expandY()
        row()

        val socialTable = Table().apply {
            add(BtnGooglePlusSignIn()).padRight(20f)

            val imgGooglePlay = Image(Assets.skinWindow, IMG_GOOGLE_PLAY)
            add(imgGooglePlay).align(Align.bottom)
        }
        add(socialTable).padBottom(30f)
    }

    /**
     * Applies the endless pulsing red-to-green color loop onto the PLAY label.
     */
    private fun Label.setupPulsingColorAction() {
        addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.color(RED, 0.7f),
                    Actions.color(GREEN, 0.7f)
                )
            )
        )
    }
}
