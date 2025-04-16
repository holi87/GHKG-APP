package ghkg.application.validation;

import ghkg.api.exception.InvalidCarDataException;
import ghkg.domain.Car;
import ghkg.domain.FuelType;

import java.util.Set;

public class CarValidator {

    private static final Set<FuelType> ZERO_CAPACITY_FUEL_TYPES =
            Set.of(FuelType.ELECTRIC, FuelType.FUEL_CELL, FuelType.OTHER);

    public static void validate(Car car) {
        if (car.getFuelType() == null) {
            throw new InvalidCarDataException("Fuel type is required");
        }

        boolean isZeroCapacityRequired = ZERO_CAPACITY_FUEL_TYPES.contains(car.getFuelType());
        int engineCapacity = car.getEngineCapacity();

        if ((isZeroCapacityRequired && engineCapacity != 0) ||
                (!isZeroCapacityRequired && engineCapacity <= 0)) {
            String requirement = isZeroCapacityRequired ? "equal to 0" : "greater than 0";
            throw new InvalidCarDataException("Cars with fuel type " + car.getFuelType() +
                    " must have engine capacity " + requirement);
        }
    }
}
