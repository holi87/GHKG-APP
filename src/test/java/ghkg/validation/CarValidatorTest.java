package ghkg.validation;

import ghkg.api.exception.InvalidCarDataException;
import ghkg.domain.car.Car;
import ghkg.domain.car.FuelType;
import ghkg.services.validation.CarValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarValidatorTest {

    @Test
    void shouldThrowExceptionWhenFuelTypeIsNull() {
        Car car = new Car();
        car.setFuelType(null);

        InvalidCarDataException ex = assertThrows(InvalidCarDataException.class,
                () -> CarValidator.validate(car));

        assertEquals("Fuel type is required", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionForZeroCapacityRequiredButNotZero() {
        Car car = new Car();
        car.setFuelType(FuelType.ELECTRIC);
        car.setEngineCapacity(1000);

        InvalidCarDataException ex = assertThrows(InvalidCarDataException.class,
                () -> CarValidator.validate(car));

        assertTrue(ex.getMessage().contains("must have engine capacity equal to 0"));
    }

    @Test
    void shouldThrowExceptionForNonZeroCapacityRequiredButZero() {
        Car car = new Car();
        car.setFuelType(FuelType.DIESEL);
        car.setEngineCapacity(0);

        InvalidCarDataException ex = assertThrows(InvalidCarDataException.class,
                () -> CarValidator.validate(car));

        assertTrue(ex.getMessage().contains("must have engine capacity greater than 0"));
    }

    @Test
    void shouldPassValidationForElectricWithZeroCapacity() {
        Car car = new Car();
        car.setFuelType(FuelType.ELECTRIC);
        car.setEngineCapacity(0);

        assertDoesNotThrow(() -> CarValidator.validate(car));
    }

    @Test
    void shouldPassValidationForDieselWithPositiveCapacity() {
        Car car = new Car();
        car.setFuelType(FuelType.DIESEL);
        car.setEngineCapacity(1500);

        assertDoesNotThrow(() -> CarValidator.validate(car));
    }
}
