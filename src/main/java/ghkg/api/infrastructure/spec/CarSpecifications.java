package ghkg.api.infrastructure.spec;

import ghkg.api.domain.car.Car;
import ghkg.api.domain.car.FuelType;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecifications {

    public static Specification<Car> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Car> fuelTypeIs(FuelType fuelType) {
        return (root, query, cb) ->
                fuelType == null ? null : cb.equal(root.get("fuelType"), fuelType);
    }

    public static Specification<Car> minCapacity(Integer min) {
        return (root, query, cb) ->
                min == null ? null : cb.greaterThanOrEqualTo(root.get("engineCapacity"), min);
    }

    public static Specification<Car> maxCapacity(Integer max) {
        return (root, query, cb) ->
                max == null ? null : cb.lessThanOrEqualTo(root.get("engineCapacity"), max);
    }
}
