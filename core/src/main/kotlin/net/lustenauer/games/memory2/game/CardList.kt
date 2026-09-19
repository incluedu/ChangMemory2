package net.lustenauer.games.memory2.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import net.lustenauer.games.memory2.game.objects.Card // Paketpfad korrigiert!

class CardList {
    var cards: com.badlogic.gdx.utils.Array<Card> = com.badlogic.gdx.utils.Array()

    private var cardcount = 0

    init {
        addAllCards()
    }

    private fun addAllCards() {
        val numCards: Int = Assets.instance.cardAssetList.size
        for (i in 2..<numCards) {
            cards.add(Card(i))
        }

        Gdx.app.log(TAG, "${cards.size} cards has been added to the cardList")
    }

    fun getCardGameSet(level: Int): com.badlogic.gdx.utils.Array<Card> {
        val levelArray = loadLevel(level)

        val gameSet = com.badlogic.gdx.utils.Array<Card>()
        val singleCards = com.badlogic.gdx.utils.Array<Card>(cards)

        singleCards.shuffle()
        singleCards.removeRange(cardcount / 2, singleCards.size - 1)

        for (card in singleCards) {
            gameSet.add(Card(card))
            gameSet.add(Card(card))
        }
        gameSet.shuffle()

        var cardIndex = 0
        for (y in levelArray.indices) {
            for (x in levelArray[0].indices) {
                if (levelArray[y][x] > 0) {
                    val posX = (10 + (x * 40)).toFloat()
                    val posY = (640 - (y * 45)).toFloat()

                    gameSet.get(cardIndex).setPosition(posX, posY)
                    cardIndex++
                }
            }
        }

        return gameSet
    }

    private fun loadLevel(level: Int): Array<IntArray> {
        Gdx.app.debug(TAG, "loadLevel(level $level)")

        val la = Array(15) { IntArray(11) }
        cardcount = 0

        var lv = level
        if (lv > 24) lv = 24

        val handle: FileHandle = Gdx.files.internal("levels/level$lv.txt")

        val text: String = handle.readString()
        val lines: Array<String> = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (x in 0..14) {
            for (y in 0..10) {
                if (lines[x][y] == '*') {
                    la[x][y] = 1
                    cardcount++
                }
            }
        }
        Gdx.app.debug(TAG, "$cardcount cards found in level $level")
        return la
    }

    companion object {
        private val TAG: String = CardList::class.java.name
    }
}
