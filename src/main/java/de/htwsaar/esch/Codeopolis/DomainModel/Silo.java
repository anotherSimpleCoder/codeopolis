package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import de.htwsaar.esch.Codeopolis.Util.LinkedList;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * The Silo class represents a storage unit for a specific type of grain.
 */
public class Silo implements Serializable, Comparable<Silo>{
	private Comparator<Harvest> queueComparator = (harvest1, harvest2) -> Float.compare(harvest1.getDurability(), harvest2.getDurability());
	private PriorityQueue<Harvest> stock;
    private final int capacity;


    @Override
    public int compareTo(Silo other) {
        return Integer.compare(this.getFillLevel(), other.getFillLevel());
    }

    public class Status {
        private final int capacity;
        private final int fillLevel;

        private Status() {
            this.capacity = Silo.this.capacity;
            this.fillLevel = Silo.this.getFillLevel();
        }

        public int getCapacity() {
            return capacity;
        }

        public int getFillLevel() {
            return fillLevel;
        }
    }

    /**
     * Constructs a Silo object with the specified initial capacity.
     *
     * @param capacity The initial capacity of the silo.x
     */
    public Silo(int capacity) {
        this.capacity = capacity;
        this.stock = new PriorityQueue<Harvest>(this.queueComparator);
    }
    
    /**
     * Copy constructor for the Silo class.
     * Creates a new Silo object as a deep copy of another Silo object.
     * This constructor is used to ensure that each property of the Silo,
     * including mutable objects, is copied and independent of the original object.
     *
     * @param other The Silo object to copy.
     */
    public Silo(Silo other) {
        this.capacity = other.capacity;

//        this.stock = new LinkedList<>();
//        this.stock = other.getStockCopy();
        
        this.stock = new PriorityQueue<Harvest>(this.queueComparator);
    }

    protected PriorityQueue<Harvest> getStockCopy()
    {
    	PriorityQueue<Harvest> copy = new PriorityQueue<>(this.queueComparator);
    	
        this.stock.forEach(current -> copy.add(Harvest.createHarvest(current))); //Übung 6
        return copy;
    }
    
    /**
     * Copies the stock from another Silo object to this one.
     * This method creates a deep copy of each Harvest object.
     *
     * @param other The Silo object to copy from.
     */
    private void copyStock(PriorityQueue<Harvest> other){
        other.forEach(current -> this.stock.add(Harvest.createHarvest(current))); //Übung 6
    }

    /**
     * Stores a harvest in the silo if there is available capacity.
     *
     * @param harvest The harvest to be stored in the silo.
     * @return The amount of grain that could not be stored due to capacity limitations.
     */
    public Harvest store(Harvest harvest) {
    	 // Check if the grain type matches the existing grain in the silo
        if (getFillLevel() > 0 && stock.peek().getGrainType() != harvest.getGrainType()) {
            throw new IllegalArgumentException("The grain type of the given Harvest does not match the grain type of the silo");
        }
        
        // Check if there is enough space in the silo
        if (getFillLevel() >= capacity) {
            return harvest; // The silo is already full, cannot be stored
        }
        
        if(getFillLevel() < capacity) {
	        // Check if the entire harvest can be stored
	        int remainingCapacity = this.capacity - this.getFillLevel();
	        if(harvest.getAmount() <= remainingCapacity) {
	        	this.stock.add(harvest);
	        	return null;
	        }
	        else {
	        	// Split the harvest and store the remaining amount
	            Harvest remainingHarvest = harvest.split(remainingCapacity);
	            stock.add(remainingHarvest); // Store the remaining harvest in the current depot
	            return harvest; // Return the surplus amount
	        }
        }
        else {
            // Depot is full, return the amount of grain that could not be stored
            return harvest;
        }
    }
    
    /**
     * Empties the silo by removing all stored harvests and returning them.
     * 
     * @return An array containing all the removed harvests from the silo.
     *         If the silo is empty, an empty array is returned.
     */
    public LinkedList<Harvest> emptySilo() {
        if (this.stock.isEmpty()) {
            return null;
        }
        else {
            LinkedList<Harvest> removedHarvests = new LinkedList<Harvest>();
            this.stock.forEach(current -> removedHarvests.addLast(current)); //Übung 6
            // As method reference: this.stock.forEach(removedHarvests::addLast);
            // This is an instance method reference to a specific object.
            stock.clear();
            stock.clear();
            return removedHarvests;
        }
    }



