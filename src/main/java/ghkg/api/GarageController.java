package ghkg.api;

import ghkg.domain.FuelType;
import ghkg.dto.CarDto;
import ghkg.dto.CarFilterDto;
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
        produces = "application/json",
        consumes = "application/json"
)
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @GetMapping("/all")
    public List<CarDto> getAll(@ModelAttribute CarFilterDto filter) {
        return garageService.findByFilter(filter).stream()
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
    @PostMapping
    public CarDto add(@RequestBody CarDto dto) {
        return CarMapper.toDto(garageService.addCar(CarMapper.fromDto(dto)));
    }

    @PutMapping("/{id}")
    public CarDto update(@PathVariable UUID id, @RequestBody CarDto dto) {
        return CarMapper.toDto(garageService.updateCar(id, CarMapper.fromDto(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        garageService.deleteCar(id);
    }
}

