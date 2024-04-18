package de.htwsaar.esch.Codeopolis.Exceptions;

/**
 * Represents an exception related to land operations (buying or selling land) in the game.
 * This exception is thrown when an operation involving land cannot be completed due to various reasons such as
 * insufficient resources, invalid acreage, or game state restrictions.
 */
public class LandOperationException extends GameException {

    /**
     * Constructs a LandOperationException with the specified detail message.
     *
     * @param message the detail message explaining the error occurred during the land operation.
     */
    public LandOperationException(String message) {
        super(message);
    }

    /**
     * Constructs a LandOperationException with the specified detail message and cause.
     *
     * @param message the detail message explaining the error occurred during the land operation.
     * @param cause the cause of the exception, for exception chaining.
     */
    public LandOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
