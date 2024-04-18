package de.htwsaar.esch.codeopolis.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.htwsaar.esch.Codeopolis.DomainModel.Depot;
import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.Silo;
import de.htwsaar.esch.Codeopolis.DomainModel.Game.GrainType;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;

public class SiloTests {

    private Silo silo;

    @BeforeEach
    public void setUp() {
        silo = new Silo(1000);
    }

    @Test
    public void testStoreAndTakeOut() {
        Harvest cornHarvest = Harvest.createHarvest(GrainType.CORN, 500, 2020);
        Harvest cornHarvest2 = Harvest.createHarvest(GrainType.CORN, 600, 2021);

        assertNull(silo.store(cornHarvest));
        assertEquals(500, silo.getFillLevel());
        silo.store(cornHarvest2); 
        assertEquals(1000, silo.getFillLevel()); 

        assertEquals(700, silo.takeOut(700)); 
        assertEquals(300, silo.getFillLevel()); 
    }

    @Test
    public void testGrainType() {
        Harvest cornHarvest = Harvest.createHarvest(GrainType.CORN, 500, 2020);
        Harvest cornHarvest2 = Harvest.createHarvest(GrainType.CORN, 600, 2021);
        silo.store(cornHarvest);
        silo.store(cornHarvest2);

        assertEquals(Game.GrainType.CORN, silo.getGrainType()); // Silo grain type should be corn
    }

    @Test
    public void testEmptySilo() {
        Harvest cornHarvest = Harvest.createHarvest(GrainType.CORN, 500, 2020);
        Harvest cornHarvest2 = Harvest.createHarvest(GrainType.CORN, 600, 2021);
        silo.store(cornHarvest);
        silo.store(cornHarvest2);

        Harvest[] removedHarvests = silo.emptySilo();

        assertEquals(2, removedHarvests.length); // Two harvests removed
        assertEquals(0, silo.getFillLevel()); // Silo empty
    }


    @Test
    public void testExtendStock() {
        for (int i = 0; i < 12; i++) {
            Harvest harvest = Harvest.createHarvest(GrainType.CORN, 10, 2020+i);
            assertNull(silo.store(harvest)); // Silo can store up to 10 harvests initially
        }
        assertEquals(120, silo.getFillLevel()); // 200 harvested stored
    }

    @Test
    public void testInvalidStore() {
    	Harvest cornHarvest = Harvest.createHarvest(GrainType.CORN, 500, 2020);
        silo.store(cornHarvest);
        
        try {
            Harvest barleyHarvest = Harvest.createHarvest(GrainType.BARLEY, 600, 2021);
            silo.store(barleyHarvest); // Attempt to store barley harvest in silo already containing corn
        } catch (IllegalArgumentException e) {
            assertEquals("The grain type of the given Harvest does not match the grain type of the silo", e.getMessage());
        }
    }

    @Test
    public void testTakeOutOverflow() {
        Harvest cornHarvest = Harvest.createHarvest(GrainType.CORN, 500, 2020);
        silo.store(cornHarvest);
        assertEquals(500, silo.takeOut(700)); // Attempt to take out more grain than available
        assertEquals(0, silo.getFillLevel()); // Silo empty after failed take out
    }

    @Test
    public void testGetFillLevel() {
        assertEquals(0, silo.getFillLevel()); // Silo initially empty

        Harvest cornHarvest = Harvest.createHarvest(GrainType.CORN, 500, 2020);
        silo.store(cornHarvest);
        assertEquals(500, silo.getFillLevel()); // 500 grain stored

        silo.takeOut(200);
        assertEquals(300, silo.getFillLevel()); // 200 grain taken out, 300 remaining
    }

    @Test
    public void testGetCapacity() {
        assertEquals(1000, silo.getCapacity()); // Silo capacity is 1000 grain
    }

    @Test
    public void testGetHarvestCount() {
        assertEquals(0, silo.getHarvestCount()); // Silo initially empty

        Harvest cornHarvest = Harvest.createHarvest(GrainType.CORN, 500, 2020);
        Harvest cornHarvest2 = Harvest.createHarvest(GrainType.CORN, 600, 2021);
        silo.store(cornHarvest);
        silo.store(cornHarvest2);

        assertEquals(2, silo.getHarvestCount()); // Two harvests stored
    }
}
