package de.htwsaar.esch.Codeopolis.DomainModel.Harvest;

import java.io.Serializable;
import java.util.Random;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.Game.GrainType;

/**
 * The Harvest class represents the annual harvest, containing information
 * about the amount of grain harvested and the year in which it occurred.
 */
public abstract class Harvest implements Serializable, Comparable<Harvest> {
    private int bushels;
    private final int year;

    private final float durability; //The durability influences the speed of the decay process.


    protected Harvest(int bushels, int year, float growsConditions) {
        this.bushels = bushels;
        this.year = year;
        this.durability = growsConditions;
    }


    /**
     * Creates a new Harvest instance based on the specified grain type, amount, and year.
     * This factory method centralizes the creation of Harvest objects, allowing for easy creation
     * of the correct type of Harvest based on the grain type. It enhances maintainability and
     * consistency across the application by providing a single point of modification for creating
     * Harvest objects.
     *
     * @param type The type of grain for the harvest, represented as a {@link Game.GrainType} enum.
     * @param amount The amount of grain harvested, which should be a positive integer.
     * @param year The year of the harvest, which should be a positive integer representing the year.
     * @return A new instance of a subclass of Harvest that corresponds to the specified grain type.
     * @throws IllegalArgumentException If the specified grain type is not recognized or if the
     *         amount or year is not positive.
     */
    public static Harvest createHarvest(Game.GrainType type, int amount, int year, float growsConditions) {
        switch(type) {
            case BARLEY:
                return new BarleyHarvest(amount, year, growsConditions);
            case CORN:
                return new CornHarvest(amount, year, growsConditions);
            case MILLET:
                return new MilletHarvest(amount, year, growsConditions);
            case RICE:
                return new RiceHarvest(amount, year, growsConditions);
            case RYE:
                return new RyeHarvest(amount, year, growsConditions);
            case WHEAT:
                return new WheatHarvest(amount, year, growsConditions);
            default:
                throw new IllegalArgumentException("Unknown grain type: " + type);
        }
    }

    /**
     * Creates and returns a deep copy of the specified {@link Harvest} object.
     * This method utilizes the factory pattern to generate a new instance of the same type
     * as the provided {@code other} harvest object, copying its state.
     *
     * This approach ensures that all properties specific to the actual type of the harvest,
     * such as grain type and amount, are preserved in the new instance, maintaining the
     * integrity of the harvest data.
     *
     * @param other The {@link Harvest} object to be copied.
     * @return A new {@link Harvest} instance that is a deep copy of the {@code other} object,
     *         maintaining the same type, amount of grain, and year of harvest.
     * @throws IllegalArgumentException if the {@code other} parameter is {@code null} or if
     *         the grain type of {@code other} is not recognized.
     */
    public static Harvest createHarvest(Harvest other) {
        if (other == null) {
            throw new IllegalArgumentException("The provided harvest object cannot be null.");
        }
        return createHarvest(other.getGrainType(), other.getAmount(), other.getYear(), other.getDurability());
    }

    /**
     * Gets the amount of grain harvested in this annual harvest.
     *
     * @return The amount of grain harvested.
     */
    public int getAmount() {
        return bushels;
    }

    /**
     * Gets the year in which the harvest occurred.
     *
     * @return The year of the harvest.
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the durability of the harvest. The durability influences the speed of the decay process.
     *
     * @return The durability of the harvest.
     */
    protected float getDurability() {
        return durability;
    }


    /**
     * Calculates a decay modifier based on the durability.
     * The modifier decreases as durability increases, particularly improving above 0.75.
     *
     * @return A double representing the decay modifier.
     */
    protected double calculateDecayModifier() {
        if (durability > 0.75) {
            // As durability increases above 0.75, decay resistance improves significantly
            return 0.5 + 0.5 * (1.0 - durability);  // Gradual improvement in decay resistance
        } else {
            // Increased decay rate as durability decreases below 0.75
            return 1.5 - 0.857 * durability;  // Gradual increase in decay rate
        }
    }

    /**
     * Removes the specified amount of grain from the harvest.
     *
     * @param amount The amount of grain to be removed.
     * @return The actual amount of grain removed from the harvest.
     */
    public int remove(int amount) {
        if (amount <= this.bushels) {
            this.bushels -= amount;
            return amount;
        } else {
            int removedAmount = this.bushels;
            this.bushels = 0;
            return removedAmount;
        }
    }

    /**
     * Simulates the decay of grain in the harvest over time.
     *
     * @return The amount of grain that decayed in this cycle.
     */
    public abstract int decay(int currentYear);

    /**
     * Retrieves the type of grain stored in the silo.
     *
     * @return An integer representing the type of grain stored.
     */
    public abstract GrainType getGrainType();

    /**
     * Returns a string representation of the harvest.
     *
     * @return A string containing information about the amount and year of the harvest.
     */
    public String toString() {
        return "Harvest{" +
                "bushels=" + bushels +
                ", year=" + year +
                '}';
    }

    /**
     * Splits the specified amount of grain into a new Harvest object,
     * leaving the remaining amount in the current Harvest object.
     *
     * @param amount The amount of grain to be split into a new Harvest object.
     * @return A new Harvest object containing the split amount of grain.
     */
    public Harvest split(int amount) {
        // Ensure the split amount is valid
        if (amount <= 0 || amount >= this.getAmount()) {
            throw new IllegalArgumentException("Invalid split amount");
        }

        // Create a new Harvest object with the split amount of grain
        Harvest newHarvest = Harvest.createHarvest(this.getGrainType(), amount, this.getYear(), this.durability);

        // Deduct the split amount from the current Harvest object
        remove(amount);

        return newHarvest;
    }

    /**
     * Creates and returns a deep copy of this Harvest object.
     *
     * @return A deep copy of this Harvest object.
     */
    public Harvest copy() {
        return Harvest.createHarvest(this);
    }

    @Override
    public int compareTo(Harvest other) {
        // First compare durability. The harvest with higher durability should come first.
        int durabilityComparison = Float.compare(other.durability, this.durability);
        if (durabilityComparison != 0) {
            return durabilityComparison;
        }
        // If durability is the same, compare by year to maintain consistency with equals (if implemented).
        return Integer.compare(this.year, other.year);
    }


}
