package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.Input.Keys.ESCAPE
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.graphics.Color.YELLOW
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align.left
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.log.logger
import ktx.scene2d.*
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.ui.actors.BtnBack
import net.lustenauer.games.memory2.ui.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XL
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XXS
import net.lustenauer.games.memory2.utils.Constants.Skins.BACKGROUND_6
import net.lustenauer.games.memory2.utils.Constants.Viewport

/**
 * Autoscrolling credits screen powered by KTX Scene2D DSL hierarchy structures.
 * Streams localized text assets inside a smooth viewport container overlay.
 *
 * @author Patric Hollenstein
 */
class CreditsScreen(game: ChangMemory) : AbstractScreen(game) {

    private val log = logger<CreditsScreen>()

    private lateinit var stage: Stage
    private lateinit var scrollPane: ScrollPane

    override fun render(deltaTime: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        update()
        stage.act(deltaTime)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        log.info { "Displaying credits and attribution rolls" }
        ChangMemory.instance.actionResolver?.setTrackerScreenName(this::class.java.name)

        stage = Stage(StretchViewport(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT))
        Gdx.input.setCatchKey(BACK, true)
        Gdx.input.inputProcessor = stage

        Scene2DSkin.defaultSkin = Assets.skinWindow

        buildUiLayout()
    }

    override fun hide() {
        stage.dispose()
    }

    override fun pause() {}

    /**
     * Orchestrates structural compilation triggers using the core KTX stack matrix.
     */
    private fun buildUiLayout() {
        stage.clear()

        stage.actors {
            stack {
                setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)
                table().buildLayerBackground
                table().buildLayerHeader
                table().buildLayerCredits
                table().buildLayerControls
            }
        }
    }

    private fun update() {
        updateInputs()
        updateScrollPane()
    }

    /**
     * Drives the automatic visual text scrolling pipeline frame by frame.
     */
    private fun updateScrollPane() {
        if (::scrollPane.isInitialized) {
            if (scrollPane.scrollY < scrollPane.maxY) {
                scrollPane.scrollY += 0.35f
            }
        }
    }

    private fun updateInputs() {
        if (Gdx.input.isKeyJustPressed(ESCAPE) || Gdx.input.isKeyPressed(BACK)) {
            doShowPrevScreen()
        }
    }

    /**
     * Extends the [KTableWidget] to cleanly assign the new green chalkboard visual context.
     */
    private val @Scene2dDsl KTableWidget.buildLayerBackground: KTableWidget
        get() {
            background = skin.getDrawable(BACKGROUND_6)
            return this
        }

    /**
     * Extends the [KTableWidget] to format static dynamic application engine labels.
     */
    private val @Scene2dDsl KTableWidget.buildLayerHeader: KTableWidget
        get() {
            top().padTop(20f)

            val versionText = ChangMemory.instance.actionResolver?.appVersion ?: "2.0-Desktop"
            label("Version $versionText", style = FONT_XXS) { color = WHITE }
                .cell(align = left, padLeft = 15f)
            row()

            label("CHANG MEMORY II", style = FONT_XL) { color = YELLOW }
                .cell(padTop = 10f)
            row()
            label("(c) 2015 - 2026 BY lustenauer.net", style = FONT_M) { color = YELLOW }
            return this
        }

    /**
     * Extends the [KTableWidget] to load external text files into the smooth scrolling layer.
     */
    private val @Scene2dDsl KTableWidget.buildLayerCredits: KTableWidget
        get() {
            val handle = Gdx.files.internal("credits.txt")
            val creditPayload = if (handle.exists()) handle.readString() else "Credits asset missing."

            scrollPane(skin = skin) {
                setSmoothScrolling(true)
                scrollPane = this

                table {
                    left().pad(10f)
                    label(creditPayload, style = FONT_XXS) { color = WHITE }
                }
            }.cell(width = 480f, height = 540f, padTop = 110f, padBottom = 70f)

            return this
        }

    /**
     * Extends the [KTableWidget] to align functional back anchors at the bottom layer.
     */
    private val @Scene2dDsl KTableWidget.buildLayerControls: KTableWidget
        get() {
            bottom().padBottom(20f)

            table {
                add(BtnBack(ChangMemory.prevScreen)).padRight(15f)
                add(BtnGooglePlusSignIn())
            }
            return this
        }

    private fun doShowPrevScreen() {
        log.info { "Rerouting screen state back to context origin" }
        game.screen = ChangMemory.prevScreen
    }
}
