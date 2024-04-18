package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

/**
 * The `Rice` class represents a specific type of spring grain, rice.
 * It inherits properties and methods from the `SpringGrain` class and defines
 * its own response to drought, pest infestation, and disease outbreak.
 */
public class Rice extends SpringGrain{
	
	/**
     * Constructs a new `Rice` object with predefined characteristics.
     */
	public Rice() {
		super(6, 0.4f, 0.1f);
	}

	/**
     * Overrides the `drought` method from the parent class `SpringGrain` to specify
     * how rice responds to drought conditions by reducing its yield ratio.
     */
    @Override
	public void drought() {
		this.yieldRatio *= 0.5;
	}

    /**
     * Overrides the `pestInfestation` method from the parent class `SpringGrain` to specify
     * how rice responds to pest infestation, including Barley Gout Fly and Delia Fly,
     * based on temperature conditions and heat resistance.
     *
     * @param pest       The type of pest infestation.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void pestInfestation(Pests pest, Conditions conditions) {
		switch(pest) {
			case BarleyGoutFly:
				this.yieldRatio *= 0.7f;
				break;
			case DeliaFly:
				if (conditions.getAverageTemperatureSummer() > (this.getOPTIMAL_SUMMER_TEMPERATURE() * (1+this.getHeatResistance()))) {
					this.yieldRatio *= 0.6f;
				}
				else {
					this.yieldRatio *= 0.7f;
				}
				break;
			default:
				break;
		}
	}

    /**
     * Overrides the `diseaseOutbreak` method from the parent class `SpringGrain` to specify
     * how rice responds to disease outbreaks, including Fusarium and Leaf Drought.
     *
     * @param disease    The type of disease outbreak.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void diseaseOutbreak(Diseases disease, Conditions conditions) {
		switch (disease) {
			case Fusarium:
				this.yieldRatio *= 0.75f;
				break;
			case LeafDrought:
				this.yieldRatio *= 0.7f;
				break;
			default:
				break;
		}
	}
}
