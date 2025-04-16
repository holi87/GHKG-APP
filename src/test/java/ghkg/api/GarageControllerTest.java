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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class GarageControllerTest {

    @Mock
    private GarageService garageService;

    @InjectMocks
    private GarageController garageController;

    private Car car;
    private CarDto carDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        car = new Car();
        car.setId(UUID.randomUUID());
        car.setName("Test Car");

        carDto = CarMapper.toDto(car);
    }

    @Test
    void getAll_ShouldReturnAllCars() {
        when(garageService.getAllCars()).thenReturn(List.of(car));

        List<CarDto> cars = garageController.getAll();

        assertEquals("Size mismatch", 1, cars.size());
        assertEquals("Name mismatch", "Test Car", cars.get(0).name());
        verify(garageService, times(1)).getAllCars();
    }

    @Test
    void getById_ShouldReturnCar_WhenExists() {
        when(garageService.getCarById(car.getId())).thenReturn(car);

        CarDto foundCar = garageController.getById(car.getId());

        assertNotNull(foundCar);
        assertEquals("Name mismatch", "Test Car", foundCar.name());
        verify(garageService, times(1)).getCarById(car.getId());
    }

    @Test
    void add_ShouldSaveAndReturnCar() {
        CreateCarDto createCarDto = new CreateCarDto("Test Car", FuelType.GASOLINE, 4);
        when(garageService.addCar(any(Car.class))).thenReturn(car);

        CarDto savedCar = garageController.add(createCarDto);

        assertNotNull(savedCar);
        assertEquals("Name mismatch", "Test Car", savedCar.name());
        verify(garageService, times(1)).addCar(any(Car.class));
    }

    @Test
    void delete_ShouldDeleteCar() {
        doNothing().when(garageService).deleteCar(car.getId());

        garageController.delete(car.getId());

        verify(garageService, times(1)).deleteCar(car.getId());
    }

    @Test
    void update_ShouldUpdateAndReturnCar_WhenValidData() {
        UpdateCarDto updateCarDto = new UpdateCarDto(car.getId(), "Updated Car", FuelType.DIESEL, 5);
        Car updatedCar = CarMapper.fromUpdateDto(updateCarDto);
        when(garageService.updateCar(car.getId(), updatedCar)).thenReturn(updatedCar);
        when(CarMapper.toDto(updatedCar)).thenReturn(new CarDto(car.getId(), "Updated Car", FuelType.DIESEL, 5));

        CarDto updatedCarDto = garageController.update(car.getId(), updateCarDto);

        assertNotNull(updatedCarDto);
        assertEquals("Name mismatch", "Updated Car", updatedCarDto.name());
        assertEquals("Fuel type mismatch", FuelType.DIESEL, updatedCarDto.fuelType());
        assertEquals("Engine capacity mismatch", 5, updatedCarDto.engineCapacity());
        verify(garageService, times(1)).updateCar(car.getId(), updatedCar);
    }

    @Test
    void update_ShouldThrowException_WhenIdsDoNotMatch() {
        UpdateCarDto updateCarDto = new UpdateCarDto(UUID.randomUUID(), "Updated Car", FuelType.DIESEL, 5);

        InvalidCarDataException exception =
                assertThrows(InvalidCarDataException.class, () -> garageController.update(car.getId(), updateCarDto));

        assertEquals("Msg mismatch","Path ID and payload ID do not match", exception.getMessage());
        verify(garageService, never()).updateCar(any(), any());
    }
}