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
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.actors.onClick
import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.*
import net.lustenauer.games.memory2.game.objects.Card
import net.lustenauer.games.memory2.game.objects.FlashLabel
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

    private lateinit var btnPause: Button
    private lateinit var lblTimeLeft: FlashLabel
    private lateinit var lblLevel: Label
    private lateinit var lblScore: Label
    private lateinit var lblInfo: Label
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
    private var visibleCards = GdxArray<Card>()
    private lateinit var achList: GdxArray<AchievementEntry>
    lateinit var cardList: CardList

    private var cardA: Card? = null
    private var cardB: Card? = null

    private var cardSetTries = 0
    private var cardSetSolvedCount = 0
    private var score = 0
    private var level = 0
    private var cardFlipCount = 0
    private var luckyStrikeCount = 0
    private var luckyStrikeInARowCount = 0
    private var minActorCount = 0
    private var timeLeft = 0f
    private var totalTime = 0f
    private var startTime: Long = 0
    private var levelCompleted = false
    private var luckyStrikeSet = false
    private var timeLeft30Seconds = false
    private var timeLeft10Seconds = false
    private var gameOver = false
    private var gamePaused = false
    private var screenPaused = false
    private var settingsScreenShow = false
    private var scoresScreenShow = false
    private var scoreSubmit = false

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

        lblLevel.setText("LEVEL: $level")
        lblScore.setText("SCORE: $score")

        if (timeLeft30Seconds) {
            lblTimeLeft.isFlashing = true
        } else {
            lblTimeLeft.isFlashing = false
            lblTimeLeft.setColor(Color.WHITE)
        }
        lblTimeLeft.setText("TIME LEFT: ${timeLeft.toInt()}")

        val musicPos = AudioManager.instance.playingMusic?.position ?: 0.0f
        lblInfo.setText(
            "Music: $musicPos\n" +
                "Count: ${ChangMemory.musicOnCompletionCounter}\n" +
                "FPS:   ${Gdx.graphics.framesPerSecond}"
        )

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
        val imgBackground = com.badlogic.gdx.scenes.scene2d.ui.Image(skinWindow, BACKGROUND_4)

        cardSetSolvedCount = 0
        cardSetTries = 0
        luckyStrikeInARowCount = 0
        levelCompleted = false
        level++

        InfoList.instance.add("LEVEL $level", size = InfoList.SIZE_XL)
        InfoList.instance.add("GET READY", flash = true, size = InfoList.SIZE_L)

        gameSet = cardList.getCardGameSet(level)
        stage.clear()
        stage.addActor(imgBackground)

        for (card in this.gameSet) {
            card.loadAllSounds()

            val randomRotation = (MathUtils.random() * 6f) - 3f
            card.rotation = randomRotation
            stage.addActor(card)
        }

        initButtonPause()
        initWindowPause()

        AchievementManager.instance.checkAchievementsLevel(level)

        log.debug { "--> Start level $level" }
    }

    private fun init() {
        if (!settingsScreenShow && !scoresScreenShow) {
            initSounds()
            initStage()

            initLabelLevel()
            initLabelScore()
            initLabelTimeLeft()
            initLabelInfo()
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

    private fun initFields() {
        cardList = CardList()

        score = 0
        cardFlipCount = 0
        luckyStrikeCount = 0
        level = 0
        totalTime = 0f
        cardSetSolvedCount = 0
        startTime = com.badlogic.gdx.utils.TimeUtils.nanoTime()
        timeLeft = 60f

        scoreSubmit = false
        timeLeft10Seconds = false
        timeLeft30Seconds = false
        gameOver = false
        levelCompleted = false
        gamePaused = false

        visibleCards = GdxArray()
    }

    /**
     * Initialize the complete GameOverWindow with all its elements.
     */
    private fun initWindowGameOver() {
        log.debug { "initGameOverWindow" }

        windowGameOver = WindowGameOver(object : CommandListener() {
            override fun performCommand(event: CommandEvent?): Boolean {
                when (event?.command) {
                    CMD_RESTART -> doGameRestart()
                    CMD_MENU -> doShowMenuScreen()
                    CMD_SCORE -> doShowScoreScreen()
                }
                return true
            }
        })
        hudStage.addActor(windowGameOver)
    }

    /**
     * Initialize the complete WindowPause window container layout layer.
     */
    private fun initWindowPause() {
        log.debug { "initWindowPause" }

        windowPause = WindowPause(object : CommandListener() {
            override fun performCommand(event: CommandEvent?): Boolean {
                when (event?.command) {
                    CMD_RESTART -> doGameRestart()
                    CMD_MENU -> doShowMenuScreen()
                    CMD_SETTINGS -> doShowSettingsScreen()
                    CMD_RESUME -> doGameResume()
                }
                return true
            }
        })
        stage.addActor(windowPause)
    }

    private fun initLabelInfo() {
        lblInfo = Label("Info:", skinWindow, "font12", Color.WHITE).apply {
            setPosition(5f, 30f, Align.left)
        }
        hudStage.addActor(lblInfo)
    }

    private fun initLabelLevel() {
        val labelStyle = Label.LabelStyle(Assets.instance.fonts.font24, Color.WHITE)
        lblLevel = Label("LEVEL: 000", labelStyle).apply {
            setPosition(440f, 765f, Align.right)
        }
        hudStage.addActor(lblLevel)
    }

    private fun initLabelScore() {
        val labelStyle = Label.LabelStyle(Assets.instance.fonts.font24, Color.WHITE)
        lblScore = Label("SCORE: 0000000000", labelStyle).apply {
            setPosition(20f, 765f, Align.left)
        }
        hudStage.addActor(lblScore)
    }

    private fun initLabelTimeLeft() {
        val labelStyle = Label.LabelStyle(Assets.instance.fonts.font24, Color.WHITE)
        lblTimeLeft = FlashLabel("TIME LEFT: 00:00:00", labelStyle).apply {
            setPosition(20f, 735f, Align.left)
        }
        hudStage.addActor(lblTimeLeft)
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
     * the card will flip to front and will add to the list off visibleCards
     */
    private fun updateFlipCard() {
        for (card in gameSet) {
            if (card.startCardFlip && visibleCards.size < 2) {
                card.flipCard()
                visibleCards.add(card)
                cardFlipCount++
            }
        }
    }

    private fun updateInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(BACK)) {
            if (gameOver || gamePaused) doShowMenuScreen()
            else doGamePause()
        }
    }

    private fun updateLevelCompleted() {
        // wait for all infoList elements are disposed before we go to the next level
        if (levelCompleted && hudStage.actors.size == minActorCount) {
            buildCardSet()
        }
    }

    private fun updateTime(deltaTime: Float) {
        if (gameOver) return

        totalTime += deltaTime

        if (timeLeft > 0 && !levelCompleted) timeLeft -= deltaTime

        if (timeLeft >= 31) timeLeft30Seconds = false
        if (timeLeft.toInt() <= 30 && !timeLeft30Seconds) {
            timeLeft30Seconds = true
            AudioManager.instance.play(beepSound)

            InfoList.instance.add("WARNING", flash = true, size = InfoList.SIZE_XXXL)
            InfoList.instance.add("30 SEC LEFT", size = InfoList.SIZE_XL, color = Color.RED)
        }

        if (timeLeft.toInt() <= 10) timeLeft10Seconds = true
        if (timeLeft.toInt() >= 11) timeLeft10Seconds = false

        if (timeLeft10Seconds && com.badlogic.gdx.utils.TimeUtils.timeSinceNanos(startTime) > 1000000000) {
            AudioManager.instance.play(beepSound)
            startTime = com.badlogic.gdx.utils.TimeUtils.nanoTime()
        }

        if (timeLeft <= 0 && visibleCards.size <= 1 && !levelCompleted) {
            doGameOver()
        }
    }

    /**
     * This method checks the Visible cards are a pair or not.
     */
    private fun updateVisibleCards() {
        if (visibleCards.size >= 2) {
            val firstCard = visibleCards[0]
            val secondCard = visibleCards[1]

            cardA = firstCard
            cardB = secondCard

            val cardAReady = firstCard.cardIsOnFront && !firstCard.cardIsFlipping
            val cardBReady = secondCard.cardIsOnFront && !secondCard.cardIsFlipping

            if (cardAReady && cardBReady) {
                if (firstCard == secondCard) {
                    firstCard.cardSolved = true
                    secondCard.cardSolved = true

                    cardSetSolvedCount++
                    cardSetTries++

                    score += firstCard.score + secondCard.score
                    val timeAdd = firstCard.time + secondCard.time
                    timeLeft += timeAdd.toFloat()

                    AchievementManager.instance.checkScoreAchievements(score)

                    log.debug { "${firstCard.cardName}: Card set found add $timeAdd seconds to timeLeft" }

                    levelCompleted = cardSetSolvedCount >= (gameSet.size / 2)

                    luckyStrikeSet = (firstCard.viewed == 0 && secondCard.viewed == 0)

                    if (luckyStrikeSet) doLuckStrikeSet()
                    else doCardSolved()

                    if (levelCompleted) doLevelCompleted()
                } else {
                    if (firstCard.isScoreZero || secondCard.isScoreZero) doToMayTry()

                    for (card in visibleCards) {
                        card.flipCard()
                        cardSetTries++
                    }
                }
                visibleCards.clear()
            }
        }
    }

    /* HANDLER */ /* ======= */
    private fun doGameOver() {
        log.debug { "doGameOver()" }
        if (gameOver) return

        gameOver = true
        InfoList.instance.add("GAME OVER", flash = true, size = InfoList.SIZE_XXXL)
        AudioManager.instance.add(gameOverSound)

        windowGameOver.apply {
            isVisible = true
            setPosition(40f, -200f)
            addAction(moveTo(40f, 320f, 3f))
            setScore(score)
            setTime(totalTime)
            setCardFlipCount(cardFlipCount)
            setCardSolvedCount(cardSetSolvedCount)
            setLuckStrikeCount(luckyStrikeCount)
        }

        Gdx.input.inputProcessor = hudStage

        AchievementManager.instance.checkGameDoneAchievement(score)
        if (!scoreSubmit) doSubmitScore()
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
        if (!scoreSubmit) doSubmitScore()
        windowGameOver.isVisible = false
        windowPause.isVisible = false
        Gdx.input.inputProcessor = stage

        AudioManager.instance.playMusic()

        initFields()
        buildCardSet()
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
        if (!scoreSubmit) doSubmitScore()
        game.setScreen(ChangMemory.menuScreen)
    }

    private fun doSubmitScore() {
        ChangMemory.actionResolver?.submitLeaderboardsGPGS(
            score,
            level,
            cardFlipCount,
            cardSetSolvedCount,
            luckyStrikeCount
        )
        ScoreList.instance.addScore(score, level, totalTime)

        scoreSubmit = true
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

    private fun doLuckStrikeSet() {
        log.debug { "doLuckStrikeSet()" }

        val a = cardA
        val b = cardB
        if (a == null || b == null) return

        InfoList.instance.add(a.cardName, size = InfoList.SIZE_L, color = Color.RED)

        InfoList.instance.add("+${a.time + b.time} sec", size = InfoList.SIZE_L)
        InfoList.instance.add("+${a.score + b.score} POINTS", size = InfoList.SIZE_L)
        InfoList.instance.add("")

        InfoList.instance.add("LUCKY TRY!", flash = true, size = InfoList.SIZE_XL)
        InfoList.instance.add("+100 EXTRA POINTS", size = InfoList.SIZE_L)

        a.playCardSolvedSound()
        AudioManager.instance.add(luckyTrySound)

        score += 100
        luckyStrikeInARowCount++
        luckyStrikeCount++

        AchievementManager.instance.checkLuckStrikeAchievements(luckyStrikeInARowCount)
    }

    private fun doCardSolved() {
        log.debug { "doCardSolved()" }

        val a = cardA
        val b = cardB
        if (a == null || b == null) return

        InfoList.instance.add(a.cardName, size = InfoList.SIZE_L)

        val ti = a.time + b.time
        if (ti >= 1) InfoList.instance.add("+$ti sec", size = InfoList.SIZE_L)

        val sc = a.score + b.score
        if (sc > 0) InfoList.instance.add("+$sc POINTS", size = InfoList.SIZE_L)

        a.playCardSolvedSound()
        luckyStrikeInARowCount = 0
    }

    private fun doToMayTry() {
        InfoList.instance.add("TO MANY TRY", size = InfoList.SIZE_L)
        InfoList.instance.add("-5 SEC", size = InfoList.SIZE_L)
        AudioManager.instance.add(toManyTrySound)
        timeLeft -= 5f
    }

}
