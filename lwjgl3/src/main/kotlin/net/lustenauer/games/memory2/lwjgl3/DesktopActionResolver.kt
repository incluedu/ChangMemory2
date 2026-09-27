package net.lustenauer.games.memory2.lwjgl3

import ktx.log.logger
import net.lustenauer.games.memory2.utils.ActionResolver
import net.lustenauer.games.memory2.utils.Constants
import net.lustenauer.games.memory2.enums.GameAchievement

/**
 * Desktop-specific implementation of the [ActionResolver] interface layer.
 * Secures runtime stability on PC modules by safely bypassing mobile sub-systems
 * (such as Google Play Games Services or AdMob layouts) and logging events via ktx-log.
 *
 * @author Patric Hollenstein
 */
object DesktopActionResolver : ActionResolver {

    private val log = logger<DesktopActionResolver>()

    override fun rateGame() {
        log.debug { "rateGame() triggered." }
    }

    override fun loadUserName(): String = "Desktop-Player"

    override val appVersion: String = Constants.APP_VERSION

    // --- CLOUD SERVICE OVERRIDES ---

    override fun signIn() {
        log.debug { "signIn() - Skipped on desktop platforms." }
    }

    override fun signOut() {
        log.debug { "signOut() - Skipped on desktop platforms." }
    }

    override val isSignedIn: Boolean = false

    override fun submitScore(
        score: Int,
        level: Int,
        cardsFlippedCount: Int,
        cardsSolvedCount: Int,
        luckyStrikeCount: Int
    ) {
        log.debug { "Score submitted ($score) to local terminal register." }
    }

    override fun showLeaderboards() {
        log.debug { "showLeaderboards() - Not available on desktop platforms." }
    }

    override fun loadAchievements() {
        // Implemented as an instant structural bypass pass-through for the desktop ecosystem
    }

    override fun showAchievements() {
        log.debug { "showAchievements() - Not available on desktop platforms." }
    }

    // --- TYPE-SAFE ACHIEVEMENTS ---

    override fun unlockAchievement(achievement: GameAchievement) {
        log.debug { "Achievement unlocked -> ${achievement.name}" }
    }

    override fun incrementAchievement(achievement: GameAchievement, step: Int) {
        log.debug { "Incremental achievement (${achievement.name}) increased by $step." }
    }

    // --- ANALYTICS & ADS ---

    override fun setTrackerScreenName(path: String?) {
        log.debug { "Analytics Desktop Screen Focus -> $path" }
    }

    override fun showAds(show: Boolean) {
        // No-op loop hook - advertisements are banned from desktop execution pipelines
    }
}
