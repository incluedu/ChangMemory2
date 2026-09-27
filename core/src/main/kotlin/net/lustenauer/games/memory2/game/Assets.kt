package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetErrorListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import ktx.assets.dispose
import ktx.assets.load
import ktx.freetype.generateFont
import ktx.log.logger
import net.lustenauer.games.memory2.game.Assets.skinWindow
import net.lustenauer.games.memory2.utils.Constants.Atlas
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_DEFAULT
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_L
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_M
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_S
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XL
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XS
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XXL
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XXS
import net.lustenauer.games.memory2.utils.Constants.Fonts.FONT_XXXL
import net.lustenauer.games.memory2.utils.Constants.SkinConfig

/**
 * Central asset management singleton orchestrating asset tracking, asynchronous loading pipelines,
 * texture atlas slicing, skin bindings, and dynamic FreeType vector font generation.
 * Implements [Disposable] and [AssetErrorListener] to secure fail-safe hardware memory teardowns.
 *
 * @author Patric Hollenstein
 */
object Assets : Disposable, AssetErrorListener {

    private val log = logger<Assets>()

    /** Core manager handling internal asynchronous loading loops. */
    lateinit var manager: AssetManager

    /** Central UI theme skin configuration mapped to window containers. */
    lateinit var skinWindow: Skin

    /** Dynamic array pooling all extracted memory card graphics configurations. */
    val cardAssetList = Array<AssetCard>()

    /** The [AssetSound] instance assigned to primary UI click feedback actions. */
    val clickSound by lazy { AssetSound(this, SOUND_CLICK, 0.8f) }

    /** The [AssetSound] instance assigned to base card flipping actions. */
    val flipSound by lazy { AssetSound(this, SOUND_FLIP, 1f) }

    /** The [AssetSound] instance assigned to solved card matches. */
    val doneSound by lazy { AssetSound(this, SOUND_DONE, 0.5f) }

    /** The [AssetSound] instance assigned to lucky strike pair configurations. */
    val luckyTrySound by lazy { AssetSound(this, SOUND_LUCKY_TRY, 0.8f) }

    /** The [AssetSound] instance assigned to successful round completion events. */
    val levelComplSound by lazy { AssetSound(this, SOUND_LEVEL_COMPLETED, 1f) }

    /** The [AssetSound] instance assigned to penalty match limitation triggers. */
    val toManyTrySound by lazy { AssetSound(this, SOUND_TOO_MANY_TRYS, 1f) }

    /** The [AssetSound] instance assigned to countdown warning threshold milestones. */
    val beepSound by lazy { AssetSound(this, SOUND_BEEP, 1f) }

    /** The [AssetSound] instance assigned to game termination timelines. */
    val gameOverSound by lazy { AssetSound(this, SOUND_GAME_OVER, 1f) }

    /** The [AssetSound] instance assigned to unlocked notification achievement badges. */
    val soundAchievement by lazy { AssetSound(this, SOUND_ACHIEVEMENT, 1f) }

    /**
     * Binds the central [AssetManager] pipeline and attaches this manager instance as the default error interceptor.
     */
    fun initManager(assetManager: AssetManager) {
        log.debug { "init(assetManager)" }
        manager = assetManager
        assetManager.setErrorListener(this)
    }

    /**
     * Triggers sub-initialization routines to map all pooled items like cards.
     */
    fun init() {
        initCards()
    }

    /**
     * Enqueues core visual components, blocks frames until completion,
     * and triggers dynamic high-definition TrueType vector font generation pipelines.
     */
    fun loadTextures() {
        manager.load<TextureAtlas>(Atlas.CARDS)
        manager.load<TextureAtlas>(Atlas.WINDOWS)
        manager.finishLoading()

        skinWindow = Skin().apply {
            addRegions(manager.get(Atlas.WINDOWS))
        }

        val fontFile = Gdx.files.internal("fonts/ArchitectsDaughter.ttf")
        if (!fontFile.exists()) {
            log.error { "CRITICAL: 'assets/fonts/ArchitectsDaughter.ttf' not found!" }
            return
        }

        val generator = FreeTypeFontGenerator(fontFile)

        generator.create(16, FONT_DEFAULT)
        generator.create(12, FONT_XXS)
        generator.create(14, FONT_XS)
        generator.create(16, FONT_S)
        generator.create(24, FONT_M)
        generator.create(32, FONT_L)
        generator.create(36, FONT_XL)
        generator.create(44, FONT_XXL)
        generator.create(56, FONT_XXXL)

        generator.dispose()

        skinWindow.load(Gdx.files.internal(SkinConfig.WINDOW))

        ktx.scene2d.Scene2DSkin.defaultSkin = skinWindow
    }

    /**
     * Enqueues the standard application audio cues into the load queue asynchronously using KTX.
     */
    fun loadSounds() {
        manager.apply {
            ALL_GLOBAL_SOUNDS.forEach { load<Sound>(it) }
            finishLoading()
        }
    }

    /**
     * Renders a high-definition TrueType size slice and registers both
     * the raw font and its matching text style inside [skinWindow].
     */
    private fun FreeTypeFontGenerator.create(targetSize: Int, fontKey: String) {
        val generatedFont = this.generateFont {
            size = targetSize
            minFilter = Linear
            magFilter = Linear
        }
        skinWindow.add(fontKey, generatedFont, BitmapFont::class.java)
        skinWindow.add(fontKey, LabelStyle(generatedFont, WHITE), LabelStyle::class.java)
    }


    /**
     * Fetches the card texture sheet from storage mappings and builds the complete asset payload array.
     */
    private fun initCards() {
        val atlas = manager.get<TextureAtlas>(Atlas.CARDS)

        cardAssetList.clear()
        cardAssetList.ensureCapacity(CARDS_COUNT)

        cardAssetList.add(AssetCard(this, atlas, "back3", "Back Card", null, 0f, true))
        cardAssetList.add(AssetCard(this, atlas, "empty", "Empty Card", null))

        CardType.entries.forEach { type ->
            cardAssetList.add(AssetCard(this, atlas, type.id, type.desc, type.soundPath))
        }

        ('A'..'Z').forEach { char ->
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

        (0..9).forEach { num ->
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

    /**
     * Unloads the underlying [AssetManager], clears all cached card references,
     * and releases skin and font texture memory allocations to clean up hardware resources.
     */
    override fun dispose() {
        manager.dispose()
        cardAssetList.dispose()
        skinWindow.dispose()
    }

    /**
     * Callback interceptor triggered by the loading pipeline when a file asset fails to resolve.
     */
    override fun error(asset: AssetDescriptor<*>?, throwable: Throwable?) {
        log.error(throwable ?: RuntimeException("Unknown AssetManager Error")) {
            "Couldn't load asset '${asset?.fileName}'"
        }
    }

    // ==========================================
    // CONSTANTS & AUDIO PATHS
    // ==========================================

    /** The total initial reservation capacity threshold for the card descriptor memory pool arrays. */
    const val CARDS_COUNT = 76

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

    private val ALL_GLOBAL_SOUNDS = listOf(
        SOUND_ACHIEVEMENT, SOUND_BEEP, SOUND_CLICK, SOUND_DING, SOUND_DING_2,
        SOUND_DONE, SOUND_FLIP, SOUND_GAME_OVER, SOUND_LEVEL_COMPLETED, SOUND_LUCKY_TRY
    )
}
