package de.htwsaar.esch.Codeopolis.DomainModel.Harvest;

import java.io.Serializable;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;


/**
 * The CornHarvest class represents a harvest of corn, a type of grain.
 * It extends the abstract Harvest class and provides specific implementation
 * for decay and grain type retrieval.
 */
public class CornHarvest extends Harvest implements Serializable{

    /**
     * Constructs a CornHarvest object with the specified amount of corn harvested
     * and the year in which the harvest occurred.
     *
     * @param bushels The amount of corn harvested.
     * @param year The year in which the harvest occurred.
     */
	protected CornHarvest(int bushels, int year) {
        super(bushels, year);
    }
    

    /**
     * Simulates the decay of corn grain in the harvest over time.
     * Corn has a shelf life of 2 years, after which it starts decaying.
     * The decay percentage increases by 3% each year after the shelf life.
     *
     * @param currentYear The current year for calculating decay.
     * @return The amount of corn that decayed in this cycle.
     */
    @Override
    public int decay(int currentYear) {
        if (currentYear > this.getYear() + 2) {
            int yearsOfDecay = currentYear - this.getYear() - 2;
            double decayPercentage = 0.02;
            for (int i = 1; i < yearsOfDecay; i++)
                decayPercentage += 0.03;

            int decayedAmount = (int) (this.getAmount() * decayPercentage);
            this.remove(decayedAmount);
            return decayedAmount;
        } else {
            return 0;
        }
    }

    /**
     * Retrieves the type of grain stored in the harvest, which is corn.
     *
     * @return An integer representing the grain type (CORN).
     */
    @Override
    public Game.GrainType getGrainType() {
    	return Game.GrainType.CORN;
    }
}
