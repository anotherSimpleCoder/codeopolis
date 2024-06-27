package de.htwsaar.esch.Codeopolis.DomainModel;

/**
 * The `GameEntity` class represents a generic entity in the game.
 * It serves as a base class for other game entities and provides a unique identifier for each entity.
 */
public abstract class GameEntity {
    private final String id; // The unique identifier for the entity

    /**
     * Constructs a `GameEntity` object with the specified identifier.
     *
     * @param id The identifier for the entity
     */
    public GameEntity(String id) {
        this.id = id;
    }

    /**
     * Returns the identifier of the entity.
     *
     * @return The identifier of the entity
     */
    public String getId() {
        return id;
    }
}
