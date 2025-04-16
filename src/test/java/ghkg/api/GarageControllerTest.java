package ghkg.api;

import ghkg.api.exception.InvalidCarDataException;
import ghkg.application.GarageService;
import ghkg.domain.Car;
import ghkg.domain.FuelType;
import ghkg.dto.CarDto;
import ghkg.dto.CreateCarDto;
import ghkg.dto.UpdateCarDto;
import ghkg.mapper.CarMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GarageControllerTest {
    // Constants for test data
    private static final String TEST_CAR_NAME = "Test Car";
    private static final String UPDATED_CAR_NAME = "Updated Car";
    private static final FuelType DEFAULT_FUEL_TYPE = FuelType.GASOLINE;
    private static final FuelType UPDATED_FUEL_TYPE = FuelType.DIESEL;
    private static final int DEFAULT_ENGINE_CAPACITY = 4;
    private static final int UPDATED_ENGINE_CAPACITY = 5;
    private static final String ID_MISMATCH_ERROR = "Path ID and payload ID do not match";

    @Mock
    private GarageService garageService;

    @InjectMocks
    private GarageController garageController;

    private UUID carId;
    private Car testCar;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carId = UUID.randomUUID();
        
        // Use builder pattern for more readable object creation
        testCar = Car.builder()
                .id(carId)
                .name(TEST_CAR_NAME)
                .fuelType(DEFAULT_FUEL_TYPE)
                .engineCapacity(DEFAULT_ENGINE_CAPACITY)
                .build();
    }

    @Test
    void shouldReturnAllCarsWhenGetAllIsCalled() {
        // Arrange
        when(garageService.getAllCars()).thenReturn(List.of(testCar));
        
        // Act
        List<CarDto> cars = garageController.getAll();
        
        // Assert
        assertEquals(1, cars.size());
        assertEquals(TEST_CAR_NAME, cars.get(0).name());
        verify(garageService).getAllCars();
    }

    @Test
    void shouldReturnCarWhenGetByIdIsCalledWithExistingId() {
        // Arrange
        when(garageService.getCarById(carId)).thenReturn(testCar);
        
        // Act
        CarDto foundCar = garageController.getById(carId);
        
        // Assert
        assertNotNull(foundCar);
        assertEquals(TEST_CAR_NAME, foundCar.name());
        verify(garageService).getCarById(carId);
    }

    @Test
    void shouldSaveAndReturnCarWhenAddIsCalled() {
        // Arrange
        CreateCarDto createCarDto = new CreateCarDto(TEST_CAR_NAME, DEFAULT_FUEL_TYPE, DEFAULT_ENGINE_CAPACITY);
        when(garageService.addCar(any(Car.class))).thenReturn(testCar);
        
        // Act
        CarDto savedCar = garageController.add(createCarDto);
        
        // Assert
        assertNotNull(savedCar);
        assertEquals(TEST_CAR_NAME, savedCar.name());
        verify(garageService).addCar(any(Car.class));
    }

    @Test
    void shouldDeleteCarWhenDeleteIsCalled() {
        // Arrange
        doNothing().when(garageService).deleteCar(carId);
        
        // Act
        garageController.delete(carId);
        
        // Assert
        verify(garageService).deleteCar(carId);
    }

    @Test
    void shouldUpdateAndReturnCarWhenUpdateIsCalledWithValidData() {
        // Arrange
        UpdateCarDto updateCarDto = new UpdateCarDto(carId, UPDATED_CAR_NAME, UPDATED_FUEL_TYPE, UPDATED_ENGINE_CAPACITY);
        Car updatedCar = CarMapper.fromUpdateDto(updateCarDto);
        when(garageService.updateCar(carId, updatedCar)).thenReturn(updatedCar);
        
        // Act
        CarDto updatedCarDto = garageController.update(carId, updateCarDto);
        
        // Assert
        assertNotNull(updatedCarDto);
        assertEquals(UPDATED_CAR_NAME, updatedCarDto.name());
        assertEquals(UPDATED_FUEL_TYPE, updatedCarDto.fuelType());
        assertEquals(UPDATED_ENGINE_CAPACITY, updatedCarDto.engineCapacity());
        verify(garageService).updateCar(carId, updatedCar);
    }

    @Test
    void shouldThrowExceptionWhenUpdateIsCalledWithMismatchedIds() {
        // Arrange
        UUID differentId = UUID.randomUUID();
        UpdateCarDto updateCarDto = new UpdateCarDto(differentId, UPDATED_CAR_NAME, UPDATED_FUEL_TYPE, UPDATED_ENGINE_CAPACITY);
        
        // Act & Assert
        InvalidCarDataException exception = assertThrows(
            InvalidCarDataException.class, 
            () -> garageController.update(carId, updateCarDto)
        );
        
        assertEquals(ID_MISMATCH_ERROR, exception.getMessage());
        verify(garageService, never()).updateCar(any(), any());
    }
}