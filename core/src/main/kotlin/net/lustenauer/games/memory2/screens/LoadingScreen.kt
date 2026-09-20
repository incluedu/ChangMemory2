package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.viewport.StretchViewport
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.utils.Constants
import net.lustenauer.games.memory2.utils.Constants.Atlas
import net.lustenauer.games.memory2.utils.Constants.SkinConfig
import net.lustenauer.games.memory2.utils.Constants.Viewport

class LoadingScreen(game: ChangMemory) : AbstractScreen(game) {
    private val TAG: String = this.javaClass.name

    private lateinit var stage: Stage
    private lateinit var windowSkin: Skin
    private lateinit var lblLoading: Label

    override fun render(deltaTime: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)

        update(deltaTime)

        val progressPercent = (Assets.instance.manager.progress * 100).toInt()
        lblLoading.setText("LOADING ... $progressPercent%")

        stage.act(deltaTime)
        stage.draw()
    }



    override fun resize(width: Int, height: Int) {
        Gdx.app.debug(TAG, "resize(width $width, height $height)")

        stage.viewport.update(width, height, true)
    }

    override fun show() {
        Gdx.app.debug(TAG, "show()")

        stage = Stage(
            StretchViewport(
                Viewport.GUI_WIDTH,
                Viewport.GUI_HEIGHT
            )
        )
        Gdx.input.inputProcessor = stage

        init()
    }

    override fun hide() {
        Gdx.app.debug(TAG, "hide() ")

        stage.dispose()
    }

    override fun pause() {
        Gdx.app.debug(TAG, "pause()")
    }

    /* PRIVATE METHODS */ /* ================ */
    private fun init() {
        Gdx.app.debug(TAG, "init() ")

        windowSkin = Skin(
            Gdx.files.internal(SkinConfig.WINDOW),
            TextureAtlas(Atlas.WINDOWS)
        )

        val layerBackground = buildLayerBackground()
        val layerLogo = buildLayerLogo()
        val layerControls = buildLayerControls()

        stage.clear()
        val stack = Stack()
        stage.addActor(stack)
        stack.setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerLogo)
        stack.add(layerControls)
    }

    private fun update(deltaTime: Float) {
        if (game.readyForStart) doShowMenuScreen()
    }

    /* LAYERS AND ACTORS */ /* ================= */
    private fun buildLayerBackground(): com.badlogic.gdx.scenes.scene2d.ui.Table {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table()
        val imgBackground = com.badlogic.gdx.scenes.scene2d.ui.Image(windowSkin, "background4")
        layer.add(imgBackground)
        return layer
    }

    private fun buildLayerControls(): com.badlogic.gdx.scenes.scene2d.ui.Table? {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table().bottom()
        return layer
    }

    private fun buildLayerLogo(): com.badlogic.gdx.scenes.scene2d.ui.Table {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table()

        var lbl = Label(
            "CHANG MEMORY",
            windowSkin,
            "font48",
            com.badlogic.gdx.graphics.Color.YELLOW
        )
        lbl.setPosition((Viewport.GUI_WIDTH - lbl.getWidth()) / 2, 700f)
        layer.addActor(lbl)

        lbl = Label(
            "(c) 2015 BY Lustenauer Net",
            windowSkin,
            "font24",
            com.badlogic.gdx.graphics.Color.YELLOW
        )
        lbl.setPosition((Viewport.GUI_WIDTH - lbl.getWidth()) / 2, 660f)
        layer.addActor(lbl)

        lblLoading = Label(
            "LOADING ... 000%",
            windowSkin,
            "font48",
            com.badlogic.gdx.graphics.Color.WHITE
        )
        lblLoading.setPosition((Viewport.GUI_WIDTH - lblLoading.getWidth()) / 2, 400f)
        layer.addActor(lblLoading)

        lbl = Label(
            "Version " + ChangMemory.actionResolver?.appVersion,
            windowSkin,
            "font12",
            com.badlogic.gdx.graphics.Color.WHITE
        )
        lbl.setPosition(20f, 20f)
        layer.addActor(lbl)

        return layer
    }

    /* HANDLER METHODS */ /* ================ */
    private fun doShowMenuScreen() {
        Gdx.app.debug(TAG, "doShowMenuScreen()")
        game.setScreen(ChangMemory.menuScreen)
    }
}
