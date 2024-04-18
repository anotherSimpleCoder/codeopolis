package de.htwsaar.esch.Codeopolis.DomainModel.Harvest;

import java.io.Serializable;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;

/**
 * The MilletHarvest class represents a harvest of millet, a type of grain.
 * It extends the abstract Harvest class and provides specific implementation
 * for decay and grain type retrieval.
 */
public class MilletHarvest extends Harvest implements Serializable{

    /**
     * Constructs a MilletHarvest object with the specified amount of millet harvested
     * and the year in which the harvest occurred.
     *
     * @param bushels The amount of millet harvested.
     * @param year The year in which the harvest occurred.
     */
	protected MilletHarvest(int bushels, int year) {
        super(bushels, year);
    }
    

    /**
     * Simulates the decay of millet grain in the harvest over time.
     * Millet has a shelf life of 4 years, after which it starts decaying.
     * The decay percentage increases by 2% each year after the shelf life.
     *
     * @param currentYear The current year for calculating decay.
     * @return The amount of millet that decayed in this cycle.
     */
    @Override
    public int decay(int currentYear) {
        if (currentYear > this.getYear() + 4) {
            int yearsOfDecay = currentYear - this.getYear() - 4;
            double decayPercentage = 0.01;
            for (int i = 1; i < yearsOfDecay; i++)
                decayPercentage += 0.02;

            int decayedAmount = (int) (this.getAmount() * decayPercentage);
            this.remove(decayedAmount);
            return decayedAmount;
        } else {
            return 0;
        }
    }

    /**
     * Retrieves the type of grain stored in the harvest, which is millet.
     *
     * @return An integer representing the grain type (MILLET).
     */
    @Override
    public Game.GrainType getGrainType() {
    	return Game.GrainType.MILLET;
    }
}

