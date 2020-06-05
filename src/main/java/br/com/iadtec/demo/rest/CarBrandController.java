package br.com.iadtec.demo.rest;

import br.com.iadtec.demo.dto.CarBrandDTO;
import br.com.iadtec.demo.entity.CarBrand;
import br.com.iadtec.demo.service.CarBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "carBrand")
public class CarBrandController {

    private final CarBrandService carBrandService;

    @Autowired
    public CarBrandController(CarBrandService carBrandService) {
        this.carBrandService = carBrandService;
    }

    @GetMapping
    public ResponseEntity<List<CarBrandDTO>> findAll() {
        List<CarBrandDTO> carBrands = carBrandService.findAll();
        if (carBrands.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carBrands);
    }

    @PostMapping
    public ResponseEntity<Long> createBrand(@RequestBody CarBrandDTO carBrandDTO) {
        CarBrand carBrandSaved = carBrandService.save(carBrandDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(carBrandSaved.getId());
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Void> updateBrand(@PathVariable(name = "id") Long id, @RequestBody CarBrandDTO carBrandDTO) {

        carBrandService.update(id, carBrandDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable(name = "id") Long id) {
        carBrandService.delete(id);
        return ResponseEntity.ok().build();
    }
}
