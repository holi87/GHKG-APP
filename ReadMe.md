# Class Structure

## Domain Layer

### Car (Entity)

- Properties:
    - id: UUID
    - name: String
    - fuelType: FuelType (Enum)
    - engineCapacity: Integer

### CarRepository (Interface)

- Methods:
    - findAll(): List<Car>
    - findById(UUID): Optional<Car>
    - save(Car): Car
    - deleteById(UUID): void
    - findAll(Specification<Car>): List<Car>
    - existsById(UUID): boolean

### FuelType (Enum)

- Values:
    - ELECTRIC
    - FUEL_CELL
    - GASOLINE
    - DIESEL

## Service Layer

### GarageService

- Dependencies:
    - CarRepository
- Methods:
    - getAllCars(): List<Car>
    - getCarById(UUID): Car
    - addCar(Car): Car
    - deleteCar(UUID): void
    - findByFilter(CarFilterDto): List<Car>

## Exception Handling

### CarNotFoundException

- Thrown when requested car is not found

### InvalidCarDataException

- Thrown when car data validation fails

## DTOs

### CarFilterDto

- Properties:
    - name: String
    - type: FuelType
    - minCapacity: Integer
    - maxCapacity: Integer
