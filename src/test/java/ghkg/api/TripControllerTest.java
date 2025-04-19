package ghkg.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghkg.application.TripService;
import ghkg.config.SecurityConfig;
import ghkg.domain.trips.TripType;
import ghkg.dto.trips.CreateTripRequest;
import ghkg.dto.trips.TripResponse;
import ghkg.dto.trips.UpdateTripRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.sql.init.mode=never")
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TripService tripService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"WORKER"})
    void shouldCreateTrip() throws Exception {
        CreateTripRequest request = new CreateTripRequest("Trip", TripType.CAR, "Start", "End", 123.4, Duration.ofHours(2), 8);
        TripResponse response = new TripResponse(1L, "Trip", TripType.CAR, "Start", "End", 123.4, Duration.ofHours(2), 8);

        Mockito.when(tripService.createTrip(any())).thenReturn(response);

        mockMvc.perform(post("/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Trip"));
    }

    @Test
    @WithMockUser(roles = {"WORKER"})
    void shouldUpdateTrip() throws Exception {
        UpdateTripRequest request = new UpdateTripRequest("Updated", TripType.BIKE, "A", "B", 100.0, Duration.ofMinutes(90), 7);
        TripResponse response = new TripResponse(1L, "Updated", TripType.BIKE, "A", "B", 100.0, Duration.ofMinutes(90), 7);

        Mockito.when(tripService.updateTrip(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/trips/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteTrip() throws Exception {
        mockMvc.perform(delete("/trips/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldGetTripById() throws Exception {
        TripResponse response = new TripResponse(1L, "Trip", TripType.CAR, "Start", "End", 123.4, Duration.ofHours(2), 8);

        Mockito.when(tripService.getTripById(1L)).thenReturn(response);

        mockMvc.perform(get("/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Trip"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldGetAllTrips() throws Exception {
        TripResponse response = new TripResponse(1L, "Trip", TripType.CAR, "Start", "End", 123.4, Duration.ofHours(2), 8);

        Mockito.when(tripService.getAllTrips()).thenReturn(List.of(response));

        mockMvc.perform(get("/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Trip"));
    }
}
