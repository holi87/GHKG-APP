package ghkg.mapper;

import ghkg.domain.Car;
import ghkg.dto.CreateCarDto;
import ghkg.domain.FuelType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarMapperTest {

    /**
     * Unit tests for the {@link CarMapper#fromCreateDto(CreateCarDto)} method.
     * This method maps a {@link CreateCarDto} object to a {@link Car} object.
     */

    @Test
    void testFromCreateDto_whenValidInput_thenReturnCarObjectWithCorrectValues() {
        // Arrange
        CreateCarDto createCarDto = new CreateCarDto("Tesla Model S", FuelType.ELECTRIC, 1000);

        // Act
        Car car = CarMapper.fromCreateDto(createCarDto);

        // Assert
        assertNotNull(car, "Car object should not be null.");
        assertEquals("Tesla Model S", car.getName(), "Car name should match the input DTO.");
        assertEquals(FuelType.ELECTRIC, car.getFuelType(), "Car fuel type should match the input DTO.");
        assertEquals(1000, car.getEngineCapacity(), "Car engine capacity should match the input DTO.");
    }

    @Test
    void testFromCreateDto_whenEmptyName_thenReturnCarWithEmptyName() {
        // Arrange
        CreateCarDto createCarDto = new CreateCarDto("", FuelType.GASOLINE, 1200);

        // Act
        Car car = CarMapper.fromCreateDto(createCarDto);

        // Assert
        assertNotNull(car, "Car object should not be null.");
        assertEquals("", car.getName(), "Car name should match the input DTO.");
        assertEquals(FuelType.GASOLINE, car.getFuelType(), "Car fuel type should match the input DTO.");
        assertEquals(1200, car.getEngineCapacity(), "Car engine capacity should match the input DTO.");
    }

    @Test
    void testFromCreateDto_whenNullFuelType_thenReturnCarWithNullFuelType() {
        // Arrange
        CreateCarDto createCarDto = new CreateCarDto("Generic Car", null, 800);

        // Act
        Car car = CarMapper.fromCreateDto(createCarDto);

        // Assert
        assertNotNull(car, "Car object should not be null.");
        assertEquals("Generic Car", car.getName(), "Car name should match the input DTO.");
        assertEquals(null, car.getFuelType(), "Car fuel type should match the input DTO.");
        assertEquals(800, car.getEngineCapacity(), "Car engine capacity should match the input DTO.");
    }

    @Test
    void testFromCreateDto_whenNegativeEngineCapacity_thenReturnCarWithNegativeEngineCapacity() {
        // Arrange
        CreateCarDto createCarDto = new CreateCarDto("Faulty Engine Car", FuelType.DIESEL, -500);

        // Act
        Car car = CarMapper.fromCreateDto(createCarDto);

        // Assert
        assertNotNull(car, "Car object should not be null.");
        assertEquals("Faulty Engine Car", car.getName(), "Car name should match the input DTO.");
        assertEquals(FuelType.DIESEL, car.getFuelType(), "Car fuel type should match the input DTO.");
        assertEquals(-500, car.getEngineCapacity(), "Car engine capacity should match the input DTO.");
    }
}