package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.actors.BtnBack
import net.lustenauer.games.memory2.game.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.game.actors.BtnGooglePlusSignOut
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.Constants
import net.lustenauer.games.memory2.utils.Constants.Viewport
import net.lustenauer.games.memory2.utils.GamePreferences

class SettingsScreen(game: ChangMemory) : AbstractScreen(game) {
    private val TAG: String = this.javaClass.getName()

    private var stage: com.badlogic.gdx.scenes.scene2d.Stage? = null
    private var skinWindow: com.badlogic.gdx.scenes.scene2d.ui.Skin? = null
    private var lblSoundPercent: Label? = null
    private var lblMusicPercent: Label? = null

    // Statt: private var sldSound: Slider? = null
    private lateinit var chkSound: CheckBox
    private lateinit var sldSound: Slider
    private lateinit var chkMusic: CheckBox
    private lateinit var sldMusic: Slider


    private val myChangeListener: EventListener?

    init {
        myChangeListener = MyChangeListener()
    }

    override fun render(deltaTime: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)

        update(deltaTime)
        stage!!.act(deltaTime)
        stage!!.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height, true)
    }

    override fun show() {
        Gdx.app.debug(TAG, "show()")
        ChangMemory.actionResolver?.setTrackerScreenName(TAG)
        stage = com.badlogic.gdx.scenes.scene2d.Stage(
            StretchViewport(
                Viewport.GUI_WIDTH,
                Viewport.GUI_HEIGHT
            )
        )
        Gdx.input.setCatchKey(BACK, true)
        Gdx.input.inputProcessor = stage

        init()
    }

    override fun hide() {
        stage!!.dispose()
    }

    override fun pause() {
    }

    /* PRIVATE METHODS */ /* ================ */
    private fun init() {
        skinWindow = Assets.skinWindow

        val layerBackground: Actor = buildLayerBackground()
        val layerLogo: Actor = buildLayerLogo()
        val layerControls: Actor = buildLayerControls()

        stage!!.clear()
        val stack = Stack()
        stage!!.addActor(stack)
        stack.setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerLogo)
        stack.add(layerControls)
    }

    private fun update(deltaTime: Float) {
        updateInputs()
    }

    private fun updateInputs() {
        // return to menu Screen
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) or Gdx.input.isKeyPressed(BACK)) {
            game.setScreen(ChangMemory.prevScreen)
        }
    }

    /* LAYERS AND ACTORS */ /* ================= */
    private fun buildLayerBackground(): Table {
        val layer = Table()
        val imgBackground = Image(skinWindow, "background4")
        layer.add(imgBackground)
        return layer
    }

    private fun buildLayerControls(): Actor {
        val layer = Table().bottom()

        layer.addActor(buildWindowSettings())
        layer.addActor(BtnBack(ChangMemory.prevScreen))
        layer.addActor(BtnGooglePlusSignIn())
        layer.addActor(BtnGooglePlusSignOut())

        return layer
    }

    private fun buildLayerLogo(): Table {
        val layer = Table()

        var lbl = Label(
            "CHANG MEMORY",
            skinWindow,
            "font48",
            Color.YELLOW
        )
        lbl.setPosition((Viewport.GUI_WIDTH - lbl.getWidth()) / 2, 700f)
        layer.addActor(lbl)

        lbl = Label(
            "(c) 2015 BY Lustenauer Net",
            skinWindow,
            "font24",
            Color.YELLOW
        )
        lbl.setPosition((Viewport.GUI_WIDTH - lbl.getWidth()) / 2, 660f)
        layer.addActor(lbl)

        return layer
    }

    private fun buildWindowSettings(): Table {

        val tbl = Table(skinWindow)
        tbl.setBackground("background6")
        tbl.setSize(400f, 400f)
        tbl.setPosition(40f, 200f)
        tbl.align(Align.topLeft)
        tbl.pad(20f)

        // + Title: "Audio:"
        val lblAudio = Label(
            "Audio",
            skinWindow,
            "font24",
            Color.WHITE
        )

        // + Checkbox, "Sound" label, sound volume slider
        chkSound = CheckBox("", skinWindow)
        val lblSound = Label(
            "Sound",
            skinWindow,
            "font16",
            Color.LIGHT_GRAY
        )
        lblSoundPercent = Label(
            "${(GamePreferences.instance.volSound * 100).toInt()}%",
            skinWindow,
            "font16",
            Color.LIGHT_GRAY
        )
        sldSound = Slider(0.0f, 1.0f, 0.1f, false, skinWindow)

        chkSound.setChecked(GamePreferences.instance.sound)
        sldSound.setValue(GamePreferences.instance.volSound)

        chkSound.addListener(myChangeListener)
        sldSound.addListener(myChangeListener)

        // + Checkbox, "Music" label, music volume slider
        chkMusic = CheckBox("", skinWindow)
        val lblMusic = Label(
            "Music",
            skinWindow,
            "font16",
            Color.LIGHT_GRAY
        )
        lblMusicPercent = Label(
            "${(GamePreferences.instance.volMusic * 100).toInt()}%",
            skinWindow,
            "font16",
            Color.LIGHT_GRAY
        )
        sldMusic = Slider(0.0f, 1.0f, 0.1f, false, skinWindow)

        chkMusic.setChecked(GamePreferences.instance.music)
        sldMusic.setValue(GamePreferences.instance.volMusic)

        chkMusic.addListener(myChangeListener)
        sldMusic.addListener(myChangeListener)

        tbl.add(lblAudio).colspan(3).left().row()

        tbl.add(chkSound).padRight(10f)
        tbl.add(lblSound).padRight(10f)
        tbl.add(sldSound).padRight(10f)
        tbl.add<Label?>(lblSoundPercent).left().row()

        tbl.add(chkMusic).padRight(10f)
        tbl.add(lblMusic).padRight(10f)
        tbl.add(sldMusic).padRight(10f)
        tbl.add<Label?>(lblMusicPercent).left().row()

        return tbl
    }

    /* HANDLER METHODS */ /* ================ */
    private inner class MyChangeListener : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            GamePreferences.instance.music = chkMusic.isChecked()
            GamePreferences.instance.sound = chkSound.isChecked()
            GamePreferences.instance.volMusic = sldMusic.value
            GamePreferences.instance.volSound = sldSound.value

            AudioManager.instance.onSettingsUpdated()

            lblMusicPercent!!.setText((sldMusic.value * 100).toInt().toString() + "%")
            lblSoundPercent!!.setText((sldSound.value * 100).toInt().toString() + "%")
        }
    }
}
