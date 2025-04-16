package ghkg.infrastructure.repository;

import ghkg.domain.Car;
import ghkg.domain.CarRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarJpaRepository extends JpaRepository<Car, UUID>, JpaSpecificationExecutor<Car>, CarRepository {
}
