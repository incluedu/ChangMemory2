package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.Disposable
import ktx.assets.dispose
import net.lustenauer.games.memory2.utils.Constants.Font.FONT12
import net.lustenauer.games.memory2.utils.Constants.Font.FONT16
import net.lustenauer.games.memory2.utils.Constants.Font.FONT24
import net.lustenauer.games.memory2.utils.Constants.Font.FONT32
import net.lustenauer.games.memory2.utils.Constants.Font.FONT48
import net.lustenauer.games.memory2.utils.Constants.Font.FONT56
import net.lustenauer.games.memory2.utils.Constants.Font.FONT72

/**
 * Handles initialization, texture filtering, and lifecycle management for all bitmap fonts
 * used throughout the application layout containers.
 * Implements [Disposable] to prevent native texture allocation memory leaks.
 */
class AssetFonts : Disposable {
    /** Extra small layout font size 12 configuration. */
    val font12 = BitmapFont(Gdx.files.internal("$FONT12.fnt"), false)

    /** Small layout font size 16 configuration. */
    val font16 = BitmapFont(Gdx.files.internal("$FONT16.fnt"), false)

    /** Standard button font size 24 configuration. */
    val font24 = BitmapFont(Gdx.files.internal("$FONT24.fnt"), false)

    /** Header banner font size 32 configuration. */
    val font32 = BitmapFont(Gdx.files.internal("$FONT32.fnt"), false)

    /** Large announcement font size 48 configuration. */
    val font48 = BitmapFont(Gdx.files.internal("$FONT48.fnt"), false)

    /** Extra large announcement font size 56 configuration. */
    val font56 = BitmapFont(Gdx.files.internal("$FONT56.fnt"), false)

    /** Massive scoreboard font size 72 configuration. */
    val font72 = BitmapFont(Gdx.files.internal("$FONT72.fnt"), false)

    init {
        // Apply smooth linear texture filtering to all fonts to avoid pixelation on scaling
        listOf(font12, font16, font24, font32, font48, font56, font72).forEach {
            it.region.texture.setFilter(Linear, Linear)
        }
    }

    /**
     * Disposes of all loaded [BitmapFont] resources to clean up native graphical video memory.
     */
    override fun dispose() {
        listOf(font12, font16, font24, font32, font48, font56, font72).dispose()
    }
}
