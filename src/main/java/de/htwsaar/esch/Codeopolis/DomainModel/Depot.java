package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.Silo.Status;
import de.htwsaar.esch.Codeopolis.DomainModel.Game.GrainType;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import de.htwsaar.esch.Codeopolis.Utils.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Depot {
    private LinkedList<Silo> silos;
    
    private class DepotIterator implements Iterator<Status> {
    	private int i;
    	private GrainType type;
    	
    	public DepotIterator(GrainType type) {
    		this.i = 0;
    		this.type = type;
    	}
    	
		@Override
		public boolean hasNext() {
			for(int j = this.i; j < silos.size(); j++) {
				Silo silo = silos.get(j);
				
				if(silo.getGrainType() == this.type || (silo.getGrainType() == null && silo.getFillLevel() == 0)) {
					i = j;
					return true;
				}
			}
			
			return false;
		}

		@Override
		public Status next() throws NoSuchElementException {
			if(!this.hasNext()) {
				throw new NoSuchElementException();
			}
			
			Silo s = silos.get(i);
			this.i++;
			return s.getStatus();
		}
    	
    }

    /**
     * Constructs a Depot object with the specified number of silos and capacity per silo.
     *
     * @param numberOfSilos    The number of silos in the depot.
     * @param capacityPerSilo  The capacity per silo.
     */
    public Depot(int numberOfSilos, int capacityPerSilo) {
        this.silos = new LinkedList<Silo>();
        for (int i = 0; i < numberOfSilos; i++) {
            this.silos.addLast(new Silo(capacityPerSilo));
        }
    }
    
    /**
     * Constructs a Depot object with the specified array of silos.
     * Each silo in the array is deeply copied to ensure that the Depot has its own separate instances.
     *
     * @param silosArray The array of Silo objects to be copied into the depot.
     */
    public Depot(LinkedList<Silo> silos) {
        if (silos == null) {
            this.silos = silos;
        } else {
            this.silos = new LinkedList<Silo>();
            
            for(Silo silo: silos) {
            	this.silos.addLast(silo);
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
        Iterator<Status> iterator = this.getIterator(grainType);
        
        while(iterator.hasNext()) {
        	try {
        		Silo.Status status = iterator.next();
            	totalFillLevel += status.getFillLevel();
        	} catch(Exception e) {
        		continue;
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
    //TODO: Implement the array copy
    public LinkedList<Silo> getSilos() {
        // Create a new array of Silo with the same length as the original
        LinkedList<Silo> silosCopy = new LinkedList<Silo>();
        
        
        for(Silo silo: this.silos) {
        	silosCopy.addLast(silo);
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
    	

    	for(Silo silo: this.silos) {
    		totalBushels += silo.getFillLevel();
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
        Iterator<Status> iterator = this.getIterator(grainType);
        
        while(iterator.hasNext()) {
        	try {
            	Silo.Status siloStatus = iterator.next();
            	totalCapacity += siloStatus.getCapacity();
        	} catch(Exception e) {
        		continue;
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
    		
    		for(Silo silo: this.silos) {
    			silo.emptySilo();
    		}
    		
    		return totalAmountOfBushels;
    	}
    	int partion = amount / this.silos.size();
    	int remainder = amount % this.silos.size();
    	
    	for(Silo silo: this.silos) {
    		if(silo.getFillLevel() < partion) {
    			remainder += partion - silo.getFillLevel();
    			silo.emptySilo();
    		}
    		else {	
    			silo.takeOut(partion);
    		}
    	}
    	
    	
    	int j = 0;
    	while(remainder > 0) {
    		if(this.silos.get(j).getFillLevel() > 0) {
    			this.silos.get(j).takeOut(1);
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
        int newSiloSize = this.silos.size() + numberOfSilos;
        for(int i = silos.size(); i < newSiloSize; i++) {
        	this.silos.addLast(new Silo(capacityPerSilo));
        }
        
        this.takeOut((int)(numberOfSilos * GameConfig.DEPOT_EXPANSION_COST)); //#Issue42
    }

    /**
     * Performs defragmentation on the depot to redistribute grain across silos.
     */
    public void defragment() {
        Harvest[] allHarvests = new Harvest[getTotalHarvestCount()];

        int index = 0;
        for (Silo silo : silos) {
            LinkedList<Harvest> siloHarvests = silo.emptySilo();
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
		
		for(Silo silo: this.silos) {
			totalCapacity += silo.getCapacity();
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
		class DepotVisualizer {
			private int siloCount;
			private LinkedList<String> siloInfos;
			
			public DepotVisualizer() {
				this.siloInfos = new LinkedList<String>();
			}
			
			public void appendSiloInfo(Silo silo) {
				String header = String.format("Silo %d: %s", siloCount, silo.getGrainType() != null ? silo.getGrainType().toString() : "EMPTY");
				String grainAmount = String.format("Amount of Grain %d units", silo.getFillLevel());
				double fillPercentage = (double) silo.getFillLevel() / silo.getCapacity() * 100;
				
				String fillBar = String.format("%s %.2f %& filled", buildBar(fillPercentage), fillPercentage);
				String capacity = String.format("Capacity %d unit", silo.getCapacity());
				
				String result = String.format("%s \n %s \n %s \n %s \n", header, grainAmount, fillBar, capacity);
				siloInfos.addLast(result);
			}
			
			public String visualize() {
				this.siloInfos.sort();
				return String.join("\n", this.siloInfos);
			}
			
			
			private String buildBar(double fillPercentage) {
				StringBuilder builder = new StringBuilder("|");
				
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
		        builder.append("| ");
		        
		        return builder.toString();
			}
		}
		
		DepotVisualizer result = new DepotVisualizer();
		for (Silo silo : silos) {
			result.appendSiloInfo(silo);
		}
		return result.visualize();
	}
	
	public DepotIterator getIterator(GrainType grainType) {
		return this.new DepotIterator(grainType);
	}

}
