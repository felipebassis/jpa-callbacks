package br.com.iadtec.demo.rest;

import br.com.iadtec.demo.dto.ModelDTO;
import br.com.iadtec.demo.service.ModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "model")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    public ResponseEntity<List<ModelDTO>> findAll() {
        List<ModelDTO> models = modelService.findAll();

        if (models.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(models);
    }

    @PostMapping
    public ResponseEntity<Void> createModel(@RequestBody ModelDTO modelDTO) {
        modelService.save(modelDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "{brandId}/{name}")
    public ResponseEntity<Void> removeModel(@PathVariable("brandId") Long carBrandId,
                                            @PathVariable("name") String modelName) {
        modelService.deleteById(carBrandId, modelName);
        return ResponseEntity.noContent().build();
    }
}
