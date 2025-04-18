package ghkg.dto.car;

import ghkg.domain.car.FuelType;

import java.util.UUID;

public record CarDto(
        UUID id,
        String name,
        FuelType fuelType,
        int engineCapacity
) {}
