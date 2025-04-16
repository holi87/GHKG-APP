package ghkg.mapper;

import ghkg.domain.Car;
import ghkg.dto.CarDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CarMapper {
    public static CarDto toDto(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .name(car.getName())
                .engineCapacity(car.getEngineCapacity())
                .fuelType(car.getFuelType())
                .build();
    }

    public static Car fromDto(CarDto dto) {
        return Car.builder()
                .id(dto.getId())
                .name(dto.getName())
                .engineCapacity(dto.getEngineCapacity())
                .fuelType(dto.getFuelType())
                .build();
    }
}
