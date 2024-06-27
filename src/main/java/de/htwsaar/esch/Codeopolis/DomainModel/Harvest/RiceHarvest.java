package de.htwsaar.esch.Codeopolis.DomainModel.Harvest;

import java.io.Serializable;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;

/**
 * The RiceHarvest class represents a harvest of rice, a type of grain.
 * It extends the abstract Harvest class and provides specific implementation
 * for decay and grain type retrieval.
 */
public class RiceHarvest extends Harvest implements Serializable{

    /**
     * Constructs a RiceHarvest object with the specified amount of rice harvested
     * and the year in which the harvest occurred.
     *
     * @param bushels The amount of rice harvested.
     * @param year The year in which the harvest occurred.
     */
    protected RiceHarvest(int bushels, int year, float growsConditions) {
        super(bushels, year, growsConditions);
    }



    /**
     * Simulates the decay of rice grain in the harvest over time.
     * Rice has a shelf life of 1 year, after which it starts decaying.
     * The decay percentage doubles each year after the shelf life.
     *
     * @param currentYear The current year for calculating decay.
     * @return The amount of rice that decayed in this cycle.
     */
    @Override
    public int decay(int currentYear) {
        if (currentYear > this.getYear() + 1) {
            int yearsOfDecay = currentYear - this.getYear() - 1;
            double decayPercentage = 0.02;
            for (int i = 1; i < yearsOfDecay; i++)
                decayPercentage *= 2;
            decayPercentage = decayPercentage * this.calculateDecayModifier();
            int decayedAmount = (int) (this.getAmount() * decayPercentage);
            this.remove(decayedAmount);
            return decayedAmount;
        } else {
            return 0;
        }
    }

    /**
     * Retrieves the type of grain stored in the harvest, which is rice.
     *
     * @return An integer representing the grain type (RICE).
     */
    @Override
    public Game.GrainType getGrainType() {
        return Game.GrainType.RICE;
    }
}
