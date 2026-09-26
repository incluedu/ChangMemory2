package net.lustenauer.games.memory2.utils

import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.InfoList
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * Central Google Play Games Services (GPGS) achievement monitoring engine.
 * Tracks level milestones, blind luck streaks, and specific point achievements
 * dynamically during a live campaign loop.
 *
 * @author Patric Hollenstein
 */
object AchievementManager {

    private val log = logger<AchievementManager>()

    private var points5000 = false
    private var points10000 = false
    private var points15000 = false
    private var points20000 = false
    private var points25000 = false
    private var points30000 = false
    private var points35000 = false
    private var points40000 = false
    private var points50000 = false

    private lateinit var achievements: GdxArray<AchievementEntry>

    /**
     * Initializes the memory pools for active tracking metrics.
     */
    fun init() {
        log.debug { "init()" }
        achievements = GdxArray<AchievementEntry>()
    }

    /**
     * Search for an AchievementId inside the active memory pool registers.
     */
    private fun find(id: String?): AchievementEntry? {
        if (id == null) return null
        for (i in 0 until achievements.size) {
            val entry = achievements[i]
            if (entry.id == id) return entry
        }
        return null
    }

    /**
     * Deploys notification rewards onto the UI and signals the platform backend.
     */
    private fun doAchievement(achievement: GameAchievement, id: String): Boolean {
        val entry = find(id) ?: return false

        if (!entry.isUnlocked) {
            InfoList.add("CONGRATULATIONS", flash = true, size = InfoList.SIZE_XL)
            InfoList.add(entry.name, size = InfoList.SIZE_L)

            AudioManager.add(Assets.soundAchievement)

            entry.isUnlocked = true
            ChangMemory.instance.actionResolver?.unlockAchievement(achievement)
            return true
        }
        return true
    }

    /**
     * Pulls the current synchronised leaderboard achievements container.
     */
    fun getAchievements(): GdxArray<AchievementEntry> {
        if (achievements.size == 0) {
            ChangMemory.instance.actionResolver?.loadAchievements()
        }
        return achievements
    }

    /**
     * Validates if the player broke a specific level milestone.
     */
    fun checkAchievementsLevel(level: Int) {
        if (ChangMemory.instance.actionResolver?.isSignedIn != true) return

        when (level) {
            5  -> doAchievement(GameAchievement.LEVEL_5, ID_LEVEL5)
            10 -> doAchievement(GameAchievement.LEVEL_10, ID_LEVEL10)
            15 -> doAchievement(GameAchievement.LEVEL_15, ID_LEVEL15)
            20 -> doAchievement(GameAchievement.LEVEL_20, ID_LEVEL20)
            25 -> doAchievement(GameAchievement.LEVEL_25, ID_LEVEL25)
            30 -> doAchievement(GameAchievement.LEVEL_30, ID_LEVEL30)
            35 -> doAchievement(GameAchievement.LEVEL_35, ID_LEVEL35)
            40 -> doAchievement(GameAchievement.LEVEL_40, ID_LEVEL40)
            45 -> doAchievement(GameAchievement.LEVEL_45, ID_LEVEL45)
            50 -> doAchievement(GameAchievement.LEVEL_50, ID_LEVEL50)
        }
    }

    /**
     * Validates if the player achieved multiple blind pairs back-to-the-row.
     */
    fun checkLuckyStrikeAchievements(luckyStrikeInARowCount: Int) {
        if (ChangMemory.instance.actionResolver?.isSignedIn != true) return

        when (luckyStrikeInARowCount) {
            1 -> doAchievement(GameAchievement.IN_A_ROW_1, ID_IN_A_ROW_1)
            2 -> doAchievement(GameAchievement.IN_A_ROW_2, ID_IN_A_ROW_2)
            3 -> doAchievement(GameAchievement.IN_A_ROW_3, ID_IN_A_ROW_3)
            4 -> doAchievement(GameAchievement.IN_A_ROW_4, ID_IN_A_ROW_4)
            5 -> doAchievement(GameAchievement.IN_A_ROW_5, ID_IN_A_ROW_5)
        }
    }

