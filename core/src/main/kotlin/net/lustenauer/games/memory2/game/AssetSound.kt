package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

/**
 * This class handles everything for a soundAsset
 *
 * @author Patric Hollenstein
 */
class AssetSound @JvmOverloads constructor(
    private val assets: Assets,
    private val soundPath: String?,
    var soundVolume: Float = 1f
) {
    private var sound: Sound? = null

    /**
     * check the sound is loaded and load it if necessary
     */
    fun loadSound() {
        Gdx.app.debug(Assets.Companion.TAG, "loadSound() --> Path: $soundPath")

        if (!assets.manager.isLoaded(soundPath, Sound::class.java)) {
            assets.manager.load<Sound?>(soundPath, Sound::class.java)
        }
        assets.manager.finishLoadingAsset<Any?>(soundPath)
    }

    /**
     * Return the sound after a check it is loaded or not
     *
     * @return the Sound after a check
     */
    fun getSound(): Sound {
        // Gdx.app.debug(TAG, "getSound() --> Path: " + soundPath);

        loadSound()
        sound = assets.manager.get<Sound>(soundPath, Sound::class.java)
        return sound!!
    }
}
