package ghkg.application.validation;

import ghkg.api.exception.InvalidCarDataException;
import ghkg.domain.Car;
import ghkg.domain.FuelType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CarValidatorTest {

    /**
     * Tests for the CarValidator class.
     * This class validates that a Car object adheres to certain rules
     * regarding its fuel type and engine capacity:
     * - Cars with certain fuel types (electric, fuel cell, other) must have a zero engine capacity.
     * - Cars with other fuel types must have an engine capacity greater than zero.
     */

    @Test
    void validate_ShouldThrowException_WhenFuelTypeIsNull() {
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .name("Test Car")
                .fuelType(null)
                .build();

        assertThrows(InvalidCarDataException.class, () -> CarValidator.validate(car), "Fuel type is required");
    }

    @Test
    void validate_ShouldThrowException_WhenEngineCapacityIsNotZero_ForElectricFuelType() {
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .name("Electric Car")
                .fuelType(FuelType.ELECTRIC)
                .engineCapacity(100)
                .build();

        assertThrows(InvalidCarDataException.class, () -> CarValidator.validate(car),
                "Cars with fuel type ELECTRIC must have engine capacity equal to 0");
    }

    @Test
    void validate_ShouldNotThrowException_WhenEngineCapacityIsZero_ForElectricFuelType() {
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .name("Electric Car")
                .fuelType(FuelType.ELECTRIC)
                .engineCapacity(0)
                .build();

        CarValidator.validate(car); // No exception should be thrown
    }

    @Test
    void validate_ShouldThrowException_WhenEngineCapacityIsZero_ForNonElectricFuelType() {
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .name("Diesel Car")
                .fuelType(FuelType.DIESEL)
                .engineCapacity(0)
                .build();

        assertThrows(InvalidCarDataException.class, () -> CarValidator.validate(car),
                "Cars with fuel type DIESEL must have engine capacity greater than 0");
    }

    @Test
    void validate_ShouldNotThrowException_WhenEngineCapacityIsGreaterThanZero_ForNonElectricFuelType() {
        Car car = Car.builder()
                .id(UUID.randomUUID())
                .name("Diesel Car")
                .fuelType(FuelType.DIESEL)
                .engineCapacity(2000)
                .build();

        CarValidator.validate(car); // No exception should be thrown
    }
}