    /**
     * Takes out a specified amount of grain from the silo.
     *
     * @param amount The amount of grain to be taken out.
     * @return The actual amount of grain taken out from the silo.
     */
    public int takeOut(int amount) {
        int takenAmount = 0;
        
        for(Iterator<Harvest> harvestIterator = this.stock.iterator(); harvestIterator.hasNext() && amount > 0;) {
        	Harvest currentHarvest = harvestIterator.next();
        	int taken = currentHarvest.remove(amount);
        	amount -= taken;
        	takenAmount += taken;
        	
        	if(currentHarvest.getAmount() == 0) {
        		harvestIterator.remove();
        	}
        }
        
        return takenAmount;
    }

    /**
     * Gets the current fill level of the silo.
     *
     * @return The number of harvests currently stored in the silo.
     */
    public int getFillLevel() {        
        return this.stock.stream()
        		.map(harvest -> harvest.getAmount())
        		.reduce(0, (accumulator, currentElement) -> accumulator + currentElement);
    }

    /**
     * Gets the capacity of the silo.
     *
     * @return The maximum number of harvests the silo can store.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the grain type stored in the silo.
     *
     * @return A string representation of the grain type.
     */
    public Game.GrainType getGrainType() {
        // Assuming each silo stores only one type of grain, we can retrieve the grain type from the first stored harvest
        if (getFillLevel() > 0 && stock.peek() != null) {
            return stock.peek().getGrainType();
        }
        else {
            return null; 
        }
    }
    
    /**
     * Retrieves the number of harvests currently stored in the silo.
     *
     * @return The number of harvests stored in the silo.
     */
    public int getHarvestCount() {
        return this.stock.size();
    }
    
    /**
     * Simulates the decay of grain in all harvests stored in the silo over time.
     *
     * @param currentYear The current year used to calculate the decay.
     * @return The total amount of grain that decayed in all harvests in the silo.
     */
    public int decay(int currentYear) {
//        int totalDecayedAmount = 0;
//        Iterator<Harvest> iterator = this.stock.iterator();
//        Harvest currentHarvest;
//        while (iterator.hasNext()){
//            currentHarvest = iterator.next();
//            totalDecayedAmount += currentHarvest.decay(currentYear);
//        }
        
        int totalDecayedAmount = this.stock.stream()
        	.map(harvest -> harvest.decay(currentYear))
        	.reduce(0, (accumulator, decayAmount) -> accumulator + decayAmount);
        
        
        this.removeEmptyHarvests();//Übung 6
        return totalDecayedAmount;
    }

    /**
     * Retrieves the status of the silo, including its capacity and fill level.
     *
     * @return A SiloStatus object representing the current state of the silo.
     */
    public Status getStatus() {
        return new Status();
    }


    /**
     * Removes all empty Harvest objects from the silo.
     */
    private void removeEmptyHarvests() {
        stock.removeIf(harvest -> harvest.getAmount() == 0);
        // As method reference: stock.removeIf(Harvest::isEmpty);
        // This is an instance method reference. However, this would require an isEmpty method to exist in the Harvest class.
    }

    /**
     * Returns a string representation of the silo, including its grain type, capacity, fill level, and harvest details.
     *
     * @return A string containing information about the silo, including the grain type, capacity, fill level, and harvest details.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");

        String grainName = (getGrainType() != null) ? getGrainType().toString() : "EMPTY";
        builder.append("Silo:\n");
        builder.append("  Grain Type: ").append(grainName).append("\n");
        builder.append("  Capacity: ").append(getCapacity()).append("\n");
        builder.append("  Fill Level: ").append(getFillLevel()).append("\n");

        // ASCII-ART representation of the fill level
        double fillPercentage = (double) getFillLevel() / getCapacity() * 100;
        int fillBarLength = 20;
        int filledBars = (int) (fillPercentage / 100 * fillBarLength);
        int emptyBars = fillBarLength - filledBars;

        builder.append("  Fill Status: |");
        for (int j = 0; j < filledBars; j++) {
            builder.append("=");
        }
        for (int j = 0; j < emptyBars; j++) {
            builder.append("-");
        }
        builder.append("| ").append(df.format(fillPercentage)).append("% filled\n");

        builder.append("  Harvests:\n");
        Iterator<Harvest> iterator = this.stock.iterator();
        while (iterator.hasNext()) {
            Harvest harvest = iterator.next();
            builder.append("    - Year: ").append(harvest.getYear()).append(", Amount: ").append(harvest.getAmount()).append("\n");
        }

        return builder.toString();
    }
}

