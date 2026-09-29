package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.Color.LIGHT_GRAY
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.graphics.Color.YELLOW
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.utils.Align.left
import com.badlogic.gdx.utils.Align.topLeft
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.actors.onChange
import ktx.log.logger
import ktx.scene2d.*
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.model.GamePreferences
import net.lustenauer.games.memory2.ui.actors.BtnBack
import net.lustenauer.games.memory2.ui.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.ui.actors.BtnGooglePlusSignOut
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_S
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XL
import net.lustenauer.games.memory2.utils.Constants.Skins.BACKGROUND_4
import net.lustenauer.games.memory2.utils.Constants.Skins.BACKGROUND_6
import net.lustenauer.games.memory2.utils.Constants.Viewport
import net.lustenauer.games.memory2.utils.Constants.Viewport.GUI_HEIGHT
import net.lustenauer.games.memory2.utils.Constants.Viewport.GUI_WIDTH

/**
 * Responsive settings configuration screen powered by KTX Scene2D DSL syntax.
 * Harmonizes sound and music volume boundaries directly with [GamePreferences] and hardware engines.
 *
 * Provides granular structural partitioning by leveraging tailored, local extension properties
 * to maintain a flat and readable layout declaration graph.
 *
 * @author Patric Hollenstein
 */
class SettingsScreen(game: ChangMemory) : AbstractScreen(game) {

    private val log = logger<SettingsScreen>()

    /** The centralized scene2d staging ground rendering all layout components. */
    private lateinit var stage: Stage

    /** Text metric tracking and displaying the active sound effects volume percentage scaling. */
    private lateinit var lblSoundPercent: Label

    /** Text metric tracking and displaying the active background music volume percentage scaling. */
    private lateinit var lblMusicPercent: Label

    /** Interactive checkbox widget monitoring global sound effect execution toggles. */
    private lateinit var chkSound: CheckBox

    /** Manual scroll bar controlling sound effect gain calibrations. */
    private lateinit var sldSound: Slider

    /** Interactive checkbox widget monitoring global background music stream toggles. */
    private lateinit var chkMusic: CheckBox

    /** Manual scroll bar controlling background music stream gain calibrations. */
    private lateinit var sldMusic: Slider

    /**
     * Wipes background frame content and commands attached staging layers to act and draw.
     */
    override fun render(deltaTime: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        updateInputs()
        stage.act(deltaTime)
        stage.draw()
    }

    /**
     * Fits the inner viewport metrics proportionally to fresh hardware window sizes.
     */
    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    /**
     * Initializes structural framework listeners, locks mobile hardware back buttons,
     * hooks up input processing streams, and triggers the core layout compilation tree.
     */
    override fun show() {
        log.info { "Displaying configuration screen layers" }
        ChangMemory.instance.actionResolver?.setTrackerScreenName(this::class.java.name)

        stage = Stage(StretchViewport(GUI_WIDTH, GUI_HEIGHT))
        Gdx.input.setCatchKey(BACK, true)
        Gdx.input.inputProcessor = stage

        Scene2DSkin.defaultSkin = Assets.skinWindow

        buildUiLayout()
        syncPreferences()
    }

    /**
     * Releases memory buffers locked by attached staging actors during visibility shifts.
     */
    override fun hide() {
        stage.dispose()
    }

    override fun pause() {}

    /**
     * Constructs the responsive multi-layered layout utilizing the KTX stack builder tree.
     */
    private fun buildUiLayout() {
        stage.clear()

        stage.actors {
            stack {
                setSize(GUI_WIDTH, GUI_HEIGHT)

                table().buildLayerBackground
                table().buildLayerHeader
                table().buildLayerSettingsAndControls
            }
        }
    }

    /**
     * Extends the [KTableWidget] to embed the standard screen canvas background graphic region.
     */
    private val @Scene2dDsl KTableWidget.buildLayerBackground: KTableWidget
        get() {
            image(BACKGROUND_4)
            return this
        }

    /**
     * Extends the [KTableWidget] to cleanly mount the static yellow chalkboard screen branding title texts.
     */
    private val @Scene2dDsl KTableWidget.buildLayerHeader: KTableWidget
        get() {
            top().padTop(40f)
            label("CHANG MEMORY II", style = FONT_XL) { color = YELLOW }
            row()
            label("(c) 2015 - 2026 BY lustenauer.net", style = FONT_M) { color = YELLOW }
            return this
        }

    /**
     * Extends the [KTableWidget] to coordinate nested structural grids including settings cells and action links.
     */
    private val @Scene2dDsl KTableWidget.buildLayerSettingsAndControls: KTableWidget
        get() {
            bottom().padBottom(30f)
            table().buildTableSettings.cell(width = 400f, height = 400f, padBottom = 20f)
            row()
            table().buildControlsWidget
            return this
        }

