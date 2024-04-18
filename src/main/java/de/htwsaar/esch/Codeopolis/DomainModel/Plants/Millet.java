package de.htwsaar.esch.Codeopolis.DomainModel.Plants;

/**
 * The `Millet` class represents a specific type of spring grain, millet.
 * It inherits properties and methods from the `SpringGrain` class and defines
 * its own response to drought, pest infestation, and disease outbreak.
 */
public class Millet extends SpringGrain {

	/**
     * Constructs a new `Millet` object with predefined characteristics.
     */
	public Millet() {
		super(2f, 0.1f, 0.45f);
	}

	/**
     * Overrides the `drought` method from the parent class `SpringGrain` to specify
     * how millet responds to drought conditions by reducing its yield ratio.
     */
    @Override
	public void drought() {
		this.yieldRatio *= 0.95f;
	}

    /**
     * Overrides the `pestInfestation` method from the parent class `SpringGrain` to specify
     * how millet responds to pest infestation, specifically Barley Gout Fly, based on soil conditions.
     *
     * @param pest       The type of pest infestation.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void pestInfestation(Pests pest, Conditions conditions) {
		switch(pest) {
			case BarleyGoutFly:
				if(conditions.getSoilConditions() < 0.8)
					this.yieldRatio *= 0.85f;
				else
					this.yieldRatio *= 0.9f;
				break;
			default:
				break;
		}
	}

    /**
     * Overrides the `diseaseOutbreak` method from the parent class `SpringGrain` to specify
     * that millet is not vulnerable to diseases and does not have a yield reduction due to disease outbreaks.
     *
     * @param disease    The type of disease outbreak.
     * @param conditions The environmental conditions affecting grain growth.
     */
    @Override
	public void diseaseOutbreak(Diseases disease, Conditions conditions) {
		// Millet is not vulnerable to diseases
		
	}



}
