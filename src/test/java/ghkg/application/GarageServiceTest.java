package ghkg.application;

import ghkg.api.exception.CarNotFoundException;
import ghkg.domain.Car;
import ghkg.domain.CarRepository;
import ghkg.domain.FuelType;
import ghkg.dto.CarFilterDto;
import ghkg.infrastructure.spec.CarSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GarageServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private GarageService garageService;

    private Car car;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        car = new Car();
        car.setId(UUID.randomUUID());
        car.setName("Test Car");
    }

    @Test
    void getAllCars_ShouldReturnAllCars() {
        when(carRepository.findAll()).thenReturn(List.of(car));

        List<Car> cars = garageService.getAllCars();

        assertEquals(1, cars.size());
        assertEquals("Test Car", cars.get(0).getName());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCarById_ShouldReturnCar_WhenExists() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        Car foundCar = garageService.getCarById(car.getId());

        assertNotNull(foundCar);
        assertEquals("Test Car", foundCar.getName());
        verify(carRepository, times(1)).findById(car.getId());
    }

    @Test
    void getCarById_ShouldThrowException_WhenNotFound() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> garageService.getCarById(car.getId()));
        verify(carRepository, times(1)).findById(car.getId());
    }

    @Test
    void addCar_ShouldSaveAndReturnCar() {
        when(carRepository.save(car)).thenReturn(car);

        Car savedCar = garageService.addCar(car);

        assertNotNull(savedCar);
        assertEquals("Test Car", savedCar.getName());
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void addCar_ShouldThrowException_WhenCarIsInvalid() {
        // This test would require PowerMockito or Mockito extension for static methods
        // For now, we'll just test the exception behavior directly
        car.setFuelType(FuelType.ELECTRIC);
        car.setEngineCapacity(100); // Invalid for ELECTRIC cars

        assertThrows(IllegalArgumentException.class, () -> garageService.addCar(car));
        verify(carRepository, never()).save(any());
    }

    @Test
    void deleteCar_ShouldDeleteCar_WhenExists() {
        when(carRepository.existsById(car.getId())).thenReturn(true);

        garageService.deleteCar(car.getId());

        verify(carRepository, times(1)).deleteById(car.getId());
    }

    @Test
    void deleteCar_ShouldThrowException_WhenNotFound() {
        when(carRepository.existsById(car.getId())).thenReturn(false);

        assertThrows(CarNotFoundException.class, () -> garageService.deleteCar(car.getId()));
        verify(carRepository, never()).deleteById(car.getId());
    }

    @Test
    void findByFilter_ShouldReturnFilteredCars() {
        CarFilterDto filter = new CarFilterDto();
        Specification<Car> spec = CarSpecifications.nameContains(filter.getName());
        when(carRepository.findAll(any(Specification.class))).thenReturn(List.of(car));

        List<Car> cars = garageService.findByFilter(filter);

        assertEquals(1, cars.size());
        verify(carRepository, times(1)).findAll(any(Specification.class));
    }
}