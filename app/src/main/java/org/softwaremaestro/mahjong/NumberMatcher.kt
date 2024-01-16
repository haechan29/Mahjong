package org.softwaremaestro.mahjong

class NumberMatcher {
    private val numbers = mutableListOf<Int>()

    fun put(number: Int) {
        numbers.add(number)
    }

    fun isMatching(): Boolean {
        return numbers[0] == numbers[1]
    }

    fun clear(): List<Int> {
        val newNumbers = mutableListOf<Int>().apply {
            addAll(numbers)
        }
        numbers.clear()
        return newNumbers
    }

    fun isEmpty(): Boolean {
        return numbers.isEmpty()
    }
}
