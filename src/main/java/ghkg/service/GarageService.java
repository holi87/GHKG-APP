package ghkg.service;

import ghkg.model.Car;
import ghkg.model.FuelType;
import ghkg.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GarageService {
    private final CarRepository carRepository;

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public Optional<Car> getCarById(UUID id) {
        return carRepository.findById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car updateCar(UUID id, Car car) {
        if (!carRepository.existsById(id)) {
            throw new IllegalArgumentException("Car not found");
        }
        car.setId(id);
        return carRepository.save(car);
    }

    public void deleteCar(UUID id) {
        carRepository.deleteById(id);
    }

    public List<Car> getCarsByFuelType(FuelType fuelType) {
        return carRepository.findByFuelType(fuelType);
    }

}
