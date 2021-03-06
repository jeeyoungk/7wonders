package jee

import org.junit.Test

import org.junit.Assert.*

class GameObjectsKtTest {
    @Test
    fun permute() {
        assertEquals(
                listOf(listOf(1), listOf(2)),
                permute(listOf(listOf(1, 2)))
        )
        assertEquals(
                listOf(
                        listOf(1, 3), listOf(1, 4),
                        listOf(2, 3), listOf(2, 4)
                ),
                permute(listOf(listOf(1, 2), listOf(3, 4)))
        )
    }

    @Test
    fun isSubList() {
        assertTrue(isSubList(listOf<Int>(), listOf<Int>()))
        assertTrue(isSubList(listOf(1), listOf(1)))
        assertTrue(isSubList(listOf(1, 2, 3), listOf(2)))
        assertTrue(isSubList(listOf(1, 2, 2, 3, 4), listOf(2, 3)))
        assertFalse(isSubList(listOf(), listOf(1)))
        assertFalse(isSubList(listOf(2, 3), listOf(2, 3, 4)))
    }
}