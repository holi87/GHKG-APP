package ghkg.domain.trips;

import ghkg.infrastructure.converter.DurationConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Entity
@Table(name = "trips")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TripType type;

    private String startLocation;

    private String destination;

    @NotNull
    @Positive
    private double distance;

    @PositiveOrZero
    @Column(name = "duration")
    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @Min(0)
    @Max(10)
    private Integer rating;

}

