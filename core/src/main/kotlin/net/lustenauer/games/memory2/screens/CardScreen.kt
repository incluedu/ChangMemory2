package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.actors.onClick
import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.AssetSound
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.InfoList
import net.lustenauer.games.memory2.game.objects.Card
import net.lustenauer.games.memory2.game.windows.WindowGameOver
import net.lustenauer.games.memory2.game.windows.WindowPause
import net.lustenauer.games.memory2.utils.AchievementEntry
import net.lustenauer.games.memory2.utils.AchievementManager
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.Constants.Atlas
import net.lustenauer.games.memory2.utils.Constants.SkinConfig
import net.lustenauer.games.memory2.utils.Constants.Skins.BACKGROUND_4
import net.lustenauer.games.memory2.utils.Constants.Viewport
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_MENU
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESTART
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESUME
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SCORE
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SETTINGS
import net.lustenauer.gdx.scenes.scene2d.CommandListener
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * The core gameplay screen of ChangMemory II. Manages the main memory card grid mechanics,
 * layout calculations, score logic, visual announcements, game timer streams,
 * multi-stage overlays, and user interaction states.
 *
 * @param game The main execution game instance configuration wrapper.
 * @author Patric Hollenstein
 */
class CardScreen(game: ChangMemory) : AbstractScreen(game) {
    private val log = logger<CardScreen>()

    private val controller = GameController()
    private lateinit var hud: GameHUD
    private lateinit var btnPause: Button
    private lateinit var windowGameOver: WindowGameOver
    private lateinit var windowPause: WindowPause
    private lateinit var skinWindow: Skin
    private lateinit var stack: Stack
    private lateinit var stage: Stage
    private lateinit var hudStage: Stage

    private lateinit var beepSound: AssetSound
    private lateinit var gameOverSound: AssetSound
    private lateinit var toManyTrySound: AssetSound
    private lateinit var levelCompleteSound: AssetSound
    private lateinit var luckyTrySound: AssetSound

    private var gameSet = GdxArray<Card>()
    private lateinit var achList: GdxArray<AchievementEntry>

    private var level = 0
    private var minActorCount = 0
    private var gameOver = false
    private var gamePaused = false
    private var screenPaused = false
    private var settingsScreenShow = false
    private var scoresScreenShow = false

    override fun show() {
        log.debug { "show()" }
        ChangMemory.actionResolver?.setTrackerScreenName(CardScreen::class.java.name)

        Gdx.input.setCatchKey(BACK, true)

        achList = AchievementManager.instance.getAchievements()
        AudioManager.instance.playMusic()
        init()
    }

    override fun render(deltaTime: Float) {
        super.render(deltaTime)

        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (!screenPaused) hudStage.act(deltaTime)
        update(deltaTime)

        hud.update(controller.level, controller.score, controller.timeLeft, controller.timeLeft30Seconds)

        stage.draw()
        hudStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        log.debug { "CardScreen resize to $width, $height" }

        stage.viewport.update(width, height, true)
        hudStage.viewport.update(width, height, true)
    }

    override fun pause() {
        log.debug { "CardScreen paused" }
        screenPaused = true
        AudioManager.instance.pauseMusic()
    }

    override fun hide() {
        log.debug { "CardScreen hide" }
        if (!settingsScreenShow && !scoresScreenShow) {
            stage.dispose()
            hudStage.dispose()
        }
    }

    override fun resume() {
        super.resume()
        log.debug { "CardScreen resume after Pause" }
        screenPaused = false
        AudioManager.instance.playMusic()
    }

    /* GETTER AND SETTER */ /* ================= */

    /* PRIVATE METHODS */ /* ================ */

    /**
     * Constructs a fresh card game set configuration layout matrix matching the newly incremented difficulty scale index.
     * Injects flashing announcement overlays and randomizes geometric actor alignments.
     */
    private fun buildCardSet() {
        val imgBackground = Image(skinWindow, BACKGROUND_4)

        stage.clear()
        stage.addActor(imgBackground)

        val currentSet = controller.startNextLevel()

        for (card in currentSet) {
            card.loadAllSounds()

            val randomRotation = (MathUtils.random() * 6f) - 3f
            card.rotation = randomRotation
            stage.addActor(card)
        }

        initButtonPause()
        initWindowPause()

        windowPause.isVisible = false
        if (::windowGameOver.isInitialized) {
            windowGameOver.isVisible = false
        }

        AchievementManager.instance.checkAchievementsLevel(controller.level)
        log.debug { "--> Start level ${controller.level}" }

        Gdx.input.inputProcessor = stage
    }

