package org.softwaremaestro.mahjong

class CardMatcher {
    private val cards = mutableListOf<Card>()

    fun put(card: Card) {
        cards.add(card)
    }

    fun isMatching(): Boolean {
        return cards[0].number == cards[1].number
    }

    fun clear(): List<Card> {
        val newCards = mutableListOf<Card>().apply {
            addAll(cards)
        }
        cards.clear()
        return newCards
    }

    fun isEmpty(): Boolean {
        return cards.isEmpty()
    }
}

data class Card(
    val idx: Int,
    val number: Int
)
