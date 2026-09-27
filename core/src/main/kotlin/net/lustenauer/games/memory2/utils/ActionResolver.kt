package net.lustenauer.games.memory2.utils

import net.lustenauer.games.memory2.enums.GameAchievement

/**
 * Platform abstraction interface layer orchestrating cross-platform hardware features
 * like mobile advertisements, leaderboards, analytical trackers, and achievement subsystems.
 *
 * Each platform module (android, lwjgl3, ios) provides its own discrete implementation.
 *
 * @author Patric Hollenstein
 */
interface ActionResolver {

    /** Opens the application's native store page for user rating prompts. */
    fun rateGame()

    /** Fetches the active player profile synchronization name. */
    fun loadUserName(): String?

    /** The compiled system application build version string marker. */
    val appVersion: String?

    // --- LEADERBOARDS & CLOUD SUBSYSTEMS ---

    /** Triggers the platform-specific cloud gaming profile sign-in overlay. */
    fun signIn()

    /** Logs the current user profile out of active cloud subsystem sessions. */
    fun signOut()

    /** Returns true if a valid cloud service session is authenticated. */
    val isSignedIn: Boolean

    /** Commits current session benchmarks back onto global leaderboards. */
    fun submitScore(
        score: Int,
        level: Int,
        cardsFlippedCount: Int,
        cardsSolvedCount: Int,
        luckyStrikeCount: Int
    )

    /** Displays the platform's leaderboard visual user interface overlay. */
    fun showLeaderboards()

    // --- TYPE-SAFE ACHIEVEMENTS ---

    /** Asynchronously loads unlocked achievements from the cloud platform caches. */
    fun loadAchievements()

    /** Displays the platform's achievement overview interface panel layer. */
    fun showAchievements()

    /** Unlocks a specific [net.lustenauer.games.memory2.enums.GameAchievement] safely using platform-native store triggers. */
    fun unlockAchievement(achievement: GameAchievement)

    /** Increments an ongoing milestone tracker achievement safely by a specified [step]. */
    fun incrementAchievement(achievement: GameAchievement, step: Int)

    // --- ANALYTICS & ADS ---

    /** Dispatches operational tracking context logs onto active analytics nodes. */
    fun setTrackerScreenName(path: String?)

    /** Toggles adaptive advertising banner presentation layers. */
    fun showAds(show: Boolean)
}
