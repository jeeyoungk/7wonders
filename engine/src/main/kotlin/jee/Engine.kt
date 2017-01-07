package jee

import com.sun.org.apache.xpath.internal.operations.Bool
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
- implement game log.
 */
class Engine(players: Int,
             private val allCards: List<Card>,
             private val agents: List<Agent>,
             seed: Long) : ExposedWorld {
    val random = java.util.Random(seed)
    val players: List<Player> = ArrayList((0..players - 1).map(::Player))
    val numPlayers: Int get() = players.size

    init {
        if (agents.size != players) {
            throw IllegalArgumentException("# of agents must equal # of players.")
        }
    }

    var hands: List<List<Card>> = mutableListOf()
        private set

    /** current age of the game age 1 -> 2 -> 3 */
    override var age = 0
        private set
    /** turn 1 -> 2 -> 3 ... -> 6 -> 7 (babylon) */
    override var turn = 1
        private set
    var state: EngineState = EngineState.UNINITIALIZED
        private set

    enum class EngineState {
        UNINITIALIZED,
        INITIALIZED,
        IN_GAME,
        END_OF_AGE
    }

    fun initialize() {
        state = EngineState.INITIALIZED
    }

    fun doNewAge() {
        assert(state == EngineState.INITIALIZED || state == EngineState.END_OF_AGE, { "expected initialized state." })
        assert(age > 0 && age < 3, { "age must be in [0, 3)" })
        age++
        turn = 0
        val presentCards = obtainCards(age)
        Collections.shuffle(presentCards, random)
        hands = prepareHands(presentCards)
        state = EngineState.IN_GAME
    }

    fun playRound() {
        // give cards to players.
        val moves: List<Action> = players.indices.map {
            val player = players[it]
            val agent = agents[it]
            // val move = agent.play(this)
            // validate the move.
            val scopedGame = AgentScopedGameImpl(this, player, hands[it], it)
            val action = agent.play(scopedGame)
            validateAction(action, player)
            action
        }

        moves.map {
            // apply the move.
        }

        // calculate the effect of cards.

        // shift the cards.
        shiftHands()
    }

    fun endAge() {
        // calculate military.
        assert(state == EngineState.IN_GAME, { "expected in game state." })
        interactAll { current, enemy ->
            if (current.militaryPoints > enemy.militaryPoints) {
                // todo - per age military.
                current.militaryTokens.add(MilitaryToken(1))
            } else if (current.militaryPoints < enemy.militaryPoints) {
                current.militaryTokens.add(MilitaryToken(-1))
            }
        }
        state = EngineState.END_OF_AGE
    }

    /**
     * helper function to interact between players.
     */
    private fun interactAll(interaction: (Player, Player) -> Unit) {
        for (pos in players.indices) {
            val curPlayer = players[pos]
            val left = leftPlayer(pos)
            val right = rightPlayer(pos)
            interaction(curPlayer, left)
            interaction(curPlayer, right)
        }
    }

    private fun validateAction(action: Action, player: Player): Boolean {
        // card is in the player's hand.
        if (!hands[player.position].contains(action.card)) {
            return false
        }
        for (usage in action.usages) {
            val fromPlayer = players[usage.fromPlayer]
            if (usage.fromPlayer == player.position) {
                if (!fromPlayer.built.contains(usage.fromCard)) {
                    return false
                }
                // validate the resource
                // validate the resource
            } else if (isAdjacent(player.position, usage.fromPlayer)) {

            } else {
                // usage is from wrong player.
            }
        }
        // check for other things.
        return true
    }

    private fun leftPlayer(position: Int): Player = players[(position - 1) % numPlayers]

    private fun rightPlayer(position: Int): Player = players[(position + 1) % numPlayers]

    private fun shiftHands() {
        if (age == 1 || age == 3) {
            hands = players.indices.map {
                hands[(it + 1) % numPlayers]
            }
        } else if (age == 2) {
            hands = players.indices.map {
                hands[(it - 1) % numPlayers]
            }
        } else {
            throw RuntimeException("Unsupported age.")
        }
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

    internal fun isAdjacent(positionA: Int, positionB: Int): Boolean {
        val delta = ((positionA - positionB + numPlayers) % numPlayers)
        return delta == 1 || delta == numPlayers - 1
    }

    internal fun prepareHands(presentCards: List<Card>): List<List<Card>> {
        val tmpHands = ArrayList((1..numPlayers).map { mutableListOf<Card>() })
        for ((i, card) in presentCards.withIndex()) {
            tmpHands[i % numPlayers].add(card)
        }
        return tmpHands
    }
}


data class AgentScopedGameImpl(
        override val world: ExposedWorld,
        override val player: ExposedPlayer,
        override val hand: List<Card>,
        override val playerIndex: Int) : AgentScopedGame {
}

/**
 * Represents state for a given player.
 */
class Player(override var position: Int) : ExposedPlayer {
    var gold: Int = 0
    /** List of built cards + wonders. */
    val built: MutableList<Card> = mutableListOf()
    /** List of accumulated military tokens. */
    val militaryTokens: MutableList<MilitaryToken> = mutableListOf()

    /** returns # of military points available for a player. */
    val militaryPoints: Int get() {
        return built.map(Card::militaryPoints).sum()
    }

    /** returns # of military victory points available for a player. */
    val militaryVictoryPoints: Int get() = militaryTokens.map(MilitaryToken::point).sum()

    val improvementVictoryPoints: Int get() = built.map(Card::victoryPoints).sum()

    val moneyVictoryPoints: Int get() = gold / 3

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
    val e = Engine(4, CARDS, listOf(PlayerAgent(), PlayerAgent(), PlayerAgent(), PlayerAgent()), 0)
    e.doNewAge()
}