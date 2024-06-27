package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

import java.util.Random;

/**
 * The `Grain` class represents an abstract grain plant with common attributes and behaviors.
 * Subclasses of `Grain` define specific types of grain plants.
 */
public abstract class Grain {

	public enum Pests{FritFly, DeliaFly, BarleyGoutFly}

	public enum Diseases{PowderyMildew, LeafDrought, Fusarium}

	private final float basicYieldRatio; // The basic yield ratio of the grain.

	private boolean harvested = false; // Indicates if the grain has been harvested.

	private int acresPlanted; // Number of acres where the grain is planted.

	private Conditions conditions; //The growing conditions for this grain

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
	 * Retrieves the current growing conditions of the grain.
	 * This method allows access to the `Conditions` object that encapsulates environmental factors
	 * such as soil conditions, temperature, and potential pest or disease outbreaks which affect the grain's growth.
	 *
	 * @return The current `Conditions` object associated with this instance of grain.
	 */
	protected Conditions getConditions(){
		return this.conditions;
	}


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
	private final float cropFailureDueToBadConditions;

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
	public void grow(Conditions conditions) {
		this.conditions = conditions;
		this.yieldRatio = conditions.getSoilConditions() * basicYieldRatio;
		if(conditions.isDrought())
			this.drought();
		if(conditions.isFusarium())
			this.diseaseOutbreak(Grain.Diseases.Fusarium);
		if(conditions.isLeafDrought())
			this.diseaseOutbreak(Grain.Diseases.LeafDrought);
		if(conditions.isPowderyMildew())
			this.diseaseOutbreak(Grain.Diseases.PowderyMildew);
		if(conditions.isBarleyGoutFly())
			this.pestInfestation(Grain.Pests.BarleyGoutFly);
		if(conditions.isDeliaFly())
			this.pestInfestation(Grain.Pests.DeliaFly);
		if(conditions.isFritFly())
			this.pestInfestation(Grain.Pests.FritFly);
	}

	/**
	 * Handles drought conditions for the grain.
	 */
	public abstract void drought();

	/**
	 * Handles pest infestation conditions for the grain.
	 *
	 * @param pest       The type of pest infestation.
	 */
	public abstract void pestInfestation(Pests pest);

	/**
	 * Handles disease outbreaks for the grain.
	 *
	 * @param disease    The type of disease outbreak.
	 */
	public abstract void diseaseOutbreak(Diseases disease);

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


	public static class Conditions {

		private final float soilConditions;
		private final float averageTemperatureSummer;
		private final float averageTemperatureWinter;
		private boolean drought;
		private boolean fusarium;
		private boolean leafDrought;
		private boolean powderyMildew;
		private boolean barleyGoutFly;
		private boolean deliaFly;
		private boolean fritFly;

		/**
		 * Constructs a new `Conditions` object with specified values for soil conditions and temperatures.
		 *
		 * @param soilConditions         The soil conditions affecting plant growth (a float value between 0.0 and 1.0).
		 * @param averageTemperatureSummer The average summer temperature (in degrees Celsius).
		 * @param averageTemperatureWinter The average winter temperature (in degrees Celsius).
		 */
		public Conditions(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter ) {
			this.soilConditions = soilConditions;
			this.averageTemperatureSummer = averageTemperatureSummer;
			this.averageTemperatureWinter = averageTemperatureWinter;
		}

		/**
		 * Gets the soil conditions affecting plant growth.
		 *
		 * @return The soil conditions (a float value between 0.0 and 1.0).
		 */
		public float getSoilConditions() {
			return soilConditions;
		}

		/**
		 * Gets the average summer temperature.
		 *
		 * @return The average summer temperature (in degrees Celsius).
		 */
		public float getAverageTemperatureSummer() {
			return averageTemperatureSummer;
		}

		/**
		 * Gets the average winter temperature.
		 *
		 * @return The average winter temperature (in degrees Celsius).
		 */
		public float getAverageTemperatureWinter() {
			return averageTemperatureWinter;
		}

		/**
		 * Gets the drought condition affecting plant growth.
		 *
		 * @return true if there is a drought condition, false otherwise.
		 */
		public boolean isDrought() {
			return drought;
		}

		/**
		 * Gets the fusarium condition affecting plant growth.
		 *
		 * @return true if there is a fusarium condition, false otherwise.
		 */
		public boolean isFusarium() {
			return fusarium;
		}

		/**
		 * Gets the leaf drought condition affecting plant growth.
		 *
		 * @return true if there is a leaf drought condition, false otherwise.
		 */
		public boolean isLeafDrought() {
			return leafDrought;
		}

		/**
		 * Gets the powdery mildew condition affecting plant growth.
		 *
		 * @return true if there is a powdery mildew condition, false otherwise.
		 */
		public boolean isPowderyMildew() {
			return powderyMildew;
		}

