package br.com.iadtec.demo.rest;

import br.com.iadtec.demo.entity.CarBrand;
import br.com.iadtec.demo.entity.BrandDTO;
import br.com.iadtec.demo.persistence.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public CarBrand findById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entity nod found"));
    }

    public CarBrand save(CarBrand carBrand) {
        return brandRepository.save(carBrand);
    }

    public void delete(Long id) {
        brandRepository.deleteById(id);
    }

    public void updateBatch(List<BrandDTO> brandDTOS) {
        brandRepository.saveAll(brandDTOS.stream()
                .map(CarBrand::new)
                .collect(Collectors.toList()));
    }
}
