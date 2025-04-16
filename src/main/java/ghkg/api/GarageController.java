package ghkg.api;

import ghkg.domain.Car;
import ghkg.domain.FuelType;
import ghkg.dto.CarDto;
import ghkg.mapper.CarMapper;
import ghkg.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/garage")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @GetMapping
    public List<CarDto> getAllCars() {
        return garageService.getAllCars()
                .stream()
                .map(CarMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CarDto getCarById(@PathVariable UUID id) {
        return CarMapper.toDto(garageService.getCarById(id));
    }

    @GetMapping("/type/{fuelType}")
    public List<CarDto> getCarsByFuel(@PathVariable FuelType fuelType) {
        return garageService.getCarsByFuelType(fuelType)
                .stream()
                .map(CarMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public CarDto addCar(@RequestBody CarDto dto) {
        return CarMapper.toDto(garageService.addCar(CarMapper.fromDto(dto)));
    }

    @PutMapping("/{id}")
    public CarDto updateCar(@PathVariable UUID id, @RequestBody CarDto dto) {
        return CarMapper.toDto(garageService.updateCar(id, CarMapper.fromDto(dto)));
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable UUID id) {
        garageService.deleteCar(id);
    }
}

