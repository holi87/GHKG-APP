package ghkg.api;

import ghkg.api.exception.InvalidCarDataException;
import ghkg.application.GarageService;
import ghkg.config.ApiPaths;
import ghkg.domain.Car;
import ghkg.dto.PageResponse;
import ghkg.dto.car.CarDto;
import ghkg.dto.car.CarFilterDto;
import ghkg.dto.car.CreateCarDto;
import ghkg.dto.car.UpdateCarDto;
import ghkg.mapper.CarMapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(
        value = ApiPaths.CARS,
        produces = "application/json"
)
@RequiredArgsConstructor
public class GarageController extends BaseController {

    private final GarageService garageService;

    @GetMapping
    public ResponseEntity<PageResponse<CarDto>> getCars(
            @Parameter(description = "Car filters")
            @ParameterObject CarFilterDto filter,

            @Parameter(description = "Pagination and sorting parameters e.g. `?page=0&size=10&sort=brand,desc`")
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC)
            @ParameterObject Pageable pageable
    ) {
        Page<CarDto> page = garageService.getCars(filter, pageable);
        return ResponseEntity.ok(toPageResponse(page));
    }

    @GetMapping("/{id}")
    public CarDto getById(@PathVariable UUID id) {
        return CarMapper.toDto(garageService.getCarById(id));
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public CarDto add(@RequestBody CreateCarDto dto) {
        return CarMapper.toDto(garageService.addCar(CarMapper.fromCreateDto(dto)));
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public CarDto update(@PathVariable UUID id, @RequestBody UpdateCarDto dto) {
        if (!id.equals(dto.id())) {
            throw new InvalidCarDataException("Path ID and payload ID do not match");
        }
        Car car = CarMapper.fromUpdateDto(dto);
        return CarMapper.toDto(garageService.updateCar(id, car));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        garageService.deleteCar(id);
    }
}

