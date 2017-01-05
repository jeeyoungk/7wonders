package jee

import java.util.*

/**
 * TODO - initialization sequence.
// playing a card means:
// choose 1 card
// - build
// - discard
// - wonder
// for build/wonder:
// - can afford?
// - if can't afford, then can trade? with who? (multiple possibilities).
 */
class Engine(players: Int, val allCards: List<Card>) {
    val players: List<Player> = ArrayList((1..players).map { Player() })
    val numPlayers: Int get() {
        return players.size
    }

    val hands: List<List<Card>> = listOf();
    /** current age of the game age 1 -> 2 -> 3 */
    var age = 1
        private set
    /** turn 1 -> 2 -> 3 ... -> 6 -> 7 (babylon) */
    var turn = 1
        private set
    var state: EngineState = EngineState.UNINITIALIZED
        private set

    enum class EngineState {
        UNINITIALIZED,
        INITIALIZED,
        NEW_AGE,
        END_OF_AGE
    }

    fun initialize() {
        state = EngineState.INITIALIZED
    }

    fun doNewAge() {
        val presentCards = obtainCards(age)
        // shuffle cards.
    }

    fun obtainCards(age: Int): List<Card> {
        val presentCards: MutableList<Card> = mutableListOf()
        for (card in allCards.filter { c -> c.age == age }) {
            for (appear in card.appears) {
                if (appear <= numPlayers) {
                    presentCards.add(card)
                }
            }
        }
        return presentCards
    }
}

/**
 * Represents state for a given player.
 */
class Player {
    var gold: Int = 0
    /** List of built cards + wonders. */
    var built: List<Card> = listOf()
    /** List of accumulated military tokens. */
    var militaryTokens: List<MilitaryToken> = listOf()

    /** returns # of military points available for a player. */
    val militaryPoints: Int get() {
        return built.map(Card::militaryPoints).sum()
    }

    /** returns # of military victory points available for a player. */
    val militaryVictoryPoints: Int get() {
        return militaryTokens.map(MilitaryToken::point).sum()
    }

    val improvementVictoryPoints: Int get() {
        return built.map(Card::victoryPoints).sum()
    }

    val moneyVictoryPoints: Int get() {
        return gold / 3
    }

    val scienceVictoryPoints: Int get() {
        return scoreScience(
                built.filter { t -> t.science == Science.GEAR }.count(),
                built.filter { t -> t.science == Science.COMPASS }.count(),
                built.filter { t -> t.science == Science.TABLET }.count(),
                built.filter { t -> t.science == Science.WILDCARD }.count()
        )
    }

    val totalVictoryPoints: Int get() {
        return militaryVictoryPoints + improvementVictoryPoints + moneyVictoryPoints + scienceVictoryPoints
    }
}

/**
 * Represents an agent playing the game.
 */
interface Agent {

}

class PlayerAgent : Agent {

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
    val e = Engine(4, CARDS)
    e.doNewAge()
}