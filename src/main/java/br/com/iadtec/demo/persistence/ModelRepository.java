package br.com.iadtec.demo.persistence;

import br.com.iadtec.demo.entity.Model;
import br.com.iadtec.demo.entity.ModelId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, ModelId> {
}
