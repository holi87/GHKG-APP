package ghkg.application;

import ghkg.api.exception.CarNotFoundException;
import ghkg.application.validation.CarValidator;
import ghkg.domain.Car;
import ghkg.domain.CarRepository;
import ghkg.dto.CarFilterDto;
import ghkg.infrastructure.spec.CarSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
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
        if (!carRepository.existsById(id)) {
            throw new CarNotFoundException("Car with ID " + id + " not found");
        }
        carRepository.deleteById(id);
    }

    public List<Car> findByFilter(CarFilterDto filter) {
        Specification<Car> spec = Specification.where(CarSpecifications.nameContains(filter.getName()))
                .and(CarSpecifications.fuelTypeIs(filter.getType()))
                .and(CarSpecifications.minCapacity(filter.getMinCapacity()))
                .and(CarSpecifications.maxCapacity(filter.getMaxCapacity()));

        return carRepository.findAll(spec);
    }
}