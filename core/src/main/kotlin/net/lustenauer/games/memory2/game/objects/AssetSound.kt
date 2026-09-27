package net.lustenauer.games.memory2.game.objects

import com.badlogic.gdx.audio.Sound
import ktx.assets.load
import ktx.log.logger
import net.lustenauer.games.memory2.game.Assets

/**
 * Manages individual sound assets within the game.
 * Handles lazy loading, volume control, and safe retrieval of libGDX Sound objects.
 *
 * @property assets The central asset manager instance used to load and retrieve resources.
 * @property soundPath The internal file path to the audio file (e.g., "sounds/click.ogg").
 * @property soundVolume The playback volume configuration for this specific sound asset (default is 1.0).
 * @author Patric Hollenstein
 */
class AssetSound @JvmOverloads constructor(
    private val assets: Assets,
    private val soundPath: String?,
    var soundVolume: Float = 1f
) {
    /**
     * The cached libGDX Sound instance, populated once the asset is requested.
     */
    private var sound: Sound? = null

    /**
     * Checks if the sound asset path is valid and loaded into memory.
     * If it is not loaded, it triggers an asynchronous load request via the AssetManager
     * and blocks the execution thread until the asset is fully loaded.
     */
    fun loadSound() {
        log.debug { "loadSound() --> Path: $soundPath" }

        if (soundPath != null) {
            if (!assets.manager.isLoaded(soundPath)) {
                assets.manager.load<Sound>(soundPath)
            }
            assets.manager.finishLoadingAsset<Any>(soundPath)
        }
    }

    /**
     * Verifies asset availability and returns the fully loaded libGDX Sound object.
     * Automatically triggers [loadSound] to ensure thread-safe and crash-free retrieval.
     *
     * @return The ready-to-play [Sound] instance from the AssetManager.
     * @throws IllegalStateException if the soundPath is null or the asset cannot be retrieved.
     */
    fun getSound(): Sound {
        loadSound()

        val loadedSound = soundPath?.let { path ->
            assets.manager.get<Sound>(path)
        } ?: throw IllegalStateException("Cannot retrieve sound because soundPath is null!")

        sound = loadedSound
        return loadedSound
    }

    companion object {
        private val log = logger<AssetSound>()
    }

}
