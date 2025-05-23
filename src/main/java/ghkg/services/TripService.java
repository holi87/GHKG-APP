package ghkg.services;

import ghkg.api.exception.InvalidTripException;
import ghkg.api.exception.TripNotFoundException;
import ghkg.domain.trips.Trip;
import ghkg.dto.trips.CreateTripRequest;
import ghkg.dto.trips.TripRequest;
import ghkg.dto.trips.TripResponse;
import ghkg.dto.trips.UpdateTripRequest;
import ghkg.infrastructure.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public TripResponse createTrip(CreateTripRequest request) {
        validateTripData(request);
        Trip trip = Trip.builder()
                .name(request.name())
                .type(request.type())
                .startLocation(request.startLocation())
                .destination(request.destination())
                .distance(request.distance())
                .duration(request.duration())
                .rating(request.rating())
                .build();
        return TripResponse.from(tripRepository.save(trip));
    }

    public TripResponse getTripById(Long id) {
        return tripRepository.findById(id)
                .map(TripResponse::from)
                .orElseThrow(() -> new TripNotFoundException("Trip not found: " + id));
    }

    public List<TripResponse> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(TripResponse::from)
                .toList();
    }

    @Transactional
    public TripResponse updateTrip(Long id, UpdateTripRequest request) {
        validateTripData(request);
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException("Trip not found: " + id));

        trip.setName(request.name());
        trip.setType(request.type());
        trip.setStartLocation(request.startLocation());
        trip.setDestination(request.destination());
        trip.setDistance(request.distance());
        trip.setDuration(request.duration());
        trip.setRating(request.rating());

        return TripResponse.from(trip);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }

    private void validateTripData(TripRequest request) {
        if (request.distance() < 0) {
            throw new InvalidTripException("Distance must be greater than or equal to 0.");
        }
        if (request.duration() == null || request.duration().isNegative() || request.duration().isZero()) {
            throw new InvalidTripException("Duration must be a positive value.");
        }
        if (request.rating() < 0 || request.rating() > 10) {
            throw new InvalidTripException("Rating must be between 0 and 10.");
        }
        if (request.startLocation().equalsIgnoreCase(request.destination())) {
            throw new InvalidTripException("Start and destination locations must be different.");
        }
    }


}
