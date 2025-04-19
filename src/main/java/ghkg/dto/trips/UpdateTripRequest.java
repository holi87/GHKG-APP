package ghkg.dto.trips;

import ghkg.domain.trips.TripType;

import java.time.Duration;

public record UpdateTripRequest(
        String name,
        TripType type,
        String startLocation,
        String destination,
        double distance,
        Duration duration,
        Integer rating
) implements TripRequest {
}
