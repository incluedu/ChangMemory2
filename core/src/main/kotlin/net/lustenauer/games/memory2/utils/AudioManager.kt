package net.lustenauer.games.memory2.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Array as GdxArray
import ktx.log.logger
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.AssetSound
import kotlin.random.Random

/**
 * Central audio playback subsystem managing sound pooling registers, sound cue delays,
 * automated random ambient music streams, and hardware configuration updates.
 * Actively binds profile preferences to scale master volume tracks dynamically.
 *
 * @author Patric Hollenstein
 */
object AudioManager {

    private val log = logger<AudioManager>()

    /** The active [Music] stream resource currently assigned to the background channels. */
    var playingMusic: Music? = null
        private set

    private val soundList = GdxArray<SoundListObject>()

    private var wait = false
    private var waitTime = 0f
    private var musicName: String? = null

    /**
     * Plays a wrapped [AssetSound] asset using its internal configured default volume.
     */
    fun play(assetSound: AssetSound) {
        play(assetSound.getSound(), assetSound.soundVolume)
    }

    /**
     * Triggers primitive sound playback with spatial layouts and frequency pitches.
     * Evaluates active player sound preferences filters before hardware submission.
     */
    @JvmOverloads
    fun play(sound: Sound, volume: Float = 1f, pitch: Float = 1f, pan: Float = 0f) {
        if (!GamePreferences.instance.sound) return
        sound.play(GamePreferences.instance.volSound * volume, pitch, pan)
    }

    /**
     * Enqueues an audio asset into the delayed playback loop with a standard offset threshold.
     */
    fun add(assetSound: AssetSound) {
        add(assetSound, 0.2f)
    }

    /**
     * Enqueues an audio asset into the delayed playback loop with a customized offset threshold.
     */
    fun add(assetSound: AssetSound, delay: Float) {
        soundList.add(SoundListObject(assetSound, delay))
    }

    /**
     * Updates chronological sound queue shifting and updates active sound streams.
     * Must be evaluated sequentially inside the central core update frame loop.
     *
     * @param delta The chronological time step increment value provided by the main frame loop.
     */
    fun update(delta: Float) {
        if (soundList.size > 0 && !wait) {
            soundList.first()?.let { play(it.assetSound) }
            waitTime = 0f
            wait = true
        }

        waitTime += delta
        if (waitTime >= 0.3f && wait) {
            wait = false
            soundList.removeIndex(0)
        }

        updateMusic()
    }

    /**
     * Suspends the background music playback loop safely.
     */
    fun pauseMusic() {
        log.debug { "pauseMusic()" }
        playingMusic?.pause()
    }

    /**
     * Restores background music playback loop streams seamlessly.
     */
    fun playMusic() {
        log.debug { "playMusic()" }
        playingMusic?.let { play(it) }
    }

    /**
     * Binds a fresh background [Music] stream and fires playback after matching user audio profiles.
     */
    fun play(music: Music) {
        log.debug { "play(Music)" }
        playingMusic = music

        if (GamePreferences.instance.music) {
            music.volume = GamePreferences.instance.volMusic
            music.play()
        }
    }

    /**
     * Terminates background music tracks completely and disposes of current operational frames.
     */
    fun stopMusic() {
        log.debug { "stopMusic()" }
        playingMusic?.let {
            it.stop()
            playingMusic = null
        }
    }

    /**
     * Synchronizes hardware audio decibel scale factors on-the-fly once user preference filters update.
     */
    fun onSettingsUpdated() {
        log.debug { "onSettingsUpdated()" }
        val music = playingMusic ?: return

        music.volume = GamePreferences.instance.volMusic
        if (GamePreferences.instance.music) {
            if (!music.isPlaying) music.play()
        } else {
            music.pause()
        }
    }

    /**
     * Verification check filtering whether a valid music container tracks active frames.
     *
     * @return True if a background music element is initialized.
     */
    fun hasMusic(): Boolean {
        val loaded = playingMusic != null
        log.debug { "hasMusic() --> result: $loaded" }
        return loaded
    }

    /**
     * Randomizes sound tracks and hooks up automated looping chains upon track termination milestones.
     */
    fun startRandomMusic() {
        log.debug { "startRandomMusic()" }
        loadRandomMusic()

        playingMusic?.let { music ->
            play(music)

            music.setOnCompletionListener {
                log.debug { "onCompletion(music)" }
                ChangMemory.musicOnCompletionCounter++
                startRandomMusic()
            }
        }
    }

    private fun updateMusic() {
    }

    /**
     * Selects and hardware-loads a random track package from the bundled local asset folders.
     */
    private fun loadRandomMusic() {
        val r = Random.nextInt(6)

        musicName = when (r) {
            0 -> Constants.Music.TITLE1
            1 -> Constants.Music.TITLE2
            2 -> Constants.Music.TITLE3
            3 -> Constants.Music.TITLE4
            4 -> Constants.Music.TITLE5
            5 -> Constants.Music.TITLE6
            else -> Constants.Music.TITLE1
        }

        playingMusic?.dispose()
        playingMusic = null

        log.debug { "Play Music '$musicName'" }
        playingMusic = Gdx.audio.newMusic(Gdx.files.internal(musicName))
    }

/**
     * Internal data carrier holding specific sound requests and their layout parameters.
     * Separated from the singleton context to ensure zero-allocation memory tracking.
     */
    private data class SoundListObject(
        val assetSound: AssetSound,
        val delay: Float
    )
}
