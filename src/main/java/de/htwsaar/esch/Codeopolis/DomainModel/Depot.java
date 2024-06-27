package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import de.htwsaar.esch.Codeopolis.Util.LinkedList;
import de.htwsaar.esch.Codeopolis.Util.Iterator;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class Depot {
    private LinkedList<Silo> silos;

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
     * @param silos The LinkedList of Silo objects to be copied into the depot.
     */
    public Depot(LinkedList<Silo> silos) {
        this.silos = new LinkedList<Silo>();
        silos.forEach(silo -> this.silos.addLast(new Silo(silo))); //Übung 6
    }

    /**
     * Retrieves the current fill level of the depot for a specific grain type.
     *
     * @param grainType The grain type for which to retrieve the fill level.
     * @return The total amount of grain stored in the depot for the specified grain type.
     */
    public int getFillLevel(Game.GrainType grainType) {
        int fillLevel = 0;
        DepotIterator iterator = new DepotIterator(grainType);
        while (iterator.hasNext()) {
            Silo.Status status = iterator.next();
            fillLevel += status.getFillLevel();
        }

        return fillLevel;
    }
    
    /**
     * Creates and returns a copy of the silos list.
     * This method creates a new list and populates it with copies of the Silo objects,
     * ensuring that modifications to the returned list do not affect the original silos.
     *
     * @return A copy of the silos list.
     */
    public LinkedList<Silo> getSilos() {
        // Create a new list of Silo with the same length as the original
        LinkedList<Silo> silosCopy = new LinkedList<>();
        this.silos.forEach(silo -> silosCopy.addLast(new Silo(silo))); //Übung 6
        return silosCopy;
    }

    /**
     * Gets the total amount of bushels (grain) stored in the depot.
     *
     * @return The total amount of bushels stored in the depot.
     */
    public int getTotalFillLevel(){
        return (int) this.silos.sum(silo -> silo.getFillLevel()); //Übung 6
        // As method reference: (int) this.silos.sum(Silo::getFillLevel);
        // This is an instance method reference.
    }
    
    /**
     * Retrieves the capacity of the depot for a specific grain type.
     *
     * @param grainType The grain type for which to retrieve the capacity.
     * @return The total capacity of the depot for the specified grain type.
     */
    public int getCapacity(Game.GrainType grainType) {
        return (int) this.silos.sum(silo -> silo.getGrainType() == grainType ? silo.getCapacity() : 0); //Übung 6
        /*
        //Alternatively more verbose:
        this.silos.sum(silo -> {
            if(silo.getGrainType() == grainType)
                return silo.getCapacity();
            return 0;
        });
        */
    }


    /**
     * Stores a harvest in the depot.
     *
     * @param harvest The harvest to be stored in the depot.
     * @return True if the harvest was successfully stored, false otherwise.
     */
    public boolean store(Harvest harvest) {
        Game.GrainType harvestGrainType = harvest.getGrainType();
        Iterator<Silo> iterator = this.silos.filter(s -> s.getGrainType() == harvestGrainType || s.getFillLevel() == 0).iterator();//Übung 6
        Silo silo;
        while(iterator.hasNext()) {
            silo = iterator.next();
           	harvest = silo.store(harvest);
            if(harvest == null) {
                return true;
            }
        }
        defragment();
        iterator = this.silos.filter(s -> s.getGrainType() == harvestGrainType || s.getFillLevel() == 0).iterator(); //Übung 6
        while(iterator.hasNext()) {
            silo = iterator.next();
            harvest = silo.store(harvest);
            if (harvest == null) {
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
        int takenAmount = 0;
        Iterator<Silo> iterator = this.silos.filter(silo -> silo.getGrainType() == grainType).iterator(); //Übung 6
        Silo silo;
        while(iterator.hasNext()) {
            silo = iterator.next();
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
            this.silos.forEach(silo -> silo.emptySilo()); //Übung 6
            // As method reference: this.silos.forEach(Silo::emptySilo);
            // This is an instance method reference.
    		return totalAmountOfBushels;
    	}
    	int partion = amount / this.silos.size();
    	int remainder = amount % this.silos.size();
        Silo current;
        Iterator<Silo> iterator = this.silos.iterator();
        while(iterator.hasNext()) {
            current = iterator.next();
    		if(current.getFillLevel() < partion) {
    			remainder += partion - current.getFillLevel();
                current.emptySilo();
    		}
    		else {
                current.takeOut(partion);
    		}
    	}
    	int j = 0;
    	while(remainder > 0) {
    		if(this.silos.get(j).getFillLevel() > 0) {
    			this.silos.get(j).takeOut(1);
    			remainder--;
    		}
    		j = (j+1)%silos.size();
    	}
    	return amount;
    }

    /**
     * Expands the depot by adding more silos with the specified capacity per silo.
     *
     * @param numberOfSilos    The number of silos to add.
     * @param capacityPerSilo  The capacity per silo.
     * @return true if the expansion was successful, false otherwise
     */
    public boolean expand(int numberOfSilos, int capacityPerSilo) {
        if((capacityPerSilo * numberOfSilos * GameConfig.DEPOT_EXPANSION_COST) > this.getTotalFillLevel())
            return false; //Expansion costs are too high
        for(int i = 0; i<numberOfSilos; i++)
            this.silos.addLast(new Silo(capacityPerSilo));
        this.takeOut((int)(capacityPerSilo*numberOfSilos * GameConfig.DEPOT_EXPANSION_COST)); //#Issue42
        return true;
    }

    /**
     * Performs defragmentation on the depot to redistribute grain across silos.
     */
    public void defragment() {
        LinkedList<Harvest> allHarvests = new LinkedList<>();
        this.silos.forEach(silo -> allHarvests.addAll(silo.emptySilo())); //Übung 6
        // Add all harvests back. Store method takes care that silos are not fragmented.
        allHarvests.forEach(harvest -> this.store(harvest)); //Übung 6
        // As method reference: allHarvests.forEach(this::store);
        // This is a reference to an instance method of the current object.
    }

    /**
     * Retrieves the total count of harvests across all silos.
     *
     * @return The total count of harvests stored in all silos combined.
     */
    private int getTotalHarvestCount() {
        return (int) this.silos.sum(silo -> silo.getHarvestCount()); //Übung 6
        // As method reference: (int) this.silos.sum(Silo::getHarvestCount);
        // This is an instance method reference.
    }


    /**
     * Simulates the decay of grain in the depot over time.
     *
     * @return The total amount of grain that decayed in the depot.
     */
    public int decay(int currentYear) {
        int totalDecayedAmount = 0;
        Iterator<Silo> iterator = this.silos.iterator();
        Silo silo;
        while (iterator.hasNext()) {
            silo = iterator.next();
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
        return this.getTotalFillLevel() >= this.totalCapacity();
	}
	
	/**
	 * Calculates the total capacity of the depot by summing the capacities of all silos.
	 * 
	 * @return The total capacity of the storage system.
	 */
	public int totalCapacity() {
		return (int) this.silos.sum(silo -> silo.getCapacity()); //Übung 6
        // As method reference: (int) this.silos.sum(Silo::getCapacity);
        // This is an instance method reference.
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
            StringBuilder builder = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.00");

            int i=0;

            String visualize() {

                return builder.toString();
            }

            void appendSiloInfo(Silo silo) {
                builder.append("Silo ").append(i + 1).append(": ");

                String grainName = (silo.getGrainType() != null) ? silo.getGrainType().toString() : "EMPTY";
                builder.append(grainName).append("\n");

                int fillLevel = silo.getFillLevel();
                int capacity = silo.getCapacity();
                double fillPercentage = (double) fillLevel / capacity * 100;
                double emptyPercentage = 100 - fillPercentage;

                // Absolute amount of grain
                builder.append("Amount of Grain: ").append(fillLevel).append(" units\n");

                // ASCII-ART representation of the fill level
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
                i++;
            }
        }

        this.silos.sort();
        DepotVisualizer result = new DepotVisualizer();
        this.silos.forEach(silo -> result.appendSiloInfo(silo));//Übung 6
        // As method reference: this.silos.forEach(result::appendSiloInfo);
        // This is an instance method reference to a specific object.
	    return result.visualize();
	}

    /**
     * Provides an iterator over silos of a specific grain type.
     * @param grainType The grain type for the iterator to focus on.
     * @return An iterator over SiloStatus for the specified grain type.
     */
    public Iterator iterator(Game.GrainType grainType) {
        return new DepotIterator(grainType);
    }


    /**
     * Provides an iterator over silos of a specific grain type within the depot.
     * This iterator allows for the traversal of only those silos that store a specified type of grain,
     * returning the status of each through {@link Silo.Status} objects.
     *
     * This class implements the {@link Iterator} interface, enabling sequential access to the silo statuses.
     * It maintains an internal cursor to track the current index of silos being accessed to ensure
     * that only silos matching the specified grain type are considered.
     *
     * Usage Example:
     * <pre>
     * DepotIterator iterator = depot.new DepotIterator(Game.GrainType.WHEAT);
     * while (iterator.hasNext()) {
     *     Silo.SiloStatus status = iterator.next();
     *     // Process status
     * }
     * </pre>
     *
     * @see Silo.Status
     */
    private class DepotIterator implements Iterator {
        private int currentIndex = 0;
        private Game.GrainType grainType;

        /**
         * Constructs an iterator for silos containing a specific type of grain.
         *
         * @param grainType The grain type that this iterator will traverse.
         */
        public DepotIterator(Game.GrainType grainType) {
            this.grainType = grainType;
            advanceIndex();
        }

        /**
         * Advances the cursor to the next silo that contains the specified grain type.
         */
        private void advanceIndex() {
            while(currentIndex < silos.size() && silos.get(currentIndex).getGrainType() != grainType && silos.get(currentIndex).getFillLevel() > 0){
                currentIndex++;
            }
        }

        /**
         * Determines if there are any more silos that contain the specified grain type.
         *
         * @return {@code true} if there are additional silos to be iterated over; {@code false} otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < silos.size();
        }

        /**
         * Returns the status of the next silo containing the specified grain type.
         * This method should only be called if {@code hasNext()} returns {@code true}.
         *
         * @return The {@link Silo.Status} of the next relevant silo.
         * @throws NoSuchElementException if there are no more silos to iterate over.
         */
        @Override
        public Silo.Status next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more silos available of the specified grain type.");
            }
            Silo.Status status = silos.get(currentIndex).getStatus();
            currentIndex++;
            advanceIndex();
            return status;
        }
    }

    /**
     * Filters the silos by grain type.
     *
     * @param grainType The grain type to filter by.
     * @return A new LinkedList containing silos of the specified grain type.
     */
    public LinkedList<Silo> getSilosByGrainType(Game.GrainType grainType) {
        return this.silos.filter(silo -> silo.getGrainType() == grainType);
    }

    /**
     * Filters the silos to get fully filled silos.
     *
     * @return A new LinkedList containing fully filled silos.
     */
    public LinkedList<Silo> getFullSilos() {
        return this.silos.filter(silo -> silo.getFillLevel() == silo.getCapacity());
    }

    /**
     * Converts the details of silos in the depot, including harvest details, with optional filtering and sorting, to a string.
     *
     * @param filter The predicate to filter silos (can be null if no filtering is needed).
     * @param comparator The comparator to sort silos (can be null if no sorting is needed).
     * @return A string representation of the silos.
     */
    public String toString(Predicate<Silo> filter, Comparator<Silo> comparator) {
        StringBuilder builder = new StringBuilder();
        LinkedList<Silo> filteredSilos = (filter != null) ? this.silos.filter(filter) : this.silos;
        if (comparator != null) {
            filteredSilos.sort(comparator);
        }

        filteredSilos.forEach(silo -> builder.append(silo.toString()).append("\n"));

        return builder.toString();
    }

}
