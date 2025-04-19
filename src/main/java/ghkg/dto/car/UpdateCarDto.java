package ghkg.dto.car;

import ghkg.domain.car.FuelType;

public record UpdateCarDto(
        Long id,
        String name,
        FuelType fuelType,
        int engineCapacity
) {}