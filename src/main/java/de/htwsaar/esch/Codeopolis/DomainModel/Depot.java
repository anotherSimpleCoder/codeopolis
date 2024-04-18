package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import java.text.DecimalFormat;

public class Depot {
    private Silo[] silos;

    /**
     * Constructs a Depot object with the specified number of silos and capacity per silo.
     *
     * @param numberOfSilos    The number of silos in the depot.
     * @param capacityPerSilo  The capacity per silo.
     */
    public Depot(int numberOfSilos, int capacityPerSilo) {
        this.silos = new Silo[numberOfSilos];
        for (int i = 0; i < numberOfSilos; i++) {
            this.silos[i] = new Silo(capacityPerSilo);
        }
    }
    
    /**
     * Constructs a Depot object with the specified array of silos.
     * Each silo in the array is deeply copied to ensure that the Depot has its own separate instances.
     *
     * @param silosArray The array of Silo objects to be copied into the depot.
     */
    public Depot(Silo[] silosArray) {
        if (silosArray == null) {
            this.silos = new Silo[0];
        } else {
            this.silos = new Silo[silosArray.length];
            for (int i = 0; i < silosArray.length; i++) {
                // Assuming Silo has a copy constructor to create a deep copy
                this.silos[i] = new Silo(silosArray[i]);
            }
        }
    }

    /**
     * Retrieves the current fill level of the depot for a specific grain type.
     *
     * @param grainType The grain type for which to retrieve the fill level.
     * @return The total amount of grain stored in the depot for the specified grain type.
     */
    public int getFillLevel(Game.GrainType grainType) {
        int totalFillLevel = 0;
        for (Silo silo : silos) {
            if (silo.getGrainType() == grainType) {
                totalFillLevel += silo.getFillLevel();
            }
        }
        return totalFillLevel;
    }
    
    /**
     * Creates and returns a copy of the silos array.
     * This method creates a new array and populates it with copies of the Silo objects,
     * ensuring that modifications to the returned array do not affect the original silos.
     *
     * @return A copy of the silos array.
     */
    public Silo[] getSilos() {
        // Create a new array of Silo with the same length as the original
        Silo[] silosCopy = new Silo[this.silos.length];
        for (int i = 0; i < this.silos.length; i++) {
            // Assume Silo has a copy constructor to create a deep copy of each Silo object
            silosCopy[i] = new Silo(this.silos[i]);
        }
        return silosCopy;
    }

    /**
     * Gets the total amount of bushels (grain) stored in the depot.
     *
     * @return The total amount of bushels stored in the depot.
     */
    public int getTotalFillLevel(){
    	int totalBushels = 0;

        for (int i = 0; i < this.silos.length; i++) {
            totalBushels += silos[i].getFillLevel();
        }
        return totalBushels;
    }
    
    /**
     * Retrieves the capacity of the depot for a specific grain type.
     *
     * @param grainType The grain type for which to retrieve the capacity.
     * @return The total capacity of the depot for the specified grain type.
     */
    public int getCapacity(Game.GrainType grainType) {
        int totalCapacity = 0;
        for (Silo silo : silos) {
            if (silo.getGrainType() == grainType || silo.getGrainType() == null) {
                totalCapacity += silo.getCapacity();
            }
        }
        return totalCapacity;
    }

