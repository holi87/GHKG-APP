package ghkg.dto;

import ghkg.domain.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private UUID id;
    private String name;
    private int engineCapacity;
    private FuelType fuelType;
}
