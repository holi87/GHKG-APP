package ghkg.domain.car;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
    Optional<Car> findById(UUID id);
    Car save(Car car);
    void deleteById(UUID id);

    Page<Car> findAll(Specification<Car> spec, Pageable pageable);
    boolean existsById(UUID id);
}
