package ghkg.dto.trips;

import ghkg.domain.trips.Trip;
import ghkg.domain.trips.TripType;

import java.time.Duration;

public record TripResponse(
        Long id,
        String name,
        TripType type,
        String startLocation,
        String destination,
        double distance,
        Duration duration,
        Integer rating
) {
    public static TripResponse from(Trip trip) {
        return new TripResponse(
                trip.getId(),
                trip.getName(),
                trip.getType(),
                trip.getStartLocation(),
                trip.getDestination(),
                trip.getDistance(),
                trip.getDuration(),
                trip.getRating()
        );
    }
}