		/**
		 * Gets the barley gout fly condition affecting plant growth.
		 *
		 * @return true if there is a barley gout fly condition, false otherwise.
		 */
		public boolean isBarleyGoutFly() {
			return barleyGoutFly;
		}

		/**
		 * Gets the delia fly condition affecting plant growth.
		 *
		 * @return true if there is a delia fly condition, false otherwise.
		 */
		public boolean isDeliaFly() {
			return deliaFly;
		}

		/**
		 * Gets the frit fly condition affecting plant growth.
		 *
		 * @return true if there is a frit fly condition, false otherwise.
		 */
		public boolean isFritFly() {
			return fritFly;
		}


		/**
		 * Calculates an overall condition score based on soil conditions, average temperatures, and pest/disease conditions.
		 * This score integrates both positive influences (like optimal soil and temperature) and negative influences
		 * (like pests and diseases) to provide a comprehensive assessment of the growing conditions.
		 *
		 * The method uses weighted factors to balance the influence of each condition:
		 * - Soil conditions and average temperatures are given higher weights as they fundamentally affect plant growth.
		 * - Pest and disease impacts are aggregated and subtracted to reflect their detrimental effects.
		 *
		 * @return A float value representing the overall condition score.
		 */
		public float calculateConditionScore() {
			final float soilWeight = 0.3f;  // Weight factor for soil conditions
			final float tempWeight = 0.4f;  // Weight factor for temperatures
			final float pestWeight = 0.3f;  // Weight factor for pest/disease impact
			final float idealTemperature = 10.65f;  // Ideal average temperature in degrees Celsius

			// Calculate the actual average temperature from summer and winter values
			float avgTemperature = (averageTemperatureSummer + averageTemperatureWinter) / 2;

			// Calculate the temperature deviation from the ideal value
			float tempDeviation = Math.abs(avgTemperature - idealTemperature);

			// Normalize the temperature deviation, assuming maximum tolerable deviation is 20 degrees
			float normalizedTempDeviation = tempDeviation / 20.0f;
			normalizedTempDeviation = Math.min(normalizedTempDeviation, 1.0f);  // Limit to a maximum of 1

			// Calculate the negative impact of pest and disease conditions
			float pestImpact = 0.0f;
			if (drought) pestImpact += 0.1f;
			if (fusarium) pestImpact += 0.1f;
			if (leafDrought) pestImpact += 0.1f;
			if (powderyMildew) pestImpact += 0.1f;
			if (barleyGoutFly) pestImpact += 0.1f;
			if (deliaFly) pestImpact += 0.1f;
			if (fritFly) pestImpact += 0.1f;

			// Final calculation of the condition score, integrating all factors
			float conditionScore = (soilConditions * soilWeight) +
					((1.0f - normalizedTempDeviation) * tempWeight) - // Negatively impacted by temperature deviation
					(pestImpact * pestWeight);

			// Prevent negative values and normalize the value range to (0,1)
			conditionScore = Math.max(0.0f, conditionScore);  // Allow no negative values

			return conditionScore;
		}




		/**
		 * Factory method to create a new Conditions object with random values for all fields.
		 *
		 * @return A new Conditions object with random values.
		 */
		public static de.htwsaar.esch.Codeopolis.DomainModel.Plants.Grain.Conditions generateRandomConditions() {
			Random random = new Random();
			float soilConditions = random.nextFloat(); // generates a random float value between 0.0 (inclusive) and 1.0 (exclusive)
			float averageTemperatureSummer = random.nextFloat() * 30.0f; // generates a random float value between 0.0 (inclusive) and 30.0 (exclusive)
			float averageTemperatureWinter = random.nextFloat() * 20.0f - 10.0f; // generates a random float value between -10.0 (inclusive) and 10.0 (exclusive)

			de.htwsaar.esch.Codeopolis.DomainModel.Plants.Grain.Conditions newConditions = new de.htwsaar.esch.Codeopolis.DomainModel.Plants.Grain.Conditions(soilConditions, averageTemperatureSummer, averageTemperatureWinter);

			newConditions.drought = random.nextFloat() > 0.8 ? true : false;
			newConditions.fusarium = random.nextFloat() > 0.8 ? true : false;
			newConditions.leafDrought = random.nextFloat() > 0.8 ? true : false;
			newConditions.powderyMildew = random.nextFloat() > 0.8 ? true : false;
			newConditions.barleyGoutFly = random.nextFloat() > 0.8 ? true : false;
			newConditions.deliaFly = random.nextFloat() > 0.8 ? true : false;
			newConditions.fritFly = random.nextFloat() > 0.8 ? true : false;

			return newConditions;
		}
	}
}