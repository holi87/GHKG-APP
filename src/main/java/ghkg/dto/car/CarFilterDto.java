package ghkg.dto.car;

import ghkg.domain.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarFilterDto {
    private String name;
    private FuelType type;
    private Integer minCapacity;
    private Integer maxCapacity;
}
