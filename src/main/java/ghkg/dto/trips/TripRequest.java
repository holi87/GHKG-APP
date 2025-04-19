package ghkg.dto.trips;

import ghkg.domain.trips.TripType;

import java.time.Duration;

public interface TripRequest {
    String name();

    TripType type();

    String startLocation();

    String destination();

    double distance();

    Duration duration();

    Integer rating();
}

