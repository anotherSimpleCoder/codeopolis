package de.htwsaar.esch.codeopolis.tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.htwsaar.esch.Codeopolis.DomainModel.City;
import de.htwsaar.esch.Codeopolis.DomainModel.Game;
import de.htwsaar.esch.Codeopolis.DomainModel.GameConfig;
import de.htwsaar.esch.Codeopolis.Exceptions.*;
import de.htwsaar.esch.Codeopolis.DomainModel.Game.Difficulty;

/**
 * Unit tests for the City class.
 */
class CityTest {
	City testCity;
	
	// Test data for buying acres
		private static int[][] buyTestData() {
	        return new int[][] { { 0 , 20 , 1000, 3000, 1}, { 1, 25, 1001, 2975, 1}, { 300, 10, 1300, 0, 1}, { 301, 10, 1000, 3000, 0}, { 300, 100, 1000, 3000, 0}, { 20, 0, 1020, 3000 , 1}, { 300, 15, 1000, 3000, 0 }, { 500, 1, 1500, 2500, 1} };
	    }
	
	// Test data for selling acres
	private static int[][] sellTestData() {
        return new int[][] { { 0 , 20 , 1000, 3000, 1}, { 1, 25, 999, 3025, 1}, { 280, 10, 720, 5800, 1}, { 300, 0, 700, 3000, 1}, { 20, 0, 980, 3000 , 1}, { 1000, 10, 0, 9000, 1 }, { 1001, 10, 1000, 3000, 0} };
    }
	
	// Test data for feeding the population
	private static int[][] feedTestData() {
        return new int[][] { { 0 , 3000 , 1}, { 1, 2999, 1}, { 3000, 0, 1}, { 3001, 3000, 0}, { 1000, 2000, 1} };
    }
	
	// Test data for planting acres (not enough bushels)
	private static int[][] plantTestDataEnoughtBushles() {
		return new int[][] {{ 0 , 1}, { 1, 1}, { 800 , 1}, { 801, 0}};
    }
	
	// Test data for running a turn
	private static int[][] turnTestData() {
		return new int[][] {{ 0 , 100, 0}, { 100, 95, 1}, { 1980 , 1, 100}, { 1990, 1, 200}, { 2000, 0, 800}, { 2020, 0, 780}};
    }
	
	/**
	 * Setup before each test
	 */
	@BeforeEach
    public void setup() {
        testCity = new City(java.util.UUID.randomUUID().toString(), "My Test City", new GameConfig(Difficulty.EASY));
    }

	/**
     * Tests the instantiation of an easy city.
     */
	@Test
	void testInstantiateEasy() {
		
		assertAll(() -> assertEquals(1000, testCity.getState().getAcres()),
				() -> assertEquals(3000, testCity.getState().getTotalAmountOfBushels()), 
				() -> assertEquals(100, testCity.getState().getResidents()));		
	}
	
	/**
     * Tests the instantiation of a medium city.
     */
	@Test
	void testInstantiateMedium() {
		testCity = new City(java.util.UUID.randomUUID().toString(), "My Test City", new GameConfig(Difficulty.MEDIUM));
		assertAll(() -> assertEquals(900, testCity.getState().getAcres()),
				() -> assertEquals(3000, testCity.getState().getTotalAmountOfBushels()), 
				() -> assertEquals(100, testCity.getState().getResidents()));
	}
	
	/**
     * Tests the instantiation of a hard city.
     */
	@Test
	void testInstantiateHard() {
		testCity = new City(java.util.UUID.randomUUID().toString(), "My Test City", new GameConfig(Difficulty.HARD));
		assertAll(() -> assertEquals(800, testCity.getState().getAcres()),
				() -> assertEquals(3000, testCity.getState().getTotalAmountOfBushels()), 
				() -> assertEquals(100, testCity.getState().getResidents()));
	}
	
