package ghkg.dto;

import ghkg.domain.FuelType;

public record CreateCarDto(
        String name,
        FuelType fuelType,
        int engineCapacity
) {}