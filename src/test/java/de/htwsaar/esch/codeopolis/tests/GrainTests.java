package de.htwsaar.esch.codeopolis.tests;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import de.htwsaar.esch.Codeopolis.DomainModel.Plants.*;

public class GrainTests {



    /**
     * Tests the functionality of the Corn class.
     * - Tests the growth of corn under different conditions.
     * - Tests the harvest yield under different conditions.
     * - Tests the effects of drought, pest infestation, and disease outbreak on the harvest yield.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/cornTestData.csv", numLinesToSkip = 1, encoding = "UTF-8" )
    public void testCorn(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, int acres, int expectedCrop, int expectedCropDrought, int expectedCropFritFly, int expectedCropDeliaFly, int expectedCropBarleyGoutFly, int expectedCropPowderyMildew, int expectedCropLeafDrought, int expectedCropFusarium) {

        Corn corn = new Corn();
        Grain.Conditions conditions = new Grain.Conditions(soilConditions, averageTemperatureSummer, averageTemperatureWinter);

        //test harvesting without planting and growing.
        int crop = corn.harvest();
        Assertions.assertEquals(0, crop);

        //test harvesting without growing
        corn = new Corn();
        corn.plant(acres);
        crop = corn.harvest();
        Assertions.assertEquals(0, crop);

        //test growing without planting
        corn = new Corn();
        corn.grow(conditions);
        crop = corn.harvest();
        Assertions.assertEquals(0, crop);

        //test plant and grow
        corn = new Corn();
        corn.plant(acres);
        corn.grow(conditions);
        crop = corn.harvest();
        Assertions.assertEquals(expectedCrop, crop);

        //test double harvest
        crop = corn.harvest();
        Assertions.assertEquals(0, crop);

        //test drought
        corn = new Corn();
        corn.plant(acres);
        corn.grow(conditions);
        corn.drought();
        crop = corn.harvest();
        Assertions.assertEquals(expectedCropDrought, crop);

        //test expectedCropFritFly
        corn = new Corn();
        corn.plant(acres);
        corn.grow(conditions);
        corn.drought();
        corn.pestInfestation(Grain.Pests.FritFly);
        crop = corn.harvest();
        Assertions.assertEquals(expectedCropFritFly, crop);

        //test expectedCropDeliaFly
        corn = new Corn();
        corn.plant(acres);
        corn.grow(conditions);
        corn.drought();
        corn.pestInfestation(Grain.Pests.FritFly);
        corn.pestInfestation(Grain.Pests.DeliaFly);
        crop = corn.harvest();
        Assertions.assertEquals(expectedCropDeliaFly, crop);

        //test expectedCropBarleyGoutFly
        corn = new Corn();
        corn.plant(acres);
        corn.grow(conditions);
        corn.drought();
        corn.pestInfestation(Grain.Pests.FritFly);
        corn.pestInfestation(Grain.Pests.DeliaFly);
        corn.pestInfestation(Grain.Pests.BarleyGoutFly);
        crop = corn.harvest();
        Assertions.assertEquals(expectedCropBarleyGoutFly, crop);

        //test expectedCropPowderyMildew
        corn = new Corn();
        corn.plant(acres);
        corn.grow(conditions);
        corn.drought();
        corn.pestInfestation(Grain.Pests.FritFly);
        corn.pestInfestation(Grain.Pests.DeliaFly);
        corn.pestInfestation(Grain.Pests.BarleyGoutFly);
        corn.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        crop = corn.harvest();
        Assertions.assertEquals(expectedCropPowderyMildew, crop);

        //test expectedCropLeafDrought
        corn = new Corn();
        corn.plant(acres);
        corn.grow(conditions);
        corn.drought();
        corn.pestInfestation(Grain.Pests.FritFly);
        corn.pestInfestation(Grain.Pests.DeliaFly);
        corn.pestInfestation(Grain.Pests.BarleyGoutFly);
        corn.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        corn.diseaseOutbreak(Grain.Diseases.LeafDrought);
        crop = corn.harvest();
        Assertions.assertEquals(expectedCropLeafDrought, crop);

        //test expectedCropFusarium
        corn = new Corn();
        corn.plant(acres);
        corn.grow(conditions);
        corn.drought();
        corn.pestInfestation(Grain.Pests.FritFly);
        corn.pestInfestation(Grain.Pests.DeliaFly);
        corn.pestInfestation(Grain.Pests.BarleyGoutFly);
        corn.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        corn.diseaseOutbreak(Grain.Diseases.LeafDrought);
        corn.diseaseOutbreak(Grain.Diseases.Fusarium);
        crop = corn.harvest();
        Assertions.assertEquals(expectedCropFusarium, crop);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/milletTestData.csv", numLinesToSkip = 1, encoding = "UTF-8" )
    public void testMillet(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, int acres, int expectedCrop, int expectedCropDrought, int expectedCropFritFly, int expectedCropDeliaFly, int expectedCropBarleyGoutFly, int expectedCropPowderyMildew, int expectedCropLeafDrought, int expectedCropFusarium) {

        Millet millet = new Millet();
        Grain.Conditions conditions = new Grain.Conditions(soilConditions, averageTemperatureSummer, averageTemperatureWinter);

        //test harvesting without planting and growing.
        int crop = millet.harvest();
        Assertions.assertEquals(0, crop);

        //test harvesting without growing
        millet = new Millet();
        millet.plant(acres);
        crop = millet.harvest();
        Assertions.assertEquals(0, crop);

        //test growing without planting
        millet = new Millet();
        millet.grow(conditions);
        crop = millet.harvest();
        Assertions.assertEquals(0, crop);


        //test plant and grow
        millet = new Millet();
        millet.plant(acres);
        millet.grow(conditions);
        crop = millet.harvest();
        Assertions.assertEquals(expectedCrop, crop);

        //test double harvest
        crop = millet.harvest();
        Assertions.assertEquals(0, crop);

        //test drought
        millet = new Millet();
        millet.plant(acres);
        millet.grow(conditions);
        millet.drought();
        crop = millet.harvest();
        Assertions.assertEquals(expectedCropDrought, crop);

        //test expectedCropFritFly
        millet = new Millet();
        millet.plant(acres);
        millet.grow(conditions);
        millet.drought();
        millet.pestInfestation(Grain.Pests.FritFly);
        crop = millet.harvest();
        Assertions.assertEquals(expectedCropFritFly, crop);

        //test expectedCropDeliaFly
        millet = new Millet();
        millet.plant(acres);
        millet.grow(conditions);
        millet.drought();
        millet.pestInfestation(Grain.Pests.FritFly);
        millet.pestInfestation(Grain.Pests.DeliaFly);
        crop = millet.harvest();
        Assertions.assertEquals(expectedCropDeliaFly, crop);

        //test expectedCropBarleyGoutFly
        millet = new Millet();
        millet.plant(acres);
        millet.grow(conditions);
        millet.drought();
        millet.pestInfestation(Grain.Pests.FritFly);
        millet.pestInfestation(Grain.Pests.DeliaFly);
        millet.pestInfestation(Grain.Pests.BarleyGoutFly);
        crop = millet.harvest();
        Assertions.assertEquals(expectedCropBarleyGoutFly, crop);

        //test expectedCropPowderyMildew
        millet = new Millet();
        millet.plant(acres);
        millet.grow(conditions);
        millet.drought();
        millet.pestInfestation(Grain.Pests.FritFly);
        millet.pestInfestation(Grain.Pests.DeliaFly);
        millet.pestInfestation(Grain.Pests.BarleyGoutFly);
        millet.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        crop = millet.harvest();
        Assertions.assertEquals(expectedCropPowderyMildew, crop);

        //test expectedCropLeafDrought
        millet = new Millet();
        millet.plant(acres);
        millet.grow(conditions);
        millet.drought();
        millet.pestInfestation(Grain.Pests.FritFly);
        millet.pestInfestation(Grain.Pests.DeliaFly);
        millet.pestInfestation(Grain.Pests.BarleyGoutFly);
        millet.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        millet.diseaseOutbreak(Grain.Diseases.LeafDrought);
        crop = millet.harvest();
        Assertions.assertEquals(expectedCropLeafDrought, crop);

        //test expectedCropFusarium
        millet = new Millet();
        millet.plant(acres);
        millet.grow(conditions);
        millet.drought();
        millet.pestInfestation(Grain.Pests.FritFly);
        millet.pestInfestation(Grain.Pests.DeliaFly);
        millet.pestInfestation(Grain.Pests.BarleyGoutFly);
        millet.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        millet.diseaseOutbreak(Grain.Diseases.LeafDrought);
        millet.diseaseOutbreak(Grain.Diseases.Fusarium);
        crop = millet.harvest();
        Assertions.assertEquals(expectedCropFusarium, crop);
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/wheatTestData.csv", numLinesToSkip = 1, encoding = "UTF-8" )
    public void testWheat(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, int acres, int expectedCrop, int expectedCropDrought, int expectedCropFritFly, int expectedCropDeliaFly, int expectedCropBarleyGoutFly, int expectedCropPowderyMildew, int expectedCropLeafDrought, int expectedCropFusarium) {

        Wheat wheat = new Wheat();
        Grain.Conditions conditions = new Grain.Conditions(soilConditions, averageTemperatureSummer, averageTemperatureWinter);

        //test harvesting without planting and growing.
        int crop = wheat.harvest();
        Assertions.assertEquals(0, crop);

        //test harvesting without growing
        wheat = new Wheat();
        wheat.plant(acres);
        crop = wheat.harvest();
        Assertions.assertEquals(0, crop);

        //test growing without planting
        wheat = new Wheat();
        wheat.grow(conditions);
        crop = wheat.harvest();
        Assertions.assertEquals(0, crop);

        //test plant and grow
        wheat = new Wheat();
        wheat.plant(acres);
        wheat.grow(conditions);
        crop = wheat.harvest();
        Assertions.assertEquals(expectedCrop, crop);

        //test double harvest
        crop = wheat.harvest();
        Assertions.assertEquals(0, crop);

        //test drought
        wheat = new Wheat();
        wheat.plant(acres);
        wheat.grow(conditions);
        wheat.drought();
        crop = wheat.harvest();
        Assertions.assertEquals(expectedCropDrought, crop);

        //test expectedCropFritFly
        wheat = new Wheat();
        wheat.plant(acres);
        wheat.grow(conditions);
        wheat.drought();
        wheat.pestInfestation(Grain.Pests.FritFly);
        crop = wheat.harvest();
        Assertions.assertEquals(expectedCropFritFly, crop, 1);

        //test expectedCropDeliaFly
        wheat = new Wheat();
        wheat.plant(acres);
        wheat.grow(conditions);
        wheat.drought();
        wheat.pestInfestation(Grain.Pests.FritFly);
        wheat.pestInfestation(Grain.Pests.DeliaFly);
        crop = wheat.harvest();
        Assertions.assertEquals(expectedCropDeliaFly, crop,1);

        //test expectedCropBarleyGoutFly
        wheat = new Wheat();
        wheat.plant(acres);
        wheat.grow(conditions);
        wheat.drought();
        wheat.pestInfestation(Grain.Pests.FritFly);
        wheat.pestInfestation(Grain.Pests.DeliaFly);
        wheat.pestInfestation(Grain.Pests.BarleyGoutFly);
        crop = wheat.harvest();
        Assertions.assertEquals(expectedCropBarleyGoutFly, crop,1);

        //test expectedCropPowderyMildew
        wheat = new Wheat();
        wheat.plant(acres);
        wheat.grow(conditions);
        wheat.drought();
        wheat.pestInfestation(Grain.Pests.FritFly);
        wheat.pestInfestation(Grain.Pests.DeliaFly);
        wheat.pestInfestation(Grain.Pests.BarleyGoutFly);
        wheat.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        crop = wheat.harvest();
        Assertions.assertEquals(expectedCropPowderyMildew, crop, 1);

        //test expectedCropLeafDrought
        wheat = new Wheat();
        wheat.plant(acres);
        wheat.grow(conditions);
        wheat.drought();
        wheat.pestInfestation(Grain.Pests.FritFly);
        wheat.pestInfestation(Grain.Pests.DeliaFly);
        wheat.pestInfestation(Grain.Pests.BarleyGoutFly);
        wheat.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        wheat.diseaseOutbreak(Grain.Diseases.LeafDrought);
        crop = wheat.harvest();
        Assertions.assertEquals(expectedCropLeafDrought, crop, 1);

        //test expectedCropFusarium
        wheat = new Wheat();
        wheat.plant(acres);
        wheat.grow(conditions);
        wheat.drought();
        wheat.pestInfestation(Grain.Pests.FritFly);
        wheat.pestInfestation(Grain.Pests.DeliaFly);
        wheat.pestInfestation(Grain.Pests.BarleyGoutFly);
        wheat.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        wheat.diseaseOutbreak(Grain.Diseases.LeafDrought);
        wheat.diseaseOutbreak(Grain.Diseases.Fusarium);
        crop = wheat.harvest();
        Assertions.assertEquals(expectedCropFusarium, crop, 1);
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/riceTestData.csv", numLinesToSkip = 1, encoding = "UTF-8" )
    public void testRice(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, int acres, int expectedCrop, int expectedCropDrought, int expectedCropFritFly, int expectedCropDeliaFly, int expectedCropBarleyGoutFly, int expectedCropPowderyMildew, int expectedCropLeafDrought, int expectedCropFusarium) {

        Rice rice = new Rice();
        Grain.Conditions conditions = new Grain.Conditions(soilConditions, averageTemperatureSummer, averageTemperatureWinter);

        //test harvesting without planting and growing.
        int crop = rice.harvest();
        Assertions.assertEquals(0, crop);

        //test harvesting without growing
        rice = new Rice();
        rice.plant(acres);
        crop = rice.harvest();
        Assertions.assertEquals(0, crop);

        //test growing without planting
        rice = new Rice();
        rice.grow(conditions);
        crop = rice.harvest();
        Assertions.assertEquals(0, crop);

        //test grow
        rice = new Rice();
        rice.plant(acres);
        rice.grow(conditions);
        crop = rice.harvest();
        Assertions.assertEquals(expectedCrop, crop);

        //test double harvest
        crop = rice.harvest();
        Assertions.assertEquals(0, crop);

        //test drought
        rice = new Rice();
        rice.plant(acres);
        rice.grow(conditions);
        rice.drought();
        crop = rice.harvest();
        Assertions.assertEquals(expectedCropDrought, crop);

        //test expectedCropFritFly
        rice = new Rice();
        rice.plant(acres);
        rice.grow(conditions);
        rice.drought();
        rice.pestInfestation(Grain.Pests.FritFly);
        crop = rice.harvest();
        Assertions.assertEquals(expectedCropFritFly, crop);

        //test expectedCropDeliaFly
        rice = new Rice();
        rice.plant(acres);
        rice.grow(conditions);
        rice.drought();
        rice.pestInfestation(Grain.Pests.FritFly);
        rice.pestInfestation(Grain.Pests.DeliaFly);
        crop = rice.harvest();
        Assertions.assertEquals(expectedCropDeliaFly, crop);

        //test expectedCropBarleyGoutFly
        rice = new Rice();
        rice.plant(acres);
        rice.grow(conditions);
        rice.drought();
        rice.pestInfestation(Grain.Pests.FritFly);
        rice.pestInfestation(Grain.Pests.DeliaFly);
        rice.pestInfestation(Grain.Pests.BarleyGoutFly);
        crop = rice.harvest();
        Assertions.assertEquals(expectedCropBarleyGoutFly, crop,1);

        //test expectedCropPowderyMildew
        rice = new Rice();
        rice.plant(acres);
        rice.grow(conditions);
        rice.drought();
        rice.pestInfestation(Grain.Pests.FritFly);
        rice.pestInfestation(Grain.Pests.DeliaFly);
        rice.pestInfestation(Grain.Pests.BarleyGoutFly);
        rice.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        crop = rice.harvest();
        Assertions.assertEquals(expectedCropPowderyMildew, crop, 1);

        //test expectedCropLeafDrought
        rice = new Rice();
        rice.plant(acres);
        rice.grow(conditions);
        rice.drought();
        rice.pestInfestation(Grain.Pests.FritFly);
        rice.pestInfestation(Grain.Pests.DeliaFly);
        rice.pestInfestation(Grain.Pests.BarleyGoutFly);
        rice.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        rice.diseaseOutbreak(Grain.Diseases.LeafDrought);
        crop = rice.harvest();
        Assertions.assertEquals(expectedCropLeafDrought, crop, 1);

        //test expectedCropFusarium
        rice = new Rice();
        rice.plant(acres);
        rice.grow(conditions);
        rice.drought();
        rice.pestInfestation(Grain.Pests.FritFly);
        rice.pestInfestation(Grain.Pests.DeliaFly);
        rice.pestInfestation(Grain.Pests.BarleyGoutFly);
        rice.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        rice.diseaseOutbreak(Grain.Diseases.LeafDrought);
        rice.diseaseOutbreak(Grain.Diseases.Fusarium);
        crop = rice.harvest();
        Assertions.assertEquals(expectedCropFusarium, crop);
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/ryeTestData.csv", numLinesToSkip = 1, encoding = "UTF-8" )
    public void testRye(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, int acres, int expectedCrop, int expectedCropDrought, int expectedCropFritFly, int expectedCropDeliaFly, int expectedCropBarleyGoutFly, int expectedCropPowderyMildew, int expectedCropLeafDrought, int expectedCropFusarium) {

        Rye rye = new Rye();
        Grain.Conditions conditions = new Grain.Conditions(soilConditions, averageTemperatureSummer, averageTemperatureWinter);

        //test harvesting without growing.
        int crop = rye.harvest();
        Assertions.assertEquals(0, crop);

        //test harvesting without growing
        rye = new Rye();
        rye.plant(acres);
        crop = rye.harvest();
        Assertions.assertEquals(0, crop);

        //test growing without planting
        rye = new Rye();
        rye.grow(conditions);
        crop = rye.harvest();
        Assertions.assertEquals(0, crop);

        //test grow
        rye = new Rye();
        rye.plant(acres);
        rye.grow(conditions);
        crop = rye.harvest();
        Assertions.assertEquals(expectedCrop, crop);

        //test double harvest
        crop = rye.harvest();
        Assertions.assertEquals(0, crop);

        //test drought
        rye = new Rye();
        rye.plant(acres);
        rye.grow(conditions);
        rye.drought();
        crop = rye.harvest();
        Assertions.assertEquals(expectedCropDrought, crop);

        //test expectedCropFritFly
        rye = new Rye();
        rye.plant(acres);
        rye.grow(conditions);
        rye.drought();
        rye.pestInfestation(Grain.Pests.FritFly);
        crop = rye.harvest();
        Assertions.assertEquals(expectedCropFritFly, crop);

        //test expectedCropDeliaFly
        rye = new Rye();
        rye.plant(acres);
        rye.grow(conditions);
        rye.drought();
        rye.pestInfestation(Grain.Pests.FritFly);
        rye.pestInfestation(Grain.Pests.DeliaFly);
        crop = rye.harvest();
        Assertions.assertEquals(expectedCropDeliaFly, crop);

        //test expectedCropBarleyGoutFly
        rye = new Rye();
        rye.plant(acres);
        rye.grow(conditions);
        rye.drought();
        rye.pestInfestation(Grain.Pests.FritFly);
        rye.pestInfestation(Grain.Pests.DeliaFly);
        rye.pestInfestation(Grain.Pests.BarleyGoutFly);
        crop = rye.harvest();
        Assertions.assertEquals(expectedCropBarleyGoutFly, crop,1);

        //test expectedCropPowderyMildew
        rye = new Rye();
        rye.plant(acres);
        rye.grow(conditions);
        rye.drought();
        rye.pestInfestation(Grain.Pests.FritFly);
        rye.pestInfestation(Grain.Pests.DeliaFly);
        rye.pestInfestation(Grain.Pests.BarleyGoutFly);
        rye.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        crop = rye.harvest();
        Assertions.assertEquals(expectedCropPowderyMildew, crop, 1);

        //test expectedCropLeafDrought
        rye = new Rye();
        rye.plant(acres);
        rye.grow(conditions);
        rye.drought();
        rye.pestInfestation(Grain.Pests.FritFly);
        rye.pestInfestation(Grain.Pests.DeliaFly);
        rye.pestInfestation(Grain.Pests.BarleyGoutFly);
        rye.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        rye.diseaseOutbreak(Grain.Diseases.LeafDrought);
        crop = rye.harvest();
        Assertions.assertEquals(expectedCropLeafDrought, crop, 1);

        //test expectedCropFusarium
        rye = new Rye();
        rye.plant(acres);
        rye.grow(conditions);
        rye.drought();
        rye.pestInfestation(Grain.Pests.FritFly);
        rye.pestInfestation(Grain.Pests.DeliaFly);
        rye.pestInfestation(Grain.Pests.BarleyGoutFly);
        rye.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        rye.diseaseOutbreak(Grain.Diseases.LeafDrought);
        rye.diseaseOutbreak(Grain.Diseases.Fusarium);
        crop = rye.harvest();
        Assertions.assertEquals(expectedCropFusarium, crop);
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/barleyTestData.csv", numLinesToSkip = 1, encoding = "UTF-8" )
    public void testBarley(float soilConditions, float averageTemperatureSummer, float averageTemperatureWinter, int acres, int expectedCrop, int expectedCropDrought, int expectedCropFritFly, int expectedCropDeliaFly, int expectedCropBarleyGoutFly, int expectedCropPowderyMildew, int expectedCropLeafDrought, int expectedCropFusarium) {

        Barley barley = new Barley();
        Grain.Conditions conditions = new Grain.Conditions(soilConditions, averageTemperatureSummer, averageTemperatureWinter);

        //test harvesting without growing.
        int crop = barley.harvest();
        Assertions.assertEquals(0, crop);

        //test harvesting without growing
        barley = new Barley();
        barley.plant(acres);
        crop = barley.harvest();
        Assertions.assertEquals(0, crop);

        //test growing without planting
        barley = new Barley();
        barley.grow(conditions);
        crop = barley.harvest();
        Assertions.assertEquals(0, crop);

        //test grow
        barley = new Barley();
        barley.plant(acres);
        barley.grow(conditions);
        crop = barley.harvest();
        Assertions.assertEquals(expectedCrop, crop);

        //test double harvest
        crop = barley.harvest();
        Assertions.assertEquals(0, crop);

        //test drought
        barley = new Barley();
        barley.plant(acres);
        barley.grow(conditions);
        barley.drought();
        crop = barley.harvest();
        Assertions.assertEquals(expectedCropDrought, crop);

        //test expectedCropFritFly
        barley = new Barley();
        barley.plant(acres);
        barley.grow(conditions);
        barley.drought();
        barley.pestInfestation(Grain.Pests.FritFly);
        crop = barley.harvest();
        Assertions.assertEquals(expectedCropFritFly, crop);

        //test expectedCropDeliaFly
        barley = new Barley();
        barley.plant(acres);
        barley.grow(conditions);
        barley.drought();
        barley.pestInfestation(Grain.Pests.FritFly);
        barley.pestInfestation(Grain.Pests.DeliaFly);
        crop = barley.harvest();
        Assertions.assertEquals(expectedCropDeliaFly, crop);

        //test expectedCropBarleyGoutFly
        barley = new Barley();
        barley.plant(acres);
        barley.grow(conditions);
        barley.drought();
        barley.pestInfestation(Grain.Pests.FritFly);
        barley.pestInfestation(Grain.Pests.DeliaFly);
        barley.pestInfestation(Grain.Pests.BarleyGoutFly);
        crop = barley.harvest();
        Assertions.assertEquals(expectedCropBarleyGoutFly, crop,1);

        //test expectedCropPowderyMildew
        barley = new Barley();
        barley.plant(acres);
        barley.grow(conditions);
        barley.drought();
        barley.pestInfestation(Grain.Pests.FritFly);
        barley.pestInfestation(Grain.Pests.DeliaFly);
        barley.pestInfestation(Grain.Pests.BarleyGoutFly);
        barley.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        crop = barley.harvest();
        Assertions.assertEquals(expectedCropPowderyMildew, crop, 1);

        //test expectedCropLeafDrought
        barley = new Barley();
        barley.plant(acres);
        barley.grow(conditions);
        barley.drought();
        barley.pestInfestation(Grain.Pests.FritFly);
        barley.pestInfestation(Grain.Pests.DeliaFly);
        barley.pestInfestation(Grain.Pests.BarleyGoutFly);
        barley.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        barley.diseaseOutbreak(Grain.Diseases.LeafDrought);
        crop = barley.harvest();
        Assertions.assertEquals(expectedCropLeafDrought, crop, 1);

        //test expectedCropFusarium
        barley = new Barley();
        barley.plant(acres);
        barley.grow(conditions);
        barley.drought();
        barley.pestInfestation(Grain.Pests.FritFly);
        barley.pestInfestation(Grain.Pests.DeliaFly);
        barley.pestInfestation(Grain.Pests.BarleyGoutFly);
        barley.diseaseOutbreak(Grain.Diseases.PowderyMildew);
        barley.diseaseOutbreak(Grain.Diseases.LeafDrought);
        barley.diseaseOutbreak(Grain.Diseases.Fusarium);
        crop = barley.harvest();
        Assertions.assertEquals(expectedCropFusarium, crop, 1);
    }
}
