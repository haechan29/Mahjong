package org.softwaremaestro.mahjong

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun putNumberAndGetLayout() {
        assertEquals(1, getLayout(4).size)
        assertEquals(2, getLayout(8).size)
    }

    @Test
    fun layoutConsistsOfPairOfNumbers() {
        val layout = getLayout(8)
        for (i in 1 until 4) {
            assertEquals(2, layout.flatten().count { it == i })
        }
    }

    private fun getLayout(number: Int): Array<Array<Int>> {
        val shuffledNumbers = mutableListOf<Int>().apply {
            repeat(2) {
                for (i in 1..number / 2) add(i)
            }
        }.shuffled()
        return Array(number / 4) {
            shuffledNumbers.slice(it * 4 until (it + 1) * 4).toTypedArray()
        }
    }
}