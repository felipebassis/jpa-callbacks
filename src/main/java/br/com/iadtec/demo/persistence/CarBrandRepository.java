package br.com.iadtec.demo.persistence;

import br.com.iadtec.demo.entity.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {
}
