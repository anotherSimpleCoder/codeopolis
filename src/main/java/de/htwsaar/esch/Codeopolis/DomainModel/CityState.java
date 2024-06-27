package de.htwsaar.esch.Codeopolis.DomainModel;

import de.htwsaar.esch.Codeopolis.Util.LinkedList;
import de.htwsaar.esch.Codeopolis.Util.Iterator;

import java.io.Serializable;

/**
 * Represents the state of a city.
 */
public class CityState implements Serializable{
	private final int residents;
	private final int[] bushels;
	private final int acres;
	private final int year;
	private final String name;
	private final String id;
	private final LinkedList<Silo> silos;
	private final int freeStorage;
	
	/**
     * Constructs a new CityState object with the specified residents, bushels, and acres.
     * 
     * @param name      The name of the city.
     * @param id        The unique identifier of the city.
     * @param residents The number of residents in the city.
     * @param bushels   The stockpile of bushels in the city, divided by grain type.
     * @param acres     The amount of land area of the city.
     * @param year      The current year of the city state.
     * @param freeStorage The number of free storage. 
     * @param silos     The array of silos representing the city's grain storage facilities.
     */
	public CityState(String name, String id, int residents, int[] bushels, int acres, int year, int freeStorage, LinkedList<Silo> silos) {
		this.name = name;
		this.id = id;
		this.residents = residents;
		this.bushels = bushels;
		this.acres = acres;
		this.year = year;
		this.freeStorage = freeStorage;
		this.silos = silos;
	}

	/**
     * Returns the number of residents in the city.
     * 
     * @return The number of residents.
     */
	public int getResidents() {
		return residents;
	}
	
	
    /**
     * Returns the unique identifier of the city.
     *
     * @return The unique identifier of the city.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the number of free storage. 
     * @return The number of free storage. 
     */
	public int getFreeStorage() {
		return this.freeStorage;
	}
	
    /**
     * Returns the silos in the city.
     *
     * @return The array of silos.
     */
    public LinkedList<Silo> getSilos() {
        return silos;
    }

    /**
     * Returns the total number of bushels in the city.
     *
     * @return The total number of bushels.
     */
	public int getTotalAmountOfBushels() {
		int total = 0;
	    for (Game.GrainType grainType : Game.GrainType.values()) {
	        total += this.bushels[grainType.ordinal()];
	    }
	    return total;
	}
	
    /**
     * Returns the number of bushels of a specific grain type in the city.
     *
     * @param grainType The type of grain.
     * @return The number of bushels of the specified grain type.
     */
	public int getBushels(Game.GrainType grainType) {
		return bushels[grainType.ordinal()];
	}

	/**
     * Returns the number of bushels in the city.
     * 
     * @return The number of bushels.
     */
	public int[] getBushels() {
		return bushels;
	}

	/**
     * Returns the number of acres in the city.
     * 
     * @return The number of acres.
     */
	public int getAcres() {
		return acres;
	}

	/**
	 * Returns the year of the city
	 *
	 * @return The year of the city.
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Returns the name of the city
	 *
	 * @return The name of the city.
	 */
	public String getName() {
		return name;
	}
	


	/**
	 * Returns a string representation of the CityState object.
	 *
	 * @return A string representation of the CityState object.
	 */
	@Override
	public String toString() {
	    return "CityState{" +
	            "name=" + name +
	            ", year=" + year +
	            ", bushels=" + bushels +
	            ", acres=" + acres +
	            ", residents='" + residents + '\'' +
	            '}';
	}

    /**
     * Indicates whether some other object is equal to this one by means of value equality.
     *
     * @param obj The reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		LinkedList<Silo> other = ((CityState) obj).getSilos();
		if (this.silos.size() != other.size()) {
			return false;
		}
		Iterator<Silo> it1 = this.getSilos().iterator();
		Iterator<Silo> it2 = other.iterator();
		while (it1.hasNext() && it2.hasNext()) {
			if (!it1.next().equals(it2.next())) {
				return false;
			}
		}
		return !(it1.hasNext() || it2.hasNext());
	}



}
