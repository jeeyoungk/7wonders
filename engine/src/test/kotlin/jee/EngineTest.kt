package jee

import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EngineTest {
    @Test
    fun testScience() {
        // todo - this actually tests utility.
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
            val e = makeEngine(players)
            assertEquals(players * 7, e.obtainCards(1).size, "Failed to get the correct # of cards for $players")
        }
    }

    @Test
    fun testAdjancy() {
        val e = makeEngine(5)
        assertTrue(e.isAdjacent(0, 1))
        assertTrue(e.isAdjacent(1, 0))
        assertTrue(e.isAdjacent(0, 4))
        assertFalse(e.isAdjacent(0, 0))
        assertFalse(e.isAdjacent(0, 2))
        assertFalse(e.isAdjacent(0, 3))
    }

    @Test
    fun testShuffle() {
        val e = makeEngine(5)
        e.initialize()
        e.doNewAge()
        val previous = ArrayList(e.hands)
        e.shiftHands()
        assertEquals(previous[1], e.hands[0])
        assertEquals(previous[2], e.hands[1])
        assertEquals(previous[3], e.hands[2])
        assertEquals(previous[4], e.hands[3])
        assertEquals(previous[0], e.hands[4])
    }

    fun makeEngine(numPlayers: Int): Engine {
        val agents: List<Agent> = (1..numPlayers).map({ RandomAgent() });
        return Engine(numPlayers, CARDS, agents, 1)
    }
}
