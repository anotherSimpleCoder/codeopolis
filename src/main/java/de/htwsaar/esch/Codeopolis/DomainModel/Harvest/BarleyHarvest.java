package de.htwsaar.esch.Codeopolis.DomainModel.Harvest;

import java.io.Serializable;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;

/**
 * The BarleyHarvest class represents a harvest of barley, a type of grain.
 * It extends the abstract Harvest class and provides specific implementation
 * for decay and grain type retrieval.
 */
public class BarleyHarvest extends Harvest implements Serializable{

    /**
     * Constructs a BarleyHarvest object with the specified amount of barley harvested
     * and the year in which the harvest occurred.
     *
     * @param bushels The amount of barley harvested.
     * @param year The year in which the harvest occurred.
     */
    protected BarleyHarvest(int bushels, int year) {
        super(bushels, year);
    }
    


    
    
    /**
     * Simulates the decay of barley grain in the harvest over time.
     * Barley has a shelf life of 2 years, after which it starts decaying.
     * The decay percentage increases by 3% each year after the shelf life.
     *
     * @param currentYear The current year for calculating decay.
     * @return The amount of barley that decayed in this cycle.
     */
    @Override
    public int decay(int currentYear) {
        if (currentYear > this.getYear()+2) {
            int yearsOfDecay = currentYear - this.getYear() - 2;
            double decayPercentage = 0.02;
            for(int i = 1; i < yearsOfDecay; i++)
                decayPercentage += 0.03;

            int decayedAmount = (int) (this.getAmount() * decayPercentage);
            this.remove(decayedAmount);
            return decayedAmount;
        } else {
            return 0;
        }
    }

    /**
     * Retrieves the type of grain stored in the harvest, which is barley.
     *
     * @return An integer representing the grain type (BARLEY).
     */
    @Override
    public Game.GrainType getGrainType() {
    	return Game.GrainType.BARLEY;
    }
}
