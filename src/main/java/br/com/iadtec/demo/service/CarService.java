package br.com.iadtec.demo.service;

import br.com.iadtec.demo.dto.CarDTO;
import br.com.iadtec.demo.entity.Car;
import br.com.iadtec.demo.entity.CarId;
import br.com.iadtec.demo.entity.Model;
import br.com.iadtec.demo.persistence.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final ModelService modelService;

    private final CarRepository carRepository;

    public CarService(ModelService modelService, CarRepository carRepository) {
        this.modelService = modelService;
        this.carRepository = carRepository;
    }

    public Car save(CarDTO carDTO) {
        Model model = modelService.findById(carDTO.getModelName(), carDTO.getBrandId());

        Car car = Car.builder()
                .id(CarId.from(carDTO.getYear(), model))
                .basePrice(carDTO.getBasePrice())
                .build();

        return carRepository.save(car);
    }

    public void update(CarDTO carDTO) {
        Model model = modelService.findById(carDTO.getModelName(), carDTO.getBrandId());
        Car car = carRepository.findById(CarId.from(carDTO.getYear(), model))
                .orElseThrow(() -> new IllegalArgumentException("Entity not found"));
        Optional.ofNullable(carDTO.getBasePrice())
                .ifPresent(car::setBasePrice);
        carRepository.save(car);
    }

    public void deleteById(Long brandId, String modelName, Long year) {
        Model model = modelService.findById(modelName, brandId);
        carRepository.deleteById(CarId.from(year, model));
    }

    public List<CarDTO> findAll() {
        return carRepository.findAll()
                .stream()
                .map(car -> CarDTO.builder()
                        .brandId(car.getCarBrandId())
                        .modelName(car.getModelName())
                        .year(car.getYear())
                        .basePrice(car.getBasePrice())
                        .build())
                .collect(Collectors.toList());
    }
}
