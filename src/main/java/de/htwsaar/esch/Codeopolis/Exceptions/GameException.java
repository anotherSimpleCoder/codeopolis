package de.htwsaar.esch.Codeopolis.Exceptions;

/**
 * A general exception class for the game, used as a base class for more specific game-related exceptions.
 * This class extends {@link RuntimeException} and provides constructors to create exception instances
 * with error messages and causes.
 */
public class GameException extends RuntimeException {

    /**
     * Constructs a new GameException with the specified detail message.
     * The message explains the error that caused the exception.
     *
     * @param message the detail message, which provides more information about the reason for the exception.
     */
    public GameException(String message) {
        super(message);
    }

    /**
     * Constructs a new GameException with the specified detail message and cause.
     * This constructor allows chaining exceptions by providing an underlying cause.
     *
     * @param message the detail message, which provides more information about the reason for the exception.
     * @param cause the cause of the exception, allowing exception chaining.
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}