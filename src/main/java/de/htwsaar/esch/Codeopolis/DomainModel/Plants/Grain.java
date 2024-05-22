package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The `Grain` class represents an abstract grain plant with common attributes and behaviors.
 * Subclasses of `Grain` define specific types of grain plants.
 */
public abstract class Grain {
	
	public enum Pests{FritFly, DeliaFly, BarleyGoutFly};
	
	public enum Diseases{PowderyMildew, LeafDrought, Fusarium}
	
	private float basicYieldRatio; // The basic yield ratio of the grain.
	
	private boolean harvested = false; // Indicates if the grain has been harvested.
	
	private int acresPlanted; // Number of acres where the grain is planted.
	
	/*
	 * yieldRatio is protected and therefore visible in the whole package. 
	 * Ideally, it would only be visible in the subclasses only. 
	 * However, this is achieved by having a separate package for grain and all its subclasses. 
	 * 
	 * As an alternative, there could also be a private variable in each subclass of Grain. 
	 * Grain could then define an abstract method "public float getYieldRatio", which is also implemented in the subclasses. 
	 * However, this would mean a redundant implementation. 
	 * The getYieldRatio method would be needed since yieldRatio is required in the subclasses as well as in Grain's harvest method. 
	 */
	
    /**
     * The yield ratio represents the ratio of yield obtained from the planted grain.
     * It is affected by the quality of soil conditions and the grain's basic yield ratio.
     */
	protected float yieldRatio = 0f;

    /**
     * Gets the current yield ratio of the grain.
     *
     * @return The current yield ratio.
     */
	public float getYieldRatio() {
		return yieldRatio;
	}

	/**
     * The crop failure due to bad conditions represents the percentage of crop failure
     * caused by adverse environmental conditions, such as drought or pests.
     */
	private float cropFailureDueToBadConditions;
	
	public static class Conditions {
		public static final float SUMMER_TEMPERATURE_LOWER = 0.0f;
		public static final float SUMMER_TEMPERATURE_HIGHER = 30.0f;
		public static final float WINTER_TEMPERATURE_LOWER = -10.0f;
		public static final float WINTER_TEMPERATURE_HIGHER = 10.0f;
		
		private float soilConditions;
		private float averageTemperatureSummer;
		private float averageTemperatureWinter;
		private boolean drought;
		private boolean fusarium;
		private boolean leafDrought;
		private boolean powdryMildrew;
		private boolean barleyGoutFly;
		private boolean delioFly;
		private boolean fritFly;
		
		/**
	     * Constructor for the Conditions class.
	     * @param soilConditions the quality of the soil
	     * @param averageTemperatureSummer the average temperature during summer
	     * @param averageTemperatureWinter the average temperature during winter
	     * @param drought whether there is a drought
	     * @param fusarium whether there is fusarium
	     * @param leafDrought whether there is leaf drought
	     * @param powdryMildrew whether there is powdry mildrew
	     * @param barleyGoutFly whether there is barley gout fly
	     * @param delioFly whether there is delio fly
	     * @param fritFly whether there is frit fly
	     */
		private Conditions(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, boolean drought, boolean fusarium, boolean leafDrought, boolean powdryMildrew, boolean barleyGoutFly, boolean delioFly, boolean fritFly) {
			this.soilConditions = soilConditions;
			this.averageTemperatureSummer = averageTemperatureSummer;
			this.averageTemperatureWinter = averageTemperatureWinter;
			this.drought = drought;
			this.fusarium = fusarium;
			this.leafDrought = leafDrought;
			this.powdryMildrew = powdryMildrew;
			this.barleyGoutFly = barleyGoutFly;
			this.delioFly = delioFly;
			this.fritFly = fritFly;
		}

