package br.com.iadtec.demo.persistence;

import br.com.iadtec.demo.entity.Car;
import br.com.iadtec.demo.entity.CarId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, CarId> {
}
