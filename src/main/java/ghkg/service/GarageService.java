package ghkg.service;

import ghkg.domain.Car;
import ghkg.domain.CarRepository;
import ghkg.domain.FuelType;
import ghkg.infrastructure.repository.CarJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GarageService {

    private final CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(UUID id) {
        return carRepository.findById(id).orElse(null);
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public Car updateCar(UUID id, Car updatedCar) {
        updatedCar.setId(id);
        return carRepository.save(updatedCar);
    }

    public void deleteCar(UUID id) {
        carRepository.deleteById(id);
    }

    public List<Car> getCarsByFuelType(FuelType type) {
        return carRepository.findByFuelType(type);
    }
}
