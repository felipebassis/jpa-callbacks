package br.com.iadtec.demo.service;

import br.com.iadtec.demo.dto.ModelDTO;
import br.com.iadtec.demo.entity.CarBrand;
import br.com.iadtec.demo.entity.Model;
import br.com.iadtec.demo.entity.ModelId;
import br.com.iadtec.demo.persistence.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelService {

    private final ModelRepository modelRepository;

    private final CarBrandService carBrandService;

    public ModelService(ModelRepository modelRepository,
                        CarBrandService carBrandService) {
        this.modelRepository = modelRepository;
        this.carBrandService = carBrandService;
    }

    public Model findById(String modelName, Long brandId) {
        CarBrand carBrand = carBrandService.findById(brandId);

        return modelRepository.findById(ModelId.from(modelName, carBrand))
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
    }

    public List<ModelDTO> findAll() {
        return modelRepository.findAll()
                .stream()
                .map(model -> ModelDTO.builder()
                        .carBrandId(model.getCarBrandId())
                        .carBrandName(model.getCarBrandName())
                        .name(model.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public void save(ModelDTO modelDTO) {
        CarBrand carBrand = carBrandService.findById(modelDTO.getCarBrandId());
        Model model = Model.builder()
                .id(ModelId.from(modelDTO.getName(), carBrand))
                .build();
        modelRepository.save(model);
    }

    public void deleteById(Long carBrandId, String modelName) {
        CarBrand carBrand = carBrandService.findById(carBrandId);
        modelRepository.deleteById(ModelId.from(modelName, carBrand));
    }
}
