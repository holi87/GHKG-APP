package ghkg.infrastructure.repository;

import ghkg.domain.trips.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
