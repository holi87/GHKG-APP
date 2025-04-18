package ghkg.application;

import ghkg.api.exception.CarNotFoundException;
import ghkg.api.exception.InvalidCarDataException;
import ghkg.domain.Car;
import ghkg.domain.CarRepository;
import ghkg.domain.FuelType;
import ghkg.dto.car.CarFilterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GarageServiceTest {
    @Mock
    private CarRepository carRepository;
    
    @InjectMocks
    private GarageService garageService;
    
    private UUID testCarId;
    private Pageable testPageable;
    private final String TEST_CAR_NAME = "Test Car";
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testCarId = UUID.randomUUID();
    }
    
    @Test
    void shouldReturnCar_whenCarExistsWithGivenId() {
        // Given
        Car testCar = createTestCar(testCarId, TEST_CAR_NAME);
        when(carRepository.findById(testCarId)).thenReturn(Optional.of(testCar));
        
        // When
        Car foundCar = garageService.getCarById(testCarId);
        
        // Then
        assertNotNull(foundCar);
        assertEquals(TEST_CAR_NAME, foundCar.getName());
        verify(carRepository).findById(testCarId);
        testPageable = PageRequest.of(0, 5, Sort.by("name"));
    }
    
    @Test
    void shouldThrowCarNotFoundException_whenCarDoesNotExistWithGivenId() {
        // Given
        when(carRepository.findById(testCarId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(CarNotFoundException.class, () -> garageService.getCarById(testCarId));
        verify(carRepository).findById(testCarId);
    }
    
    @Test
    void shouldSaveAndReturnCar_whenAddCarIsCalledWithValidCar() {
        // Given
        Car testCar = createTestCar(testCarId, TEST_CAR_NAME);
        when(carRepository.save(testCar)).thenReturn(testCar);
        
        // When
        Car savedCar = garageService.addCar(testCar);
        
        // Then
        assertNotNull(savedCar);
        assertEquals(TEST_CAR_NAME, savedCar.getName());
        verify(carRepository).save(testCar);
    }
    
    @Test
    void shouldThrowIllegalArgumentException_whenAddCarIsCalledWithInvalidCar() {
        // Given
        Car invalidCar = createTestCar(testCarId, TEST_CAR_NAME);
        invalidCar.setFuelType(FuelType.ELECTRIC);
        invalidCar.setEngineCapacity(100); // Invalid for ELECTRIC cars
        
        // When/Then
        assertThrows(InvalidCarDataException.class, () -> garageService.addCar(invalidCar));
        verify(carRepository, never()).save(any());
    }
    
    @Test
    void shouldDeleteCar_whenCarExistsWithGivenId() {
        // Given
        when(carRepository.existsById(testCarId)).thenReturn(true);
        
        // When
        garageService.deleteCar(testCarId);
        
        // Then
        verify(carRepository).deleteById(testCarId);
    }
    
    @Test
    void shouldThrowCarNotFoundException_whenDeletingNonExistentCar() {
        // Given
        when(carRepository.existsById(testCarId)).thenReturn(false);
        
        // When/Then
        assertThrows(CarNotFoundException.class, () -> garageService.deleteCar(testCarId));
        verify(carRepository, never()).deleteById(any());
    }

    /**
     * Creates a test car with the specified id and name
     */
    private Car createTestCar(UUID id, String name) {
        Car car = new Car();
        car.setId(id);
        car.setName(name);
        car.setFuelType(FuelType.FUEL_CELL);
        return car;
    }
    
    /**
     * Creates a car filter with the specified parameters
     */
    private CarFilterDto createCarFilter(String name, FuelType type, Integer minCapacity, Integer maxCapacity) {
        return CarFilterDto.builder()
                .name(name)
                .type(type)
                .minCapacity(minCapacity)
                .maxCapacity(maxCapacity)
                .build();
    }
}