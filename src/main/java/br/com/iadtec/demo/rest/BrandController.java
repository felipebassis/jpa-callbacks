package br.com.iadtec.demo.rest;

import br.com.iadtec.demo.entity.CarBrand;
import br.com.iadtec.demo.entity.BrandBatchDTO;
import br.com.iadtec.demo.entity.BrandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "brand")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<Long> createBrand(@RequestBody BrandDTO brandDTO) {
        CarBrand carBrand = new CarBrand(brandDTO);
        carBrand.setName(brandDTO.getName());

        CarBrand carBrandSaved = brandService.save(carBrand);

        return ResponseEntity.status(HttpStatus.CREATED).body(carBrandSaved.getId());
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Void> updateBrand(@PathVariable(name = "id") Long id, @RequestBody BrandDTO brandDTO) {
        CarBrand carBrand = brandService.findById(id);
        carBrand.setName(brandDTO.getName());

        brandService.save(carBrand);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable(name = "id") Long id) {
        brandService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "batch")
    public ResponseEntity<Void> batchDelete(@RequestBody BrandBatchDTO brands) {
        brandService.updateBatch(brands.getBrands());
        return ResponseEntity.ok().build();
    }
}
