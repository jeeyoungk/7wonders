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
    STARTING,
    /** Sample card - reserved for tests. */
    SAMPLE
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

fun <T> permute(options: List<Collection<T>>): List<List<T>> {
    return permuteMap(options, { ArrayList(it) })
}

fun <T, S> permuteMap(options: List<Collection<T>>, func: (List<T>) -> S): List<S> {
    val result: MutableList<S> = mutableListOf()
    permuteImpl(options, func, mutableListOf(), result)
    return result
}

fun <T, S> permuteImpl(options: List<Collection<T>>, func: (List<T>) -> S, current: MutableList<T>, result: MutableCollection<S>) {
    if (options.isEmpty()) {
        result.add(func(current))
    } else {
        val option = options.first()
        for (opt in option) {
            current.add(opt)
            permuteImpl(options.subList(1, options.size), func, current, result)
            current.removeAt(current.size - 1)
        }
    }
}

fun <T : Comparable<T>> isListEqual(a: List<T>, b: List<T>): Boolean {
    if (a.size != b.size) {
        return false;
    }
    val sortedA = a.sorted()
    val sortedB = b.sorted()
    for (pos in sortedA.indices) {
        if (sortedA[pos] != sortedB[pos]) {
            return false;
        }
    }
    return true;
}

fun <T : Comparable<T>> isSubList(superlist: List<T>, sublist: List<T>): Boolean {
    if (superlist.size < sublist.size) {
        return false;
    }
    val sortedSuper = superlist.sorted()
    val sortedSub = sublist.sorted()
    var subIdx = 0
    var superIdx = 0
    while (subIdx < sortedSub.size && superIdx < sortedSuper.size) {
        val superVal = sortedSuper[superIdx]
        val subVal = sortedSub[subIdx]
        if (superVal == subVal) {
            subIdx++
            superIdx++
        } else if (superVal < subVal) {
            superIdx++
        } else {
            return false
        }
    }
    return subIdx == sortedSub.size;
}

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
        /** list of resources provided by this building. (CNF form) */
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

    /**
     * Determine whether this card can provide the given set of resources.
     */
    fun canProvide(resources: List<Resource>): Boolean {
        // TODO - maybe i can optimize this?
        // i feel like this problem is equivalent to SAT in general case.
        return permuteMap(providedResources, { isSubList(it, resources) }).any({ it })
    }

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
        /** List of mutually exclusive resources provided by the server. */
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
                providedResources = listOf(setOf(value))
            }

        var choosingResources: Set<Resource>
            get() {
                throw RuntimeException("Not supported")
            }
            set(value) {
                providedResources = listOf(value)
            }
    }
}