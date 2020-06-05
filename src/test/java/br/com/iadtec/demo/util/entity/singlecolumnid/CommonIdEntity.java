package br.com.iadtec.demo.util.entity.singlecolumnid;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@AllArgsConstructor
@Entity
public class CommonIdEntity {

    @Id
    public final Long id;
}
