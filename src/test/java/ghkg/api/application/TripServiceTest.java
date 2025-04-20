package ghkg.api.application;

import ghkg.api.controllers.exception.InvalidTripException;
import ghkg.api.controllers.exception.TripNotFoundException;
import ghkg.api.domain.trips.Trip;
import ghkg.api.domain.trips.TripType;
import ghkg.api.dto.trips.CreateTripRequest;
import ghkg.api.dto.trips.UpdateTripRequest;
import ghkg.api.infrastructure.repository.TripRepository;
import ghkg.api.services.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TripServiceTest {

    private TripRepository tripRepository;
    private TripService tripService;

    @BeforeEach
    void setUp() {
        tripRepository = mock(TripRepository.class);
        tripService = new TripService(tripRepository);
    }

    @Test
    void shouldCreateTrip() {
        CreateTripRequest request = new CreateTripRequest("Trip1", TripType.CAR, "A", "B", 100.0, Duration.ofHours(2), 8);

        Trip savedTrip = Trip.builder()
                .name("Trip1")
                .type(TripType.CAR)
                .startLocation("A")
                .destination("B")
                .distance(100.0)
                .duration(Duration.ofHours(2))
                .rating(8)
                .build();

        when(tripRepository.save(any())).thenReturn(savedTrip);

        var result = tripService.createTrip(request);

        assertThat(result.name()).isEqualTo("Trip1");
        verify(tripRepository).save(any());
    }

    @Test
    void shouldThrowWhenTripNotFound() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.getTripById(1L))
                .isInstanceOf(TripNotFoundException.class)
                .hasMessageContaining("Trip not found");
    }

    @Test
    void shouldGetAllTrips() {
        Trip trip = Trip.builder().name("TripX").build();
        when(tripRepository.findAll()).thenReturn(List.of(trip));

        var result = tripService.getAllTrips();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("TripX");
    }

    @Test
    void shouldUpdateTrip() {
        Trip trip = Trip.builder()
                .id(1L)
                .name("Old")
                .build();

        UpdateTripRequest request = new UpdateTripRequest("New", TripType.BIKE, "X", "Y", 50, Duration.ofMinutes(90), 9);

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        var result = tripService.updateTrip(1L, request);

        assertThat(result.name()).isEqualTo("New");
        verify(tripRepository).findById(1L);
    }

    @Test
    void shouldDeleteTrip() {
        tripService.deleteTrip(1L);
        verify(tripRepository).deleteById(1L);
    }

    @Test
    void shouldThrowOnInvalidDistance() {
        CreateTripRequest request = new CreateTripRequest("x", TripType.CAR, "A", "B", -5, Duration.ofHours(1), 5);

        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(InvalidTripException.class)
                .hasMessageContaining("Distance");
    }

    @Test
    void shouldThrowOnInvalidRating() {
        CreateTripRequest request = new CreateTripRequest("x", TripType.CAR, "A", "B", 5, Duration.ofHours(1), 20);

        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(InvalidTripException.class)
                .hasMessageContaining("Rating");
    }

    @Test
    void shouldThrowOnSameStartAndDestination() {
        CreateTripRequest request = new CreateTripRequest("x", TripType.CAR, "A", "A", 5, Duration.ofHours(1), 5);

        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(InvalidTripException.class)
                .hasMessageContaining("Start and destination");
    }

    @Test
    void shouldThrowOnInvalidDuration() {
        CreateTripRequest request = new CreateTripRequest("x", TripType.CAR, "A", "B", 5, Duration.ZERO, 5);

        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(InvalidTripException.class)
                .hasMessageContaining("Duration");
    }
}
