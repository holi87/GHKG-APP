package ghkg.mapper;

import ghkg.domain.Car;
import ghkg.dto.CreateCarDto;
import ghkg.domain.FuelType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarMapperTest {
    /**
     * Unit tests for the {@link CarMapper#fromCreateDto(CreateCarDto)} method.
     * This method maps a {@link CreateCarDto} object to a {@link Car} object.
     */
    
    @Test
    void shouldMapValidInputToCarObject() {
        // Arrange
        CreateCarDto dto = new CreateCarDto("Tesla Model S", FuelType.ELECTRIC, 1000);
        
        // Act
        Car car = CarMapper.fromCreateDto(dto);
        
        // Assert
        assertCarMatchesDto(car, dto);
    }
    
    @Test
    void shouldHandleEmptyName() {
        // Arrange
        CreateCarDto dto = new CreateCarDto("", FuelType.GASOLINE, 1200);
        
        // Act
        Car car = CarMapper.fromCreateDto(dto);
        
        // Assert
        assertCarMatchesDto(car, dto);
    }
    
    @Test
    void shouldHandleNullFuelType() {
        // Arrange
        CreateCarDto dto = new CreateCarDto("Generic Car", null, 800);
        
        // Act
        Car car = CarMapper.fromCreateDto(dto);
        
        // Assert
        assertCarMatchesDto(car, dto);
    }
    
    @Test
    void shouldHandleNegativeEngineCapacity() {
        // Arrange
        CreateCarDto dto = new CreateCarDto("Faulty Engine Car", FuelType.DIESEL, -500);
        
        // Act
        Car car = CarMapper.fromCreateDto(dto);
        
        // Assert
        assertCarMatchesDto(car, dto);
    }
    
    /**
     * Helper method to verify that a Car object correctly reflects the data from a CreateCarDto
     */
    private void assertCarMatchesDto(Car car, CreateCarDto dto) {
        assertNotNull(car, "Car object should not be null.");
        assertEquals(dto.name(), car.getName(), "Car name should match the input DTO.");
        assertEquals(dto.fuelType(), car.getFuelType(), "Car fuel type should match the input DTO.");
        assertEquals(dto.engineCapacity(), car.getEngineCapacity(), "Car engine capacity should match the input DTO.");
    }
}