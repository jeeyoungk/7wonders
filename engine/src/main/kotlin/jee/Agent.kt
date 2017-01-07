package jee

enum class ActionType {
    BUY, DISCARD, WONDER
}

/**
 * Game, as known to an agent.
 */
interface AgentScopedGame {
    val world: ExposedWorld
    val player: ExposedPlayer
    /** current hand of cards. */
    val hand: List<Card>
    /** Current index of the player. */
    val playerIndex: Int
}

/**
 * World exposed to agents. It is
 * - read-only view.
 * - does not leak information that a player should not see.
 */
interface ExposedWorld {
    val turn: Int
    val age: Int
}

/**
 * Players exposed to agents without
 */
interface ExposedPlayer {
    val position: Int
}

/**
 * Decision returned by a user.
 */
data class Action(val card: Card, val actionType: ActionType, val usages: List<Usage>) {

}

/**
 * Additional information regarding the decision returned by the user.
 */
data class Usage(
        val resources: List<Resource>,
        val fromPlayer: Int,
        val fromCard: Card) {
}

/**
 * Represents an agent playing the game.
 */
interface Agent {
    fun play(game: AgentScopedGame): Action
}

class PlayerAgent : Agent {
    override fun play(game: AgentScopedGame): Action {
        throw UnsupportedOperationException("not implemented")
    }

}

class RandomAgent : Agent {
    override fun play(game: AgentScopedGame): Action {
        throw UnsupportedOperationException("not implemented")
    }
}