package ghkg.api.dto.car;

import ghkg.api.domain.car.FuelType;

public record CreateCarDto(
        String name,
        FuelType fuelType,
        int engineCapacity
) {}