	/**
     * Parameterized test for buying acres in the city.
     * 
     * @param testData The test data array containing input and expected output values.
     */
	@ParameterizedTest
    @MethodSource(value =  "buyTestData")
	void testBuy(int [] testData) {
		
		int acresToBuy = testData[0];
		int pricePerAcres = testData[1];
		int expectedAcresAfter = testData[2];
		int expectedBushelsAfter = testData[3];
		int expectedResult = testData[4];
		
		if(expectedResult == 1)
		{
			assertAll(
			        () -> assertDoesNotThrow(() -> testCity.buy(pricePerAcres, acresToBuy)), 
			        () -> assertEquals(expectedAcresAfter, testCity.getState().getAcres()), 
			        () -> assertEquals(expectedBushelsAfter, testCity.getState().getTotalAmountOfBushels()) 
			    );
		}
		else {
			assertThrows(InsufficientResourcesException.class, () -> testCity.buy(pricePerAcres, acresToBuy));
		}

	}
	
	
	/**
     * Parameterized test for selling acres.
     * 
     * @param testData The test data array containing input and expected output values.
     */
	
	@ParameterizedTest
    @MethodSource(value =  "sellTestData")
	void testSell(int [] testData) {
		int acresToSell = testData[0];
		int pricePerAcres = testData[1];
		int acresAfter = testData[2];
		int bushelsAfter = testData[3];
		int expectedResult = testData[4];

		if(expectedResult == 1)
		{
			assertAll(
			        () -> assertDoesNotThrow(() -> testCity.sell(pricePerAcres, acresToSell)), 
			        () -> assertEquals(acresAfter, testCity.getState().getAcres()), 
			        () -> assertEquals(bushelsAfter, testCity.getState().getTotalAmountOfBushels())
			    );
		}
		else {
			assertThrows(GameException.class, () -> testCity.sell(pricePerAcres, acresToSell)); 
		}
	}
	
	
	/**
     * Parameterized test for feeding the population in the city.
     * 
     * @param testData The test data array containing input and expected output values.
     */
	@ParameterizedTest
    @MethodSource(value =  "feedTestData")
	void testFeed(int [] testData) {
		int bushelsToFeed = testData[0];
		int bushelsAfter = testData[1];
		int expectedResult = testData[2];
	
		
		if(expectedResult == 1)
		{
			assertAll(
			        () -> assertDoesNotThrow(() -> testCity.feed(bushelsToFeed)),
			        () -> assertEquals(bushelsAfter, testCity.getState().getTotalAmountOfBushels()) 
			    );
		}
		else {
			assertThrows(InsufficientResourcesException.class, () -> testCity.feed(bushelsToFeed));
		}
	}
	
	
	/**
     * Test for planting acres without enough available bushels in the city.
     * 
     */
    @Test
    public void testPlantInsufficientBushels() {
        testCity.buy(100, 25);
        int[] acres = {this.testCity.getState().getBushels(Game.GrainType.BARLEY)+1, 0, 0, 0, 0, 0}; 
        assertThrows(GameException.class, () -> testCity.plant(acres));
    }
    
    /**
     * Test for planting acres without enough available acres.
     * 
     */
    @Test
    public void testPlantInsufficientAcres() {
        
        int[] acres = {this.testCity.getState().getBushels(Game.GrainType.BARLEY), 
        		this.testCity.getState().getBushels(Game.GrainType.CORN), 
        		this.testCity.getState().getBushels(Game.GrainType.MILLET), 
        		this.testCity.getState().getBushels(Game.GrainType.RICE), 
        		this.testCity.getState().getBushels(Game.GrainType.RYE), 
        		this.testCity.getState().getBushels(Game.GrainType.WHEAT)}; 
    
    assertThrows(LandOperationException.class, () -> this.testCity.plant(acres));      
    }

    /**
     * This test method verifies that the 'plant' method can handle an input array of acres 
     * that exceeds the number of defined grain types without throwing an exception. 
     * The array of acres to plant has more elements than there are grain types, 
     * which could potentially lead to out-of-bounds access if not handled properly.
     * The test expects that no exception is thrown when calling the 'plant' method with this array.
     */
    @Test
    public void testPlantInvalidGrainType() {       
        int[] acres = {1, 1, 1, 1, 1, 1, 1}; 
        Assertions.assertDoesNotThrow(() -> {
            this.testCity.plant(acres);
        });
    }
	
	/**
     * Parameterized test for planting acres without enough available residents in the city.
	 */
	@Test
	void testPlantEnoughtResidents() {
		testCity.IDKFA();
        int[] acres = {200, 200, 200, 200, 200, 1}; 
        assertThrows(LandOperationException.class, () -> 
        this.testCity.plant(acres)); 
  	}
	 
	

}