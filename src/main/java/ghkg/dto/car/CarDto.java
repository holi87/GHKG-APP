package ghkg.dto.car;

import ghkg.domain.car.FuelType;

public record CarDto(
        Long id,
        String name,
        FuelType fuelType,
        int engineCapacity
) {}
