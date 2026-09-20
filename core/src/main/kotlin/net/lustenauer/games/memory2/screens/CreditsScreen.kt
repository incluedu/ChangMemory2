package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.StretchViewport
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.actors.BtnBack
import net.lustenauer.games.memory2.game.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.utils.Constants.Atlas
import net.lustenauer.games.memory2.utils.Constants.SkinConfig
import net.lustenauer.games.memory2.utils.Constants.Viewport

class CreditsScreen(game: ChangMemory) : AbstractScreen(game) {
    private val TAG: String = this.javaClass.getName()

    private var stage: com.badlogic.gdx.scenes.scene2d.Stage? = null
    private var skinWindow: com.badlogic.gdx.scenes.scene2d.ui.Skin? = null

    private var scrollPane: com.badlogic.gdx.scenes.scene2d.ui.ScrollPane? = null

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
        skinWindow = com.badlogic.gdx.scenes.scene2d.ui.Skin(
            Gdx.files.internal(SkinConfig.WINDOW),
            TextureAtlas(Atlas.WINDOWS)
        )

        val layerBackground: Actor = buildLayerBackground()
        val layerLogo: Actor = buildLayerLogo()
        val layerControls: Actor = buildLayerControls()
        val layerCreditsText: Actor = buildLayerCreditsText()

        stage!!.clear()
        val stack = com.badlogic.gdx.scenes.scene2d.ui.Stack()
        stage!!.addActor(stack)
        stack.setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerLogo)
        stack.add(layerCreditsText)
        stack.add(layerControls)
    }

    private fun update(deltaTime: Float) {
        updateInputs()
        updateScrollPane()
    }

    private fun updateScrollPane() {
        if (scrollPane!!.getScrollY() < scrollPane!!.getMaxY()) scrollPane!!.setScrollY(scrollPane!!.getScrollY() + 0.2f)
    }

    private fun updateInputs() {
        // return to menu Screen
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE) or Gdx.input.isKeyPressed(BACK)) {
            doShowPrevScreen()
        }
    }

    /* LAYERS AND ACTORS */ /* ================= */
    private fun buildLayerBackground(): Table {
        val layer = Table()
        val imgBackground = com.badlogic.gdx.scenes.scene2d.ui.Image(skinWindow, "background4")
        layer.add(imgBackground)
        return layer
    }

    private fun buildLayerControls(): Actor {
        val layer = com.badlogic.gdx.scenes.scene2d.Group()

        layer.addActor(BtnBack(ChangMemory.prevScreen))
        layer.addActor(BtnGooglePlusSignIn())
        return layer
    }

    private fun buildLayerCreditsText(): Actor {
        val handle = Gdx.files.internal("credits.txt")
        val text: String? = handle.readString()

        val layer = Table()
        val txtPane = Table().left().pad(10f)
        val txt =
            Label(text, skinWindow, "font12", Color.WHITE)

        txtPane.add(txt)

        scrollPane = com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(txtPane, skinWindow)
        scrollPane!!.setSize(480f, 580f)
        scrollPane!!.setPosition(0f, 70f)
        scrollPane!!.setSmoothScrolling(true)
        layer.addActor(scrollPane)
        return layer
    }

    private fun buildLayerLogo(): Table {
        val layer = Table()

        val lblTitle = Label("CHANG MEMORY", skinWindow, "font48", Color.YELLOW)
        lblTitle.setPosition((Viewport.GUI_WIDTH - lblTitle.width) / 2, 710f)
        layer.addActor(lblTitle)

        val lblCopyright = Label("(c) 2015 BY Lustenauer Net", skinWindow, "font24", Color.YELLOW)
        lblCopyright.setPosition((Viewport.GUI_WIDTH - lblCopyright.width) / 2, 670f)
        layer.addActor(lblCopyright)

        val versionText = ChangMemory.actionResolver?.appVersion ?: "1.0-Desktop"
        val lblVersion = Label("Version $versionText", skinWindow, "font12", Color.WHITE)
        lblVersion.setPosition(10f, 780f)
        layer.addActor(lblVersion)

        return layer
    }

    /* HANDLER METHODS */ /* ================ */
    private fun doShowPrevScreen() {
        Gdx.app.debug(TAG, "doShowPrevScreen()")
        game.setScreen(ChangMemory.prevScreen)
    }
}
