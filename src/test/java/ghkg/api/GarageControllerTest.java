package ghkg.api;

import ghkg.domain.car.Car;
import ghkg.domain.car.FuelType;
import ghkg.dto.PageResponse;
import ghkg.dto.car.CarDto;
import ghkg.dto.car.CarFilterDto;
import ghkg.dto.car.CreateCarDto;
import ghkg.dto.car.UpdateCarDto;
import ghkg.mapper.CarMapper;
import ghkg.services.GarageService;
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
        Long id = UUID.randomUUID().getMostSignificantBits();
        CarFilterDto filter = new CarFilterDto("Tesla", null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        CarDto carDto = new CarDto(id, "Tesla", FuelType.ELECTRIC, 0);
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
        Long id = UUID.randomUUID().getMostSignificantBits();
        Car car = Car.builder().id(id).name("Car").fuelType(FuelType.GASOLINE).engineCapacity(1000).build();

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
        Long id = UUID.randomUUID().getMostSignificantBits();
        CreateCarDto createDto = new CreateCarDto("NewCar", FuelType.DIESEL, 1600);
        Car saved = Car.builder().id(id).name("NewCar").fuelType(FuelType.DIESEL).engineCapacity(1600).build();

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
        Long id = UUID.randomUUID().getMostSignificantBits();
        UpdateCarDto dto = new UpdateCarDto(id, "Updated", FuelType.GASOLINE, 1200);
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
        Long id = UUID.randomUUID().getMostSignificantBits();

        // when
        controller.delete(id);

        // then
        verify(garageService).deleteCar(id);
    }
}
