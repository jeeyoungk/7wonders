package jee

/**
 * List of all cards
 */
val CARDS: List<Card> = arrayListOf(
        // basic resources
        Card.build {
            name = "lumber yard"
            singleResource = Resource.WOOD
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(3, 4)
        },
        Card.build {
            name = "stone pit"
            singleResource = Resource.STONE
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(3, 5)
        },
        Card.build {
            name = "clay pool"
            singleResource = Resource.CLAY
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(3, 5)
        },
        Card.build {
            name = "ore vein"
            singleResource = Resource.ORE
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(3, 4)
        },
        // basic choosing resources
        Card.build {
            name = "tree farm"
            requiredGold = 1
            choosingResources = setOf(Resource.WOOD, Resource.CLAY)
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(6)
        },
        Card.build {
            name = "excavation"
            requiredGold = 1
            choosingResources = setOf(Resource.STONE, Resource.CLAY)
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(4)
        },
        Card.build {
            name = "clay pit"
            requiredGold = 1
            choosingResources = setOf(Resource.ORE, Resource.CLAY)
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(3)
        },
        Card.build {
            name = "timber yard"
            requiredGold = 1
            choosingResources = setOf(Resource.WOOD, Resource.STONE)
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(3)
        },
        Card.build {
            name = "forest cave"
            requiredGold = 1
            choosingResources = setOf(Resource.WOOD, Resource.ORE)
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(5)
        },
        Card.build {
            name = "mine"
            requiredGold = 1
            choosingResources = setOf(Resource.STONE, Resource.ORE)
            cardType = CardType.BASIC_RESOURCE
            age = 1
            appears = listOf(6)
        },
        // advanced resources
        Card.build {
            name = "loom"
            singleResource = Resource.TEXTILE
            cardType = CardType.ADVANCED_RESOURCE
            age = 1
            appears = listOf(3, 6)
        },
        Card.build {
            name = "glassworks"
            singleResource = Resource.GLASS
            cardType = CardType.ADVANCED_RESOURCE
            age = 1
            appears = listOf(3, 6)
        },
        Card.build {
            name = "press"
            singleResource = Resource.PAPYRUS
            cardType = CardType.ADVANCED_RESOURCE
            age = 1
            appears = listOf(3, 6)
        },
        // trade cards - TODO - implement them.
        Card.build {
            name = "tavern"
            cardType = CardType.TRADE
            appears = listOf(4, 5, 7)
            age = 1
        },
        Card.build {
            name = "east trading port"
            cardType = CardType.TRADE
            appears = listOf(3, 7)
            age = 1
        },
        Card.build {
            name = "west trading port"
            cardType = CardType.TRADE
            appears = listOf(3, 7)
            age = 1
        },
        Card.build {
            name = "marketplace"
            cardType = CardType.TRADE
            appears = listOf(3, 6)
            age = 1
        },
        // victory points
        Card.build {
            name = "pawnshop"
            victoryPoints = 3
            cardType = CardType.IMPROVEMENTS
            age = 1
            appears = listOf(4, 7)
        },
        Card.build {
            name = "baths"
            victoryPoints = 3
            requiredResources = mapOf(Pair(Resource.STONE, 1))
            cardType = CardType.IMPROVEMENTS
            age = 1
            appears = listOf(3, 7)
        },
        Card.build {
            name = "altar"
            victoryPoints = 2
            cardType = CardType.IMPROVEMENTS
            age = 1
            appears = listOf(3, 5)
        },
        Card.build {
            name = "theater"
            victoryPoints = 2
            cardType = CardType.IMPROVEMENTS
            age = 1
            appears = listOf(3, 6)
        },
        // military
        Card.build {
            name = "stockade"
            requiredResources = mapOf(Pair(Resource.WOOD, 1))
            militaryPoints = 1
            cardType = CardType.MILITARY
            age = 1
            appears = listOf(3, 7)
        },
        Card.build {
            name = "barracks"
            requiredResources = mapOf(Pair(Resource.ORE, 1))
            militaryPoints = 1
            cardType = CardType.MILITARY
            age = 1
            appears = listOf(3, 5)
        },
        Card.build {
            name = "guard tower"
            requiredResources = mapOf(Pair(Resource.CLAY, 1))
            militaryPoints = 1
            cardType = CardType.MILITARY
            age = 1
            appears = listOf(3, 4)
        },
        // science
        Card.build {
            name = "apothecary"
            requiredResources = mapOf(Pair(Resource.TEXTILE, 1))
            science = Science.COMPASS
            cardType = CardType.SCIENCE
            age = 1
            appears = listOf(3, 5)
        },
        Card.build {
            name = "workshop"
            requiredResources = mapOf(Pair(Resource.TEXTILE, 1))
            science = Science.GEAR
            cardType = CardType.SCIENCE
            age = 1
            appears = listOf(3, 7)
        },
        Card.build {
            name = "scriptorium"
            requiredResources = mapOf(Pair(Resource.TEXTILE, 1))
            science = Science.TABLET
            cardType = CardType.SCIENCE
            age = 1
            appears = listOf(3, 4)
        }
)