package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.Silo.Status;
import de.htwsaar.esch.Codeopolis.DomainModel.Game.GrainType;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import de.htwsaar.esch.Codeopolis.Utils.DepotVisualizer;
import de.htwsaar.esch.Codeopolis.Utils.LinkedList;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Depot {
    private LinkedList<Silo> silos;
    
    private class DepotIterator implements Iterator<Status> {
    	private int index;
    	private GrainType type;
    	
    	public DepotIterator(GrainType type) {
    		this.index = 0;
    		this.type = type;
    	}
    	
		@Override
		public boolean hasNext() {
			for(int currentPosition = this.index; currentPosition < silos.size(); currentPosition++) {
				Silo silo = silos.get(currentPosition);
				
				if(silo.getGrainType() == this.type || (silo.getGrainType() == null && silo.getFillLevel() == 0)) {
					index = currentPosition;
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
			
			Silo currentSilo = silos.get(index);
			this.index++;
			return currentSilo.getStatus();
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
        for (int index = 0; index < numberOfSilos; index++) {
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
            
            silos.forEach(this.silos::addLast);
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
        
        this.silos.forEach(silosCopy::addLast);
        

        return silosCopy;
    }

    /**
     * Gets the total amount of bushels (grain) stored in the depot.
     *
     * @return The total amount of bushels stored in the depot.
     */
    public int getTotalFillLevel(){    	
    	return (int)this.silos.sum((silo)-> (double)silo.getFillLevel());
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
    	final GrainType harvestGrainType = harvest.getGrainType();
    	LinkedList<Silo> silosOfGrainType = this.silos.filter((silo)-> silo.getGrainType() == harvestGrainType || silo.getFillLevel() == 0);
    	
        for (Silo silo : silosOfGrainType) {
        	harvest = silo.store(harvest);
            if(harvest == null) {
                return true;
            }
        }
        defragment();
        for (Silo silo : silosOfGrainType) {
        	harvest = silo.store(harvest);
            if(harvest == null) {
                return true;
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
        LinkedList<Silo> listOfGrainType = this.silos.filter((silo)->silo.getGrainType() == grainType);
    	
    	int takenAmount = 0;
        for (Silo silo : listOfGrainType) {
            int amountTaken = silo.takeOut(amount);
            amount -= amountTaken;
            takenAmount += amountTaken;
            if (amount <= 0) {
                break;
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
    		
    		this.silos.forEach(Silo::emptySilo);
    		
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
    	
    	int siloPosition = 0;
    	while(remainder > 0) {
    		if(this.silos.get(siloPosition).getFillLevel() > 0) {
    			this.silos.get(siloPosition).takeOut(1);
    			remainder--;
    		}
    		siloPosition = (siloPosition+1)%Game.GrainType.values().length;	
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
            siloHarvests = siloHarvests.filter((harvest)-> harvest != null);
            
        	for (Harvest harvest : siloHarvests) {
                allHarvests[index++] = harvest;
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
		DepotVisualizer visualizer = new DepotVisualizer();
		this.silos.forEach(visualizer::appendSiloInfo);
		
		
		return visualizer.visualize();
	}
	
	
	
	public String toString(Predicate<Silo> predicateToPrint, Comparator<Silo> sortComparator) {
		DepotVisualizer visualizer = new DepotVisualizer();
		LinkedList<Silo> matchedSilos = this.silos.filter(predicateToPrint);
		matchedSilos.sort(sortComparator);
		
		matchedSilos.forEach(visualizer::appendSiloInfo);
		return visualizer.visualize();
	}
	
	public DepotIterator getIterator(GrainType grainType) {
		return this.new DepotIterator(grainType);
	}

}
