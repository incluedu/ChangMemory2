package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.ScoreList
import net.lustenauer.games.memory2.game.actors.BtnBack
import net.lustenauer.games.memory2.game.actors.BtnGooglePlusSignIn
import net.lustenauer.games.memory2.game.actors.BtnGooglePlusSignOut
import net.lustenauer.games.memory2.game.actors.BtnPlayAchievements
import net.lustenauer.games.memory2.game.actors.BtnPlayLeaderboards
import net.lustenauer.games.memory2.utils.AchievementEntry
import net.lustenauer.games.memory2.utils.AchievementManager
import net.lustenauer.games.memory2.utils.Constants

class ScoreScreen(game: ChangMemory) : AbstractScreen(game) {
    private val TAG: kotlin.String = this.javaClass.getName()

    private var stage: Stage? = null
    private var windowSkin: com.badlogic.gdx.scenes.scene2d.ui.Skin? = null

    private lateinit var achList: com.badlogic.gdx.utils.Array<AchievementEntry>
    private lateinit var btnAchievements: BtnPlayAchievements
    private lateinit var btnLeaderboards: BtnPlayLeaderboards

    public override fun render(deltaTime: kotlin.Float) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)

        update(deltaTime)
        stage!!.act(deltaTime)
        stage!!.draw()
    }

    public override fun resize(width: kotlin.Int, height: kotlin.Int) {
        stage!!.getViewport().update(width, height, true)
    }

    override fun show() {
        Gdx.app.debug(TAG, "show()")

        ChangMemory.actionResolver?.setTrackerScreenName(TAG)

        achList = AchievementManager.instance.getAchievements()

        stage = Stage(
            StretchViewport(
                Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT
            )
        )

        // 4. Moderner GDX-Aufruf für den Back-Key (falls BACK rot ist, vollen Pfad nutzen)
        Gdx.input.setCatchKey(com.badlogic.gdx.Input.Keys.BACK, true)
        Gdx.input.setInputProcessor(stage)

        init()
    }


    public override fun hide() {
        Gdx.app.debug(TAG, "hide()")
        stage!!.dispose()
    }

    public override fun pause() {
        Gdx.app.debug(TAG, "pause()")
    }

    /* PRIVATE METHODES */ /* ================ */
    private fun init() {
        windowSkin = com.badlogic.gdx.scenes.scene2d.ui.Skin(
            Gdx.files.internal(Constants.SKIN_WINDOW),
            TextureAtlas(Constants.TEXTURE_ATLAS_WINDOS)
        )

        val layerBackground = buildLayerBackground()
        val layerLogo = buildLayerLogo()
        val layerControls = buildLayerContorls()

        stage!!.clear()
        val stack = com.badlogic.gdx.scenes.scene2d.ui.Stack()
        stage!!.addActor(stack)
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT)
        stack.add(layerBackground)
        stack.add(layerLogo)
        stack.add(layerControls)
    }

    private fun update(deltaTime: kotlin.Float) {
        updateInputs()
        updateButtons()
    }

    private fun updateButtons() {
        val signedIn = ChangMemory.actionResolver?.isSignedInGPGS ?: false

        btnAchievements.isVisible = signedIn
        btnLeaderboards.isVisible = signedIn
    }


    private fun updateInputs() {
        // return to menu Screen
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE) or Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.BACK)) {
            doShowPrevScreen()
        }
    }

    /* LAYERS AND ACTORS */ /* ================= */
    private fun buildLayerBackground(): com.badlogic.gdx.scenes.scene2d.ui.Table {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table()
        val imgBackground = com.badlogic.gdx.scenes.scene2d.ui.Image(windowSkin, "background4")
        layer.add<com.badlogic.gdx.scenes.scene2d.ui.Image?>(imgBackground)
        return layer
    }

    private fun buildLayerContorls(): com.badlogic.gdx.scenes.scene2d.ui.Table {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table().bottom()

        btnAchievements = BtnPlayAchievements()
        btnLeaderboards = BtnPlayLeaderboards()

        layer.addActor(ScoreList.instance.scorePane)

        layer.addActor(BtnBack(ChangMemory.prevScreen))
        layer.addActor(btnAchievements)
        layer.addActor(btnLeaderboards)
        layer.addActor(BtnGooglePlusSignIn())
        layer.addActor(BtnGooglePlusSignOut())
        return layer
    }

    private fun buildLayerLogo(): com.badlogic.gdx.scenes.scene2d.ui.Table {
        val layer = com.badlogic.gdx.scenes.scene2d.ui.Table()
        var lbl: com.badlogic.gdx.scenes.scene2d.ui.Label?

        lbl = com.badlogic.gdx.scenes.scene2d.ui.Label(
            "CHANG MEMORY",
            windowSkin,
            "font48",
            com.badlogic.gdx.graphics.Color.YELLOW
        )
        lbl.setPosition((Constants.VIEWPORT_GUI_WIDTH - lbl.getWidth()) / 2, 700f)
        layer.addActor(lbl)

        lbl = com.badlogic.gdx.scenes.scene2d.ui.Label(
            "(c) 2015 BY Lustenauer Net",
            windowSkin,
            "font24",
            com.badlogic.gdx.graphics.Color.YELLOW
        )
        lbl.setPosition((Constants.VIEWPORT_GUI_WIDTH - lbl.getWidth()) / 2, 660f)
        layer.addActor(lbl)

        return layer
    }

    /* HANDLER METHODES */ /* ================ */
    private fun doShowPrevScreen() {
        Gdx.app.debug(TAG, "doShowPrevScreen()")
        game.setScreen(ChangMemory.prevScreen)
    }
}
