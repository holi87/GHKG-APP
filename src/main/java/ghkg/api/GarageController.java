package ghkg.api;

import ghkg.api.exception.InvalidCarDataException;
import ghkg.domain.Car;
import ghkg.dto.car.CarDto;
import ghkg.dto.car.CarFilterDto;
import ghkg.dto.car.CreateCarDto;
import ghkg.dto.car.UpdateCarDto;
import ghkg.mapper.CarMapper;
import ghkg.application.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        value = "/api/v1/cars",
        produces = "application/json"
)
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @GetMapping("/all")
    public List<CarDto> getAll() {
        return garageService.getAllCars().stream()
                .map(CarMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CarDto getById(@PathVariable UUID id) {
        return CarMapper.toDto(garageService.getCarById(id));
    }

    @GetMapping
    public List<CarDto> getFilteredCars(@ModelAttribute CarFilterDto filter) {
        return garageService.findByFilter(filter).stream()
                .map(CarMapper::toDto)
                .collect(Collectors.toList());
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

