package net.lustenauer.games.memory2.utils

import com.badlogic.gdx.Gdx
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.Assets
import net.lustenauer.games.memory2.game.InfoList

class AchievementManager  // Constructor for singleton
protected constructor() {
    val ID_LEVEL5: String = "CgkIl6WA-7keEAIQHA"
    val ID_LEVEL10: String = "CgkIl6WA-7keEAIQAg"
    val ID_LEVEL15: String = "CgkIl6WA-7keEAIQAw"
    val ID_LEVEL20: String = "CgkIl6WA-7keEAIQBA"
    val ID_LEVEL25: String = "CgkIl6WA-7keEAIQBQ"
    val ID_LEVEL30: String = "CgkIl6WA-7keEAIQBg"
    val ID_LEVEL35: String = "CgkIl6WA-7keEAIQGA"
    val ID_LEVEL40: String = "CgkIl6WA-7keEAIQGQ"
    val ID_LEVEL45: String = "CgkIl6WA-7keEAIQGg"
    val ID_LEVEL50: String = "CgkIl6WA-7keEAIQGw"
    val ID_IN_A_ROW_1: String = "CgkIl6WA-7keEAIQCw"
    val ID_IN_A_ROW_2: String = "CgkIl6WA-7keEAIQCg"
    val ID_IN_A_ROW_3: String = "CgkIl6WA-7keEAIQDA"
    val ID_IN_A_ROW_4: String = "CgkIl6WA-7keEAIQDQ"
    val ID_IN_A_ROW_5: String = "CgkIl6WA-7keEAIQDg"
    val ID_POINTS_5000: String = "CgkIl6WA-7keEAIQDw"
    val ID_POINTS_10000: String = "CgkIl6WA-7keEAIQEA"
    val ID_POINTS_15000: String = "CgkIl6WA-7keEAIQEQ"
    val ID_POINTS_20000: String = "CgkIl6WA-7keEAIQEg"
    val ID_POINTS_25000: String = "CgkIl6WA-7keEAIQEw"
    val ID_POINTS_30000: String = "CgkIl6WA-7keEAIQFA"
    val ID_POINTS_35000: String = "CgkIl6WA-7keEAIQFQ"
    val ID_POINTS_40000: String = "CgkIl6WA-7keEAIQFg"
    val ID_POINTS_50000: String = "CgkIl6WA-7keEAIQFw"

    val ID_WORM: String = "CgkIl6WA-7keEAIQHQ"
    val ID_MOUSE: String = "CgkIl6WA-7keEAIQHg"
    val ID_HEDGEHOG: String = "CgkIl6WA-7keEAIQHw"
    val ID_PARROT: String = "CgkIl6WA-7keEAIQIA"

    // private boolean level10;
    // private boolean level15;
    // private boolean level20;
    // private boolean level25;
    // private boolean level30;
    // private boolean level35;
    // private boolean level40;
    // private boolean level45;
    // private boolean level50;
    // private boolean inARow1;
    // private boolean inARow2;
    // private boolean inARow3;
    // private boolean inARow4;
    // private boolean inARow5;
    private var points5000 = false
    private var points10000 = false
    private var points15000 = false
    private var points20000 = false
    private var points25000 = false
    private var points30000 = false
    private var points35000 = false
    private var points40000 = false
    private var points50000 = false

    val TAG: String = this.javaClass.getName()
    private var achievements: com.badlogic.gdx.utils.Array<AchievementEntry>? = null

    fun init() {
        Gdx.app.debug(TAG, "init()")

        achievements = com.badlogic.gdx.utils.Array<AchievementEntry>()
    }

    /**
     * Search for an AchievementId
     *
     * @param id String with the AchivementID
     * @return the index of the AchivementID or -1 when not found
     */
    private fun find(id: String?): AchievementEntry? {
        for (i in 0..<achievements!!.size) {
            val entry = achievements!!.get(i)
            if (entry.id == id) return entry
        }
        return null
    }

    private fun doAchievement(id: String?): Boolean {
        val entry = find(id)
        if (entry != null) {
            if (!entry.isUnlocked) {
                InfoList.add("CONGRATULATIONS", flash = true, size = InfoList.SIZE_XL)
                InfoList.add(entry.name, size = InfoList.SIZE_L)

                AudioManager.add(Assets.soundAchievement)

                entry.isUnlocked = true

                ChangMemory.actionResolver?.unlockAchievementGPGS(id)
                return true
            } else {
                return true
            }
        }
        return false
    }


    /**
     *
     * @return
     */
    fun getAchievements(): com.badlogic.gdx.utils.Array<AchievementEntry> {
        if (achievements!!.size == 0) ChangMemory.actionResolver?.loadAchievements()
        return achievements!!
    }

    fun checkAchievementsLevel(level: Int) {
        if (ChangMemory.actionResolver?.isSignedInGPGS != true) return

        when (level) {
            5 -> doAchievement(ID_LEVEL5)
            10 -> doAchievement(ID_LEVEL10)
            15 -> doAchievement(ID_LEVEL15)
            20 -> doAchievement(ID_LEVEL20)
            25 -> doAchievement(ID_LEVEL25)
            30 -> doAchievement(ID_LEVEL30)
            35 -> doAchievement(ID_LEVEL35)
            40 -> doAchievement(ID_LEVEL40)
            45 -> doAchievement(ID_LEVEL45)
            50 -> doAchievement(ID_LEVEL50)
        }
    }

    fun checkLuckStrikeAchievements(luckyStrikeInARowCount: Int) {
        if (ChangMemory.actionResolver?.isSignedInGPGS != true) return

        when (luckyStrikeInARowCount) {
            1 -> doAchievement(ID_IN_A_ROW_1)
            2 -> doAchievement(ID_IN_A_ROW_2)
            3 -> doAchievement(ID_IN_A_ROW_3)
            4 -> doAchievement(ID_IN_A_ROW_4)
            5 -> doAchievement(ID_IN_A_ROW_5)
        }
    }

    fun checkScoreAchievements(score: Int) {
        if (score >= 5000 && score < 10000 && !points5000) points5000 = doAchievement(ID_POINTS_5000)
        if (score >= 10000 && score < 15000 && !points10000) points10000 = doAchievement(ID_POINTS_10000)
        if (score >= 15000 && score < 20000 && !points15000) points15000 = doAchievement(ID_POINTS_15000)
        if (score >= 20000 && score < 25000 && !points20000) points20000 = doAchievement(ID_POINTS_20000)
        if (score >= 25000 && score < 30000 && !points25000) points25000 = doAchievement(ID_POINTS_25000)
        if (score >= 30000 && score < 35000 && !points30000) points30000 = doAchievement(ID_POINTS_30000)
        if (score >= 35000 && score < 40000 && !points35000) points35000 = doAchievement(ID_POINTS_35000)
        if (score >= 40000 && score < 50000 && !points40000) points40000 = doAchievement(ID_POINTS_40000)
        if (score >= 50000 && !points50000) points50000 = doAchievement(ID_POINTS_50000)
    }

    fun checkGameDoneAchievement(score: Int) {
        if (score < 2000) return

        ChangMemory.actionResolver?.incrementAchievementGPGS(ID_WORM, 1)
        ChangMemory.actionResolver?.incrementAchievementGPGS(ID_MOUSE, 1)
        ChangMemory.actionResolver?.incrementAchievementGPGS(ID_HEDGEHOG, 1)
        ChangMemory.actionResolver?.incrementAchievementGPGS(ID_PARROT, 1)
    }

    companion object {
        val instance: AchievementManager = AchievementManager() // Initialize class as a singleton
    }
}
