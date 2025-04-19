package ghkg.api;

import ghkg.application.GarageService;
import ghkg.domain.car.Car;
import ghkg.dto.PageResponse;
import ghkg.dto.car.CarDto;
import ghkg.dto.car.CarFilterDto;
import ghkg.dto.car.CreateCarDto;
import ghkg.dto.car.UpdateCarDto;
import ghkg.mapper.CarMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GarageControllerTest {

    private GarageService garageService;
    private GarageController controller;

    @BeforeEach
    void setUp() {
        garageService = mock(GarageService.class);
        controller = new GarageController(garageService);
    }

    @Test
    void shouldGetCarsWithPaginationAndFilter() {
        // given
        CarFilterDto filter = new CarFilterDto("Tesla", null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        CarDto carDto = new CarDto(UUID.randomUUID(), "Tesla", ghkg.domain.car.FuelType.ELECTRIC, 0);
        Page<CarDto> page = new PageImpl<>(List.of(carDto));

        when(garageService.getCars(filter, pageable)).thenReturn(page);

        // when
        ResponseEntity<PageResponse<CarDto>> response = controller.getCars(filter, pageable);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).containsExactly(carDto);
    }

    @Test
    void shouldGetCarById() {
        // given
        UUID id = UUID.randomUUID();
        Car car = Car.builder().id(id).name("Car").fuelType(ghkg.domain.car.FuelType.GASOLINE).engineCapacity(1000).build();

        when(garageService.getCarById(id)).thenReturn(car);

        // when
        CarDto result = controller.getById(id);

        // then
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Car");
    }

    @Test
    void shouldAddNewCar() {
        // given
        CreateCarDto createDto = new CreateCarDto("NewCar", ghkg.domain.car.FuelType.DIESEL, 1600);
        Car car = CarMapper.fromCreateDto(createDto);
        Car saved = Car.builder().id(UUID.randomUUID()).name("NewCar").fuelType(ghkg.domain.car.FuelType.DIESEL).engineCapacity(1600).build();

        when(garageService.addCar(any(Car.class))).thenReturn(saved);

        // when
        CarDto result = controller.add(createDto);

        // then
        assertThat(result.name()).isEqualTo("NewCar");
        assertThat(result.engineCapacity()).isEqualTo(1600);
    }

    @Test
    void shouldUpdateCar() {
        // given
        UUID id = UUID.randomUUID();
        UpdateCarDto dto = new UpdateCarDto(id, "Updated", ghkg.domain.car.FuelType.GASOLINE, 1200);
        Car updatedCar = CarMapper.fromUpdateDto(dto);

        when(garageService.updateCar(eq(id), any(Car.class))).thenReturn(updatedCar);

        // when
        CarDto result = controller.update(id, dto);

        // then
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Updated");
    }

    @Test
    void shouldDeleteCar() {
        // given
        UUID id = UUID.randomUUID();

        // when
        controller.delete(id);

        // then
        verify(garageService).deleteCar(id);
    }
}