		/**
		 * This method generates random conditions for growing barley.
		 * @return a Conditions object with random values
		 */
		public static Conditions generateRandomConditions() {
			Random random = new Random();
			HashMap<String, Boolean> settings = new HashMap<String, Boolean>(
				Map.of(
						"drought", false,
						"fusarium", false,
						"leadDrought", false,
						"powdryMildrew", false,
						"barleyGoutFly", false,
						"delioFly", false,
						"fritFly", false	
				)
			);
			
			//Generate seed upon which the probabilities will be set
			long lowerBoundary = 0x10000000000000L;
			long upperBoundary = 0x64646464646464L;

			long seed = random.nextLong(lowerBoundary, upperBoundary);

			//Some bit magic
			for(String key: settings.keySet()) {
				long setting = seed & 0x64;
				seed >>= 8;
				
				if(setting >= 80) {
					settings.put(key, true);
				}
			}
			
			return new Conditions(
					random.nextFloat(0.0f, 1.0f),
					random.nextFloat(Conditions.SUMMER_TEMPERATURE_LOWER, Conditions.SUMMER_TEMPERATURE_HIGHER),
					random.nextFloat(Conditions.WINTER_TEMPERATURE_LOWER, Conditions.WINTER_TEMPERATURE_HIGHER),
					settings.get("drought"),
					settings.get("fusarium"),
					settings.get("leadDrought"),
					settings.get("powdryMildrew"),
					settings.get("barleyGoutFly"),
					settings.get("delioFly"),
					settings.get("fritFly")
			);
		}
		
		/** Getters **/
		/**
		 * This method returns the soil conditions.
		 * @return the quality of the soil
		 */
		public float getSoilConditions() {
		    return soilConditions;
		}

		/**
		 * This method returns the average temperature during summer.
		 * @return the average temperature during summer
		 */
		public float getAverageTemperatureSummer() {
		    return averageTemperatureSummer;
		}

		/**
		 * This method returns the average temperature during winter.
		 * @return the average temperature during winter
		 */
		public float getAverageTemperatureWinter() {
		    return averageTemperatureWinter;
		}

		/**
		 * This method returns whether there is barley gout fly.
		 * @return whether there is barley gout fly
		 */
		public boolean isBarleyGoutFly() {
		    return barleyGoutFly;
		}

		/**
		 * This method returns whether there is delio fly.
		 * @return whether there is delio fly
		 */
		public boolean isDelioFly() {
		    return delioFly;
		}

		/**
		 * This method returns whether there is drought.
		 * @return whether there is drought
		 */
		public boolean isDrought() {
		    return drought;
		}

		/**
		 * This method returns whether there is frit fly.
		 * @return whether there is frit fly
		 */
		public boolean isFritFly() {
		    return fritFly;
		}

		/**
		 * This method returns whether there is fusarium.
		 * @return whether there is fusarium
		 */
		public boolean isFusarium() {
		    return fusarium;
		}

		/**
		 * This method returns whether there is leaf drought.
		 * @return whether there is leaf drought
		 */
		public boolean isLeafDrought() {
		    return leafDrought;
		}

		/**
		 * This method returns whether there is powdry mildrew.
		 * @return whether there is powdry mildrew
		 */
		public boolean isPowdryMildrew() {
		    return powdryMildrew;
		}

		/**
		 * This method returns the upper limit of summer temperature.
		 * @return the upper limit of summer temperature
		 */
		public static float getSummerTemperatureHigher() {
		    return SUMMER_TEMPERATURE_HIGHER;
		}

		/**
		 * This method returns the lower limit of summer temperature.
		 * @return the lower limit of summer temperature
		 */
		public static float getSummerTemperatureLower() {
		    return SUMMER_TEMPERATURE_LOWER;
		}

		/**
		 * This method returns the upper limit of winter temperature.
		 * @return the upper limit of winter temperature
		 */
		public static float getWinterTemperatureHigher() {
		    return WINTER_TEMPERATURE_HIGHER;
		}

		/**
		 * This method returns the lower limit of winter temperature.
		 * @return the lower limit of winter temperature
		 */
		public static float getWinterTemperatureLower() {
		    return WINTER_TEMPERATURE_LOWER;
		}

		/** Setters **/

		/**
		 * This method sets the average temperature during summer.
		 * @param averageTemperatureSummer the average temperature during summer
		 */
		public void setAverageTemperatureSummer(float averageTemperatureSummer) {
		    this.averageTemperatureSummer = averageTemperatureSummer;
		}

		/**
		 * This method sets the average temperature during winter.
		 * @param averageTemperatureWinter the average temperature during winter
		 */
		public void setAverageTemperatureWinter(float averageTemperatureWinter) {
		    this.averageTemperatureWinter = averageTemperatureWinter;
		}

