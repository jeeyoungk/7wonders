package jee

import java.util.*

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
 * - military
 *
 */

enum class CardType {
    BASIC_RESOURCE,
    ADVANCED_RESOURCE,
    TRADE,
    MILITARY,
    IMPROVEMENTS,
    GUILD,
    SCIENCE
}

enum class Resource(val advanced: Boolean) {
    WOOD(false), ORE(false), CLAY(false), STONE(false),
    GLASS(true), PAPYRUS(true), TEXTILE(true);
}

enum class Science() {
    COMPASS, GEAR, TABLET, WILDCARD
}

class MilitaryToken(val point: Int)

val BASIC_RESOURCES: Set<Resource> = HashSet(Resource.values().filter { it -> !it.advanced })
val ADVANCED_RESOURCES: Set<Resource> = HashSet(Resource.values().filter { it -> it.advanced })

class Card(
        val name: String,
        val requiredGold: Int,
        val requiredResources: Map<Resource, Int>,
        val optionalUpgrade: List<Card>,
        val providedResources: List<Set<Resource>>,
        val tradable: Boolean,
        val victoryPoints: Int,
        val militaryPoints: Int,
        val science: Science?
) {
    private constructor(builder: Builder) : this(
            builder.name,
            builder.requiredGold,
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
        var name: String = ""
        var requiredGold: Int = 0
        var requiredResources: Map<Resource, Int> = HashMap()
        var optionalUpgrade: List<Card> = ArrayList()
        /** list of resources provided by this building. */
        var providedResources: List<Set<Resource>> = ArrayList()
        var tradable: Boolean = true
        var victoryPoints: Int = 0
        var militaryPoints: Int = 0
        var science: Science? = null
        fun build() = Card(this)

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

class Engine {
    val players: List<Player> = ArrayList()
}

class Player {
    var gold: Int = 0
    /** List of built cards + wonders. */
    var built: List<Card> = ArrayList()
    /** List of accumulated military tokens. */
    var militaryTokens: List<MilitaryToken> = ArrayList()
    // playing a card means:
    // choose 1 card
    // - build
    // - discard
    // - wonder
    // for build/wonder:
    // - can afford?
    // - if can't afford, then can trade? with who? (multiple possibilities).
    val military: Int get() {
        return built.map(Card::militaryPoints).sum()
    }

    val victory: Int get() {
        return built.map(Card::victoryPoints).sum()
    }
}

/**
 * Calculate the victory points for # of science.
 *
 * TODO - memoize?
 */
fun scoreScience(a: Int, b: Int, c: Int, wild: Int): Int {
    val min = Math.min(Math.min(a, b), c)
    if (wild == 0) {
        return a * a + b * b + c * c + 7 * min
    } else {
        val newa = scoreScience(a + 1, b, c, wild - 1)
        val newb = scoreScience(a, b + 1, c, wild - 1)
        val newc = scoreScience(a, b, c + 1, wild - 1)
        return Math.max(Math.max(newa, newb), newc)
    }
}

fun main(args: Array<String>) {
    val cards: List<Card> = arrayListOf(
            // basic resources
            Card.build {
                name = "lumber yard"
                singleResource = Resource.WOOD
            },
            Card.build {
                name = "stone pit"
                singleResource = Resource.STONE
            },
            Card.build {
                name = "clay pool"
                singleResource = Resource.CLAY
            },
            Card.build {
                name = "ore vein"
                singleResource = Resource.ORE
            },
            // basic choosing resources
            Card.build {
                name = "tree farm"
                requiredGold = 1
                choosingResources = setOf(Resource.WOOD, Resource.CLAY)
            },
            Card.build {
                name = "excavation"
                requiredGold = 1
                choosingResources = setOf(Resource.STONE, Resource.CLAY)
            },
            Card.build {
                name = "clay pit"
                requiredGold = 1
                choosingResources = setOf(Resource.ORE, Resource.CLAY)
            },
            Card.build {
                name = "timber yard"
                requiredGold = 1
                choosingResources = setOf(Resource.WOOD, Resource.STONE)
            },
            Card.build {
                name = "forest cave"
                requiredGold = 1
                choosingResources = setOf(Resource.WOOD, Resource.ORE)
            },
            Card.build {
                name = "mine"
                requiredGold = 1
                choosingResources = setOf(Resource.STONE, Resource.ORE)
            },
            // advanced resources
            Card.build {
                name = "loom"
                singleResource = Resource.TEXTILE
            },
            Card.build {
                name = "glassworks"
                singleResource = Resource.GLASS
            },
            Card.build {
                name = "press"
                singleResource = Resource.PAPYRUS
            },
            // victory points
            Card.build {
                name = "pawnshop"
                victoryPoints = 3
            },
            Card.build {
                name = "baths"
                victoryPoints = 3
                requiredResources = mapOf(Pair(Resource.STONE, 1))
            },
            Card.build {
                name = "altar"
                victoryPoints = 2
            },
            Card.build {
                name = "theater"
                victoryPoints = 2
            },
            // military
            Card.build {
                name = "stockade"
                requiredResources = mapOf(Pair(Resource.WOOD, 1))
                militaryPoints = 1
            },
            Card.build {
                name = "barracks"
                requiredResources = mapOf(Pair(Resource.ORE, 1))
                militaryPoints = 1
            },
            Card.build {
                name = "guard tower"
                requiredResources = mapOf(Pair(Resource.CLAY, 1))
                militaryPoints = 1
            },
            // science
            Card.build {
                name = "apothecary"
                requiredResources = mapOf(Pair(Resource.TEXTILE, 1))
            }
    )
}

