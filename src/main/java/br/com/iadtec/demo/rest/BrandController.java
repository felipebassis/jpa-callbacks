package br.com.iadtec.demo.rest;

import br.com.iadtec.demo.entity.Brand;
import br.com.iadtec.demo.entity.BrandDTO;
import br.com.iadtec.demo.persistence.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "brand")
public class BrandController {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @PostMapping
    public ResponseEntity<UUID> createBrand(@RequestBody BrandDTO brandDTO) {
        Brand brand = new Brand();
        brand.setName(brandDTO.getName());

        Brand brandSaved = brandRepository.save(brand);

        return ResponseEntity.status(HttpStatus.CREATED).body(brandSaved.getId());
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Void> updateBrand(@PathVariable(name = "id") UUID id, @RequestBody BrandDTO brandDTO) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        brand.setName(brandDTO.getName());

        brandRepository.save(brand);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable(name = "id") UUID id) {
        brandRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
