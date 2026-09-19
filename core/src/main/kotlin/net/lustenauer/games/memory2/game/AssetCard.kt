package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable

class AssetCard @JvmOverloads constructor(
    private val assets: Assets, atlas: TextureAtlas,
    assetName: String?,
    desc: String?,
    soundPath: String?,
    soundVolume: Float = 1.0f,
    notFlip: Boolean = false
) : Disposable {
    var card: TextureAtlas.AtlasRegion
    var doneSound: AssetSound? = null
    var desc: String?

    init {
        Gdx.app.debug(Assets.Companion.TAG, "add card -->" + assetName)

        this.desc = desc
        card = atlas.findRegion(assetName)
        card.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        if (!notFlip) card.flip(true, false)
        if (soundPath == null) doneSound = null
        else doneSound = AssetSound(assets, soundPath, soundVolume)
    }

    override fun dispose() {
        doneSound?.getSound()?.dispose()
    }
}