		/**
		 * This method sets whether there is barley gout fly.
		 * @param barleyGoutFly whether there is barley gout fly
		 */
		public void setBarleyGoutFly(boolean barleyGoutFly) {
		    this.barleyGoutFly = barleyGoutFly;
		}

		/**
		 * This method sets whether there is delio fly.
		 * @param delioFly whether there is delio fly
		 */
		public void setDelioFly(boolean delioFly) {
		    this.delioFly = delioFly;
		}

		/**
		 * This method sets whether there is drought.
		 * @param drought whether there is drought
		 */
		public void setDrought(boolean drought) {
		    this.drought = drought;
		}

		/**
		 * This method sets whether there is frit fly.
		 * @param fritFly whether there is frit fly
		 */
		public void setFritFly(boolean fritFly) {
		    this.fritFly = fritFly;
		}

		/**
		 * This method sets whether there is fusarium.
		 * @param fusarium whether there is fusarium
		 */
		public void setFusarium(boolean fusarium) {
		    this.fusarium = fusarium;
		}

		/**
		 * This method sets whether there is leaf drought.
		 * @param leafDrought whether there is leaf drought
		 */
		public void setLeafDrought(boolean leafDrought) {
		    this.leafDrought = leafDrought;
		}

		/**
		 * This method sets whether there is powdry mildrew.
		 * @param powdryMildrew whether there is powdry mildrew
		 */
		public void setPowdryMildrew(boolean powdryMildrew) {
		    this.powdryMildrew = powdryMildrew;
		}
		
		/**
		 * This method sets the quality of the soil.
		 * @param soilConditions the quality of the soil
		 */
		public void setSoilConditions(float soilConditions) {
		    this.soilConditions = soilConditions;
		}
	}
	
    /**
     * Constructs a `Grain` object with the specified basic yield ratio and crop failure due to bad conditions.
     *
     * @param basicYieldRatio             The basic yield ratio of the grain.
     * @param cropFailureDueToBadConditions The crop failure due to bad conditions.
     */
	public Grain(float basicYieldRatio, float cropFailureDueToBadConditions) {
		this.basicYieldRatio = basicYieldRatio;
		this.cropFailureDueToBadConditions = cropFailureDueToBadConditions;
		this.acresPlanted = 0;
	}
	
    /**
     * Plants the grain on a specified number of acres.
     *
     * @param acres The number of acres to plant the grain.
     * @return True if the planting was successful, false otherwise.
     */
	public boolean plant(int acres) {
		if(acresPlanted > 0) // Cannot double-plant a grain.
			return false;
		if(acres<0)
			return false;
		acresPlanted = acres;
		harvested = false;
		return true;
	}
		
    /**
     * Simulates the growth of the grain based on environmental conditions.
     *
     * @param conditions The environmental conditions affecting the grain's growth.
     */
	public void grow(Grain.Conditions conditions) {
		this.yieldRatio = conditions.getSoilConditions() * basicYieldRatio;
	}
	
    /**
     * Handles drought conditions for the grain.
     */
	public abstract void drought();
	
    /**
     * Handles pest infestation conditions for the grain.
     *
     * @param pest       The type of pest infestation.
     * @param conditions The environmental conditions affecting the pest infestation.
     */
	public abstract void pestInfestation(Pests pest, Conditions conditions);
	
    /**
     * Handles disease outbreaks for the grain.
     *
     * @param disease    The type of disease outbreak.
     * @param conditions The environmental conditions affecting the disease outbreak.
     */
	public abstract void diseaseOutbreak(Diseases disease, Conditions conditions);
	
    /**
     * Harvests the grain, returning the crop yield.
     *
     * @return The crop yield obtained from harvesting the grain.
     */
	public int harvest() {
		if(harvested)
			return 0;
		int crop =  (int) (Math.round(this.acresPlanted * this.yieldRatio));
		harvested = true;
		acresPlanted = 0;
		return crop;
	}
		
    /**
     * Retrieves the basic yield ratio of the grain.
     *
     * @return The basic yield ratio of the grain.
     */
	protected float getBasicYieldRatio() {
		return basicYieldRatio;
	}

    /**
     * Retrieves the crop failure rate due to bad conditions.
     *
     * @return The crop failure rate due to bad conditions.
     */
	protected float getCropFailureDueToBadConditions() {
		return cropFailureDueToBadConditions;
	}
	
}