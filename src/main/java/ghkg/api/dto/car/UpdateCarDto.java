package ghkg.api.dto.car;

import ghkg.api.domain.car.FuelType;

public record UpdateCarDto(
        Long id,
        String name,
        FuelType fuelType,
        int engineCapacity
) {}