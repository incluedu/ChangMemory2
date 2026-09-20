package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import ktx.assets.dispose
import ktx.assets.load
import net.lustenauer.games.memory2.utils.Constants.Atlas
import net.lustenauer.games.memory2.utils.Constants.SkinConfig

class Assets private constructor() : Disposable, AssetErrorListener {
    lateinit var manager: AssetManager
    lateinit var fonts: AssetFonts
    lateinit var skinWindow: Skin

    var cardAssetList: Array<AssetCard> = Array()

    lateinit var clickSound: AssetSound
    lateinit var doneSound: AssetSound
    lateinit var levelComplSound: AssetSound
    lateinit var luckyTrySound: AssetSound
    lateinit var flipSound: AssetSound
    lateinit var toManyTrySound: AssetSound
    lateinit var beepSound: AssetSound
    lateinit var gameOverSound: AssetSound
    lateinit var soundAchievement: AssetSound

    fun initManager(assetManager: AssetManager) {
        Gdx.app.debug(TAG, "init(assetManager)")
        this.manager = assetManager
        assetManager.setErrorListener(this)
    }

    fun init() {
        initCards()
        initSounds()
    }

    fun loadTextures() {
        manager.load<TextureAtlas>(Atlas.CARDS)
        manager.finishLoading()

        fonts = AssetFonts()

        skinWindow = Skin(
            Gdx.files.internal(SkinConfig.WINDOW),
            TextureAtlas(Atlas.WINDOWS)
        )
    }

    /**
     * Load the most important sounds. The card sounds will be loaded when a card is used for the first time.
     */
    fun loadSounds() {
        manager.apply {
            load<Sound>(SOUND_ACHIEVEMENT)
            load<Sound>(SOUND_BEEP)
            load<Sound>(SOUND_CLICK)
            load<Sound>(SOUND_DING)
            load<Sound>(SOUND_DING_2)
            load<Sound>(SOUND_DONE)
            load<Sound>(SOUND_FLIP)
            load<Sound>(SOUND_GAME_OVER)
            load<Sound>(SOUND_LEVEL_COMPLETED)
            load<Sound>(SOUND_LUCKY_TRY)

            finishLoading()

            load<Sound>(SOUND_BIRD)
        }
    }

    private fun initCards() {
        val atlas = manager.get(Atlas.CARDS, TextureAtlas::class.java)

        cardAssetList.clear()
        cardAssetList.ensureCapacity(CARDSCOUNT)

        cardAssetList.add(AssetCard(this, atlas, "back3", "Back Card", null, 0f, true))
        cardAssetList.add(AssetCard(this, atlas, "empty", "Empty Card", null))

        cardAssetList.add(AssetCard(this, atlas, "alligator", "Alligator", null))
        cardAssetList.add(AssetCard(this, atlas, "angel", "Angel", null))
        cardAssetList.add(AssetCard(this, atlas, "ant", "Ant", null))
        cardAssetList.add(AssetCard(this, atlas, "apple", "Apple", null))
        cardAssetList.add(AssetCard(this, atlas, "bee", "Bee", null))

        cardAssetList.add(AssetCard(this, atlas, "bird", "Bird", SOUND_BIRD))
        cardAssetList.add(AssetCard(this, atlas, "corn", "Corn", null))
        cardAssetList.add(AssetCard(this, atlas, "crab", "Crab", null))
        cardAssetList.add(AssetCard(this, atlas, "dog", "Dog", SOUND_DOG))
        cardAssetList.add(AssetCard(this, atlas, "dragonfly", "Dragonfly", null))
        cardAssetList.add(AssetCard(this, atlas, "duck", "Duck", null))
        cardAssetList.add(AssetCard(this, atlas, "elefant", "Elephant", SOUND_ELEPHANT))
        cardAssetList.add(AssetCard(this, atlas, "fish", "Fish", SOUND_BUBBLES))

        cardAssetList.add(AssetCard(this, atlas, "frog", "Frog", null))
        cardAssetList.add(AssetCard(this, atlas, "ghost", "Ghost", null))
        cardAssetList.add(AssetCard(this, atlas, "hedgehog", "Hedgehog", null))
        cardAssetList.add(AssetCard(this, atlas, "hero", "Superhero", null))
        cardAssetList.add(AssetCard(this, atlas, "horse", "Horse", null))
        cardAssetList.add(AssetCard(this, atlas, "jellyfish", "Jellyfish", null))
        cardAssetList.add(AssetCard(this, atlas, "kid", "Kid", null))
        cardAssetList.add(AssetCard(this, atlas, "lion", "Lion", null))
        cardAssetList.add(AssetCard(this, atlas, "monkey", "Monkey", null))
        cardAssetList.add(AssetCard(this, atlas, "mouse", "Mouse", null))
        cardAssetList.add(AssetCard(this, atlas, "ninja", "Ninja", null))
        cardAssetList.add(AssetCard(this, atlas, "octopus", "Octopus", null))
        cardAssetList.add(AssetCard(this, atlas, "parrot", "Parrot", null))
        cardAssetList.add(AssetCard(this, atlas, "penguin", "Penguin", null))
        cardAssetList.add(AssetCard(this, atlas, "pig", "Pig", SOUND_PIG))
        cardAssetList.add(AssetCard(this, atlas, "rabbit", "Rabit", null))
        cardAssetList.add(AssetCard(this, atlas, "santa", "Santa", null))
        cardAssetList.add(AssetCard(this, atlas, "shark", "Shark", null))
        cardAssetList.add(AssetCard(this, atlas, "snail", "Snail", null))
        cardAssetList.add(AssetCard(this, atlas, "troll", "Troll", null))
        cardAssetList.add(AssetCard(this, atlas, "turkey", "Turkey", null))
        cardAssetList.add(AssetCard(this, atlas, "turtle", "Turtle", null))
        cardAssetList.add(AssetCard(this, atlas, "vampire", "Vampire", null))
        cardAssetList.add(AssetCard(this, atlas, "worm", "Worm", null))
        cardAssetList.add(AssetCard(this, atlas, "zombie", "Zombie", SOUND_ZOMBIE))

        for (char in 'A'..'Z') {
            cardAssetList.add(
                AssetCard(
                    assets = this,
                    atlas = atlas,
                    assetName = char.toString(),
                    desc = "Letter $char",
                    soundPath = "sounds/abc/${char.lowercase()}.ogg"
                )
            )
        }

        for (num in 0..9) {
            cardAssetList.add(
                AssetCard(
                    assets = this,
                    atlas = atlas,
                    assetName = num.toString(),
                    desc = "Number $num",
                    soundPath = "sounds/abc/$num.ogg"
                )
            )
        }
    }

