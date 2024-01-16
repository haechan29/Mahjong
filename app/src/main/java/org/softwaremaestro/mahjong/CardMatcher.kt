package org.softwaremaestro.mahjong

class CardMatcher {
    private val cards = mutableListOf<Int>()

    fun put(card: Int) {
        cards.add(card)
    }

    fun isMatching(): Boolean {
        return cards[0] == cards[1]
    }

    fun clear(): List<Int> {
        val newCards = mutableListOf<Int>().apply {
            addAll(cards)
        }
        cards.clear()
        return newCards
    }

    fun isEmpty(): Boolean {
        return cards.isEmpty()
    }
}