    private fun init() {
        if (!settingsScreenShow && !scoresScreenShow) {
            initSounds()
            initStage()

            initWindowGameOver()

            minActorCount = hudStage.actors.size // save for level completed

            initFields()
            buildCardSet()
        }

        if (scoresScreenShow) {
            Gdx.input.inputProcessor = hudStage
        } else {
            Gdx.input.inputProcessor = stage
        }

        settingsScreenShow = false
        scoresScreenShow = false
    }

    /**
     * Initializes the interactive pause button, positions it in the upper-right UI corner,
     * and attaches a modern, safe KTX click listener to trigger the game suspension state.
     */
    private fun initButtonPause() {
        btnPause = Button(skinWindow, "pausePlay").apply {
            setPosition(470f, 765f, Align.right)

            onClick { doGamePause() }
        }
        stage.addActor(btnPause)
    }

    /**
     * Resets all session metrics back to defaults before a fresh campaign starts.
     */
    private fun initFields() {
        controller.resetFields()
    }

    /**
     * Initializes the [WindowGameOver] container layer.
     */
    private fun initWindowGameOver() {
        log.debug { "initWindowGameOver" }

        windowGameOver = WindowGameOver(CommandListener { event ->
            when (event?.command) {
                CMD_RESTART -> doGameRestart()
                CMD_MENU    -> doShowMenuScreen()
                CMD_SCORE   -> doShowScoreScreen()
            }
        })
        hudStage.addActor(windowGameOver)
    }

    /**
     * Initializes the [WindowPause] container layer.
     */
    private fun initWindowPause() {
        log.debug { "initWindowPause" }

        windowPause = WindowPause(CommandListener { event ->
            when (event?.command) {
                CMD_RESTART  -> doGameRestart()
                CMD_MENU     -> doShowMenuScreen()
                CMD_SETTINGS -> doShowSettingsScreen()
                CMD_RESUME   -> doGameResume()
            }
        })
        stage.addActor(windowPause)
    }

    private fun initSounds() {
        beepSound = Assets.instance.beepSound
        gameOverSound = Assets.instance.gameOverSound
        luckyTrySound = Assets.instance.luckyTrySound
        levelCompleteSound = Assets.instance.levelComplSound
        toManyTrySound = Assets.instance.toManyTrySound

        beepSound.loadSound()
        gameOverSound.loadSound()
        luckyTrySound.loadSound()
        levelCompleteSound.loadSound()
        toManyTrySound.loadSound()
    }

    private fun initStage() {
        skinWindow = Skin(
            Gdx.files.internal(SkinConfig.WINDOW),
            TextureAtlas(Atlas.WINDOWS)
        )

        stage = Stage(StretchViewport(Viewport.CARD_WIDTH, Viewport.CARD_HEIGHT))
        stack = Stack().apply {
            setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)
        }
        stage.addActor(stack)

