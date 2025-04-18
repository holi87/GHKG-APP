package ghkg.application;

import ghkg.api.exception.CarNotFoundException;
import ghkg.api.exception.InvalidCarDataException;
import ghkg.domain.car.Car;
import ghkg.domain.car.CarRepository;
import ghkg.domain.car.FuelType;
import ghkg.dto.car.CarFilterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
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
    private final String TEST_CAR_NAME = "Test Car";
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testCarId = UUID.randomUUID();
    }
    
    @Test
    void shouldReturnCar_whenCarExistsWithGivenId() {
        // Given
        Car testCar = createTestCar(testCarId);
        when(carRepository.findById(testCarId)).thenReturn(Optional.of(testCar));
        
        // When
        Car foundCar = garageService.getCarById(testCarId);
        
        // Then
        assertNotNull(foundCar);
        assertEquals(TEST_CAR_NAME, foundCar.getName());
        verify(carRepository).findById(testCarId);
        Pageable testPageable = PageRequest.of(0, 5, Sort.by("name"));
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
        Car testCar = createTestCar(testCarId);
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
        Car invalidCar = createTestCar(testCarId);
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
    private Car createTestCar(UUID id) {
        Car car = new Car();
        car.setId(id);
        car.setName("Test Car");
        car.setFuelType(FuelType.FUEL_CELL);
        car.setEngineCapacity(0);
        return car;
    }

    /**
     * Creates a car for tests with full details
     */
    private Car createDetailedTestCar(String name, FuelType fuelType, int engineCapacity) {
        Car car = new Car();
        car.setId(UUID.randomUUID());
        car.setName(name);
        car.setFuelType(fuelType);
        car.setEngineCapacity(engineCapacity);
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

    @Test
    void shouldReturnPageOfCars_whenFilterApplied() {
        // Given
        CarFilterDto filter = createCarFilter("Test", FuelType.GASOLINE, 1500, 2500);
        Pageable pageable = PageRequest.of(0, 2);
        List<Car> cars = List.of(
                createDetailedTestCar("Test Car 1", FuelType.GASOLINE, 2000),
                createDetailedTestCar("Test Car 2", FuelType.GASOLINE, 1800)
        );
        when(carRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(cars));

        // When
        var result = garageService.getCars(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(carRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldReturnPaginatedCars_whenNoFilterProvided() {
        // Given
        CarFilterDto emptyFilter = createCarFilter(null, null, null, null);
        Pageable pageable = PageRequest.of(0, 3);
        List<Car> cars = List.of(
                createDetailedTestCar("Car A", FuelType.DIESEL, 3000),
                createDetailedTestCar("Car B", FuelType.ELECTRIC, 0),
                createDetailedTestCar("Car C", FuelType.GASOLINE, 1800)
        );
        when(carRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(cars));

        // When
        var result = garageService.getCars(emptyFilter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
    }

    @Test
    void shouldReturnEmptyPage_whenNoCarsMatchFilter() {
        // Given
        CarFilterDto unmatchedFilter = createCarFilter("Nonexistent", FuelType.HYBRID, 5000, 7000);
        Pageable pageable = PageRequest.of(0, 2);
        when(carRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(Page.empty());

        // When
        var result = garageService.getCars(unmatchedFilter, pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}