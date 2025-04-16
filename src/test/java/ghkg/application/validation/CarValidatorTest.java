package ghkg.application.validation;

import ghkg.api.exception.InvalidCarDataException;
import ghkg.domain.Car;
import ghkg.domain.FuelType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the CarValidator class.
 * This class validates that a Car object adheres to certain rules
 * regarding its fuel type and engine capacity:
 * - Cars with certain fuel types (electric, fuel cell, other) must have a zero engine capacity.
 * - Cars with other fuel types must have an engine capacity greater than zero.
 */
class CarValidatorTest {

    private static final String ELECTRIC_CAR_NAME = "Electric Car";
    private static final String DIESEL_CAR_NAME = "Diesel Car";
    private static final String TEST_CAR_NAME = "Test Car";

    // Fuel type validation tests
    
    @Test
    void shouldThrowExceptionWhenFuelTypeIsNull() {
        Car car = buildTestCar(TEST_CAR_NAME, null, 0);
        
        assertThrows(InvalidCarDataException.class, 
                () -> CarValidator.validate(car), 
                "Fuel type is required");
    }

    // Zero engine capacity fuel type tests
    
    @Test
    void shouldThrowExceptionWhenEngineCapacityNotZeroForElectricCar() {
        Car car = buildTestCar(ELECTRIC_CAR_NAME, FuelType.ELECTRIC, 100);
        
        assertThrows(InvalidCarDataException.class, 
                () -> CarValidator.validate(car),
                "Cars with fuel type ELECTRIC must have engine capacity equal to 0");
    }

    @Test
    void shouldAcceptZeroEngineCapacityForElectricCar() {
        Car car = buildTestCar(ELECTRIC_CAR_NAME, FuelType.ELECTRIC, 0);
        
        assertDoesNotThrow(() -> CarValidator.validate(car));
    }

    // Non-zero engine capacity fuel type tests
    
    @Test
    void shouldThrowExceptionWhenEngineCapacityZeroForDieselCar() {
        Car car = buildTestCar(DIESEL_CAR_NAME, FuelType.DIESEL, 0);
        
        assertThrows(InvalidCarDataException.class, 
                () -> CarValidator.validate(car),
                "Cars with fuel type DIESEL must have engine capacity greater than 0");
    }

    @Test
    void shouldAcceptNonZeroEngineCapacityForDieselCar() {
        Car car = buildTestCar(DIESEL_CAR_NAME, FuelType.DIESEL, 2000);
        
        assertDoesNotThrow(() -> CarValidator.validate(car));
    }
    
    /**
     * Helper method to build a car for testing
     */
    private Car buildTestCar(String name, FuelType fuelType, int engineCapacity) {
        return Car.builder()
                .id(UUID.randomUUID())
                .name(name)
                .fuelType(fuelType)
                .engineCapacity(engineCapacity)
                .build();
    }
}