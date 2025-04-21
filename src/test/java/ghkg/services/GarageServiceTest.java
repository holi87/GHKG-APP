package ghkg.services;

import ghkg.api.exception.CarNotFoundException;
import ghkg.domain.car.Car;
import ghkg.domain.car.CarRepository;
import ghkg.domain.car.FuelType;
import ghkg.dto.car.CarDto;
import ghkg.dto.car.CarFilterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GarageServiceTest {

    private CarRepository carRepository;
    private GarageService garageService;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        garageService = new GarageService(carRepository);
    }

    @Test
    void shouldReturnPagedCars() {
        CarFilterDto filter = new CarFilterDto("Tesla", FuelType.ELECTRIC, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Car car = Car.builder()
                .id(UUID.randomUUID().getMostSignificantBits())
                .name("Tesla")
                .fuelType(FuelType.ELECTRIC)
                .engineCapacity(0)
                .build();

        when(carRepository.findAll(any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(car)));

        Page<CarDto> result = garageService.getCars(filter, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Tesla");
    }

    @Test
    void shouldReturnCarById() {
        Long id = UUID.randomUUID().getMostSignificantBits();
        Car car = new Car(id, "BMW", 2000, FuelType.GASOLINE);
        when(carRepository.findById(id)).thenReturn(Optional.of(car));

        Car result = garageService.getCarById(id);

        assertThat(result).isEqualTo(car);
    }

    @Test
    void shouldThrowWhenCarNotFoundById() {
        Long id = UUID.randomUUID().getMostSignificantBits();
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> garageService.getCarById(id))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void shouldAddCar() {
        Car car = new Car(null, "Hyundai", 1600, FuelType.HYBRID);
        Car saved = new Car(1L, "Hyundai", 1600, FuelType.HYBRID);

        when(carRepository.save(car)).thenReturn(saved);

        Car result = garageService.addCar(car);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Hyundai");
    }

    @Test
    void shouldUpdateCar() {
        Long id = 1L;
        Car car = new Car(null, "VW", 1900, FuelType.DIESEL);
        Car updated = new Car(id, "VW", 1900, FuelType.DIESEL);

        when(carRepository.save(any(Car.class))).thenReturn(updated);

        Car result = garageService.updateCar(id, car);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("VW");
    }

    @Test
    void shouldDeleteCar() {
        Long id = UUID.randomUUID().getMostSignificantBits();
        when(carRepository.existsById(id)).thenReturn(true);

        garageService.deleteCar(id);

        verify(carRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingMissingCar() {
        Long id = 1L;
        when(carRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> garageService.deleteCar(id))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
