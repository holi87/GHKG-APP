package ghkg.application;

import ghkg.api.exception.CarNotFoundException;
import ghkg.api.exception.InvalidCarDataException;
import ghkg.application.validation.CarValidator;
import ghkg.domain.Car;
import ghkg.domain.CarRepository;
import ghkg.domain.FuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GarageService {

    private final CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(UUID id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with ID: " + id + " not found"));
    }

    public Car addCar(Car car) {
        CarValidator.validate(car);
        return carRepository.save(car);
    }

    public Car updateCar(UUID id, Car car) {
        CarValidator.validate(car);
        car.setId(id);
        return carRepository.save(car);
    }

    public void deleteCar(UUID id) {
        carRepository.deleteById(id);
    }

    public List<Car> getCarsByFuelType(FuelType type) {
        return carRepository.findByFuelType(type);
    }
}