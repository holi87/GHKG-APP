package ghkg.application;

import ghkg.api.exception.CarNotFoundException;
import ghkg.application.validation.CarValidator;
import ghkg.domain.Car;
import ghkg.domain.CarRepository;
import ghkg.dto.car.CarDto;
import ghkg.dto.car.CarFilterDto;
import ghkg.infrastructure.spec.CarSpecifications;
import ghkg.mapper.CarMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GarageService {

    private final CarRepository carRepository;

    public Page<CarDto> getCars(CarFilterDto filter, Pageable pageable) {
        Specification<Car> spec = Specification.where(CarSpecifications.nameContains(filter.getName()))
                .and(CarSpecifications.fuelTypeIs(filter.getType()))
                .and(CarSpecifications.minCapacity(filter.getMinCapacity()))
                .and(CarSpecifications.maxCapacity(filter.getMaxCapacity()));

        return carRepository.findAll(spec, pageable).map(CarMapper::toDto);
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

}