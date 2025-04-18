package ghkg.infrastructure.repository;

import ghkg.domain.car.Car;
import ghkg.domain.car.CarRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarJpaRepository extends JpaRepository<Car, UUID>, JpaSpecificationExecutor<Car>, CarRepository {
}
