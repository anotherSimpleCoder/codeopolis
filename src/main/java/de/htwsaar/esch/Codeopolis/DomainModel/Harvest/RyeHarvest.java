package de.htwsaar.esch.Codeopolis.DomainModel.Harvest;

import java.io.Serializable;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;

/**
 * The RyeHarvest class represents a harvest of rye, a type of grain.
 * It extends the abstract Harvest class and provides specific implementation
 * for decay and grain type retrieval.
 */
public class RyeHarvest extends Harvest implements Serializable{

    /**
     * Constructs a RyeHarvest object with the specified amount of rye harvested
     * and the year in which the harvest occurred.
     *
     * @param bushels The amount of rye harvested.
     * @param year The year in which the harvest occurred.
     */
    protected RyeHarvest(int bushels, int year, float growsConditions) {
        super(bushels, year, growsConditions);
    }


    /**
     * Simulates the decay of rye grain in the harvest over time.
     * Rye has a shelf life of 4 years, after which it starts decaying.
     * The decay percentage increases by 0.02 each year after the shelf life.
     *
     * @param currentYear The current year for calculating decay.
     * @return The amount of rye that decayed in this cycle.
     */
    @Override
    public int decay(int currentYear) {
        if (currentYear > this.getYear() + 4) {
            int yearsOfDecay = currentYear - this.getYear() - 4;
            double decayPercentage = 0.01;
            for (int i = 1; i < yearsOfDecay; i++)
                decayPercentage += 0.02;
            decayPercentage = decayPercentage * this.calculateDecayModifier();
            int decayedAmount = (int) (this.getAmount() * decayPercentage);
            this.remove(decayedAmount);
            return decayedAmount;
        } else {
            return 0;
        }
    }

    /**
     * Retrieves the type of grain stored in the harvest, which is rye.
     *
     * @return An integer representing the grain type (RYE).
     */
    @Override
    public Game.GrainType getGrainType() {
        return Game.GrainType.RYE;
    }
}