    private fun initSounds() {
        clickSound = AssetSound(this, SOUND_CLICK, 0.8f)
        flipSound = AssetSound(this, SOUND_FLIP, 1f)
        doneSound = AssetSound(this, SOUND_DONE, 0.5f)
        luckyTrySound = AssetSound(this, SOUND_LUCKY_TRY, 0.8f)
        levelComplSound = AssetSound(this, SOUND_LEVEL_COMPLETED, 1f)
        toManyTrySound = AssetSound(this, SOUND_TOO_MANY_TRYS, 1f)
        beepSound = AssetSound(this, SOUND_BEEP, 1f)
        gameOverSound = AssetSound(this, SOUND_GAME_OVER, 1f)
        soundAchievement = AssetSound(this, SOUND_ACHIEVEMENT, 1f)
    }

    override fun dispose() {
        manager.dispose()
        cardAssetList.dispose()
        skinWindow.dispose()
        fonts.dispose()
    }

    override fun error(asset: AssetDescriptor<*>?, throwable: Throwable?) {
        Gdx.app.error(TAG, "Couldn't load asset '${asset?.fileName}'", throwable)
    }

    companion object {
        val TAG: String = Assets::class.java.name

        val instance = Assets()
        const val CARDSCOUNT = 76

        private const val SOUND_ACHIEVEMENT = "sounds/achievement.ogg"
        private const val SOUND_BEEP = "sounds/beep.ogg"
        private const val SOUND_CLICK = "sounds/click.ogg"
        private const val SOUND_DING = "sounds/ding.ogg"
        private const val SOUND_DING_2 = "sounds/ding2.ogg"
        private const val SOUND_DONE = "sounds/done.ogg"
        private const val SOUND_FLIP = "sounds/flip.ogg"
        private const val SOUND_GAME_OVER = "sounds/gameOver.ogg"
        private const val SOUND_LEVEL_COMPLETED = "sounds/levelCompleted.ogg"
        private const val SOUND_LUCKY_TRY = "sounds/luckyTry.ogg"
        private const val SOUND_TOO_MANY_TRYS = "sounds/toManyTry.ogg"

        private const val SOUND_BIRD = "sounds/bird.ogg"
        private const val SOUND_DOG = "sounds/dog.ogg"
        private const val SOUND_ELEPHANT = "sounds/elephant.ogg"
        private const val SOUND_BUBBLES = "sounds/bubbles.ogg"
        private const val SOUND_PIG = "sounds/pig.ogg"
        private const val SOUND_ZOMBIE = "sounds/zombie.ogg"
    }
}
