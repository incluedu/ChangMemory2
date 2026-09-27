package net.lustenauer.games.memory2.screens

import com.badlogic.gdx.graphics.Color.RED
import com.badlogic.gdx.utils.TimeUtils
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

    // --- GAME METRICS ---
    var score = 0
        private set
    var level = 0
        private set
    var timeLeft = START_TIME
    var totalTime = 0f
        private set

    var cardSetTries = 0
    var cardSetSolvedCount = 0
    var totalCardSetSolvedCount = 0
        private set
    var cardFlipCount = 0
    var luckyStrikeCount = 0
    var luckyStrikeInARowCount = 0

    // --- GAME STATE FLAGS ---
    var startTime: Long = 0
    var levelCompleted = false
    var luckyStrikeSet = false
    var timeLeft30Seconds = false
    var timeLeft10Seconds = false
    var gameOver = false
    var gamePaused = false
    var scoreSubmit = false

    // --- CARD CONTAINERS ---
    var visibleCards = GdxArray<Card>()
    val cardList = CardList()
    var gameSet = GdxArray<Card>()

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
        startTime = TimeUtils.nanoTime()
        timeLeft = START_TIME

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

        InfoList.add("LEVEL $level", size = InfoList.SIZE_XL)
        InfoList.add("GET READY", flash = true, size = InfoList.SIZE_L)

        gameSet = cardList.getCardGameSet(level)
        return gameSet
    }

    /**
     * Executes chronological time subtractions on remaining match countdowns.
     * Triggers callbacks if limits break critical hardware milestones.
     */
    fun updateTime(delta: Float, onGameOverTrigger: () -> Unit, playBeepSound: () -> Unit) {
        if (gameOver) return

        totalTime += delta

        if (timeLeft > 0 && !levelCompleted) {
            timeLeft -= delta
        }

        if (timeLeft >= THRESHOLD_WARN_LOW + 1) timeLeft30Seconds = false
        if (timeLeft.toInt() <= THRESHOLD_WARN_LOW && !timeLeft30Seconds) {
            timeLeft30Seconds = true
            playBeepSound()

            InfoList.add("WARNING", flash = true, size = InfoList.SIZE_XXXL)
            InfoList.add("30 SEC LEFT", size = InfoList.SIZE_XL, color = RED)
        }

        if (timeLeft.toInt() <= THRESHOLD_WARN_HIGH) timeLeft10Seconds = true
        if (timeLeft.toInt() >= THRESHOLD_WARN_HIGH + 1) timeLeft10Seconds = false

        if (timeLeft10Seconds && (TimeUtils.nanoTime() - startTime) > ONE_SECOND_NANOS) {
            playBeepSound()
            startTime = TimeUtils.nanoTime()
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
            val firstCard = visibleCards.first()
            val secondCard = visibleCards.last()

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
                    timeLeft += (firstCard.time + secondCard.time)
                    AchievementManager.checkScoreAchievements(score)

                    log.debug { "${firstCard.cardName}: Card set found, added ${firstCard.time + secondCard.time} seconds to timeLeft." }

                    levelCompleted = cardSetSolvedCount >= (gameSet.size / 2)
                    luckyStrikeSet = (firstCard.viewed == 0 && secondCard.viewed == 0)

                    if (luckyStrikeSet) {
                        score += BONUS_LUCKY_STRIKE
                        luckyStrikeInARowCount++
                        luckyStrikeCount++
                        AchievementManager.checkLuckyStrikeAchievements(luckyStrikeInARowCount)
                        onLuckyStrike(firstCard, secondCard)
                    } else {
                        onSolved(firstCard, secondCard)
                    }

                    if (levelCompleted) onLevelComplete()
                } else {
                    if (firstCard.isScoreZero || secondCard.isScoreZero) {
                        InfoList.add("TOO MANY TRIES", size = InfoList.SIZE_L)
                        InfoList.add("-$PENALTY_SECONDS SEC", size = InfoList.SIZE_L)
                        onPlayToManySound()
                        timeLeft -= PENALTY_SECONDS
                    }

                    visibleCards.forEach { card -> card.flipCard() }
                    cardSetTries++
                }
                visibleCards.clear()
            }
        }
    }

    /**
     * Local score calculation anchor that commits metrics back into the leaderboards.
     */
    fun submitScore() {
        ChangMemory.instance.actionResolver?.submitScore(
            score, level, cardFlipCount, cardSetSolvedCount, luckyStrikeCount
        )
        ScoreList.addScore(score, level, totalTime)
        scoreSubmit = true
    }

    companion object {
        // --- BALANCING CONSTANTS ---
        private const val START_TIME = 60f
        private const val PENALTY_SECONDS = 5f
        private const val BONUS_LUCKY_STRIKE = 100

        private const val THRESHOLD_WARN_LOW = 30
        private const val THRESHOLD_WARN_HIGH = 10
        private const val ONE_SECOND_NANOS = 1_000_000_000L
    }
}
