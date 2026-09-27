package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.ui.MenuLayout
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.Constants
import net.lustenauer.games.memory2.utils.Constants.Viewport

/**
 * Lifecycle and input router manager for the main menu screen.
 * Delegates visual layouts entirely onto [net.lustenauer.games.memory2.ui.MenuLayout].
 *
 * @author Patric Hollenstein
 */
class MenuScreen(game: ChangMemory) : AbstractScreen(game), MenuLayout.MenuActionHandler {

    private val log = logger<MenuScreen>()
    private var stage: Stage? = null

    /**
     * Fired when this screen becomes the current active window for the game.
     * Initializes the stage, viewports, background assets, and mounts the [MenuLayout].
     */
    override fun show() {
        log.debug { "show()" }
        ChangMemory.instance.actionResolver?.setTrackerScreenName("MenuScreen")
        Gdx.input.setCatchKey(BACK, true)

        AudioManager.playMusic()

        stage = Stage(StretchViewport(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT))
        Gdx.input.inputProcessor = stage

        val backgroundImage = Image(Assets.skinWindow, Constants.Skins.BACKGROUND_4).apply {
            setFillParent(true)
        }
        stage?.addActor(backgroundImage)

        val menuLayout = MenuLayout(this)
        stage?.addActor(menuLayout)
    }

    /**
     * Executes chronological frame updates, processes hardware escape input polls,
     * and triggers the Scene2D stage rendering cycle.
     *
     * @param delta the fractional time slice elapsed since the last rendered frame
     */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(BACK)) {
            onExitSelected()
        }

        stage?.act(delta)
        stage?.draw()
    }

    /**
     * Handles window resizing events by updating the boundaries of the active [Stage] viewport.
     *
     * @param width the fresh virtual pixel width specification
     * @param height the fresh virtual pixel height specification
     */
    override fun resize(width: Int, height: Int) {
        log.debug { "resize($width, $height)" }
        stage?.viewport?.update(width, height, true)
    }

    /**
     * Invoked when the application loses operating system focus (e.g., incoming phone call).
     */
    override fun pause() {
        log.debug { "pause()" }
    }

    /**
     * Invoked when this screen is replaced by another focus context inside the game engine routing.
     */
    override fun hide() {
        log.debug { "hide()" }
    }

    /**
     * Destroys active structural graphics subsystems, unlinking active stages to clear memory.
     */
    override fun dispose() {
        log.debug { "dispose() -> Destroying stage context" }
        stage?.dispose()
        super.dispose()
    }

    /**
     * Route the user focus directly into the core card matching gameplay arena loop.
     */
    override fun onPlaySelected() {
        log.debug { "onPlaySelected()" }
        game.setScreen(ChangMemory.cardScreen)
    }

    /**
     * Route the user focus onto the local chalkboard leaderboard register panel.
     */
    override fun onScoresSelected() {
        log.debug { "onScoresSelected()" }
        game.setScreen(ChangMemory.scoreScreen)
    }

    /**
     * Route the user focus into the configuration panel to alter systemic properties.
     */
    override fun onSettingsSelected() {
        log.debug { "onSettingsSelected()" }
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.settingsScreen)
    }

    /**
     * Route the user focus onto the roll credits overview acknowledgment scene.
     */
    override fun onCreditsSelected() {
        log.debug { "onCreditsSelected()" }
        game.setScreen(ChangMemory.creditsScreen)
    }

    /**
     * Safely triggers a systemic termination invocation to exit the game.
     */
    override fun onExitSelected() {
        log.info { "onExitSelected() -> Exit ChangMemory II" }
        Gdx.app.exit()
    }
}
