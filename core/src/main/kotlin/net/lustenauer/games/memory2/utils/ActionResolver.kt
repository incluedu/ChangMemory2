package net.lustenauer.games.memory2.utils

interface ActionResolver {
    /* PRIVATE */
    /**
     * Open the Game in the PlayStore (Android)
     */
    fun rateGame()

    fun loadUserName(): String?

    val appVersion: String?

    /* GOOGLE API */ /* ========== */
    fun signInGPGS()

    fun signOutGPGS()

    fun submitLeaderboardsGPGS(
        score: Int,
        level: Int,
        cardsFlippedCount: Int,
        cardsSolvedCount: Int,
        luckyStrikeCount: Int
    )

    fun showLeaderboardsGPGS()

    fun unlockAchievementGPGS(id: String?)

    fun incrementAchievementGPGS(id: String?, step: Int)

    fun showAchievementsGPGS()

    val isSignedInGPGS: Boolean

    fun loadAchievements()

    /* Google Analytics */ /* ================ */
    /**
     * Set the TrackerScreenName for Google Analytics
     *
     * @param path
     */
    fun setTrackerScreenName(path: String?)

    /* ADMOB */ /* ===== */
    /**
     * Show or hide the AdMob view
     *
     * @param show
     */
    fun showAds(show: Boolean)
}
