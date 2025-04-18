package ghkg.dto.car;

import ghkg.domain.car.FuelType;

public record CreateCarDto(
        String name,
        FuelType fuelType,
        int engineCapacity
) {}