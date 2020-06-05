package br.com.iadtec.demo.rest;

import br.com.iadtec.demo.dto.CarDTO;
import br.com.iadtec.demo.entity.Car;
import br.com.iadtec.demo.entity.CarId;
import br.com.iadtec.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "car")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> findAll() {
        List<CarDTO> cars = carService.findAll();
        if (cars.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<CarId> createCar(@RequestBody CarDTO carDTO) {

        Car carSaved = carService.save(carDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(carSaved.getId());
    }

    @PutMapping
    public ResponseEntity<Void> updateCar(@RequestBody CarDTO carDTO) {
        carService.update(carDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCar(@RequestBody CarDTO carDTO) {
        carService.deleteById(carDTO.getBrandId(), carDTO.getModelName(), carDTO.getYear());
        return ResponseEntity.ok().build();
    }
}
