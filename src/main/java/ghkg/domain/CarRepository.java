package ghkg.domain;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
    List<Car> findAll();
    Optional<Car> findById(UUID id);
    Car save(Car car);
    void deleteById(UUID id);
    List<Car> findByFuelType(FuelType fuelType);
    List<Car> findAll(Specification<Car> spec);

}
