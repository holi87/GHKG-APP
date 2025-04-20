package ghkg.api.domain.car;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface CarRepository {
    Optional<Car> findById(Long id);
    Car save(Car car);

    void deleteById(Long id);

    Page<Car> findAll(Specification<Car> spec, Pageable pageable);

    boolean existsById(Long id);
}
