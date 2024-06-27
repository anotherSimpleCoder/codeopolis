package de.htwsaar.esch.codeopolis.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.htwsaar.esch.Codeopolis.DomainModel.Depot;
import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.Harvest.*;


public class DepotTest {

    private Depot depot;
    private Harvest wheatHarvest;
    private Harvest cornHarvest;
    private Harvest cornHarvest2;

    @BeforeEach
    public void setUp() {
        depot = new Depot(3, 1000);
        wheatHarvest = Harvest.createHarvest(Game.GrainType.WHEAT, 500, 2023, 1f);
        cornHarvest = Harvest.createHarvest(Game.GrainType.CORN, 700, 2023, 1f);
        cornHarvest2 = Harvest.createHarvest(Game.GrainType.CORN, 700, 2023, 1f);
    }

    @Test
    public void testStoreAndTakeOut() {
        assertTrue(depot.store(wheatHarvest));
        assertEquals(500, depot.getFillLevel(Game.GrainType.WHEAT));
        assertTrue(depot.store(cornHarvest));
        assertEquals(700, depot.getFillLevel(Game.GrainType.CORN));
        assertTrue(depot.store(cornHarvest2));
        assertEquals(1400, depot.getFillLevel(Game.GrainType.CORN));

        int takenOut = depot.takeOut(200, Game.GrainType.WHEAT);
        assertEquals(200, takenOut);
        assertEquals(300, depot.getFillLevel(Game.GrainType.WHEAT));

        // Taking out more than available
        takenOut = depot.takeOut(400, Game.GrainType.WHEAT);
        assertEquals(300, takenOut);
        assertEquals(0, depot.getFillLevel(Game.GrainType.WHEAT));

        takenOut = depot.takeOut(1000, Game.GrainType.CORN);
        assertEquals(1000, takenOut);
        assertEquals(400, depot.getFillLevel(Game.GrainType.CORN));
        takenOut = depot.takeOut(400, Game.GrainType.CORN);
        assertEquals(400, takenOut);
        assertEquals(0, depot.getFillLevel(Game.GrainType.CORN));
    }

    @Test
    public void testExpand() {
        assertTrue(depot.store(wheatHarvest));
        depot.expand(2, 100);
        assertEquals(5, depot.getSilos().size());
    }


}