    /**
     * Extends the [KTableWidget] to compile the specific audio preference cells inside the chalkboard box.
     */
    private val @Scene2dDsl KTableWidget.buildTableSettings: KTableWidget
        get() {
            background = skin.getDrawable(BACKGROUND_6)
            align(topLeft)
            pad(20f)

            label("Audio", style = FONT_M) { color = WHITE }
                .cell(colspan = 4, align = left, padBottom = 15f)
            row()

            // --- SOUND SETTINGS ROW ---
            add(buildSoundCheckBox()).padRight(10f)
            add(Label("Sound", skin, FONT_S).apply { color = LIGHT_GRAY }).padRight(10f)
            add(buildSoundSlider()).width(190f).padRight(10f)
            add(buildSoundPercentLabel()).align(left)
            row()

            // --- MUSIC SETTINGS ROW ---
            add(buildMusicCheckBox()).padRight(10f).padTop(15f)
            add(Label("Music", skin, FONT_S).apply { color = LIGHT_GRAY }).padRight(10f).padTop(15f)
            add(buildMusicSlider()).width(190f).padRight(10f).padTop(15f)
            add(buildMusicPercentLabel()).align(left).padTop(15f)
            row()
            return this
        }

    /**
     * Extends the [KTableWidget] to line up the unified platform action buttons horizontally.
     */
    private val @Scene2dDsl KTableWidget.buildControlsWidget: KTableWidget
        get() {
            add(BtnBack(ChangMemory.prevScreen)).padRight(10f)
            add(BtnGooglePlusSignIn()).padRight(10f)
            add(BtnGooglePlusSignOut())
            return this
        }

    // --- LOGICAL WIDGET FACTORIES ---

    /** Builds the interactive sound effects checkbox widget mapped onto the configuration synchronization blocks. */
    private fun buildSoundCheckBox() = CheckBox("", Assets.skinWindow).apply {
        isChecked = GamePreferences.sound
        chkSound = this
        onChange { syncPreferences() }
    }

    /** Builds the manual sound volume adjustment slider widget mapped onto the configuration synchronization blocks. */
    private fun buildSoundSlider() = Slider(0.0f, 1.0f, 0.1f, false, Assets.skinWindow).apply {
        value = GamePreferences.volSound
        sldSound = this
        onChange { syncPreferences() }
    }

    /** Builds the localized text label monitoring active sound playback percentage states. */
    private fun buildSoundPercentLabel() = Label("${(GamePreferences.volSound * 100).toInt()}%", Assets.skinWindow, FONT_S).apply {
        color = LIGHT_GRAY
        lblSoundPercent = this
    }

    /** Builds the interactive music playback checkbox widget mapped onto the configuration synchronization blocks. */
    private fun buildMusicCheckBox() = CheckBox("", Assets.skinWindow).apply {
        isChecked = GamePreferences.music
        chkMusic = this
        onChange { syncPreferences() }
    }

    /** Builds the manual music volume adjustment slider widget mapped onto the configuration synchronization blocks. */
    private fun buildMusicSlider() = Slider(0.0f, 1.0f, 0.1f, false, Assets.skinWindow).apply {
        value = GamePreferences.volMusic
        sldMusic = this
        onChange { syncPreferences() }
    }

    /** Builds the localized text label monitoring active music playback percentage states. */
    private fun buildMusicPercentLabel() = Label("${(GamePreferences.volMusic * 100).toInt()}%", Assets.skinWindow, FONT_S).apply {
        color = LIGHT_GRAY
        lblMusicPercent = this
    }

    /**
     * Synchronizes active UI widget modifications straight back into [GamePreferences].
     * Evaluates volume limits and updates percentage labels safely without allocation overhead.
     */
    private fun syncPreferences() {
        GamePreferences.sound = chkSound.isChecked
        GamePreferences.music = chkMusic.isChecked
        GamePreferences.volSound = sldSound.value
        GamePreferences.volMusic = sldMusic.value

        AudioManager.onSettingsUpdated()

        sldSound.isDisabled = !chkSound.isChecked
        sldMusic.isDisabled = !chkMusic.isChecked

        lblSoundPercent.setText("${(sldSound.value * 100).toInt()}%")
        lblMusicPercent.setText("${(sldMusic.value * 100).toInt()}%")
    }

    /**
     * Traps hardware and keyboard polling actions to cleanly reroute execution flows to preceding screen stacks.
     */
    private fun updateInputs() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(BACK)) {
            game.screen = ChangMemory.prevScreen
        }
    }
}