        hudStage = Stage(StretchViewport(Viewport.CARD_WIDTH, Viewport.CARD_HEIGHT))
        hud = GameHUD(hudStage, skinWindow)
    }

    private fun update(deltaTime: Float) {
        if (!screenPaused && !gamePaused) {
            stage.act(deltaTime)

            updateTime(deltaTime)
            InfoList.instance.addInfoTable(hudStage)

            updateFlipCard()
            updateLevelCompleted()
            updateVisibleCards()

            AudioManager.instance.update(deltaTime)
        }
        updateInputs()
    }

    /**
     * This method check all the cards in the cardSet. When a card is found where it is necessary to flip to front then
     * the card will flip to front and will add to the list off visibleCards.
     * Delegates all state properties directly to the backend [controller].
     */
    private fun updateFlipCard() {
        for (card in controller.gameSet) {
            if (card.startCardFlip && controller.visibleCards.size < 2) {
                card.flipCard()
                controller.visibleCards.add(card)
                controller.cardFlipCount++
            }
        }
    }

    private fun updateInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(BACK)) {
            if (gameOver || gamePaused) doShowMenuScreen()
            else doGamePause()
        }
    }

    /**
     * Monitors the level completion status.
     * Waits for all active info pooling elements to fade out before generating the next world stage.
     */
    private fun updateLevelCompleted() {
        if (controller.levelCompleted && hudStage.actors.size == minActorCount) {
            buildCardSet()
        }
    }

    /**
     * Executes chronological time subtractions on the remaining match countdown.
     * Triggers warnings if limits break critical milestones.
     */
    fun updateTime(deltaTime: Float) {
        controller.updateTime(
            deltaTime = deltaTime,
            onGameOverTrigger = { doGameOver() },
            playBeepSound = { AudioManager.instance.play(beepSound) }
        )
    }

    /**
     * Checks if the currently selected cards match a pair.
     * Delegates full mathematical validation back to the [controller].
     */
    private fun updateVisibleCards() {
        controller.processVisibleCards(
            onPlayToManySound = { AudioManager.instance.play(toManyTrySound) },
            onLuckyStrike = { cardA, cardB -> doLuckStrikeSet(cardA, cardB) },
            onSolved = { cardA, cardB -> doCardSolved(cardA, cardB) },
            onLevelComplete = { doLevelCompleted() }
        )
    }

    /* HANDLER */

    private fun doGameOver() {
        log.debug { "doGameOver()" }
        if (controller.gameOver) return

        controller.gameOver = true
        InfoList.instance.add("GAME OVER", flash = true, size = InfoList.SIZE_XXXL)
        AudioManager.instance.add(gameOverSound)

        windowGameOver.apply {
            isVisible = true
            setPosition(40f, -200f)
            addAction(moveTo(40f, 320f, 3f))
            setScore(controller.score)
            setTime(controller.totalTime)
            setCardFlipCount(controller.cardFlipCount)
            setTotalCardSolvedCount(controller.totalCardSetSolvedCount)
            setLuckStrikeCount(controller.luckyStrikeCount)
        }

        Gdx.input.inputProcessor = hudStage

        AchievementManager.instance.checkGameDoneAchievement(controller.score)

        if (!controller.scoreSubmit) {
            controller.submitScore()
        }
    }

    private fun doGamePause() {
        log.debug { "doGamePause()" }
        if (!gamePaused) {
            gamePaused = true
            windowPause.isVisible = true
        } else {
            doGameResume()
        }
    }

    private fun doGameRestart() {
        log.debug { "doGameRestart()" }

        if (!controller.scoreSubmit) {
            controller.submitScore()
        }

        windowGameOver.isVisible = false
        windowPause.isVisible = false

        gamePaused = false
        controller.gamePaused = false

        Gdx.app.postRunnable {
            initFields()
            buildCardSet()
            AudioManager.instance.playMusic()
        }
    }

    /**
     * Resumes the active gameplay session. Hides the pause overlay menu,
     * resets the visual state of the interactive pause button, and restarts
     * the background music stream.
     */
    private fun doGameResume() {
        log.debug { "doGameResume()" }

        windowPause.isVisible = false
        btnPause.isChecked = false
        gamePaused = false

        AudioManager.instance.playMusic()

        gameSet.forEach { it.startCardFlip = false }
    }

    /**
     * Triggers the level completed state. Displays a floating announcement
     * on the screen and plays the victory jingle via the audio manager.
     */
    private fun doLevelCompleted() {
        log.debug { "doLevelCompleted() --> Level: $level" }
        InfoList.instance.add("LEVEL COMPLETED!", size = InfoList.SIZE_L)
        AudioManager.instance.add(levelCompleteSound)
    }

    fun doShowMenuScreen() {
        log.debug { "doShowMenuScreen()" }
        if (!controller.scoreSubmit) controller.submitScore()

        gamePaused = false
        controller.gamePaused = false

        game.setScreen(ChangMemory.menuScreen)
    }

    fun doShowSettingsScreen() {
        log.debug { "doShowSettingsScreen()" }
        settingsScreenShow = true
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.settingsScreen)
    }

    private fun doShowScoreScreen() {
        scoresScreenShow = true
        log.debug { "doShowScoreScreen()" }
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.scoreScreen)
    }

    private fun doLuckStrikeSet(a: Card, b: Card) {
        log.debug { "doLuckStrikeSet()" }

        InfoList.instance.add(a.cardName, size = InfoList.SIZE_L, color = Color.RED)
        InfoList.instance.add("+${a.time + b.time} sec", size = InfoList.SIZE_L)
        InfoList.instance.add("+${a.score + b.score} POINTS", size = InfoList.SIZE_L)
        InfoList.instance.add("")

        InfoList.instance.add("LUCKY TRY!", flash = true, size = InfoList.SIZE_XL)
        InfoList.instance.add("+100 EXTRA POINTS", size = InfoList.SIZE_L)

        a.playCardSolvedSound()
        AudioManager.instance.play(luckyTrySound)
    }

    private fun doCardSolved(a: Card, b: Card) {
        log.debug { "doCardSolved()" }

        InfoList.instance.add(a.cardName, size = InfoList.SIZE_L)

        val ti = a.time + b.time
        if (ti >= 1) InfoList.instance.add("+$ti sec", size = InfoList.SIZE_L)

        val sc = a.score + b.score
        if (sc > 0) InfoList.instance.add("+$sc POINTS", size = InfoList.SIZE_L)

        a.playCardSolvedSound()
    }
}
