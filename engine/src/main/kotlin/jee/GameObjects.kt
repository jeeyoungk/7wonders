package jee

import java.util.*

/**
 * Created by jeeyoungk on 1/4/17.
 */
/**
 * Things to implement:
 *
 * cards
 * - resource
 * player
 * - civilization
 * - wonder stage.
 * rules
 * - playing a card
 * - "trading".
 * - military
 *
 *
 * Optional features:
 * - wonder for babylon (side b)
 * - wonder for Halicarnassus (side a, b)
 */

enum class CardType {
    BASIC_RESOURCE,
    ADVANCED_RESOURCE,
    TRADE,
    MILITARY,
    IMPROVEMENTS,
    GUILD,
    SCIENCE,
    WONDER,
    STARTING
}

enum class Resource(val advanced: Boolean) {
    WOOD(false), ORE(false), CLAY(false), STONE(false),
    GLASS(true), PAPYRUS(true), TEXTILE(true);
}

enum class Science() {
    COMPASS, GEAR, TABLET, WILDCARD
}

data class MilitaryToken(val point: Int)

val BASIC_RESOURCES: Set<Resource> = HashSet(Resource.values().filter { it -> !it.advanced })
val ADVANCED_RESOURCES: Set<Resource> = HashSet(Resource.values().filter(Resource::advanced))

/**
 * Immutable representation of a card.
 */
data class Card(
        val name: String,
        val cardType: CardType,
        val requiredGold: Int,
        /** Age this card is available from. Wonders may have different value. */
        val age: Int?,
        val appears: List<Int>,
        val requiredResources: Map<Resource, Int>,
        val optionalUpgrade: List<Card>,
        /** list of resources provided by this building. */
        val providedResources: List<Set<Resource>>,
        /** Whether resources provided by this building is tradable from other players. */
        val tradable: Boolean,
        val victoryPoints: Int,
        val militaryPoints: Int,
        val science: Science?
) {
    private constructor(builder: Builder) : this(
            builder.name!!,
            builder.cardType!!,
            builder.requiredGold,
            builder.age!!,
            builder.appears,
            builder.requiredResources,
            builder.optionalUpgrade,
            builder.providedResources,
            builder.tradable,
            builder.victoryPoints,
            builder.militaryPoints,
            builder.science
    )

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var name: String? = null
        var cardType: CardType? = null
        var requiredGold: Int = 0
        var age: Int? = null
        var appears: List<Int> = listOf()
        var requiredResources: Map<Resource, Int> = hashMapOf()
        var optionalUpgrade: List<Card> = arrayListOf()
        var providedResources: List<Set<Resource>> = arrayListOf()
        var tradable: Boolean = true
        var victoryPoints: Int = 0
        var militaryPoints: Int = 0
        var science: Science? = null
        fun build(): Card {
            if (name == null) {
                throw IllegalArgumentException("Name is required.")
            }
            if (cardType == null) {
                throw IllegalArgumentException("Card $name requires a cardtype.")
            }
            return Card(this)
        }

        var singleResource: Resource
            get() {
                throw RuntimeException("Not supported")
            }
            set(value) {
                providedResources = listOf(hashSetOf(value))
            }

        var choosingResources: Set<Resource>
            get() {
                throw RuntimeException("Not supported")
            }
            set(value) {
                providedResources = ArrayList(value.map { it -> hashSetOf(it) })
            }
    }
}