package de.htwsaar.esch.Codeopolis.Exceptions;

/**
 * Exception thrown when there are not enough resources to complete an operation.
 * This class is used to signify situations where operations like planting, feeding, or construction
 * cannot proceed due to insufficient resources like bushels, acres, or building materials.
 */
public class InsufficientResourcesException extends GameException {
    private final int required;
    private final int available;

    /**
     * Constructs a new InsufficientResourcesException with the specified detail message.
     * The message should provide a specific explanation of why the resources are deemed insufficient.
     *
     * @param message the detail message, which provides more information about the resource shortfall.
     * @param required  the amount of the resource that was required
     * @param available the amount of the resource that was available
     */
	public InsufficientResourcesException(String message, int required, int available) {
        super(message);
        this.required = required;
        this.available = available;
    }

    /**
     * Constructs a new InsufficientResourcesException with the specified detail message and cause.
     * This constructor is useful for chaining exceptions and providing more context about the error.
     *
     * @param message the detail message, which provides more information about the resource shortfall.
     * @param cause the cause of the exception, typically another exception that led to this condition.
     */
    public InsufficientResourcesException(String message, int required, int available, Throwable cause) {
        super(message, cause);
        this.required = required;
        this.available = available;
    }
    
    /**
     * Gets the amount of the resource that was required.
     *
     * @return the required amount
     */
    public int getRequired() {
        return required;
    }

    /**
     * Gets the amount of the resource that was available.
     *
     * @return the available amount
     */
    public int getAvailable() {
        return available;
    }
}

