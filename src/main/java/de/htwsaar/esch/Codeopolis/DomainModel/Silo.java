package de.htwsaar.esch.Codeopolis.DomainModel;


import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;
import de.htwsaar.esch.Codeopolis.Utils.LinkedList;

import java.io.Serializable;

/**
 * The Silo class represents a storage unit for a specific type of grain.
 */
public class Silo implements Serializable{
    private LinkedList<Harvest> stock;
    private final int capacity;
    private int fillLevel;
    private int stockIndex = -1;
    
    public class Status {
    	private int capacity;
    	private int fillLevel;
    	
    	private Status(int capacity, int fillLevel) {
    		this.capacity = capacity;
    		this.fillLevel = fillLevel;
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
     * @param initialCapacity The initial capacity of the silo.
     */
    public Silo(int capacity) {
        this.capacity = capacity;
        this.stock = new LinkedList<Harvest>();
        this.fillLevel = 0;
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
        this.fillLevel = other.fillLevel;
        this.stockIndex = other.stockIndex;

        this.stock = other.stock;
    }

    /**
     * Stores a harvest in the silo if there is available capacity.
     *
     * @param harvest The harvest to be stored in the silo.
     * @return The amount of grain that could not be stored due to capacity limitations.
     */
    public Harvest store(Harvest harvest) {
    	 // Check if the grain type matches the existing grain in the silo
        if (fillLevel > 0 && stock.get(0).getGrainType() != harvest.getGrainType()) {
            throw new IllegalArgumentException("The grain type of the given Harvest does not match the grain type of the silo");
        }
        
        // Check if there is enough space in the silo
        if (fillLevel >= capacity) {
            return harvest; // The silo is already full, cannot be stored
        }

        
        if(fillLevel < capacity) {
	        // Check if the entire harvest can be stored
	        int remainingCapacity = this.capacity - this.fillLevel;
	        if(harvest.getAmount() <= remainingCapacity) {
	        	this.stockIndex++;
	        	this.stock.addLast(harvest);
	        	this.fillLevel += harvest.getAmount();
	        	return null;
	        }
	        else {
	        	// Split the harvest and store the remaining amount
	            Harvest remainingHarvest = harvest.split(remainingCapacity);
	            this.stockIndex++;
	            this.stock.addLast(remainingHarvest);
	            this.fillLevel += remainingHarvest.getAmount();
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
        if (stockIndex == -1) {
            return null;
        }
        else {
        	LinkedList<Harvest> removedHarvests = new LinkedList<Harvest>();
        	for(Harvest harvest: stock) {
        		removedHarvests.addLast(harvest);
        	}
        	stock.clear();
        	
            stockIndex = -1;
            fillLevel = 0;
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

        for (int i = 0; i <= stockIndex && amount > 0; i++) {
            Harvest currentHarvest = stock.get(i);
            int taken = currentHarvest.remove(amount);
            amount -= taken;
            takenAmount += taken;

            if (currentHarvest.getAmount() == 0) {
                // Remove empty harvest
                for (int j = i; j < stockIndex; j++) {
                    stock.set(stock.get(j + 1), j);
                }
                stock.remove(stockIndex);
                stockIndex--;
                i--; // Check the same index again, as a new harvest may have been moved here
            }
        }
        this.fillLevel -= takenAmount;
        return takenAmount;
    }

    /**
     * Gets the current fill level of the silo.
     *
     * @return The number of harvests currently stored in the silo.
     */
    public int getFillLevel() {
    	return this.fillLevel;
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
        if (fillLevel > 0 && stock.get(0) != null) {
            return stock.get(0).getGrainType();
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
        return this.stockIndex+1;
    }
    
    public Status getStatus() {
		return new Status(this.capacity, this.fillLevel);
	}
    
    /**
     * Simulates the decay of grain in all harvests stored in the silo over time.
     *
     * @param currentYear The current year used to calculate the decay.
     * @return The total amount of grain that decayed in all harvests in the silo.
     */
    public int decay(int currentYear) {
        int totalDecayedAmount = 0;
        
        for(Harvest harvest: stock) {
        	totalDecayedAmount += harvest.decay(currentYear);
        }

        fillLevel -= totalDecayedAmount;
        return totalDecayedAmount;
    }
}

