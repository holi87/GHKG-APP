package ghkg.api.infrastructure.repository;

import ghkg.api.domain.trips.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
