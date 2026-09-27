package net.lustenauer.games.memory2.utils

/**
 * Type-safe architectural command registry routing UI button triggers
 * back into global game controller action pipelines.
 */
enum class GameCommand {
    MENU,
    RESTART,
    BACK,
    RESUME,
    SETTINGS,
    SCORE
}
