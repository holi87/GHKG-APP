package ghkg.api.dto.trips;

import ghkg.api.domain.trips.TripType;

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
