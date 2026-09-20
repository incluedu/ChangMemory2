package net.lustenauer.games.memory2.utils


object Constants {
    // ---- VIEWPORTS & DIMENSIONS ----
    object Viewport {
        const val WIDTH = 5.0f
        const val HEIGHT = 5.0f
        const val CARD_WIDTH = 480.0f
        const val CARD_HEIGHT = 800.0f
        const val GUI_WIDTH = 480.0f
        const val GUI_HEIGHT = 800.0f
    }

    // ---- TEXTURE ATLASES ----
    object Atlas {
        const val CARDS = "images/cards.atlas"
        const val LIBGDX_UI = "images/uiskin.atlas"
        const val WINDOWS = "skinsets/window.atlas"
    }

    // ---- SKIN CONFIGURATIONS (JSON) ----
    object SkinConfig {
        const val LIBGDX_UI = "images/uiskin.json"
        const val FONTS = "fonts/changMemory.json"
        const val WINDOW = "skinsets/window.json"
    }

    // ---- BITMAP FONTS ----
    object Font {
        const val FONT12 = "fonts/font12"
        const val FONT16 = "fonts/font16"
        const val FONT24 = "fonts/font24"
        const val FONT32 = "fonts/font32"
        const val FONT48 = "fonts/font48"
        const val FONT56 = "fonts/font56"
        const val FONT72 = "fonts/font72"
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

    // ---- DRAWABLE NAMES INSIDE SKINS ----
    object Skins {
        const val BACKGROUND_3 = "background3"
        const val BACKGROUND_4 = "background4"
        const val BACKGROUND_6 = "background6"
        const val BTN_BACK = "btnBack"
        const val BTN_BLUE = "blue"
        const val BTN_BLUE_BIG = "blueBig"
        const val DEFAULT_FONT = "font16"
        const val FONT_16 = "font16"
        const val FONT_24 = "font24"
        const val FONT_32 = "font32"
        const val FONT_48 = "font48"
        const val FONT_56 = "font56"
        const val FONT_72 = "font72"
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



