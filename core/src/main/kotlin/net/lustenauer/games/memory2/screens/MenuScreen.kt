package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.Color.*
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.actors.onClick
import ktx.log.logger
import ktx.scene2d.image
import ktx.scene2d.scene2d
import ktx.scene2d.table
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets.skinWindow
import net.lustenauer.games.memory2.game.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.Constants.Skins
import net.lustenauer.games.memory2.utils.Constants.Viewport

/**
 * The main menu entry hub for ChangMemory II.
 * Coordinates the primary user interface layers including branding logos,
 * local configuration loads, and operational platform routing targets
 * separate from the core gameplay loop.
 *
 * @author Patric Hollenstein
 */
class MenuScreen(game: ChangMemory) : AbstractScreen(game) {

    private val log = logger<MenuScreen>()

    private var stage: Stage? = null
    private var windowSkin: Skin? = null

    override fun show() {
        log.debug { "show()" }
        ChangMemory.instance.actionResolver?.setTrackerScreenName("MenuScreen")
        Gdx.input.setCatchKey(BACK, true)

        AudioManager.playMusic()
        initStage()
    }

    /**
     * Executes chronological frame updates, processes input polls,
     * and triggers the Scene2D stage rendering cycle.
     *
     * @param delta the fractional time slice elapsed since the last rendered frame
     */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        update()
        stage?.act(delta)
        stage?.draw()
    }

    override fun resize(width: Int, height: Int) {
        log.debug { "resize($width, $height)" }
        stage?.viewport?.update(width, height, true)
    }

    override fun pause() {
        log.debug { "pause()" }
    }

    override fun hide() {
        log.debug { "hide()" }
    }

    override fun dispose() {
        log.debug { "dispose() -> Destroying MenuScreen stages" }
        stage?.dispose()
        super.dispose()
    }

    /**
     * Constructs the unified graphic overlay architecture utilizing standalone multi-layered
     * Scene2D [Table] composites packed securely into a root screen-filling [Stack].
     */
    private fun initStage() {
        windowSkin = skinWindow

        stage = Stage(
            StretchViewport(
                Viewport.GUI_WIDTH,
                Viewport.GUI_HEIGHT
            )
        )
        Gdx.input.inputProcessor = stage

        val layerBackground = buildBackgroundLayer()
        val layerLogo = buildLogoLayer()
        val layerControls = buildControlsLayer()

        stage?.clear()

        val stack = Stack()
        stage?.addActor(stack)

        stack.setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerLogo)
        stack.add(layerControls)
    }

    /**
     * Constructs the structural layout layer holding the primary chalkboard background asset.
     *
     * @return a screen-filling [Table] container containing the background image actor
     */
    private fun buildBackgroundLayer(): Table =
        scene2d.table { image("background4") }.apply { setFillParent(true) }

    /**
     * Constructs the primary interactive control layer housing all tactical navigation
     * buttons and social cloud platform routing links.
     *
     * @return an input-focused [Actor] composite mapping the structural menu controls
     */
    private fun buildControlsLayer(): Actor {
        val layer = Table(skinWindow)

        // PLAY BUTTON
        val btnStart = Button(skinWindow, Skins.BTN_BLUE_BIG).apply {
            add(Label(
                "PLAY",
                skinWindow,
                Skins.FONT_32,
                WHITE
            ).apply { setupPulsingColorAction() })
            onClick { doShowCardScreen() }
        }

        // SCORES BUTTON
        val btnScore = Button(skinWindow, Skins.BTN_BLUE).apply {
            add("SCORES")
            onClick { doShowScoreScreen() }
        }

        // SETTINGS BUTTON
        val btnSettings = Button(skinWindow, Skins.BTN_BLUE).apply {
            add("SETTINGS")
            onClick { doShowSettingsScreen() }
        }

        // CREDITS BUTTON
        val btnCredits = Button(skinWindow, Skins.BTN_BLUE).apply {
            add("CREDITS")
            onClick { doShowCreditsScreen() }
        }

        // EXIT BUTTON
        val btnExit = Button(skinWindow, Skins.BTN_BLUE).apply {
            add("EXIT")
            onClick { doGameExit() }
        }

        layer.add(btnStart).pad(0f, 0f, 60f, 0f).colspan(2).row()
        layer.add(btnScore).pad(0f, 0f, 20f, 20f).width(150f).height(60f)
        layer.add(btnSettings).pad(0f, 0f, 20f, 0f).width(150f).height(60f).row()
        layer.add(btnCredits).pad(0f, 0f, 20f, 20f).width(150f).height(60f)
        layer.add(btnExit).pad(0f, 0f, 20f, 0f).width(150f).height(60f).row()

        layer.addActor(BtnGooglePlusSignIn())

        layer.addActor(scene2d.image(Skins.IMG_GOOGLE_PLAY) {
            setPosition(20f, 20f)
        })

        return layer
    }

    /**
     * Extends the libGDX Label to apply an endless red-to-green pulsing color loop transition.
     */
    private fun Label.setupPulsingColorAction() {
        color = WHITE
        addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.color(RED, 0.7f),
                    Actions.color(GREEN, 0.7f)
                )
            )
        )
    }


    /**
     * Constructs the visual branding layer holding the primary game title
     * and historical copyright coordinates.
     *
     * @return a structured [Actor] container mapping the main logo typography
     */
    private fun buildLogoLayer(): Actor {
        val layer = Table()

        val titleLabel = Label("CHANG MEMORY II", windowSkin, Skins.FONT_48, YELLOW).apply {
            val calculatedX = (Viewport.GUI_WIDTH - width) / 2
            setPosition(calculatedX, 700f)
        }
        layer.addActor(titleLabel)

        val copyrightLabel = Label("(c) 2015 - 2026 BY lustenauer.net", windowSkin, Skins.FONT_24, YELLOW).apply {
            val calculatedX = (Viewport.GUI_WIDTH - width) / 2
            setPosition(calculatedX, 660f)
        }
        layer.addActor(copyrightLabel)

        return layer
    }

    /**
     * Executes logic updates separate from the rendering loop.
     */
    private fun update() {
        updateInputs()
    }

    /**
     * Polls global hardware input registers for structural escape routing actions.
     */
    private fun updateInputs() {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(BACK)) {
            doGameExit()
        }
    }

    /**
     * Terminate the application instance securely after writing outstanding cache logs.
     */
    private fun doGameExit() {
        log.info { "Exit ChangMemory II" }
        Gdx.app.exit()
    }

    /**
     * Dispatches a screen transition focus call onto the primary card gameplay arena loop.
     */
    private fun doShowCardScreen() {
        log.debug { "doShowCardScreen()" }
        game.setScreen(ChangMemory.cardScreen)
    }

    /**
     * Dispatches a screen transition focus call onto the roll credits overview panel layer.
     */
    private fun doShowCreditsScreen() {
        log.debug { "doShowCreditsScreen()" }
        game.setScreen(ChangMemory.creditsScreen)
    }

    /**
     * Dispatches a screen transition focus call onto the local chalkboard leaderboard register.
     */
    private fun doShowScoreScreen() {
        log.debug { "doShowScoreScreen()" }
        game.setScreen(ChangMemory.scoreScreen)
    }

    /**
     * Dispatches a screen transition focus call onto the specialized system options interface.
     */
    private fun doShowSettingsScreen() {
        log.debug { "doShowSettingsScreen()" }
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.settingsScreen)
    }

}