    /**
     * Stores a harvest in the depot.
     *
     * @param harvest The harvest to be stored in the depot.
     * @return True if the harvest was successfully stored, false otherwise.
     */
    public boolean store(Harvest harvest) {
        for (Silo silo : silos) {
            if (silo.getGrainType() == harvest.getGrainType() || silo.getFillLevel() == 0) {
            	harvest = silo.store(harvest);
                if(harvest == null) {
                    return true;
                }
            }
        }
        defragment();
        for (Silo silo : silos) {
            if (silo.getGrainType() == harvest.getGrainType() || silo.getFillLevel() == 0) {
            	harvest = silo.store(harvest);
                if(harvest == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Takes out a specified amount of grain from the depot for a specific grain type.
     *
     * @param amount    The amount of grain to be taken out.
     * @param grainType The grain type for which to take out the grain.
     * @return The actual amount of grain taken out from the depot.
     */
    public int takeOut(int amount, Game.GrainType grainType) {
        int takenAmount = 0;
        for (Silo silo : silos) {
            if (silo.getGrainType() == grainType) {
                int amountTaken = silo.takeOut(amount);
                amount -= amountTaken;
                takenAmount += amountTaken;
                if (amount <= 0) {
                    break;
                }
            }
        }
        return takenAmount;
    }
    
    /**
     * Takes out the specified amount of grain from the silo, distributing it evenly among the stored bushels.
     * If the specified amount exceeds the total amount of grain in the silo, all grain is removed and returned.
     * If the specified amount is less than the total amount of grain, the grain is taken out evenly from each bushel,
     * with any remaining grain distributed among the bushels in a round-robin fashion.
     *
     * @param amount The amount of grain to be taken out from the silo.
     * @return The actual amount of grain taken out from the silo.
     */
    public int takeOut(int amount) {
    	if(amount >= this.getTotalFillLevel()){
    		int totalAmountOfBushels =  this.getTotalFillLevel();
    		for(int i = 0; i < this.silos.length; i++) {
    			silos[i].emptySilo();
    		}
    		return totalAmountOfBushels;
    	}
    	int partion = amount / this.silos.length;
    	int remainder = amount % this.silos.length;
    	for(int i = 0; i < this.silos.length; i++) {
    		if(this.silos[i].getFillLevel() < partion) {
    			remainder += partion - this.silos[i].getFillLevel();
    			this.silos[i].emptySilo();
    		}
    		else {	
    			this.silos[i].takeOut(partion);
    		}
    	}
    	int j = 0;
    	while(remainder > 0) {
    		if(this.silos[j].getFillLevel() > 0) {
    			this.silos[j].takeOut(1);
    			remainder--;
    		}
    		j = (j+1)%Game.GrainType.values().length;	
    	}
    	return amount;
    }

    /**
     * Expands the depot by adding more silos with the specified capacity per silo.
     *
     * @param numberOfSilos    The number of silos to add.
     * @param capacityPerSilo  The capacity per silo.
     */
    public void expand(int numberOfSilos, int capacityPerSilo) {
        Silo[] newSilos = new Silo[silos.length + numberOfSilos];
        System.arraycopy(silos, 0, newSilos, 0, silos.length);
        for (int i = silos.length; i < newSilos.length; i++) {
            newSilos[i] = new Silo(capacityPerSilo);
        }
        silos = newSilos;
        this.takeOut((int)(numberOfSilos * GameConfig.DEPOT_EXPANSION_COST)); //#Issue42
    }

    /**
     * Performs defragmentation on the depot to redistribute grain across silos.
     */
    public void defragment() {
        Harvest[] allHarvests = new Harvest[getTotalHarvestCount()];

        int index = 0;
        for (Silo silo : silos) {
            Harvest[] siloHarvests = silo.emptySilo();
            if(siloHarvests != null) {
	            for (Harvest harvest : siloHarvests) {
	                allHarvests[index++] = harvest;
	            }
            }
        }

        // Add all harvests back. Store method takes care that silos are not fragmented. 
        for (Harvest harvest : allHarvests) {
            if (harvest != null) {
                store(harvest);
            }
        }
    }

    /**
     * Retrieves the total count of harvests across all silos.
     *
     * @return The total count of harvests stored in all silos combined.
     */
    private int getTotalHarvestCount() {
        int totalCount = 0;
        for (Silo silo : silos) {
            totalCount += silo.getHarvestCount();
        }
        return totalCount;
    }


    /**
     * Simulates the decay of grain in the depot over time.
     *
     * @return The total amount of grain that decayed in the depot.
     */
    public int decay(int currentYear) {
        int totalDecayedAmount = 0;
        for (Silo silo : silos) {
            totalDecayedAmount += silo.decay(currentYear);
        }
        return totalDecayedAmount;
    }


    /**
     * Checks if the depot is fully occupied with grain.
     * 
     * @return {@code true} if the total fill level of all silos equals or exceeds the total capacity of the storage system, {@code false} otherwise.
     */
	public boolean full() {
		if(this.getTotalFillLevel()>=this.totalCapacity())
			return true;
		return false;
	}
	
	/**
	 * Calculates the total capacity of the depot by summing the capacities of all silos.
	 * 
	 * @return The total capacity of the storage system.
	 */
	public int totalCapacity() {
		int totalCapacity = 0;
		for(int i=0; i<this.silos.length; i++) {
			totalCapacity += this.silos[i].getCapacity();
		}
		return totalCapacity;
	}

	/**
	 * Retrieves the total amount of grain categorized by grain type.
	 * 
	 * @return An array containing the total amount of grain for each grain type, indexed by the grain type constants defined in the {@code GameConfig} class.
	 */
	public int[] getBushelsCategorizedByGrainType() {
	    int[] result = new int[Game.GrainType.values().length];
	    for(Game.GrainType grainType : Game.GrainType.values()) {
	        result[grainType.ordinal()] = getFillLevel(grainType);
	    }
	    return result;
	}
	
	


	/**
	 * Returns a string representation of the depot, including information about each silo's grain type, fill level, capacity, and absolute amount of grain.
	 *
	 * @return A string containing information about the depot, including each silo's grain type, fill level, capacity, and absolute amount of grain.
	 */
	@Override
	public String toString() {
	    StringBuilder builder = new StringBuilder("");
	    DecimalFormat df = new DecimalFormat("0.00");

	    for (int i = 0; i < silos.length; i++) {
	        builder.append("Silo ").append(i + 1).append(": ");

	        String grainName;
	        
	        if(silos[i].getGrainType() != null)
	        	grainName = silos[i].getGrainType().toString();
	        else
	        	grainName = "EMPTY";
	        
	        builder.append(grainName).append("\n");

	        int fillLevel = silos[i].getFillLevel();
	        int capacity = silos[i].getCapacity();

	        double fillPercentage = (double) fillLevel / capacity * 100;
	        double emptyPercentage = 100 - fillPercentage;

	        // Absolute amount of grain
	        builder.append("Amount of Grain: ").append(fillLevel).append(" units\n");

	        // ASCI-ART representation of the fill level
	        int fillBarLength = 20;
	        int filledBars = (int) (fillPercentage / 100 * fillBarLength);
	        int emptyBars = fillBarLength - filledBars;

	        builder.append("|");
	        for (int j = 0; j < filledBars; j++) {
	            builder.append("=");
	        }
	        for (int j = 0; j < emptyBars; j++) {
	            builder.append("-");
	        }
	        builder.append("| ").append(df.format(fillPercentage)).append("% filled\n");

	        builder.append("Capacity: ").append(capacity).append(" units\n\n");
	    }

	    return builder.toString();
	}

}
