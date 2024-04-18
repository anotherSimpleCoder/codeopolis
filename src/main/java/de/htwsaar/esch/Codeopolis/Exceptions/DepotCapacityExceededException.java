package de.htwsaar.esch.Codeopolis.Exceptions;

/**
 * Exception thrown when an operation attempts to exceed the depot's capacity.
 */
public class DepotCapacityExceededException extends GameException {
    private int depotCapacity;

    /**
     * Constructs a new DepotCapacityExceededException with the specified detail message and the depot capacity.
     *
     * @param message        the detail message.
     * @param depotCapacity  the total capacity of the depot.
     */
    public DepotCapacityExceededException(String message, int depotCapacity) {
        super(message);
        this.depotCapacity = depotCapacity;
    }
    
    /**
     * Constructs a DepotCapacityExceededException with the specified detail message, cause and depot capacity that led to the exception.
     *
     * @param message        the detail message.
     * @param cause          the cause of the exception.
     * @param depotCapacity  the total capacity of the depot.
     */
    public DepotCapacityExceededException(String message, Throwable cause, int depotCapacity) {
        super(message, cause);
        this.depotCapacity = depotCapacity;
    }



    /**
     * Returns the total capacity of the depot.
     *
     * @return the depot capacity.
     */
    public int getDepotCapacity() {
        return depotCapacity;
    }
}
