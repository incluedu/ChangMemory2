package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import ktx.log.logger
import net.lustenauer.games.memory2.game.objects.Card
import com.badlogic.gdx.utils.Array as GdxArray

/**
 * Manages the global pool of available memory cards and builds localized card sets
 * for individual game levels based on external text configuration files.
 *
 * @author Patric Hollenstein
 */
class CardList {

    private val log = logger<CardList>()

    /**
     * The master list containing one instance of every available [Card] loaded in the game.
     */
    var cards: GdxArray<Card> = GdxArray()

    /**
     * Stores the exact total number of cards required for the currently active level layout.
     */
    private var cardCount = 0

    init {
        addAllCards()
    }

    /**
     * Populates the master list with all unique card assets available from [Assets].
     * Skips the first two technical cards (e.g., back cover and empty states).
     */
    private fun addAllCards() {
        val numCards = Assets.cardAssetList.size
        for (i in 2..<numCards) {
            cards.add(Card(i))
        }

        log.info { "${cards.size} cards have been added to the master card list" }
    }

    /**
     * Generates a fully randomized, paired, and positioned set of cards for a specific level.
     * Selects a random subset of unique cards, duplicates them to form pairs, shuffles the final deck,
     * and maps them onto the coordinate grid defined by the level text file.
     *
     * @param level The current level number to load configuration files for.
     * @return A fully prepared [GdxArray] of positioned [Card] actors ready to be displayed on stage.
     */
    fun getCardGameSet(level: Int): GdxArray<Card> {
        val levelArray = loadLevel(level)

        val gameSet = GdxArray<Card>()
        val singleCards = GdxArray(cards).apply { shuffle() }

        singleCards.removeRange(cardCount / 2, singleCards.size - 1)

        for (card in singleCards) {
            gameSet.add(Card(card))
            gameSet.add(Card(card))
        }
        gameSet.shuffle()

        var cardIndex = 0
        for (y in levelArray.indices) {
            val row = levelArray[y]
            for (x in row.indices) {
                if (row[x] > 0) {
                    gameSet[cardIndex].setPosition(10f + (x * 40f), 640f - (y * 45f))
                    cardIndex++
                }
            }
        }

        return gameSet
    }

    /**
     * Reads a level configuration text file from the internal assets directory and
     * parses its character matrix into a type-safe 2D coordinate array.
     * Automatically normalizes multi-platform line endings (\r\n) to prevent parser bugs.
     *
     * @param level The targeted level index (hard-capped up to level 24).
     * @return A 15x11 grid matrix where 1 indicates a card anchor position and 0 means empty space.
     */
    private fun loadLevel(level: Int): Array<IntArray> {
        log.debug { "loadLevel(level $level)" }

        val levelMatrix = Array(15) { IntArray(11) }
        cardCount = 0

        val validatedLevel = level.coerceAtMost(24)
        val handle: FileHandle = Gdx.files.internal("levels/level$validatedLevel.txt")

        val text = handle.readString()
        val lines = text.lines().filter { it.isNotBlank() }

        for (x in 0..14) {
            val line = lines.getOrNull(x) ?: continue
            for (y in 0..10) {
                if (line.getOrNull(y) == '*') {
                    levelMatrix[x][y] = 1
                    cardCount++
                }
            }
        }

        log.debug { "$cardCount cards found in level $level" }
        return levelMatrix
    }
}
