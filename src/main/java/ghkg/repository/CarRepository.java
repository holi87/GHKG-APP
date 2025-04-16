package ghkg.repository;

import ghkg.model.Car;
import ghkg.model.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
    List<Car> findByFuelType(FuelType fuelType);

}
