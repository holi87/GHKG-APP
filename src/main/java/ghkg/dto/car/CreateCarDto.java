package ghkg.dto.car;

import ghkg.domain.FuelType;

public record CreateCarDto(
        String name,
        FuelType fuelType,
        int engineCapacity
) {}