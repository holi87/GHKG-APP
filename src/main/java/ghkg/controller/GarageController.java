package ghkg.controller;

import ghkg.model.Car;
import ghkg.model.FuelType;
import ghkg.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/garage")
@RequiredArgsConstructor
public class GarageController {
    
    private final GarageService garageService;

    @GetMapping
    public List<Car> getAllCars() {
        return garageService.getAllCars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable UUID id) {
        return garageService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fuel/{fuelType}")
    public List<Car> getCarsByFuelType(@PathVariable FuelType fuelType) {
        return garageService.getCarsByFuelType(fuelType);
    }

    @PostMapping
    public Car addCar(@RequestBody Car car) {
        return garageService.addCar(car);
    }

    @PutMapping("/{id}")
    public Car updateCar(@PathVariable UUID id, @RequestBody Car car) {
        return garageService.updateCar(id, car);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        garageService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }


}
