package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.BACK
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.StretchViewport
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.*
import net.lustenauer.games.memory2.game.windows.WindowGameOver
import net.lustenauer.games.memory2.game.windows.WindowPause
import net.lustenauer.games.memory2.game.objects.Card
import net.lustenauer.games.memory2.game.objects.FlashLabel
import net.lustenauer.games.memory2.utils.AchievementEntry
import net.lustenauer.games.memory2.utils.AchievementManager
import net.lustenauer.games.memory2.utils.AudioManager
import net.lustenauer.games.memory2.utils.Constants.Atlas
import net.lustenauer.games.memory2.utils.Constants.SkinConfig
import net.lustenauer.games.memory2.utils.Constants.Viewport
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_MENU
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESTART
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_RESUME
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SCORE
import net.lustenauer.gdx.scenes.scene2d.Command.Companion.CMD_SETTINGS
import net.lustenauer.gdx.scenes.scene2d.CommandListener


class CardScreen(game: ChangMemory) : AbstractScreen(game) {
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
    private lateinit var levelComplSound: AssetSound
    private lateinit var luckyTrySound: AssetSound

    private var gameSet: Array<Card> = Array()
    private var visibleCards: Array<Card> = Array()
    private lateinit var achList: Array<AchievementEntry>
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
    private var startTime: Long = 0 // kotlin.Long gekürzt
    private var levelCompleted = false
    private var luckyStrikeSet = false
    private var timeLeft30secound = false
    private var timeLeft10secound = false
    private var gameOver = false
    private var gamePaused = false
    private var screenPaused = false
    private var settingsScreenShow = false
    private var scoresScreenShow = false
    private var scoreSubmit = false

    /* PUBLIC METHODES */ /* =============== */
    override fun show() {
        Gdx.app.debug(TAG, "show()")
        ChangMemory.actionResolver?.setTrackerScreenName(TAG)

        Gdx.input.setCatchKey(BACK, true)

        achList = AchievementManager.instance.getAchievements()
        AudioManager.instance.playMusic()
        init()
    }

