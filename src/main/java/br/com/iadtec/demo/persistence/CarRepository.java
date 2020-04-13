package br.com.iadtec.demo.persistence;

import br.com.iadtec.demo.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
}
