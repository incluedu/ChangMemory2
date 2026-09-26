package net.lustenauer.games.memory2.utils

/**
 * Defines all unlockable achievements in ChangMemory II.
 * This enum decouples the core gameplay logic from platform-specific
 * store IDs (Google Play Games, Steam, Apple Game Center).
 */
enum class GameAchievement {
    LEVEL_5, LEVEL_10, LEVEL_15, LEVEL_20, LEVEL_25,
    LEVEL_30, LEVEL_35, LEVEL_40, LEVEL_45, LEVEL_50,

    IN_A_ROW_1, IN_A_ROW_2, IN_A_ROW_3, IN_A_ROW_4, IN_A_ROW_5,

    POINTS_5000, POINTS_10000, POINTS_15000, POINTS_20000,
    POINTS_25000, POINTS_30000, POINTS_35000, POINTS_40000, POINTS_50000,

    WORM, MOUSE, HEDGEHOG, PARROT
}