    override fun render(deltaTime: Float) {
        super.render(deltaTime)

        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f)
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)

        if (!screenPaused) hudStage.act(deltaTime)
        update(deltaTime)

        // Level
        lblLevel.setText("LEVEL: $level")

        // Score
        lblScore.setText("SCORE: $score")

        // Time Left
        if (timeLeft30secound) {
            lblTimeLeft.isFlashing = true
        } else {
            lblTimeLeft.isFlashing = false
            lblTimeLeft.setColor(Color.WHITE)
        }
        lblTimeLeft.setText("TIME LEFT: " + timeLeft.toInt())

        // Info Label
        lblInfo?.let { info ->
            val musicPos = AudioManager.instance.playingMusic?.position ?: 0.0f

            info.setText(
                "Music: $musicPos\n" +
                    "Count: ${ChangMemory.musicOnCompletionCounter}\n" +
                    "FPS:   ${Gdx.graphics.framesPerSecond}"
            )
        }

        // draw stage
        stage.draw()
        hudStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        Gdx.app.debug(TAG, "CardScreen resize to $width, $height")

        stage.viewport.update(width, height, true)
        hudStage.viewport.update(width, height, true)
    }

    override fun pause() {
        Gdx.app.debug(TAG, "CardScreen paused")
        screenPaused = true
        AudioManager.instance.pauseMusic()
    }

    override fun hide() {
        Gdx.app.debug(TAG, "CardScreen hide")
        if (!settingsScreenShow and !scoresScreenShow) {
            stage.dispose()
            hudStage.dispose()
        }
    }

    override fun resume() {
        super.resume()
        Gdx.app.debug(TAG, "CardScreen resume after Pause")
        screenPaused = false
        AudioManager.instance.playMusic()
    }

    /* GETTER AND SETTER */ /* ================= */ /* PRIVATE METHODES */ /* ================ */
    private fun buildCardSet() {
        val imgBackground = com.badlogic.gdx.scenes.scene2d.ui.Image(skinWindow, "background4")

        cardSetSolvedCount = 0
        cardSetTries = 0
        luckyStrikeInARowCount = 0
        levelCompleted = false
        level++

        InfoList.instance.add("LEVEL $level", InfoList.SIZE_XL)
        InfoList.instance.add("GET READY", true, InfoList.SIZE_L)

        gameSet = cardList.getCardGameSet(level)
        stage.clear()
        stage.addActor(imgBackground)

        for (card in this.gameSet) {
            card.loadAllSounds()

            val randomRotation = (MathUtils.random() * 6f) - 3f
            card.setRotation(randomRotation)
            stage.addActor(card)
        }

        initButtonPause()
        // initButtonNextLevel();
        initWindowPause()

        AchievementManager.instance.checkAchievementsLevel(level)
        Gdx.app.debug(TAG, "--> Start level $level")
    }

    private fun init() {
        if (!settingsScreenShow and !scoresScreenShow) {
            initSounds()
            initStage()

            initLabelLevel()
            initLabelScore()
            initLabelTimeLeft()
            initLabelInfo()
            initWindowGameOver()

            minActorCount = hudStage.getActors().size // save for level completed

            initFields()
            buildCardSet()
        }

        if (scoresScreenShow) Gdx.input.setInputProcessor(hudStage)
        else Gdx.input.setInputProcessor(stage)

        settingsScreenShow = false
        scoresScreenShow = false
    }

    private fun initButtonPause() {
        btnPause = Button(skinWindow, "pausePlay")
        stage.addActor(btnPause)
        btnPause.setPosition(470f, 765f, Align.right)
        btnPause.addListener(object : InputListener() {
            override fun touchDown(
                event: com.badlogic.gdx.scenes.scene2d.InputEvent?,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                doGamePause()
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    // private void initButtonNextLevel() {
    // Button btn = new Button(skinWindow, "blue");
    // btn.add("Next Level");
    // stage.addActor(btn);
    // btn.setPosition(470, 730, Align.right);
    // btn.addListener(new InputListener() {
    // @Override
    // public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    // levelCompleted = true;
    // doLevelCompleted();
    // return super.touchDown(event, x, y, pointer, button);
    // }
    // });
    // }
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
        timeLeft10secound = false
        timeLeft30secound = false
        gameOver = false
        levelCompleted = false
        gamePaused = false

        visibleCards = Array<Card>()
    }

    /**
     * Initialize the complete GameOverWindow with all his elements
     */
    private fun initWindowGameOver() {
        Gdx.app.debug(TAG, "initGameOverWindow")

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


    private fun initWindowPause() {
        Gdx.app.debug(TAG, "initWindowPause") // .Companion.TAG gekürzt

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
        lblInfo = Label(
            "Info:",
            skinWindow,
            "font12",
            Color.WHITE
        )
        lblInfo.setPosition(5f, 30f, Align.left)
        hudStage.addActor(lblInfo)
    }

    private fun initLabelLevel() {
        val labelStyle: Label.LabelStyle = Label.LabelStyle(Assets.instance.fonts.font24, Color.WHITE)
        lblLevel = Label("LEVEL: 000", labelStyle)
        lblLevel.setPosition(440f, 765f, Align.right)
        hudStage.addActor(lblLevel)
    }

    private fun initLabelScore() {
        val labelStyle: Label.LabelStyle = Label.LabelStyle(Assets.instance.fonts.font24, Color.WHITE)
        lblScore = Label("SCORE: 0000000000", labelStyle)
        lblScore.setPosition(20f, 765f, Align.left)
        hudStage.addActor(lblScore)
    }

    private fun initLabelTimeLeft() {
        val labelStyle: Label.LabelStyle = Label.LabelStyle(Assets.instance.fonts.font24, Color.WHITE)
        lblTimeLeft = FlashLabel("TIME LEFT: 00:00:00", labelStyle)
        lblTimeLeft.setPosition(20F, 735F, Align.left)

        hudStage.addActor(lblTimeLeft)
    }

    private fun initSounds() {
        beepSound = Assets.instance.beepSound
        gameOverSound = Assets.instance.gameOverSound
        luckyTrySound = Assets.instance.luckyTrySound
        levelComplSound = Assets.instance.levelComplSound
        toManyTrySound = Assets.instance.toManyTrySound

        beepSound.loadSound()
        gameOverSound.loadSound()
        luckyTrySound.loadSound()
        levelComplSound.loadSound()
        toManyTrySound.loadSound()
    }

    private fun initStage() {
        skinWindow = Skin(
            Gdx.files.internal(SkinConfig.WINDOW),
            TextureAtlas(Atlas.WINDOWS)
        )

        stage = Stage(
            StretchViewport(
                Viewport.CARD_WIDTH,
                Viewport.CARD_HEIGHT
            )
        )
        stack = Stack()
        stage.addActor(stack)
        stack.setSize(Viewport.GUI_WIDTH, Viewport.GUI_HEIGHT)

        hudStage = Stage(
            StretchViewport(
                Viewport.CARD_WIDTH,
                Viewport.CARD_HEIGHT
            )
        )
    }

    private fun update(deltaTime: Float) {
        if (!screenPaused and !gamePaused) {
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
            if (card.startCardFlip and (visibleCards.size < 2)) {
                card.flipCard()
                visibleCards.add(card)
                cardFlipCount++
            }
        }
    }

    private fun updateInputs() {
        // return to menu Screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) or Gdx.input.isKeyJustPressed(BACK)) {
            if (gameOver or gamePaused) doShowMenuScreen()
            else doGamePause()
        }
    }

    private fun updateLevelCompleted() {
        // wait for all infoList elements are disposed before we go to the next level
        if (levelCompleted and (hudStage.actors.size == minActorCount)) {
            buildCardSet()
        }
    }

    private fun updateTime(deltaTime: Float) {
        if (gameOver) return

        // total game time
        totalTime += deltaTime

        // time left
        if ((timeLeft > 0) and !levelCompleted) timeLeft -= deltaTime

        if (timeLeft >= 31) timeLeft30secound = false
        if ((timeLeft.toInt() <= 30) and !timeLeft30secound) {
            timeLeft30secound = true
            AudioManager.instance.play(beepSound)
            InfoList.instance.add("WARNING", true, InfoList.SIZE_XXXL)
            InfoList.instance.add("30 SEC LEFT", InfoList.SIZE_XL, Color.RED)
        }

        if (timeLeft.toInt() <= 10) timeLeft10secound = true
        if (timeLeft.toInt() >= 11) timeLeft10secound = false

        if (timeLeft10secound and (com.badlogic.gdx.utils.TimeUtils.timeSinceNanos(startTime) > 1000000000)) {
            AudioManager.instance.play(beepSound)
            startTime = com.badlogic.gdx.utils.TimeUtils.nanoTime()
        }

        if ((timeLeft <= 0) and (visibleCards!!.size <= 1) and !levelCompleted) {
            doGameOver()
        }
    }

    /**
     * This method checks the Visible cards are a pair or not
     */
    private fun updateVisibleCards() {
        if (visibleCards.size >= 2) {
            cardA = visibleCards[0] // Nutzen der eckigen Klammern [0] statt .get(0)
            cardB = visibleCards[1]

            val cardAReady = cardA?.cardIsOnFront == true && cardA?.cardIsFlipping == false
            val cardBReady = cardB?.cardIsOnFront == true && cardB?.cardIsFlipping == false

            if (cardAReady && cardBReady) {
                if (cardA == cardB) {
                    cardA?.cardSolved = true
                    cardB?.cardSolved = true

                    cardSetSolvedCount++
                    cardSetTries++

                    score += cardA!!.score + cardB!!.score
                    val timeAdd: Int = cardA!!.time + cardB!!.time
                    timeLeft += timeAdd.toFloat()

                    AchievementManager.instance.checkScoreAchievements(score)

                    Gdx.app.debug(TAG, "${cardA?.cardName}: Card set found add $timeAdd seconds to timeLeft")

                    levelCompleted = cardSetSolvedCount >= (gameSet.size / 2)

                    luckyStrikeSet = (cardA!!.viewed == 0 && cardB!!.viewed == 0)

                    if (luckyStrikeSet) doLuckStrikeSet()
                    else doCardSolved()

                    if (levelCompleted) doLevelCompleted()
                } else {
                    if (cardA!!.isScoreZero || cardB!!.isScoreZero) doToMayTry()

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
        Gdx.app.debug(TAG, "doGameOver()")
        if (gameOver) return

        gameOver = true
        InfoList.instance.add("GAME OVER", true, InfoList.SIZE_XXXL)
        AudioManager.instance.add(gameOverSound)

        windowGameOver.setVisible(true)
        windowGameOver.setPosition(40F, -200F)
        windowGameOver.addAction(moveTo(40f, 320f, 3f))
        windowGameOver.setScore(score)
        windowGameOver.setTime(totalTime)
        windowGameOver.setCardFlipCount(cardFlipCount)
        windowGameOver.setCardSolvedCount(cardSetSolvedCount)
        windowGameOver.setLuckStrikeCount(luckyStrikeCount)

        Gdx.input.setInputProcessor(hudStage)

        AchievementManager.instance.checkGameDoneAchievement(score)
        if (!scoreSubmit) doSubmitScore()
    }

    private fun doGamePause() {
        Gdx.app.debug(TAG, "doGamePause()")
        if (!gamePaused) {
            gamePaused = true
            windowPause.setVisible(true)
        } else {
            doGameResume()
        }
    }

    private fun doGameRestart() {
        Gdx.app.debug(TAG, "doGameRestart()")
        if (!scoreSubmit) doSubmitScore()
        windowGameOver.setVisible(false)
        windowPause.setVisible(false)
        Gdx.input.setInputProcessor(stage)

        AudioManager.instance.playMusic()

        initFields()
        buildCardSet()
    }

    protected fun doGameResume() {
        Gdx.app.debug(TAG, "doGameResume()")
        windowPause.setVisible(false)
        btnPause.setChecked(false)
        gamePaused = false

        AudioManager.instance.playMusic()

        // Delete all in pause clicked buttons
        // Maybe not a good practice but easy :-)
        for (card in gameSet!!) {
            card.startCardFlip = false
        }
    }

    private fun doLevelCompleted() {
        Gdx.app.debug(TAG, "doLevelCompleted() --> Level: $level")
        InfoList.instance.add("LEVEL COMPLETED!", InfoList.SIZE_L)
        AudioManager.instance.add(levelComplSound)
    }

    fun doShowMenuScreen() {
        Gdx.app.debug(TAG, "doShowMenuScreen()")
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
        Gdx.app.debug(TAG, "doShowSettingsScreen()")
        settingsScreenShow = true
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.settingsScreen)
    }

    private fun doShowScoreScreen() {
        scoresScreenShow = true
        Gdx.app.debug(TAG, "doShowScoreScreen()")
        ChangMemory.prevScreen = this
        game.setScreen(ChangMemory.scoreScreen)
    }

    private fun doLuckStrikeSet() {
        Gdx.app.debug(TAG, "doLuckStrikeSet()")
        InfoList.instance.add(cardA!!.cardName, InfoList.SIZE_L, Color.RED)
        InfoList.instance.add("+" + (cardA!!.time + cardB!!.time) + " sec", InfoList.SIZE_L)
        InfoList.instance.add("+" + (cardA!!.score + cardB!!.score) + " POINTS", InfoList.SIZE_L)
        InfoList.instance.add("")
        InfoList.instance.add("LUCKY TRY!", true, InfoList.SIZE_XL)
        InfoList.instance.add("+100 EXTRAPOINTS", InfoList.SIZE_L)

        cardA!!.playCardSolvedSound()
        AudioManager.instance.add(luckyTrySound)

        score += 100
        luckyStrikeInARowCount++
        luckyStrikeCount++

        AchievementManager.instance.checkLuckStrikeAchievements(luckyStrikeInARowCount)
    }

    private fun doCardSolved() {
        Gdx.app.debug(TAG, "doCardSolved()")

        val a = cardA
        val b = cardB

        if (a == null || b == null) return

        InfoList.instance.add(a.cardName, InfoList.SIZE_L)

        val ti: Int = a.time + b.time
        if (ti >= 1) InfoList.instance.add("+$ti sec", InfoList.SIZE_L)

        val sc: Int = a.score + b.score
        if (sc > 0) InfoList.instance.add("+$sc POINTS", InfoList.SIZE_L)

        a.playCardSolvedSound()
        luckyStrikeInARowCount = 0
    }


    private fun doToMayTry() {
        InfoList.instance.add("TO MANY TRY", InfoList.SIZE_L)
        InfoList.instance.add("-5 SEC", InfoList.SIZE_L)
        AudioManager.instance.add(toManyTrySound)
        timeLeft -= 5f
    }

    companion object {
        private val TAG: String = CardScreen::class.java.getName()
    }
}
