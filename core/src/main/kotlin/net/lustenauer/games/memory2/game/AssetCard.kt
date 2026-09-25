package net.lustenauer.games.memory2.game

import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable
import ktx.log.logger

/**
 * Represents a single wrapped card asset wrapper containing its texture region and optional victory sound effects.
 * Implements [Disposable] to properly release underlying native audio resources during screen changes.
 *
 * @property desc The localized description or name template identifying the match payload type.
 * @param assets The central asset management coordinator instance.
 * @param atlas The primary packed texture atlas containing game world graphics sheets.
 * @param assetName The specific texture region identifier key inside the atlas file mapping.
 * @param soundPath The internal storage handle file path for the card's specific matched audio cue.
 * @param soundVolume The default playback gain scaling factor configuration.
 * @param notFlip Configuration toggle preventing automatic mirror horizontal card region flipping.
 */
class AssetCard @JvmOverloads constructor(
    assets: Assets,
    atlas: TextureAtlas,
    assetName: String?,
    var desc: String?,
    soundPath: String?,
    soundVolume: Float = 1.0f,
    notFlip: Boolean = false
) : Disposable {

    /** The mapped image texture atlas region representation drawn by game board actors. */
    var card: TextureAtlas.AtlasRegion

    /** The specialized audio effect clip triggered once this card template matches successfully. */
    var doneSound: AssetSound? = null

    init {
        log.debug { "add card --> $assetName" }

        card = atlas.findRegion(assetName).apply {
            texture.setFilter(Linear, Linear)
            if (!notFlip) flip(true, false)
        }

        doneSound = if (soundPath == null) null else AssetSound(assets, soundPath, soundVolume)
    }

    override fun dispose() {
        doneSound?.getSound()?.dispose()
    }

    companion object {
        private val log = logger<AssetCard>()
    }

}
