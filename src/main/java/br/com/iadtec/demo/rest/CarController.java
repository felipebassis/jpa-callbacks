package br.com.iadtec.demo.rest;

import br.com.iadtec.demo.entity.Brand;
import br.com.iadtec.demo.entity.BrandDTO;
import br.com.iadtec.demo.entity.Car;
import br.com.iadtec.demo.entity.CarDTO;
import br.com.iadtec.demo.persistence.BrandRepository;
import br.com.iadtec.demo.persistence.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<UUID> createBrand(@RequestBody CarDTO carDTO) {
        Car car = new Car();
        car.setName(carDTO.getName());
        car.setYear(carDTO.getYear());
        car.setBrand(brandRepository.findById(carDTO.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Entity not found")));

        Car carSaved = carRepository.save(car);

        return ResponseEntity.status(HttpStatus.CREATED).body(carSaved.getId());
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Void> updateBrand(@PathVariable(name = "id") UUID id, @RequestBody CarDTO carDTO) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        car.setName(carDTO.getName());
        car.setYear(carDTO.getYear());
        car.setBrand(brandRepository.findById(carDTO.getBrandId())
                .orElseThrow(() -> new IllegalArgumentException("Entity not found")));

        carRepository.save(car);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable(name = "id") UUID id) {
        carRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
