package jee

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by jeeyoungk on 1/6/17.
 */
class GameObjectsKtTest {
    @Test
    fun permute() {
        // permute(listOf(listOf(1, 2, 3)), { x -> print(x) })
        permute(listOf(listOf(1, 2, 3, 4), listOf(2, 3, 4), listOf(4,5)), { x -> println(x) })
    }
}