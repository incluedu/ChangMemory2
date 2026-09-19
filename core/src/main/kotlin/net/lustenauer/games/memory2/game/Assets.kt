package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import net.lustenauer.games.memory2.utils.Constants
import com.badlogic.gdx.utils.Array

class Assets private constructor() : Disposable, AssetErrorListener {
    lateinit var manager: AssetManager
    lateinit var fonts: AssetFonts
    lateinit var skinWindow: Skin

    var cardAssetList: com.badlogic.gdx.utils.Array<AssetCard> = com.badlogic.gdx.utils.Array()

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
        manager.load(Constants.TEXTURE_ATLAS_CARDS, TextureAtlas::class.java)
        manager.finishLoading()

        fonts = AssetFonts()
        skinWindow = Skin(Gdx.files.internal(Constants.SKIN_WINDOW), TextureAtlas(Constants.TEXTURE_ATLAS_WINDOS))
    }

    /**
     * load the most important sounds, the card sounds will loaded when a card is used the first time
     */
    fun loadSounds() {
        manager.load("sounds/achievement.ogg", Sound::class.java)
        manager.load("sounds/beep.ogg", Sound::class.java)
        manager.load("sounds/click.ogg", Sound::class.java)
        manager.load("sounds/ding.ogg", Sound::class.java)
        manager.load("sounds/ding2.ogg", Sound::class.java)
        manager.load("sounds/done.ogg", Sound::class.java)
        manager.load("sounds/flip.ogg", Sound::class.java)
        manager.load("sounds/gameOver.ogg", Sound::class.java)
        manager.load("sounds/levelCompleted.ogg", Sound::class.java)
        manager.load("sounds/luckyTry.ogg", Sound::class.java)

        manager.finishLoading()

        manager.load("sounds/bird.ogg", Sound::class.java)
        // manager.load("sounds/bubbles.ogg", Sound::class.java)
        // manager.load("sounds/cow.ogg", Sound::class.java)
        // manager.load("sounds/dog.ogg", Sound::class.java)
        // manager.load("sounds/elephant.ogg", Sound::class.java)
        // manager.load("sounds/pig.ogg", Sound::class.java)
        // manager.load("sounds/rooster.ogg", Sound.class);
        // manager.load("sounds/snake.ogg", Sound.class);
        // manager.load("sounds/toManyTry.ogg", Sound.class);
        // manager.load("sounds/zombie.ogg", Sound.class);
        //
        // manager.load("sounds/abc/0.ogg", Sound.class);
        // manager.load("sounds/abc/1.ogg", Sound.class);
        // manager.load("sounds/abc/2.ogg", Sound.class);
        // manager.load("sounds/abc/3.ogg", Sound.class);
        // manager.load("sounds/abc/4.ogg", Sound.class);
        // manager.load("sounds/abc/5.ogg", Sound.class);
        // manager.load("sounds/abc/6.ogg", Sound.class);
        // manager.load("sounds/abc/7.ogg", Sound.class);
        // manager.load("sounds/abc/8.ogg", Sound.class);
        // manager.load("sounds/abc/9.ogg", Sound.class);
        // manager.load("sounds/abc/10.ogg", Sound.class);
        // manager.load("sounds/abc/11.ogg", Sound.class);
        // manager.load("sounds/abc/12.ogg", Sound.class);
        // manager.load("sounds/abc/13.ogg", Sound.class);
        // manager.load("sounds/abc/14.ogg", Sound.class);
        // manager.load("sounds/abc/15.ogg", Sound.class);
        // manager.load("sounds/abc/16.ogg", Sound.class);
        // manager.load("sounds/abc/17.ogg", Sound.class);
        // manager.load("sounds/abc/18.ogg", Sound.class);
        // manager.load("sounds/abc/19.ogg", Sound.class);
        // manager.load("sounds/abc/20.ogg", Sound.class);
        //
        // manager.load("sounds/abc/a.ogg", Sound.class);
        // manager.load("sounds/abc/b.ogg", Sound.class);
        // manager.load("sounds/abc/c.ogg", Sound.class);
        // manager.load("sounds/abc/d.ogg", Sound.class);
        // manager.load("sounds/abc/e.ogg", Sound.class);
        // manager.load("sounds/abc/f.ogg", Sound.class);
        // manager.load("sounds/abc/g.ogg", Sound.class);
        // manager.load("sounds/abc/h.ogg", Sound.class);
        // manager.load("sounds/abc/i.ogg", Sound.class);
        // manager.load("sounds/abc/j.ogg", Sound.class);
        // manager.load("sounds/abc/k.ogg", Sound.class);
        // manager.load("sounds/abc/l.ogg", Sound.class);
        // manager.load("sounds/abc/m.ogg", Sound.class);
        // manager.load("sounds/abc/n.ogg", Sound.class);
        // manager.load("sounds/abc/o.ogg", Sound.class);
        // manager.load("sounds/abc/p.ogg", Sound.class);
        // manager.load("sounds/abc/q.ogg", Sound.class);
        // manager.load("sounds/abc/r.ogg", Sound.class);
        // manager.load("sounds/abc/s.ogg", Sound.class);
        // manager.load("sounds/abc/t.ogg", Sound.class);
        // manager.load("sounds/abc/u.ogg", Sound.class);
        // manager.load("sounds/abc/v.ogg", Sound.class);
        // manager.load("sounds/abc/w.ogg", Sound.class);
        // manager.load("sounds/abc/x.ogg", Sound.class);
        // manager.load("sounds/abc/y.ogg", Sound.class);
        // manager.load("sounds/abc/z.ogg", Sound.class);
    }

    private fun initCards() {
        val atlas: TextureAtlas = manager.get(Constants.TEXTURE_ATLAS_CARDS)

        cardAssetList.clear()
        cardAssetList.ensureCapacity(CARDSCOUNT)

        cardAssetList.add(AssetCard(this, atlas, "back3", "Back Card", null, 0f, true))
        cardAssetList.add(AssetCard(this, atlas, "empty", "Empty Card", null))

        cardAssetList.add(AssetCard(this, atlas, "alligator", "Alligator", null))
        cardAssetList.add(AssetCard(this, atlas, "angel", "Angel", null))
        cardAssetList.add(AssetCard(this, atlas, "ant", "Ant", null))
        cardAssetList.add(AssetCard(this, atlas, "apple", "Apple", null))
        cardAssetList.add(AssetCard(this, atlas, "bee", "Bee", null))
        cardAssetList.add(AssetCard(this, atlas, "bird", "Bird", "sounds/bird.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "corn", "Corn", null))
        cardAssetList.add(AssetCard(this, atlas, "crab", "Crab", null))
        cardAssetList.add(AssetCard(this, atlas, "dog", "Dog", "sounds/dog.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "dragonfly", "Dragonfly", null))
        cardAssetList.add(AssetCard(this, atlas, "duck", "Duck", null))
        cardAssetList.add(AssetCard(this, atlas, "elefant", "Elephant", "sounds/elephant.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "fish", "Fisch", "sounds/bubbles.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "frog", "Frog", null))
        cardAssetList.add(AssetCard(this, atlas, "ghost", "Ghost", null))
        cardAssetList.add(AssetCard(this, atlas, "hedgehog", "Hedgehog", null))
        cardAssetList.add(AssetCard(this, atlas, "hero", "Supperhero", null))
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
        cardAssetList.add(AssetCard(this, atlas, "pig", "Pig", "sounds/pig.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "rabbit", "Rabit", null))
        cardAssetList.add(AssetCard(this, atlas, "santa", "Santa", null))
        cardAssetList.add(AssetCard(this, atlas, "shark", "Shark", null))
        cardAssetList.add(AssetCard(this, atlas, "snail", "Snail", null))
        cardAssetList.add(AssetCard(this, atlas, "troll", "Troll", null))
        cardAssetList.add(AssetCard(this, atlas, "turkey", "Turkey", null))
        cardAssetList.add(AssetCard(this, atlas, "turtle", "Turtle", null))
        cardAssetList.add(AssetCard(this, atlas, "vampire", "Vampiere", null))
        cardAssetList.add(AssetCard(this, atlas, "worm", "Worm", null))
        cardAssetList.add(AssetCard(this, atlas, "zombie", "Zombie", "sounds/zombie.ogg"))

        cardAssetList.add(AssetCard(this, atlas, "A", "Letter A", "sounds/abc/a.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "B", "Letter B", "sounds/abc/b.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "C", "Letter C", "sounds/abc/c.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "D", "Letter D", "sounds/abc/d.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "E", "Letter E", "sounds/abc/e.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "F", "Letter F", "sounds/abc/f.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "G", "Letter G", "sounds/abc/g.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "H", "Letter H", "sounds/abc/h.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "I", "Letter I", "sounds/abc/i.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "J", "Letter J", "sounds/abc/j.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "K", "Letter K", "sounds/abc/k.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "L", "Letter L", "sounds/abc/l.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "M", "Letter M", "sounds/abc/m.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "N", "Letter N", "sounds/abc/n.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "O", "Letter O", "sounds/abc/o.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "P", "Letter P", "sounds/abc/p.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "Q", "Letter Q", "sounds/abc/q.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "R", "Letter R", "sounds/abc/r.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "S", "Letter S", "sounds/abc/s.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "T", "Letter T", "sounds/abc/t.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "U", "Letter U", "sounds/abc/u.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "V", "Letter V", "sounds/abc/v.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "W", "Letter W", "sounds/abc/w.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "X", "Letter X", "sounds/abc/x.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "Y", "Letter Y", "sounds/abc/y.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "Z", "Letter Z", "sounds/abc/z.ogg"))

        cardAssetList.add(AssetCard(this, atlas, "0", "Number 0", "sounds/abc/0.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "1", "Number 1", "sounds/abc/1.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "2", "Number 2", "sounds/abc/2.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "3", "Number 3", "sounds/abc/3.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "4", "Number 4", "sounds/abc/4.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "5", "Number 5", "sounds/abc/5.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "6", "Number 6", "sounds/abc/6.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "7", "Number 7", "sounds/abc/7.ogg"))
        cardAssetList.add(AssetCard(this, atlas, "8", "Number 8", "sounds/abc/8.ogg"))
        cardAssetList.add(
            AssetCard(
                this,
                atlas,
                "9",
                "Number 9",
                "sounds/abc/9.ogg"
            )
        )        // cardAssetList[c++] = new AssetCard(atlas, "10", "Number 10", "sounds/abc/10.ogg");

//        cardAssetList.add(AssetCard(atlas, "11", "Number 11", "sounds/abc/11.ogg"))
//        cardAssetList.add(AssetCard(atlas, "12", "Number 12", "sounds/abc/12.ogg"))
//        cardAssetList.add(AssetCard(atlas, "13", "Number 13", "sounds/abc/13.ogg"))
//        cardAssetList.add(AssetCard(atlas, "14", "Number 14", "sounds/abc/14.ogg"))
//        cardAssetList.add(AssetCard(atlas, "15", "Number 15", "sounds/abc/15.ogg"))
//        cardAssetList.add(AssetCard(atlas, "16", "Number 16", "sounds/abc/16.ogg"))
//        cardAssetList.add(AssetCard(atlas, "17", "Number 17", "sounds/abc/17.ogg"))
//        cardAssetList.add(AssetCard(atlas, "18", "Number 18", "sounds/abc/18.ogg"))
//        cardAssetList.add(AssetCard(atlas, "19", "Number 19", "sounds/abc/19.ogg"))
//        cardAssetList.add(AssetCard(atlas, "20", "Number 20", "sounds/abc/20.ogg"))
    }

    private fun initSounds() {
        clickSound = AssetSound(this, "sounds/click.ogg", 0.8f)
        flipSound = AssetSound(this, "sounds/flip.ogg", 1f)
        doneSound = AssetSound(this, "sounds/done.ogg", 0.5f)
        luckyTrySound = AssetSound(this, "sounds/luckyTry.ogg", 0.8f)
        levelComplSound = AssetSound(this, "sounds/levelCompleted.ogg", 1f)
        toManyTrySound = AssetSound(this, "sounds/toManyTry.ogg", 1f)
        beepSound = AssetSound(this, "sounds/beep.ogg", 1f)
        gameOverSound = AssetSound(this, "sounds/gameOver.ogg", 1f)
        soundAchievement = AssetSound(this, "sounds/achievement.ogg", 1f)
    }

    override fun dispose() {
        manager.dispose()
        for (card in cardAssetList) {
            card.dispose()
        }

        fonts?.dispose()
    }

    fun error(filename: String?, type: Class<*>?, throwable: Throwable?) {
        Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", throwable)
    }

    override fun error(asset: AssetDescriptor<*>, throwable: Throwable?) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable)
    }

    companion object {
        val TAG: String = Assets::class.java.getName()
        val instance: Assets = Assets() // Initialize class as a singleton
        const val CARDSCOUNT: Int = 76 // max count off cards
    }
}
