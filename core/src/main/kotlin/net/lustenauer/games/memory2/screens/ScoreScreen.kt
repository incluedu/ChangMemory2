package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.model.ScoreList
import net.lustenauer.games.memory2.ui.actors.BtnBack
import net.lustenauer.games.memory2.ui.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.ui.actors.BtnGooglePlusSignOut
import net.lustenauer.games.memory2.ui.actors.BtnPlayAchievements
import net.lustenauer.games.memory2.ui.actors.BtnPlayLeaderboards
import net.lustenauer.games.memory2.utils.AchievementEntry
import net.lustenauer.games.memory2.utils.AchievementManager
import net.lustenauer.games.memory2.utils.Constants
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XL

/**
 * Chalkboard highscore leaderboard ledger screen context.
 * Compiles local memory arrays into a readable matrix separate from active gameplay loop updates.
 *
 * @author Patric Hollenstein
 */
class ScoreScreen(game: ChangMemory) : AbstractScreen(game) {

    private val log = logger<ScoreScreen>()

    private var stage: Stage? = null
    private var windowSkin: Skin? = null

    private lateinit var achList: Array<AchievementEntry>
    private lateinit var btnAchievements: BtnPlayAchievements
    private lateinit var btnLeaderboards: BtnPlayLeaderboards

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        update()
        stage?.act(delta)
        stage?.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage?.viewport?.update(width, height, true)
    }

    override fun show() {
        log.debug { "show()" }
        ChangMemory.instance.actionResolver?.setTrackerScreenName("ScoreScreen")

        achList = AchievementManager.getAchievements()

        stage = Stage(
            StretchViewport(
                Constants.Viewport.GUI_WIDTH,
                Constants.Viewport.GUI_HEIGHT
            )
        )

        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.inputProcessor = stage

        init()
    }

    override fun hide() {
        log.debug { "hide()" }
    }

    override fun pause() {
        log.debug { "pause()" }
    }

    override fun dispose() {
        log.debug { "dispose() -> Destroying ScoreScreen contexts" }
        stage?.dispose()
        super.dispose()
    }

    private fun init() {
        windowSkin = Assets.skinWindow

        val layerBackground = buildLayerBackground()
        val layerLogo = buildLayerLogo()
        val layerControls = buildLayerControls()

        stage?.clear()
        val stack = Stack()
        stage?.addActor(stack)
        stack.setSize(Constants.Viewport.GUI_WIDTH, Constants.Viewport.GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerLogo)
        stack.add(layerControls)
    }

    private fun update() {
        updateInputs()
        updateButtons()
    }

    private fun updateButtons() {
        val signedIn = ChangMemory.instance.actionResolver?.isSignedIn ?: false
        btnAchievements.isVisible = signedIn
        btnLeaderboards.isVisible = signedIn
    }

    private fun updateInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            doShowPrevScreen()
        }
    }

    private fun buildLayerBackground(): Table {
        val layer = Table()
        val imgBackground = Image(windowSkin, "background4")
        layer.add(imgBackground)
        return layer
    }

    private fun buildLayerControls(): Table {
        val layer = Table().bottom()

        btnAchievements = BtnPlayAchievements()
        btnLeaderboards = BtnPlayLeaderboards()

        layer.addActor(ScoreList.scorePane)

        layer.addActor(BtnBack(ChangMemory.prevScreen))
        layer.addActor(btnAchievements)
        layer.addActor(btnLeaderboards)
        layer.addActor(BtnGooglePlusSignIn())
        layer.addActor(BtnGooglePlusSignOut())
        return layer
    }

    private fun buildLayerLogo(): Table {
        val layer = Table()

        val lblTitle = Label("CHANG MEMORY II", windowSkin, FONT_XL, Color.YELLOW).apply {
            setPosition((Constants.Viewport.GUI_WIDTH - width) / 2, 725f)
        }
        layer.addActor(lblTitle)

        val lblCopyright = Label("(c) 2015 - 2026 BY lustenauer.net", windowSkin, Constants.Fonts.FONT_M, Color.YELLOW).apply {
            setPosition((Constants.Viewport.GUI_WIDTH - width) / 2, 675f)
        }
        layer.addActor(lblCopyright)

        return layer
    }

    private fun doShowPrevScreen() {
        log.debug { "doShowPrevScreen()" }
        game.setScreen(ChangMemory.prevScreen)
    }
}
