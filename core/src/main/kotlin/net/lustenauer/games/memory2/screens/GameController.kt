package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.graphics.Color
import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.CardList
import net.lustenauer.games.memory2.game.InfoList
import net.lustenauer.games.memory2.game.ScoreList
import net.lustenauer.games.memory2.game.objects.Card
import net.lustenauer.games.memory2.utils.AchievementManager
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * Manages the analytical gameplay state, score evaluations, and rules for CardScreen.
 * Handles timers, lucky strike tracking, score submissions, and level milestones
 * entirely separate from the rendering layer.
 *
 * @author Patric Hollenstein
 */
class GameController {

    private val log = logger<GameController>()

    // Core State Variables
    var score = 0
        private set
    var level = 0
        private set
    var timeLeft = 60f
    var totalTime = 0f
        private set

    var cardSetTries = 0
    var cardSetSolvedCount = 0
    var totalCardSetSolvedCount = 0
        private set
    var cardFlipCount = 0
    var luckyStrikeCount = 0
    var luckyStrikeInARowCount = 0

    var startTime: Long = 0
    var levelCompleted = false
    var luckyStrikeSet = false
    var timeLeft30Seconds = false
    var timeLeft10Seconds = false
    var gameOver = false
    var gamePaused = false
    var scoreSubmit = false

    var visibleCards = GdxArray<Card>()
    val cardList = CardList()
    var gameSet = GdxArray<Card>()

    private var cardA: Card? = null
    private var cardB: Card? = null

    /**
     * Resets all numerical state monitors back to default milestone scales
     * before a fresh gameplay campaign initiates.
     */
    fun resetFields() {
        score = 0
        cardFlipCount = 0
        luckyStrikeCount = 0
        level = 0
        totalTime = 0f
        cardSetSolvedCount = 0
        totalCardSetSolvedCount = 0
        startTime = com.badlogic.gdx.utils.TimeUtils.nanoTime()
        timeLeft = 60f

        scoreSubmit = false
        timeLeft10Seconds = false
        timeLeft30Seconds = false
        gameOver = false
        levelCompleted = false
        gamePaused = false

        visibleCards.clear()
        gameSet.clear()
    }

    /**
     * Advances the active level index and triggers standard preparation
     * announcements to display inside the info pooling overlays.
     */
    fun startNextLevel(): GdxArray<Card> {
        cardSetSolvedCount = 0
        cardSetTries = 0
        luckyStrikeInARowCount = 0
        levelCompleted = false
        level++

        InfoList.instance.add("LEVEL $level", size = InfoList.SIZE_XL)
        InfoList.instance.add("GET READY", flash = true, size = InfoList.SIZE_L)

        gameSet = cardList.getCardGameSet(level)
        return gameSet
    }

    /**
     * Executes chronological time subtractions on remaining match streams.
     * Triggers warnings if limits break critical milestones.
     */
    fun updateTime(deltaTime: Float, onGameOverTrigger: () -> Unit, playBeepSound: () -> Unit) {
        if (gameOver) return

        totalTime += deltaTime

        if (timeLeft > 0 && !levelCompleted) timeLeft -= deltaTime

        if (timeLeft >= 31) timeLeft30Seconds = false
        if (timeLeft.toInt() <= 30 && !timeLeft30Seconds) {
            timeLeft30Seconds = true
            playBeepSound()

            InfoList.instance.add("WARNING", flash = true, size = InfoList.SIZE_XXXL)
            InfoList.instance.add("30 SEC LEFT", size = InfoList.SIZE_XL, color = Color.RED)
        }

        if (timeLeft.toInt() <= 10) timeLeft10Seconds = true
        if (timeLeft.toInt() >= 11) timeLeft10Seconds = false

        if (timeLeft10Seconds && com.badlogic.gdx.utils.TimeUtils.timeSinceNanos(startTime) > 1000000000) {
            playBeepSound()
            startTime = com.badlogic.gdx.utils.TimeUtils.nanoTime()
        }

        if (timeLeft <= 0 && visibleCards.size <= 1 && !levelCompleted) {
            onGameOverTrigger()
        }
    }

    /**
     * Evaluates current active card selections to check for valid match pairs.
     * Computes point increments and expands timers upon successful matches.
     */
    fun processVisibleCards(
        onPlayToManySound: () -> Unit,
        onLuckyStrike: (Card, Card) -> Unit,
        onSolved: (Card, Card) -> Unit,
        onLevelComplete: () -> Unit
    ) {
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
                    totalCardSetSolvedCount++
                    cardSetTries++

                    score += firstCard.score + secondCard.score
                    val timeAdd = firstCard.time + secondCard.time
                    timeLeft += timeAdd.toFloat()

                    AchievementManager.instance.checkScoreAchievements(score)

                    log.debug { "${firstCard.cardName}: Card set found add $timeAdd seconds to timeLeft" }

                    levelCompleted = cardSetSolvedCount >= (gameSet.size / 2)
                    luckyStrikeSet = (firstCard.viewed == 0 && secondCard.viewed == 0)

                    if (luckyStrikeSet) {
                        score += 100
                        luckyStrikeInARowCount++
                        luckyStrikeCount++
                        onLuckyStrike(firstCard, secondCard)
                    } else {
                        onSolved(firstCard, secondCard)
                    }

                    if (levelCompleted) onLevelComplete()
                } else {
                    if (firstCard.isScoreZero || secondCard.isScoreZero) {
                        InfoList.instance.add("TO MANY TRY", size = InfoList.SIZE_L)
                        InfoList.instance.add("-5 SEC", size = InfoList.SIZE_L)
                        onPlayToManySound()
                        timeLeft -= 5f
                    }

                    for (card in visibleCards) {
                        card.flipCard()
                        cardSetTries++
                    }
                }
                visibleCards.clear()
            }
        }
    }

    /**
     * Local score calculation anchor that commits metrics back into the leaderboards.
     */
    fun submitScore() {
        ChangMemory.actionResolver?.submitLeaderboardsGPGS(
            score, level, cardFlipCount, cardSetSolvedCount, luckyStrikeCount
        )
        ScoreList.instance.addScore(score, level, totalTime)
        scoreSubmit = true
    }
}
