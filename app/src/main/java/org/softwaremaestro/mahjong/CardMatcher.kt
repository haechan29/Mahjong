package org.softwaremaestro.mahjong

class CardMatcher {
    var mCardIdx: Int? = null
    fun put(cardIdx: Int): Boolean? {
        return if (mCardIdx == null) {
            mCardIdx = cardIdx
            null
        } else {
            val matching = mCardIdx == cardIdx
            mCardIdx = null
            matching
        }
    }
}
