package ghkg.api.infrastructure.repository;

import ghkg.api.domain.car.Car;
import ghkg.api.domain.car.CarRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarJpaRepository extends JpaRepository<Car, UUID>, JpaSpecificationExecutor<Car>, CarRepository {
}
