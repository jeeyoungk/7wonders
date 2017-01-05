package jee

import org.junit.Test
import kotlin.test.assertEquals

class EngineTest {
    @Test
    fun testScience() {
        assertEquals(0, jee.scoreScience(0, 0, 0, 0))
        assertEquals(1, jee.scoreScience(1, 0, 0, 0))
        assertEquals(2, jee.scoreScience(1, 1, 0, 0))
        assertEquals(10, jee.scoreScience(1, 1, 1, 0))
        assertEquals(10, jee.scoreScience(1, 1, 0, 1))
        assertEquals(10, jee.scoreScience(0, 0, 0, 3))
        assertEquals(16, jee.scoreScience(1, 0, 0, 3))
        assertEquals(13, jee.scoreScience(1, 1, 0, 2))
    }

    @Test
    fun testCard() {
        for (players in 3..7) {
            val e = Engine(players, CARDS)
            assertEquals(players * 7, e.obtainCards(1).size, "Failed to get the correct # of cards for $players")
        }
    }
}
