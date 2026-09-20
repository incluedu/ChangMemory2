package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable
import net.lustenauer.games.memory2.game.Assets.Companion.TAG

class AssetCard @JvmOverloads constructor(
    assets: Assets,
    atlas: TextureAtlas,
    assetName: String?,
    var desc: String?,
    soundPath: String?,
    soundVolume: Float = 1.0f,
    notFlip: Boolean = false
) : Disposable {

    var card: TextureAtlas.AtlasRegion
    var doneSound: AssetSound? = null

    init {
        Gdx.app.debug(TAG, "add card --> $assetName")

        card = atlas.findRegion(assetName).apply {
            texture.setFilter(Linear, Linear)
            if (!notFlip) flip(true, false)
        }

        doneSound = if (soundPath == null) null else AssetSound(assets, soundPath, soundVolume)
    }

    override fun dispose() {
        doneSound?.getSound()?.dispose()
    }
}
