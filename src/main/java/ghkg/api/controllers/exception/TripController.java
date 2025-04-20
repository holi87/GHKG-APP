package ghkg.api.controllers.exception;

import ghkg.api.config.ApiPaths;
import ghkg.api.dto.trips.CreateTripRequest;
import ghkg.api.dto.trips.TripResponse;
import ghkg.api.dto.trips.UpdateTripRequest;
import ghkg.api.services.TripService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.TRIPS)
@RequiredArgsConstructor
@Tag(name = "Trips", description = "Endpoints for managing trips with roles")
public class TripController {

    private final TripService tripService;

    @PreAuthorize("hasAnyRole('WORKER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@Valid @RequestBody CreateTripRequest request) {
        return ResponseEntity.status(201).body(tripService.createTrip(request));
    }

    @PreAuthorize("hasAnyRole('WORKER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable Long id, @Valid @RequestBody UpdateTripRequest request) {
        return ResponseEntity.ok(tripService.updateTrip(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('WORKER', 'ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<TripResponse>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTripById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }
}
