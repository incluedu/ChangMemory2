package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.graphics.Color.YELLOW
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XS
import net.lustenauer.games.memory2.utils.Constants.Viewport.GUI_HEIGHT
import net.lustenauer.games.memory2.utils.Constants.Viewport.GUI_WIDTH

/**
 * Screen context responsible for asynchronous asset deployment visualization.
 * Pre-renders branding elements and tracks the global loading pipeline progress percentages.
 *
 * @author Patric Hollenstein
 */
class LoadingScreen(game: ChangMemory) : AbstractScreen(game) {

    private val log = logger<LoadingScreen>()

    private lateinit var stage: Stage
    private lateinit var windowSkin: Skin
    private lateinit var lblLoading: Label

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        update()

        val progressPercent = (Assets.manager.progress * 100).toInt()
        lblLoading.setText("LOADING ... $progressPercent%")

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        log.debug { "resize(width $width, height $height)" }
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        log.debug { "show()" }

        stage = Stage(
            StretchViewport(
                GUI_WIDTH,
                GUI_HEIGHT
            )
        )
        Gdx.input.inputProcessor = stage

        init()
    }

    override fun hide() {
        log.debug { "hide()" }
        stage.dispose()
    }

    override fun pause() {
        log.debug { "pause()" }
    }


    /**
     * Pre-charges core graphic generation contexts and links visual table layers.
     */
    private fun init() {
        log.debug { "init()" }

        Assets.loadTextures()
        windowSkin = Assets.skinWindow

        val layerBackground = buildLayerBackground()
        val layerLogo = buildLayerLogo()
        val layerControls = buildLayerControls()

        stage.clear()
        val stack = Stack()
        stage.addActor(stack)
        stack.setSize(GUI_WIDTH, GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerLogo)
        stack.add(layerControls)
    }

    private fun update() {
        if (game.readyForStart) doShowMenuScreen()
    }

    private fun buildLayerBackground(): Table {
        val layer = Table()
        val imgBackground = Image(windowSkin, "background4")
        layer.add(imgBackground)
        return layer
    }

    private fun buildLayerControls(): Table? {
        val layer = Table().bottom()
        return layer
    }

    private fun buildLayerLogo(): Table {
        val layer = Table()

        val lblTitle = Label("CHANG MEMORY II", windowSkin, Constants.Fonts.FONT_XL, YELLOW).apply {
            setPosition((GUI_WIDTH - width) / 2, 700f)
        }
        layer.addActor(lblTitle)

        val lblCopyright = Label("(c) 2015 - 2026 BY lustenauer.net", windowSkin, FONT_M, YELLOW).apply {
            setPosition((GUI_WIDTH - width) / 2, 660f)
        }
        layer.addActor(lblCopyright)

        lblLoading = Label("LOADING ... 000%", windowSkin, Constants.Fonts.FONT_XL, WHITE).apply {
            setPosition((GUI_WIDTH - width) / 2, 400f)
        }
        layer.addActor(lblLoading)

        val appVersion = ChangMemory.instance.actionResolver?.appVersion ?: "2.0-Desktop"
        val lblVersion = Label("Version $appVersion", windowSkin, FONT_XS, WHITE).apply {
            setPosition(20f, 20f)
        }
        layer.addActor(lblVersion)

        return layer
    }

    private fun doShowMenuScreen() {
        log.debug { "doShowMenuScreen()" }
        game.setScreen(ChangMemory.menuScreen)
    }
}
