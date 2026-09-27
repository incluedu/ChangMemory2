package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.app
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.Color
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
import net.lustenauer.games.memory2.utils.Constants.Skins.BACKGROUND_4
import net.lustenauer.games.memory2.utils.Constants.Viewport
import net.lustenauer.games.memory2.utils.GameCommand
import net.lustenauer.games.memory2.ui.CommandListener
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * The core gameplay screen of ChangMemory II. Manages the main memory card grid mechanics,
 * layout calculations, score logic, visual announcements, game timer streams,
 * multi-stage overlays, and user interaction states.
 *
 * @param game The main execution game instance configuration wrapper passed to the base [AbstractScreen].
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

    private lateinit var achList: GdxArray<AchievementEntry>

    private var minActorCount = 0
    private var gamePaused = false
    private var screenPaused = false
    private var settingsScreenShow = false
    private var scoresScreenShow = false

    /**
     * Triggered once this screen context becomes the active visibility layer inside the game loop.
     * Calibrates analytics trackers via [ChangMemory.actionResolver], captures the hardware back button,
     * activates background audio streams via [AudioManager], and triggers the core [init] sequence.
     */
    override fun show() {
        log.debug { "show()" }
        ChangMemory.instance.actionResolver?.setTrackerScreenName(CardScreen::class.java.name)

        Gdx.input.setCatchKey(BACK, true)

        achList = AchievementManager.getAchievements()
        AudioManager.playMusic()
        init()
    }

    /**
     * Core frame rendering cycle execution block.
     * Clears the graphics buffer and updates active stage behaviors chronologically.
     */
    override fun render(delta: Float) {
        ktx.app.clearScreen(100f / 255f, 149f / 255f, 237f / 255f, 1f)

        if (!screenPaused) hudStage.act(delta)
        update(delta)

        hud.update(controller.level, controller.score, controller.timeLeft, controller.timeLeft30Seconds)

        stage.draw()
        hudStage.draw()
    }

    /**
     * Standard sizing layout conversion hook triggered upon viewport scale transitions.
     * Updates both the primary card game [stage] and the [hudStage] viewports,
     * centering the cameras automatically to maintain perfect aspect ratios.
     */
    override fun resize(width: Int, height: Int) {
        log.debug { "CardScreen resize to $width, $height" }

        stage.viewport.update(width, height, true)
        hudStage.viewport.update(width, height, true)
    }

    /**
     * Invoked when the screen context minimizes or visibility focus breaks.
     * Safely freezes the rendering loop by setting [screenPaused] to true
     * and suspends active audio playback via the audio manager.
     */
    override fun pause() {
        log.debug { "CardScreen paused" }
        screenPaused = true
        AudioManager.pauseMusic()
    }

    /**
     * Triggered once active visibility layers swap away inside the central switch engine.
     * Suspends music streams and safely flags transition benchmarks.
     */
    override fun hide() {
        log.debug { "CardScreen hide" }

        if (!settingsScreenShow && !scoresScreenShow) {
            AudioManager.pauseMusic()
        }
    }

    /**
     * Synchronizes the screen session upon application resume states.
     * Safely triggers music playback via [AudioManager] and restores rendering ticks
     * unless the [windowPause] overlay layer is actively blocking focus.
     */
    override fun resume() {
        super.resume()
        log.debug { "CardScreen resume after Pause" }

        if (!windowPause.isVisible) {
            screenPaused = false
            AudioManager.playMusic()
        }
    }

    /**
     * Constructs a fresh card game set configuration layout matrix matching the newly incremented difficulty scale index.
     * Clears the active [stage], spawns the background, and randomizes geometric actor alignments.
     * Re-links interactive overlays via [initButtonPause] and [initWindowPause].
     */
    private fun buildCardSet() {
        val imgBackground = Image(skinWindow, BACKGROUND_4)

        stage.clear()
        stage.addActor(imgBackground)

        val currentSet = controller.startNextLevel()

        currentSet.forEach { card ->
            card.loadAllSounds()

            card.rotation = (MathUtils.random() * 6f) - 3f
            stage.addActor(card)
        }

        initButtonPause()
        initWindowPause()

        windowPause.isVisible = false
        if (::windowGameOver.isInitialized) {
            windowGameOver.isVisible = false
        }

        AchievementManager.checkAchievementsLevel(controller.level)
        log.debug { "--> Start level ${controller.level}" }

        Gdx.input.inputProcessor = stage
    }

    /**
     * Orchestrates the primary initialization and entry routing for the screen session.
     * Prevents destructive resource rebuilds if navigating back from child overlays,
     * calibrates the global input focus via an `if`-expression, and resets navigation flags.
     */
    private fun init() {
        if (!settingsScreenShow && !scoresScreenShow) {
            initSounds()
            initStage()

            initWindowGameOver()

            minActorCount = hudStage.actors.size // save for level completed

            initFields()
            buildCardSet()
        }

        Gdx.input.inputProcessor = if (scoresScreenShow) hudStage else stage

        settingsScreenShow = false
        scoresScreenShow = false
    }

    /**
     * Initializes the interactive [Button] pause play toggle, positions it in the upper-right UI corner,
     * and attaches a modern, safe KTX click listener via [onClick] to trigger the game suspension state.
     */
    private fun initButtonPause() {
        btnPause = Button(skinWindow, "pausePlay").apply {
            setPosition(470f, 765f, Align.right)

            onClick { doGamePause() }
        }
        stage.addActor(btnPause)
    }

    /**
     * Resets all active session metrics back to defaults via the [controller] before a fresh campaign starts.
     */
    private fun initFields() {
        controller.resetFields()
    }

    /**
     * Initializes the [WindowGameOver] overlay container layer and registers its interactive UI command routing.
     */
    private fun initWindowGameOver() {
        log.debug { "initWindowGameOver" }

        windowGameOver = WindowGameOver(CommandListener { event ->
            when (event?.command) {
                GameCommand.RESTART -> doGameRestart()
                GameCommand.MENU -> doShowMenuScreen()
                GameCommand.SCORE -> doShowScoreScreen()
                else -> log.error { "Unhandled GameCommand event in WindowGameOver scope: ${event?.command}" }
            }
        })
        hudStage.addActor(windowGameOver)
    }

    /**
     * Initializes the [WindowPause] overlay container layer and registers its interactive UI command routing.
     */
    private fun initWindowPause() {
        log.debug { "initWindowPause" }

        windowPause = WindowPause(CommandListener { event ->
            when (event?.command) {
                GameCommand.RESTART -> doGameRestart()
                GameCommand.MENU -> doShowMenuScreen()
                GameCommand.SETTINGS -> doShowSettingsScreen()
                GameCommand.RESUME -> doGameResume()
                else -> log.error { "Unhandled GameCommand event in WindowPause scope: ${event?.command}" }
            }
        })
        stage.addActor(windowPause)
    }

    /**
     * Binds the necessary audio tracking clips by fetching pre-configured
     * [AssetSound] references directly from the global [Assets] ecosystem.
     */
    private fun initSounds() {
        beepSound = Assets.beepSound
        gameOverSound = Assets.gameOverSound
        luckyTrySound = Assets.luckyTrySound
        levelCompleteSound = Assets.levelComplSound
        toManyTrySound = Assets.toManyTrySound

    }

    /**
     * Initializes the core visual architecture of the screen session.
     * Binds preloaded skin templates from [Assets], orchestrates the primary game [stage],
     * initializes responsive viewports, and links the graphical [GameHUD] infrastructure.
     */
    private fun initStage() {
        skinWindow = Assets.skinWindow

        stage = Stage(StretchViewport(Viewport.CARD_WIDTH, Viewport.CARD_HEIGHT))

        stack = Stack().apply {
            setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)
        }
        stage.addActor(stack)

        hudStage = Stage(StretchViewport(Viewport.CARD_WIDTH, Viewport.CARD_HEIGHT))
        hud = GameHUD(hudStage, skinWindow)
    }

    /**
     * Updates the underlying game board simulations and tick streams.
     * Blocks execution loops if physical pause flags evaluate to true.
     */
    private fun update(delta: Float) {
        if (!screenPaused && !gamePaused) {
            stage.act(delta)

            updateTime(delta)
            InfoList.addInfoTable(hudStage)
            updateFlipCard()
            updateLevelCompleted()
            updateVisibleCards()

            AudioManager.update(delta)
        }
        updateInputs()
    }

    /**
     * Iterates through the active card set to process user flip requests.
     * Triggers animation sequences and increments tracking counters once selections pass verification.
     * Delegates all state properties directly to the backend [controller].
     */
    private fun updateFlipCard() {
        controller.gameSet.forEach { card ->
            if (card.startCardFlip) {
                if (controller.visibleCards.size < 2) {
                    card.flipCard()
                    controller.visibleCards.add(card)
                    controller.cardFlipCount++
                } else {
                    card.startCardFlip = false
                }
            }
        }
    }

    /**
     * Polls and processes hardware input actions like the Escape key or Android Back button.
     * Manages overlay state transitions safely without bypassing interactive windows.
     */
    private fun updateInputs() {
        val backOrEscapePressed = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
            Gdx.input.isKeyJustPressed(BACK)

        if (backOrEscapePressed) {
            if (!gamePaused && !controller.gameOver) {
                doGamePause()
            } else if (gamePaused && !controller.gameOver) {
                doGameResume()
            }
        }
    }

    /**
     * Monitors the level completion status inside the execution loop.
     * Waits for all active info pooling elements to fade out (until the actor count matches [minActorCount])
     * before triggering the generation of the next world stage via [buildCardSet].
     */
    private fun updateLevelCompleted() {
        if (controller.levelCompleted && hudStage.actors.size == minActorCount) {
            buildCardSet()
        }
    }

    /**
     * Executes chronological time subtractions on the remaining match countdown.
     * Triggers [doGameOver] once limits break or plays a warning sound via [beepSound].
     */
    fun updateTime(delta: Float) {
        controller.updateTime(
            delta = delta,
            onGameOverTrigger = { doGameOver() },
            playBeepSound = { AudioManager.play(beepSound) }
        )
    }

    /**
     * Checks if the currently selected cards match a pair.
     * Delegates state and match validation back to the [controller].
     * Triggers localized audio feedback or routes state shifts via [doCardSolved] and [doLevelCompleted].
     */
    private fun updateVisibleCards() {
        controller.processVisibleCards(
            onPlayToManySound = { AudioManager.play(toManyTrySound) },
            onLuckyStrike = { cardA, cardB -> doLuckStrikeSet(cardA, cardB) },
            onSolved = { cardA, cardB -> doCardSolved(cardA, cardB) },
            onLevelComplete = { doLevelCompleted() }
        )
    }


    /**
     * Triggers the final game over state sequence.
     * Freezes game loops, animates the statistics panel container overlay onto the viewport,
     * tracks unlocked achievements, and submits records to leaderboards.
     */
    private fun doGameOver() {
        log.debug { "doGameOver()" }
        if (controller.gameOver) return

        controller.gameOver = true

        InfoList.add("GAME OVER", flash = true, size = InfoList.SIZE_XXXL)
        AudioManager.add(gameOverSound)

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

        AchievementManager.checkGameDoneAchievement(controller.score)

        if (!controller.scoreSubmit) {
            controller.submitScore()
        }
    }

    /**
     * Toggles the active gameplay suspension state.
     * Activates the [windowPause] overlay layer if running, or delegates
     * execution back to [doGameResume] if the match is already suspended.
     */
    private fun doGamePause() {
        log.debug { "doGamePause()" }
        if (!gamePaused) {
            gamePaused = true
            windowPause.isVisible = true
        } else {
            doGameResume()
        }
    }

    /**
     * Executes an atomic match reset routine.
     * Submits outstanding scores, hides all active overlay windows,
     * and posts a synchronous thread task via `Gdx.app.postRunnable`
     * to safely clear layout fields and rebuild a fresh card matrix.
     */
    private fun doGameRestart() {
        log.debug { "doGameRestart()" }

        if (!controller.scoreSubmit) {
            controller.submitScore()
        }

        windowGameOver.isVisible = false
        windowPause.isVisible = false

        gamePaused = false
        controller.gamePaused = false

        app.postRunnable {
            initFields()
            buildCardSet()
            AudioManager.playMusic()
        }
    }

    /**
     * Resumes the active gameplay session.
     * Hides the [windowPause] overlay menu, resets the visual state of the interactive
     * [btnPause] button, and restarts the background music stream.
     */
    private fun doGameResume() {
        log.debug { "doGameResume()" }

        windowPause.isVisible = false
        btnPause.isChecked = false
        gamePaused = false
        AudioManager.playMusic()
        controller.gameSet.forEach { card ->
            card.startCardFlip = false
        }
    }


    /**
     * Triggers the level completed state sequence.
     * Displays a floating announcement via [InfoList] and plays the victory jingle via the audio manager.
     */
    private fun doLevelCompleted() {
        log.debug { "doLevelCompleted() --> Level: ${controller.level}" }

        InfoList.add("LEVEL COMPLETED!", size = InfoList.SIZE_L)
        AudioManager.add(levelCompleteSound)
    }

    /**
     * Navigates the player back to the main menu screen container.
     * Ensures any outstanding active session records are safely submitted to leaderboards
     * before swapping the visibility focus via [ChangMemory.menuScreen].
     */
    private fun doShowMenuScreen() {
        log.debug { "doShowMenuScreen()" }

        if (!controller.scoreSubmit) {
            controller.submitScore()
        }

        gamePaused = false
        controller.gamePaused = false

        game.setScreen(ChangMemory.menuScreen)
    }

    /**
     * Navigates the player to the settings configuration panel overlay.
     * Caches the current screen context inside [ChangMemory.prevScreen] to secure
     * frictionless backward navigation routing.
     */
    private fun doShowSettingsScreen() {
        log.debug { "doShowSettingsScreen()" }
        settingsScreenShow = true
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.settingsScreen)
    }

    /**
     * Navigates the player to the highscore leaderboard overlay screen.
     * Caches the current screen context inside [ChangMemory.prevScreen] to secure
     * frictionless backward navigation routing.
     */
    private fun doShowScoreScreen() {
        log.debug { "doShowScoreScreen()" }
        scoresScreenShow = true
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.scoreScreen)
    }

    /**
     * Triggers the specialized lucky strike sequence once a blind match pair validates successfully.
     * Enqueues high-priority floating point announcements via [InfoList] and triggers audio feedback cues.
     */
    private fun doLuckStrikeSet(firstCard: Card, secondCard: Card) {
        log.debug { "doLuckStrikeSet() -> Match: ${firstCard.cardName}" }

        InfoList.add(firstCard.cardName, size = InfoList.SIZE_L, color = Color.RED)
        InfoList.add("+${firstCard.time + secondCard.time} sec", size = InfoList.SIZE_L)
        InfoList.add("+${firstCard.score + secondCard.score} POINTS", size = InfoList.SIZE_L)
        InfoList.add("")

        InfoList.add("LUCKY TRY!", flash = true, size = InfoList.SIZE_XL)
        InfoList.add("+100 EXTRA POINTS", size = InfoList.SIZE_L)

        firstCard.playCardSolvedSound()
        AudioManager.add(luckyTrySound)
    }

    /**
     * Processes standard successful card match sequences.
     * Computes incremental stat bonuses and enqueues corresponding floating text
     * overlays via [InfoList] before triggering victory audio cues.
     *
     * @param firstCard The first matching [Card] actor selection.
     * @param secondCard The second matching [Card] actor selection.
     */
    private fun doCardSolved(firstCard: Card, secondCard: Card) {
        log.debug { "doCardSolved() -> Match: ${firstCard.cardName}" }

        InfoList.add(firstCard.cardName, size = InfoList.SIZE_L)

        val timeIncrement = firstCard.time + secondCard.time
        if (timeIncrement >= 1) {
            InfoList.add("+$timeIncrement sec", size = InfoList.SIZE_L)
        }

        val scoreIncrement = firstCard.score + secondCard.score
        if (scoreIncrement > 0) {
            InfoList.add("+$scoreIncrement POINTS", size = InfoList.SIZE_L)
        }

        firstCard.playCardSolvedSound()
    }

}
