package de.htwsaar.esch.codeopolis.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;


public class HarvestTest {

    @Test
    public void testCornHarvestDecay() {
    	Harvest cornHarvest = Harvest.createHarvest(Game.GrainType.CORN, 1000, 2020);
        assertEquals(0, cornHarvest.decay(2020)); // No decay in the same year
        assertEquals(0, cornHarvest.decay(2021)); // No decay in the next year
        assertEquals(0, cornHarvest.decay(2022)); // No decay in the next year
        assertEquals(20, cornHarvest.decay(2023)); 
        assertEquals(49, cornHarvest.decay(2024)); 
    }

    @Test
    public void testCornHarvestGrainType() {
    	Harvest cornHarvest = Harvest.createHarvest(Game.GrainType.CORN, 1000, 2020);
        assertEquals(Game.GrainType.CORN, cornHarvest.getGrainType());
    }

    @Test
    public void testBarleyHarvestDecay() {
        Harvest barleyHarvest = Harvest.createHarvest(Game.GrainType.BARLEY, 1000, 2020);
        assertEquals(0, barleyHarvest.decay(2020)); // No decay in the same year
        assertEquals(0, barleyHarvest.decay(2021)); // No decay in the next year
        assertEquals(0, barleyHarvest.decay(2022)); // No decay in the year after
        assertEquals(20, barleyHarvest.decay(2023)); 
        assertEquals(49, barleyHarvest.decay(2024)); 

    }

    @Test
    public void testBarleyHarvestGrainType() {
    	Harvest barleyHarvest = Harvest.createHarvest(Game.GrainType.BARLEY, 1000, 2020);
        assertEquals(Game.GrainType.BARLEY, barleyHarvest.getGrainType());
    }

    @Test
    public void testMilletHarvestDecay() {
        Harvest milletHarvest = Harvest.createHarvest(Game.GrainType.MILLET, 1000, 2020);
        assertEquals(0, milletHarvest.decay(2020)); // No decay in the same year
        assertEquals(0, milletHarvest.decay(2021)); // No decay in the next year
        assertEquals(0, milletHarvest.decay(2022)); // No decay in the year after
        assertEquals(0, milletHarvest.decay(2023)); // No decay in the year after that
        assertEquals(0, milletHarvest.decay(2024)); // No decay after 4 years

        // After 5 years, decay starts
        assertEquals(10, milletHarvest.decay(2025));
    }

    @Test
    public void testMilletHarvestGrainType() {
        Harvest milletHarvest = Harvest.createHarvest(Game.GrainType.MILLET, 1000, 2020);
        assertEquals(Game.GrainType.MILLET, milletHarvest.getGrainType());
    }

    @Test
    public void testRiceHarvestDecay() {
    	Harvest riceHarvest = Harvest.createHarvest(Game.GrainType.RICE, 1000, 2020);
        assertEquals(0, riceHarvest.decay(2020)); // No decay in the same year
        assertEquals(0, riceHarvest.decay(2021)); // No decay in the next year
        assertEquals(20, riceHarvest.decay(2022)); // Decay starts after 1 year
    }

    @Test
    public void testRiceHarvestGrainType() {
    	Harvest riceHarvest = Harvest.createHarvest(Game.GrainType.RICE, 1000, 2020);
        assertEquals(Game.GrainType.RICE, riceHarvest.getGrainType());
    }

    @Test
    public void testRyeHarvestDecay() {
    	Harvest ryeHarvest = Harvest.createHarvest(Game.GrainType.RYE, 1000, 2020);
        assertEquals(0, ryeHarvest.decay(2020)); // No decay in the same year
        assertEquals(0, ryeHarvest.decay(2021)); 
        assertEquals(0, ryeHarvest.decay(2022)); 
        assertEquals(0, ryeHarvest.decay(2023)); 
        assertEquals(0, ryeHarvest.decay(2024)); 
        assertEquals(10, ryeHarvest.decay(2025)); 
        assertEquals(29, ryeHarvest.decay(2026)); 
    }

    @Test
    public void testRyeHarvestGrainType() {
    	Harvest ryeHarvest = Harvest.createHarvest(Game.GrainType.RYE, 1000, 2020);
        assertEquals(Game.GrainType.RYE, ryeHarvest.getGrainType());
    }

    @Test
    public void testWheatHarvestDecay() {
    	Harvest wheatHarvest = Harvest.createHarvest(Game.GrainType.WHEAT, 1000, 2020);
        assertEquals(0, wheatHarvest.decay(2020)); // No decay in the same year
        assertEquals(0, wheatHarvest.decay(2021)); // No decay in the next year
        assertEquals(20, wheatHarvest.decay(2022)); 
        assertEquals(39, wheatHarvest.decay(2023)); 
    }

    @Test
    public void testWheatHarvestGrainType() {
    	Harvest wheatHarvest = Harvest.createHarvest(Game.GrainType.WHEAT, 1000, 2020);
        assertEquals(Game.GrainType.WHEAT, wheatHarvest.getGrainType());
    }
}
