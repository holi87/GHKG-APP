package ghkg.mapper;

import ghkg.domain.car.Car;
import ghkg.dto.car.CarDto;
import ghkg.dto.car.CreateCarDto;
import ghkg.dto.car.UpdateCarDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CarMapper {

    public static Car fromCreateDto(CreateCarDto dto) {
        Car car = new Car();
        car.setName(dto.name());
        car.setFuelType(dto.fuelType());
        car.setEngineCapacity(dto.engineCapacity());
        return car;
    }

    public static Car fromUpdateDto(UpdateCarDto dto) {
        Car car = new Car();
        car.setId(dto.id());
        car.setName(dto.name());
        car.setFuelType(dto.fuelType());
        car.setEngineCapacity(dto.engineCapacity());
        return car;
    }
    public static CarDto toDto(Car car) {
        return new CarDto(
                car.getId(),
                car.getName(),
                car.getFuelType(),
                car.getEngineCapacity()
        );
    }
}
