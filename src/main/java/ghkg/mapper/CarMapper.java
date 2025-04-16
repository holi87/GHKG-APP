package ghkg.mapper;

import ghkg.domain.Car;
import ghkg.dto.CarDto;
import ghkg.dto.CreateCarDto;
import ghkg.dto.UpdateCarDto;
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
