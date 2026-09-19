package net.lustenauer.games.memory2.utils


object Constants {
    // Visible game world is 5 meters wide
    const val VIEWPORT_WIDTH: Float = 5.0f
    const val VIEWPORT_HEIGHT: Float = 5.0f

    // Visible game dimensions is 5 meters
    const val VIEWPORT_CARD_WIDTH: Float = 480.0f
    const val VIEWPORT_CARD_HEIGHT: Float = 800.0f

    // GUI dimensions
    const val VIEWPORT_GUI_WIDTH: Float = 480.0f
    const val VIEWPORT_GUI_HEIGHT: Float = 800.0f

    // Location of description file for texture atlas
    const val TEXTURE_ATLAS_CARDS: String = "images/cards.atlas"
    const val TEXTURE_ATLAS_LIBGDX_UI: String = "images/uiskin.atlas"
    const val TEXTURE_ATLAS_WINDOS: String = "skinsets/window.atlas"

    // Location of description file for skins
    const val SKIN_LIBGDX_UI: String = "images/uiskin.json"
    const val SKIN_FONTS: String = "fonts/changMemory.json"
    const val SKIN_WINDOW: String = "skinsets/window.json"

    // Locations of BitmapFonts
    const val FONT_UBUNTU48: String = "fonts/ubuntu48"
    const val FONT_FONT12: String = "fonts/ubuntu12"
    const val FONT_FONT16: String = "fonts/ubuntu16"
    const val FONT_FONT24: String = "fonts/ubuntu24"
    const val FONT_FONT32: String = "fonts/ubuntu32"
    const val FONT_FONT48: String = "fonts/ubuntu48"
    const val FONT_FONT56: String = "fonts/ubuntu56"
    const val FONT_FONT72: String = "fonts/ubuntu72"

    // Game preferences file
    const val PREFERENCES: String = "changmemory.prefs"

    // music files
    const val MUSIC1: String = "music/alw.ogg"
    const val MUSIC2: String = "music/av_1200.ogg"
    const val MUSIC3: String = "music/beefeater.ogg"
    const val MUSIC4: String = "music/forgottenone.ogg"
    const val MUSIC5: String = "music/mind_traveller.ogg"
    const val MUSIC6: String = "music/the_hunt_for_lars.ogg"
}
