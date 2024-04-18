package de.htwsaar.esch.Codeopolis.DomainModel.Harvest;

import java.io.Serializable;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;

/**
 * The WheatHarvest class represents a harvest of wheat, a type of grain.
 * It extends the abstract Harvest class and provides specific implementation
 * for decay and grain type retrieval.
 */
public class WheatHarvest extends Harvest implements Serializable{

    /**
     * Constructs a WheatHarvest object with the specified amount of wheat harvested
     * and the year in which the harvest occurred.
     *
     * @param bushels The amount of wheat harvested.
     * @param year The year in which the harvest occurred.
     */
    protected WheatHarvest(int bushels, int year) {
        super(bushels, year);
    }

    /**
     * Simulates the decay of wheat grain in the harvest over time.
     * Wheat has a shelf life of 1 year, after which it starts decaying.
     * The decay percentage doubles each year after the shelf life.
     *
     * @param currentYear The current year for calculating decay.
     * @return The amount of wheat that decayed in this cycle.
     */
    @Override
    public int decay(int currentYear) {
        if (currentYear > this.getYear() + 1) {
            int yearsOfDecay = currentYear - this.getYear() - 1;
            double decayPercentage = 0.02;
            for (int i = 1; i < yearsOfDecay; i++)
                decayPercentage *= 2;

            int decayedAmount = (int) (this.getAmount() * decayPercentage);
            this.remove(decayedAmount);
            return decayedAmount;
        } else {
            return 0;
        }
    }

    /**
     * Retrieves the type of grain stored in the harvest, which is wheat.
     *
     * @return An integer representing the grain type (WHEAT).
     */
    @Override
    public Game.GrainType getGrainType() {
    	return Game.GrainType.WHEAT;
    }
}
