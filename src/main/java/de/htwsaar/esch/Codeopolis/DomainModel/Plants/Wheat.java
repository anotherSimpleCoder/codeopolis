package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

/**
 * The `Wheat` class represents a specific type of winter grain, wheat.
 * It inherits properties and methods from the `WinterGrain` class and defines
 * its own response to drought, pest infestation, and disease outbreak.
 */
public class Wheat extends WinterGrain{

	/**
     * Constructs a new `Wheat` object with predefined characteristics.
     */
	public Wheat() {
		super(6f, 0.4f, 0.1f);
	}

	/**
     * Overrides the `drought` method from the parent class `WinterGrain` to specify
     * how wheat responds to drought conditions by reducing its yield ratio.
     */
    @Override
	public void drought() {
		this.yieldRatio *= 0.5;
	}

    /**
     * Overrides the `pestInfestation` method from the parent class `WinterGrain` to specify
     * how wheat responds to pest infestations based on the type of pest.
     *
     * @param pest       The type of pest infestation.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void pestInfestation(Pests pest, Conditions conditions) {
		switch(pest) {
			case FritFly:
				this.yieldRatio *= 0.75f;
				break;
			case BarleyGoutFly:
				this.yieldRatio *= 0.7f;
				break;
			default:
				break;
		}
	}

    /**
     * Overrides the `diseaseOutbreak` method from the parent class `WinterGrain` to specify
     * how wheat responds to disease outbreaks, such as Powdery Mildew and Leaf Drought, based on
     * environmental conditions.
     *
     * @param disease    The type of disease outbreak.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void diseaseOutbreak(Diseases disease, Conditions conditions) {
		switch (disease) {
			case PowderyMildew:
				this.yieldRatio *= 0.7f;
				break;
			case LeafDrought:
				if(conditions.getAverageTemperatureWinter() > this.getOPTIMAL_WINTER_TEMPERATURE() + 2f) {
					this.yieldRatio *= 0.6f;
				}
				else {
					this.yieldRatio *= 0.7;
				}
				break;
			default:
				break;
		}
	}
	


}
