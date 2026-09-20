package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import ktx.assets.dispose
import net.lustenauer.games.memory2.utils.Constants.Font.FONT12
import net.lustenauer.games.memory2.utils.Constants.Font.FONT16
import net.lustenauer.games.memory2.utils.Constants.Font.FONT24
import net.lustenauer.games.memory2.utils.Constants.Font.FONT32
import net.lustenauer.games.memory2.utils.Constants.Font.FONT48
import net.lustenauer.games.memory2.utils.Constants.Font.FONT56
import net.lustenauer.games.memory2.utils.Constants.Font.FONT72

class AssetFonts : Disposable {
    val font12 = BitmapFont(Gdx.files.internal("$FONT12.fnt"), false)
    val font16 = BitmapFont(Gdx.files.internal("$FONT16.fnt"), false)
    val font24 = BitmapFont(Gdx.files.internal("$FONT24.fnt"), false)
    val font32 = BitmapFont(Gdx.files.internal("$FONT32.fnt"), false)
    val font48 = BitmapFont(Gdx.files.internal("$FONT48.fnt"), false)
    val font56 = BitmapFont(Gdx.files.internal("$FONT56.fnt"), false)
    val font72 = BitmapFont(Gdx.files.internal("$FONT72.fnt"), false)

    private val allFonts = Array<BitmapFont>().apply {
        addAll(font12, font16, font24, font32, font48, font56, font72)
    }

    init {
        allFonts.forEach { it.region.texture.setFilter(Linear, Linear) }
    }

    override fun dispose() {
        allFonts.dispose()
    }
}