    /**
     * Increments specific tier markers once global scores break milestones.
     */
    fun checkScoreAchievements(score: Int) {
        if (score in 5000..9999 && !points5000) points5000 = doAchievement(GameAchievement.POINTS_5000, ID_POINTS_5000)
        if (score in 10000..14999 && !points10000) points10000 = doAchievement(GameAchievement.POINTS_10000, ID_POINTS_10000)
        if (score in 15000..19999 && !points15000) points15000 = doAchievement(GameAchievement.POINTS_15000, ID_POINTS_15000)
        if (score in 20000..24999 && !points20000) points20000 = doAchievement(GameAchievement.POINTS_20000, ID_POINTS_20000)
        if (score in 25000..29999 && !points25000) points25000 = doAchievement(GameAchievement.POINTS_25000, ID_POINTS_25000)
        if (score in 30000..34999 && !points30000) points30000 = doAchievement(GameAchievement.POINTS_30000, ID_POINTS_30000)
        if (score in 35000..39999 && !points35000) points35000 = doAchievement(GameAchievement.POINTS_35000, ID_POINTS_35000)
        if (score in 40000..49999 && !points40000) points40000 = doAchievement(GameAchievement.POINTS_40000, ID_POINTS_40000)
        if (score >= 50000 && !points50000) points50000 = doAchievement(GameAchievement.POINTS_50000, ID_POINTS_50000)
    }

    /**
     * Increments lifetime completion targets upon matches scoring above threshold limits.
     */
    fun checkGameDoneAchievement(score: Int) {
        if (score < 2000) return

        // BEHOBEN: Nutzt jetzt die fehlerfreie, plattformunabhängige Methode des Interfaces
        ChangMemory.instance.actionResolver?.apply {
            incrementAchievement(GameAchievement.WORM, 1)
            incrementAchievement(GameAchievement.MOUSE, 1)
            incrementAchievement(GameAchievement.HEDGEHOG, 1)
            incrementAchievement(GameAchievement.PARROT, 1)
        }
    }

    // ==========================================
    // CONSTANTS & GOOGLE PLAY IDs
    // ==========================================
    const val ID_LEVEL5 = "CgkIl6WA-7keEAIQHA"
    const val ID_LEVEL10 = "CgkIl6WA-7keEAIQAg"
    const val ID_LEVEL15 = "CgkIl6WA-7keEAIQAw"
    const val ID_LEVEL20 = "CgkIl6WA-7keEAIQBA"
    const val ID_LEVEL25 = "CgkIl6WA-7keEAIQBQ"
    const val ID_LEVEL30 = "CgkIl6WA-7keEAIQBg"
    const val ID_LEVEL35 = "CgkIl6WA-7keEAIQGA"
    const val ID_LEVEL40 = "CgkIl6WA-7keEAIQGQ"
    const val ID_LEVEL45 = "CgkIl6WA-7keEAIQGg"
    const val ID_LEVEL50 = "CgkIl6WA-7keEAIQGw"

    const val ID_IN_A_ROW_1 = "CgkIl6WA-7keEAIQCw"
    const val ID_IN_A_ROW_2 = "CgkIl6WA-7keEAIQCg"
    const val ID_IN_A_ROW_3 = "CgkIl6WA-7keEAIQDA"
    const val ID_IN_A_ROW_4 = "CgkIl6WA-7keEAIQDQ"
    const val ID_IN_A_ROW_5 = "CgkIl6WA-7keEAIQDg"

    const val ID_POINTS_5000 = "CgkIl6WA-7keEAIQDw"
    const val ID_POINTS_10000 = "CgkIl6WA-7keEAIQEA"
    const val ID_POINTS_15000 = "CgkIl6WA-7keEAIQEQ"
    const val ID_POINTS_20000 = "CgkIl6WA-7keEAIQEg"
    const val ID_POINTS_25000 = "CgkIl6WA-7keEAIQEw"
    const val ID_POINTS_30000 = "CgkIl6WA-7keEAIQFA"
    const val ID_POINTS_35000 = "CgkIl6WA-7keEAIQFQ"
    const val ID_POINTS_40000 = "CgkIl6WA-7keEAIQFg"
    const val ID_POINTS_50000 = "CgkIl6WA-7keEAIQFw"

    const val ID_WORM = "CgkIl6WA-7keEAIQHQ"
    const val ID_MOUSE = "CgkIl6WA-7keEAIQHg"
    const val ID_HEDGEHOG = "CgkIl6WA-7keEAIQHw"
    const val ID_PARROT = "CgkIl6WA-7keEAIQIA"
}
