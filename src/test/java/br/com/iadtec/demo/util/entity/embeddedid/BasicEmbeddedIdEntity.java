package br.com.iadtec.demo.util.entity.embeddedid;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@Entity
public class BasicEmbeddedIdEntity {

    @EmbeddedId
    private final Id id;

    @Getter
    @AllArgsConstructor
    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 4695002115282970362L;
        @Column(name = "a_long_id")
        private final Long aLong;

        private final String string;
    }
}
