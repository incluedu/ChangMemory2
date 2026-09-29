package net.lustenauer.games.memory2.utils


object Constants {
    // ---- APP VERSION ----
    const val APP_VERSION = "2.0.0-alpha.2"

    // ---- VIEWPORTS & DIMENSIONS ----
    object Viewport {
        const val CARD_WIDTH = 480.0f
        const val CARD_HEIGHT = 800.0f
        const val GUI_WIDTH = 480.0f
        const val GUI_HEIGHT = 800.0f
    }

    // ---- TEXTURE ATLASES ----
    object Atlas {
        const val CARDS = "images/cards.atlas"
        const val WINDOWS = "skinsets/window.atlas"
    }

    // ---- SKIN CONFIGURATIONS (JSON) ----
    object SkinConfig {
        const val WINDOW = "skinsets/window.json"
    }

    // ---- MUSIC FILES ----
    object Music {
        const val TITLE1 = "music/alw.ogg"
        const val TITLE2 = "music/av_1200.ogg"
        const val TITLE3 = "music/beefeater.ogg"
        const val TITLE4 = "music/forgottenone.ogg"
        const val TITLE5 = "music/mind_traveller.ogg"
        const val TITLE6 = "music/the_hunt_for_lars.ogg"
    }

    /**
     * Central resource registry for dynamic high-definition vector
     * and specialized monospace chalkboard fonts.
     */
    object Fonts {
        /** Fallback reference mapped directly onto the standard UI scale. (Size: 16px) */
        const val FONT_DEFAULT = "FONT_DEFAULT"

        /** Ultra-compact monospace text style engineered for fine data cell lists. (Size: 12px) */
        const val FONT_XXS = "FONT_XXS"

        /** Compact monospace text style optimized for secondary data grids. (Size: 14px) */
        const val FONT_XS = "FONT_XS"

        /** Base crisp hand-drawn vector typography segment for generic UI texts. (Size: 16px) */
        const val FONT_S = "FONT_S"

        /** Enhanced vector typography slice mapped onto standard sub-headers. (Size: 24px) */
        const val FONT_M = "FONT_M"

        /** Broad vector scale customized for action buttons and navigation links. (Size: 32px) */
        const val FONT_L = "FONT_L"

        /** Massive vector typography block assigned to screen titles and primary branding. (Size: 36px) */
        const val FONT_XL = "FONT_XL"

        /** Extra massive title scale reserved for high-impact scoreboard announcements. (Size: 44px) */
        const val FONT_XXL = "FONT_XXL"

        /** The absolute maximum vector layout scale reserved for critical screen milestones. (Size: 56px) */
        const val FONT_XXXL = "FONT_XXXL"
    }

    // ---- DRAWABLE NAMES INSIDE SKINS ----
    object Skins {
        const val BACKGROUND_3 = "background3"
        const val BACKGROUND_4 = "background4"
        const val BACKGROUND_6 = "background6"
        const val BTN_BACK = "btnBack"
        const val BTN_BLUE = "blue"
        const val BTN_BLUE_BIG = "blueBig"
        const val IMG_BUTTERFLY = "butterfly"
        const val IMG_FROG = "frog"
        const val IMG_GOOGLE_ACHIEVEMENTS = "imgGoogleAchievementsWhite"
        const val IMG_GOOGLE_LEADERBOARDS = "imgGoogleLeaderboardsWhite"
        const val IMG_GOOGLE_PLAY = "imgGooglePlay"
        const val IMG_GOOGLE_SIGN_IN = "imgGoogleSignIn"
        const val IMG_GOOGLE_SIGN_OUT = "imgGoogleSignOut"
    }

    // ---- GAME PREFERENCES ----
    object Prefs {
        const val FILE_NAME = "changmemory.prefs"
    }
}
