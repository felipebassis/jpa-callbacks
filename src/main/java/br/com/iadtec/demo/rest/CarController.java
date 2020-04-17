package br.com.iadtec.demo.rest;

import br.com.iadtec.demo.entity.Car;
import br.com.iadtec.demo.entity.CarBrand;
import br.com.iadtec.demo.entity.CarDTO;
import br.com.iadtec.demo.entity.CarId;
import br.com.iadtec.demo.persistence.BrandRepository;
import br.com.iadtec.demo.persistence.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "car")
public class CarController {

    private final CarRepository carRepository;

    private final BrandRepository brandRepository;

    @Autowired
    public CarController(CarRepository carRepository, BrandRepository brandRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
    }

    @PostMapping
    public ResponseEntity<CarId> createCar(@RequestBody CarDTO carDTO) {
        CarBrand carBrand = brandRepository.findById(carDTO.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));

        Car car = new Car(CarId.from(carRepository.count() + 1, carBrand));
        car.setName(carDTO.getName());
        car.setYear(carDTO.getYear());

        Car carSaved = carRepository.save(car);

        return ResponseEntity.status(HttpStatus.CREATED).body(carSaved.getId());
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Void> updateCar(@PathVariable(name = "id") Long id, @RequestBody CarDTO carDTO) {
        CarBrand carBrand = brandRepository.findById(carDTO.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        Car car = carRepository.findById(CarId.from(id, carBrand))
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        Optional.ofNullable(carDTO.getName())
                .ifPresent(car::setName);
        Optional.ofNullable(carDTO.getYear())
                .ifPresent(car::setYear);
        carRepository.save(car);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteCar(@RequestBody CarDTO carDTO) {
        CarBrand carBrand = brandRepository.findById(carDTO.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        carRepository.deleteById(CarId.from(carDTO.getId(), carBrand));
        return ResponseEntity.ok().build();
    }
}
