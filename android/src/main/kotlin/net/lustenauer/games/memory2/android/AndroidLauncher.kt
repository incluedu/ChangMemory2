package net.lustenauer.games.memory2.android

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.utils.ActionResolver
import net.lustenauer.games.memory2.utils.GameAchievement
import net.lustenauer.games.memory2.utils.AchievementManager
import net.lustenauer.games.memory2.utils.Constants


/**
 * Launches the Android application and handles mobile hardware hooks
 * by implementing the modernised, cross-platform [ActionResolver] interface.
 * Uses ktx-log for zero-allocation performance diagnostics.
 *
 * @author Patric Hollenstein
 */
class AndroidLauncher : AndroidApplication(), ActionResolver {

    private val log = logger<AndroidLauncher>()

    private var signedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configuration = AndroidApplicationConfiguration().apply {
            useImmersiveMode = true
            useAccelerometer = false
            useCompass = false
        }

        val game = ChangMemory.getInstance()
        game.actionResolver = this

        initialize(game, configuration)
    }

    override fun rateGame() {
        log.debug { "rateGame() triggered." }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GAME_URL))
            startActivity(intent)
        } catch (e: Exception) {
            log.error(e) { "Could not open Play Store link." }
        }
    }

    override fun loadUserName(): String = "Android-Player"

    override val appVersion: String
        get() = try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val verName = packageInfo.versionName
            val verCode = if (Build.VERSION.SDK_INT >= 28) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
            "$verName (Build: $verCode)"
        } catch (_: PackageManager.NameNotFoundException) {
            Constants.APP_VERSION
        }

    // --- GOOGLE PLAY GAMES CORE SERVICES ---

    override fun signIn() {
        log.info { "signIn() requested." }
        signedIn = true
    }

    override fun signOut() {
        log.info { "signOut() requested." }
        signedIn = false
    }

    override val isSignedIn: Boolean
        get() = signedIn

    override fun submitScore(
        score: Int,
        level: Int,
        cardsFlippedCount: Int,
        cardsSolvedCount: Int,
        luckyStrikeCount: Int
    ) {
        log.debug { "Submit Score to GPGS Leaderboards -> Score: $score, Level: $level" }
    }

    override fun showLeaderboards() {
        log.debug { "showLeaderboards() requested." }
    }

    override fun loadAchievements() {
        log.debug { "loadAchievements() from cloud storage cache." }
    }

    override fun showAchievements() {
        log.debug { "showAchievements() requested." }
    }

    // --- TYPE-SAFE ACHIEVEMENTS MAPPER ---

    override fun unlockAchievement(achievement: GameAchievement) {
        val googleId = mapEnumToGoogleId(achievement)
        if (googleId != null && isSignedIn) {
            log.debug { "Unlocking GPGS Achievement: ${achievement.name} -> $googleId" }
        }
    }

    override fun incrementAchievement(achievement: GameAchievement, step: Int) {
        val googleId = mapEnumToGoogleId(achievement)
        if (googleId != null && isSignedIn) {
            log.debug { "Incrementing GPGS Achievement: ${achievement.name} by $step" }
        }
    }

    /**
     * Maps the platform-independent [GameAchievement] enum back to the specific Google Play IDs.
     */
    private fun mapEnumToGoogleId(achievement: GameAchievement): String? {
        return when (achievement) {
            GameAchievement.LEVEL_5 -> AchievementManager.ID_LEVEL5
            GameAchievement.LEVEL_10 -> AchievementManager.ID_LEVEL10
            GameAchievement.LEVEL_15 -> AchievementManager.ID_LEVEL15
            GameAchievement.LEVEL_20 -> AchievementManager.ID_LEVEL20
            GameAchievement.LEVEL_25 -> AchievementManager.ID_LEVEL25
            GameAchievement.LEVEL_30 -> AchievementManager.ID_LEVEL30
            GameAchievement.LEVEL_35 -> AchievementManager.ID_LEVEL35
            GameAchievement.LEVEL_40 -> AchievementManager.ID_LEVEL40
            GameAchievement.LEVEL_45 -> AchievementManager.ID_LEVEL45
            GameAchievement.LEVEL_50 -> AchievementManager.ID_LEVEL50

            GameAchievement.IN_A_ROW_1 -> AchievementManager.ID_IN_A_ROW_1
            GameAchievement.IN_A_ROW_2 -> AchievementManager.ID_IN_A_ROW_2
            GameAchievement.IN_A_ROW_3 -> AchievementManager.ID_IN_A_ROW_3
            GameAchievement.IN_A_ROW_4 -> AchievementManager.ID_IN_A_ROW_4
            GameAchievement.IN_A_ROW_5 -> AchievementManager.ID_IN_A_ROW_5

            GameAchievement.POINTS_5000 -> AchievementManager.ID_POINTS_5000
            GameAchievement.POINTS_10000 -> AchievementManager.ID_POINTS_10000
            GameAchievement.POINTS_15000 -> AchievementManager.ID_POINTS_15000
            GameAchievement.POINTS_20000 -> AchievementManager.ID_POINTS_20000
            GameAchievement.POINTS_25000 -> AchievementManager.ID_POINTS_25000
            GameAchievement.POINTS_30000 -> AchievementManager.ID_POINTS_30000
            GameAchievement.POINTS_35000 -> AchievementManager.ID_POINTS_35000
            GameAchievement.POINTS_40000 -> AchievementManager.ID_POINTS_40000
            GameAchievement.POINTS_50000 -> AchievementManager.ID_POINTS_50000

            GameAchievement.WORM -> AchievementManager.ID_WORM
            GameAchievement.MOUSE -> AchievementManager.ID_MOUSE
            GameAchievement.HEDGEHOG -> AchievementManager.ID_HEDGEHOG
            GameAchievement.PARROT -> AchievementManager.ID_PARROT
        }
    }

    // --- ANALYTICS & ADS ---

    override fun setTrackerScreenName(path: String?) {
        log.debug { "Analytics Android Screen Focus -> $path" }
    }

    override fun showAds(show: Boolean) {
        // No ads implementation for Android
    }

    companion object {
        private const val GAME_URL = "https://google.com"
    }
}
