package net.lustenauer.games.memory2.game


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.Disposable

class AssetFonts : Disposable {
    val font12: BitmapFont = BitmapFont(Gdx.files.internal("fonts/font12.fnt"), false)
    val font16: BitmapFont = BitmapFont(Gdx.files.internal("fonts/font16.fnt"), false)
    val font24: BitmapFont = BitmapFont(Gdx.files.internal("fonts/font24.fnt"), false)
    val font32: BitmapFont = BitmapFont(Gdx.files.internal("fonts/font32.fnt"), false)
    val font48: BitmapFont = BitmapFont(Gdx.files.internal("fonts/font48.fnt"), false)
    val font56: BitmapFont = BitmapFont(Gdx.files.internal("fonts/font56.fnt"), false)
    val font72: BitmapFont = BitmapFont(Gdx.files.internal("fonts/font72.fnt"), false)

    init {
        val filter = Texture.TextureFilter.Linear
        font12.region.texture.setFilter(filter, filter)
        font16.region.texture.setFilter(filter, filter)
        font24.region.texture.setFilter(filter, filter)
        font32.region.texture.setFilter(filter, filter)
        font48.region.texture.setFilter(filter, filter)
        font56.region.texture.setFilter(filter, filter)
        font72.region.texture.setFilter(filter, filter)
    }

    override fun dispose() {
        font12.dispose()
        font16.dispose()
        font24.dispose()
        font32.dispose()
        font48.dispose()
        font56.dispose()
        font72.dispose()
    }
}
