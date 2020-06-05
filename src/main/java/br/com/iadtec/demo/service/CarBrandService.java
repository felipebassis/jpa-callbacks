package br.com.iadtec.demo.service;

import br.com.iadtec.demo.dto.CarBrandDTO;
import br.com.iadtec.demo.entity.CarBrand;
import br.com.iadtec.demo.persistence.CarBrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CarBrandService {

    private final CarBrandRepository carBrandRepository;

    public CarBrandService(CarBrandRepository carBrandRepository) {
        this.carBrandRepository = carBrandRepository;
    }

    public CarBrand findById(Long id) {
        return carBrandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
    }

    public List<CarBrandDTO> findAll() {
        return carBrandRepository.findAll()
                .stream()
                .map(carBrand -> {
                    CarBrandDTO carBrandDTO = new CarBrandDTO();
                    carBrandDTO.setId(carBrand.getId());
                    carBrandDTO.setName(carBrand.getName());
                    return carBrandDTO;
                }).collect(Collectors.toList());
    }

    public CarBrand save(CarBrandDTO carBrandDTO) {
        CarBrand carBrand = CarBrand.builder()
                .name(carBrandDTO.getName())
                .build();

        return carBrandRepository.save(carBrand);
    }

    public void delete(Long id) {
        CarBrand carBrand = findById(id);

        carBrandRepository.delete(carBrand);
    }

    public void update(Long id, CarBrandDTO carBrandDTO) {
        CarBrand carBrand = findById(id);
        carBrand.setName(carBrandDTO.getName());

        carBrandRepository.save(carBrand);
    }
}
