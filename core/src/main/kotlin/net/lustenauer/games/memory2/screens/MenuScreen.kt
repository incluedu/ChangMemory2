package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.scene2d.image
import ktx.scene2d.scene2d
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.Constants
import net.lustenauer.games.memory2.utils.Constants.Viewport
import net.lustenauer.games.memory2.utils.GamePreferences

class MenuScreen(game: ChangMemory) : AbstractScreen(game) {
    private val TAG: kotlin.String = this.javaClass.getName()

    private var stage: com.badlogic.gdx.scenes.scene2d.Stage? = null
    private var windowSkin: com.badlogic.gdx.scenes.scene2d.ui.Skin? = null
    protected var score: kotlin.Long = 0

    /* PUBLIC METHODES */ /* =============== */
    public override fun show() {
        Gdx.app.debug(TAG, "show()")
        ChangMemory.instance.actionResolver?.setTrackerScreenName(TAG)
        Gdx.input.setCatchKey(BACK, true)

        AudioManager.playMusic()
        initStage()
    }

    public override fun render(deltaTime: kotlin.Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)

        update(deltaTime)
        stage!!.act(deltaTime)
        stage!!.draw()
    }

    public override fun resize(width: kotlin.Int, height: kotlin.Int) {
        Gdx.app.debug(TAG, "resize(" + width + "," + height + ")")
        stage!!.getViewport().update(width, height, true)
    }

    public override fun pause() {
        Gdx.app.debug(TAG, "pause()")
    }

    public override fun hide() {
        Gdx.app.debug(TAG, "hide()")
        stage!!.dispose()
    }

    /* PRIVATE METHODES */ /* ================ */
    private fun initStage() {
        windowSkin = Assets.skinWindow

        stage = com.badlogic.gdx.scenes.scene2d.Stage(
            StretchViewport(
                Viewport.GUI_WIDTH,
                Viewport.GUI_HEIGHT
            )
        )
        Gdx.input.setInputProcessor(stage)

        val layerBackground: Actor = buildBackgroundLayer()
        val layerLogo: Actor = buildLogoLayer()
        val layerControls: Actor = buildControlsLayer()

        stage!!.clear()
        val stack = com.badlogic.gdx.scenes.scene2d.ui.Stack()
        stage!!.addActor(stack)
        stack.setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerLogo)
        stack.add(layerControls)
    }

    private fun loadSettings() {
        val prefs: GamePreferences = GamePreferences.instance
        prefs.load()
        // chkSound.setChecked(prefs.sound);
        // sldSound.setValue(prefs.volSound);
        // chkMusic.setChecked(prefs.music);
        // sldMusic.setValue(prefs.volMusic);
    }

    private fun saveSettings() {
        val prefs: GamePreferences = GamePreferences.instance
        // prefs.sound = chkSound.isChecked();
        // prefs.volSound = sldSound.getValue();
        // prefs.music = chkMusic.isChecked();
        // prefs.volMusic = sldMusic.getValue();
        prefs.save()
    }

    private fun buildBackgroundLayer(): com.badlogic.gdx.scenes.scene2d.ui.Table {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table()
        val imgBackground = Image(windowSkin, "background4")
        layer.add<Image?>(imgBackground)
        return layer
    }

    private fun buildControlsLayer(): Actor {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table()

        // layer.right().bottom();
        val btnStart = com.badlogic.gdx.scenes.scene2d.ui.Button(windowSkin, "default")
        val lbl = com.badlogic.gdx.scenes.scene2d.ui.Label(
            "PLAY",
            windowSkin,
            "font32",
            com.badlogic.gdx.graphics.Color.WHITE
        )
        lbl.addAction(
            com.badlogic.gdx.scenes.scene2d.actions.Actions.forever(
                com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(
                    com.badlogic.gdx.scenes.scene2d.actions.Actions.color(com.badlogic.gdx.graphics.Color.RED, 0.7f),
                    com.badlogic.gdx.scenes.scene2d.actions.Actions.color(com.badlogic.gdx.graphics.Color.GREEN, .7f)
                )
            )
        )
        btnStart.add<com.badlogic.gdx.scenes.scene2d.ui.Label?>(lbl)
        btnStart.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: kotlin.Float,
                y: kotlin.Float,
                pointer: kotlin.Int,
                button: kotlin.Int
            ): kotlin.Boolean {
                doShowCardScreen()
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        val btnExit = com.badlogic.gdx.scenes.scene2d.ui.Button(windowSkin, "blue")
        btnExit.add("EXIT")
        btnExit.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: kotlin.Float,
                y: kotlin.Float,
                pointer: kotlin.Int,
                button: kotlin.Int
            ): kotlin.Boolean {
                doGameExit()
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        val btnSettings = com.badlogic.gdx.scenes.scene2d.ui.Button(windowSkin, "blue")
        btnSettings.add("SETTINGS")
        btnSettings.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: kotlin.Float,
                y: kotlin.Float,
                pointer: kotlin.Int,
                button: kotlin.Int
            ): kotlin.Boolean {
                doShowSettingsScreen()
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        val btnCredits = com.badlogic.gdx.scenes.scene2d.ui.Button(windowSkin, "blue")
        btnCredits.add("CREDITS")
        btnCredits.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: kotlin.Float,
                y: kotlin.Float,
                pointer: kotlin.Int,
                button: kotlin.Int
            ): kotlin.Boolean {
                doShowCreditsScreen()
                return super.touchDown(event, x, y, pointer, button)
            }
        })

        val btnScore = com.badlogic.gdx.scenes.scene2d.ui.Button(windowSkin, "blue")
        btnScore.add("SCORES")
        btnScore.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: kotlin.Float,
                y: kotlin.Float,
                pointer: kotlin.Int,
                button: kotlin.Int
            ): kotlin.Boolean {
                doShowScoreScreen()
                return super.touchDown(event, x, y, pointer, button)
            }
        })


        layer.add(btnStart).pad(0f, 0f, 60f, 0f).colspan(2).row()
        layer.add(btnScore).pad(0f, 0f, 20f, 20f)
        layer.add(btnSettings).pad(0f, 0f, 20f, 0f).row()
        layer.add(btnCredits).pad(0f, 0f, 20f, 20f)
        layer.add(btnExit).pad(0f, 0f, 20f, 0f).row()

        layer.addActor(BtnGooglePlusSignIn())
        val skinWindow = Assets.skinWindow
        layer.addActor(scene2d.image("imgGooglePlay", skinWindow) { setPosition(20f, 20f) })

        return layer
    }

    private fun buildLogoLayer(): Actor {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table()
        var lbl: com.badlogic.gdx.scenes.scene2d.ui.Label?

        lbl = com.badlogic.gdx.scenes.scene2d.ui.Label(
            "CHANG MEMORY II",
            windowSkin,
            "font48",
            com.badlogic.gdx.graphics.Color.YELLOW
        )
        lbl.setPosition((Viewport.GUI_WIDTH - lbl.getWidth()) / 2, 700f)
        layer.addActor(lbl)

        lbl = com.badlogic.gdx.scenes.scene2d.ui.Label(
            "(c) 2015 - 2026 BY lustenauer.net",
            windowSkin,
            "font24",
            com.badlogic.gdx.graphics.Color.YELLOW
        )
        lbl.setPosition((Viewport.GUI_WIDTH - lbl.getWidth()) / 2, 660f)
        layer.addActor(lbl)

        return layer
    }

    /* UPDATE METHODES */ /* =============== */
    private fun update(deltaTime: kotlin.Float) {
        updateInputs()
    }

    private fun updateInputs() {
        // return to menu Screen
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE) or Gdx.input.isKeyJustPressed(BACK)) {
            doGameExit()
        }
    }

    /* HANDLERS */ /* ======== */
    private fun doGameExit() {
        Gdx.app.log(TAG, "Exit ChangMemory")
        Gdx.app.exit()
    }

    private fun doShowCardScreen() {
        Gdx.app.debug(TAG, "doShowCardScreen()")
        game.setScreen(ChangMemory.cardScreen)
    }

    private fun doShowCreditsScreen() {
        Gdx.app.debug(TAG, "doShowCreditsScreen()")
        game.setScreen(ChangMemory.creditsScreen)
    }

    private fun doShowScoreScreen() {
        Gdx.app.debug(TAG, "doShowScoreScreen()")
        game.setScreen(ChangMemory.scoreScreen)
    }

    private fun doShowSettingsScreen() {
        Gdx.app.debug(TAG, "doShowSettingsScreen()")
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.settingsScreen)
    }

    private fun doGooglePlayButton() {
        Gdx.app.debug(TAG, "doGooglePlay")
        // TODO Auto-generated method stub
    }

    private fun doGoogleSignIn() {
        Gdx.app.debug(TAG, "doGoogleSignIn()")
        ChangMemory.instance.actionResolver?.signIn()
    }
}
