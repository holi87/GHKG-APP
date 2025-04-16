package ghkg.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    void saveAndFindCar_ShouldPersistAndRetrieveCar() {
        Car car = new Car();
        car.setId(UUID.randomUUID());
        car.setName("Test Car");

        carRepository.save(car);

        List<Car> cars = carRepository.findAll();
        assertEquals(1, cars.size());
        assertEquals("Test Car", cars.get(0).getName());
    }
}