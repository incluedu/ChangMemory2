package net.lustenauer.games.memory2.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Array
import net.lustenauer.games.memory2.ChangMemory
import net.lustenauer.games.memory2.game.AssetSound
import java.util.*

class AudioManager private constructor() {
    private val TAG: String = this.javaClass.getName()

    // Gdx.app.debug(TAG, "getPlayingMusic()");
    var playingMusic: Music? = null
        private set
    private val soundList: Array<SoundListObject?>

    private var wait = false
    private var waitTime = 0f

    private var musicName: String? = null

    // singleton: prevent instantiation from other classes
    init {
        Gdx.app.debug(TAG, "AudioManager() <-- CONSTRUCTOR")
        soundList = Array<SoundListObject?>()
    }

    /* SOUND */ /* ===== */
    fun play(assetSound: AssetSound) {
        play(assetSound.getSound(), assetSound.soundVolume)
    }

    @JvmOverloads
    fun play(sound: Sound, volume: Float = 1f, pitch: Float = 1f, pan: Float = 0f) {
        // Gdx.app.debug(TAG, "play(Sound, " + volume + "volume, " + pitch + "pitch, " + pan + "pan)");
        if (!GamePreferences.Companion.instance.sound) return
        sound.play(GamePreferences.Companion.instance.volSound * volume, pitch, pan)
    }

    fun add(assetSound: AssetSound) {
        add(assetSound, 0.2f)
    }

    fun add(assetSound: AssetSound, delay: Float) {
        soundList.add(SoundListObject(assetSound, delay))
    }

    fun update(deltaTime: Float) {
        if ((soundList.size > 0) and !wait) {
            play(soundList.get(0)!!.assetSound)
            waitTime = 0f
            wait = true
        }

        waitTime += deltaTime
        if ((waitTime >= 0.3f) and wait) {
            wait = false
            soundList.removeIndex(0)
        }

        updateMusic()
    }

    /* MUSIC */ /* ===== */
    fun pauseMusic() {
        Gdx.app.debug(TAG, "pauseMusic()")
        if (this.playingMusic != null) {
            playingMusic!!.pause()
        }
    }

    fun playMusic() {
        Gdx.app.debug(TAG, "playMusic()")
        playingMusic?.let { music -> play(music) }
    }


    fun play(music: Music) {
        Gdx.app.debug(TAG, "play(Music)")

        this.playingMusic = music
        if (GamePreferences.instance.music) {
            music.volume = GamePreferences.instance.volMusic
            music.play()
        }
    }

    fun stopMusic() {
        Gdx.app.debug(TAG, "stopMusic()")

        if (this.playingMusic != null) {
            playingMusic!!.stop()
            this.playingMusic = null
        }
    }

    fun onSettingsUpdated() {
        Gdx.app.debug(TAG, "onSettingsUpdated()")

        if (this.playingMusic == null) return
        playingMusic!!.setVolume(GamePreferences.Companion.instance.volMusic)
        if (GamePreferences.Companion.instance.music) {
            if (!playingMusic!!.isPlaying()) playingMusic!!.play()
        } else {
            playingMusic!!.pause()
        }
    }

    /**
     *
     * @return true when a music is loaded
     */
    fun hasMusic(): Boolean {
        Gdx.app.debug(TAG, "hasMusic() --> result: " + (this.playingMusic != null))
        return this.playingMusic != null
    }


    fun startRandomMusic() {
        Gdx.app.debug(TAG, "startRandomMusic()")
        loadRandomMusic()

        playingMusic?.let { music ->
            play(music)

            music.setOnCompletionListener {
                Gdx.app.debug(TAG, "onCompletion(music)")
                ChangMemory.musicOnCompletionCounter++
                startRandomMusic()
            }
        }
    }


    private fun updateMusic() {
        // if (music == null) return;
        // if (!music.isPlaying()){
        // Gdx.app.debug(TAG, "!music.isPlaying()");
        //
        // ChangMemory.musicOnCompletionCounter++;
        // startRandomMusic();
        // }
    }

    private fun loadRandomMusic() {
        val r = Random().nextInt(6)

        musicName = when (r) {
            0 -> Constants.Music.TITLE1
            1 -> Constants.Music.TITLE2
            2 -> Constants.Music.TITLE3
            3 -> Constants.Music.TITLE4
            4 -> Constants.Music.TITLE5
            5 -> Constants.Music.TITLE6
            else -> Constants.Music.TITLE1
        }

        if (this.playingMusic != null) {
            playingMusic!!.dispose()
            this.playingMusic = null
        }

        Gdx.app.debug(TAG, "Play Music '" + musicName)

        this.playingMusic = Gdx.audio.newMusic(Gdx.files.internal(musicName))
    }

    /**
     * Private class used for store the asset with the day in the soundList
     *
     * @author Patric Hollenstein
     */
    private inner class SoundListObject(assetSound: AssetSound, delay: Float) {
        var assetSound: AssetSound
        var delay: Float

        init {
            this.assetSound = assetSound
            this.delay = delay
        }
    }

    companion object {
        val instance: AudioManager = AudioManager()
    }
}
