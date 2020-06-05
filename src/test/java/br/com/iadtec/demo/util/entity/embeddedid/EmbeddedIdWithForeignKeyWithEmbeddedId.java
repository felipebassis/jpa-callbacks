package br.com.iadtec.demo.util.entity.embeddedid;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@Entity
public class EmbeddedIdWithForeignKeyWithEmbeddedId {

    @EmbeddedId
    private final Id id;

    @Getter
    @AllArgsConstructor
    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = -6945219284430926421L;
        private final String string;

        @OneToOne
        @JoinColumns(value = {
                @JoinColumn(name = "embedded_id_string", referencedColumnName = "string"),
                @JoinColumn(name = "embedded_id_long", referencedColumnName = "a_long_id")
        })
        private final BasicEmbeddedIdEntity basicEmbeddedIdEntity;
    }
}
