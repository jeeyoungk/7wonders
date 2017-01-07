package jee

import org.junit.Assert.*
import org.junit.Test

class CardTest {
    @Test
    fun canProvideSimple() {
        val card = Card.build {
            name = "sample card"
            singleResource = Resource.WOOD
            cardType = CardType.SAMPLE
            age = 1
            appears = listOf(3, 4)
        }
        assertTrue(card.canProvide(listOf()))
        assertTrue(card.canProvide(listOf(Resource.WOOD)))
        assertFalse(card.canProvide(listOf(Resource.WOOD, Resource.WOOD)))
        assertFalse(card.canProvide(listOf(Resource.ORE)))
    }

    @Test
    fun canProvideChoosing() {
        val card = Card.build {
            name = "sample card"
            choosingResources = ADVANCED_RESOURCES
            cardType = CardType.SAMPLE
            age = 1
            appears = listOf(3, 4)
        }
        assertTrue(card.canProvide(listOf()))
        assertTrue(card.canProvide(listOf(Resource.PAPYRUS)))
        assertTrue(card.canProvide(listOf(Resource.GLASS)))
        assertTrue(card.canProvide(listOf(Resource.TEXTILE)))
        assertFalse(card.canProvide(listOf(Resource.PAPYRUS, Resource.TEXTILE)))
        assertFalse(card.canProvide(listOf(Resource.WOOD)))
    }

    @Test
    fun canProvideMultiple() {
        val card = Card.build {
            name = "sample card"
            providedResources = listOf(setOf(Resource.WOOD), setOf(Resource.WOOD))
            cardType = CardType.SAMPLE
            age = 1
            appears = listOf(3, 4)
        }
        assertTrue(card.canProvide(listOf()))
        assertTrue(card.canProvide(listOf(Resource.WOOD)))
        assertTrue(card.canProvide(listOf(Resource.WOOD, Resource.WOOD)))
        assertFalse(card.canProvide(listOf(Resource.WOOD, Resource.WOOD, Resource.WOOD)))
        assertFalse(card.canProvide(listOf(Resource.ORE)))
    }
}