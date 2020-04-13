package br.com.iadtec.demo.entity;

import br.com.iadtec.demo.listener.AuditJpaListener;
import org.apache.commons.lang3.SerializationUtils;

import javax.persistence.*;
import java.io.Serializable;

@EntityListeners(AuditJpaListener.class)
@MappedSuperclass
public abstract class Auditable<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = -2884411994947722920L;

    @Id
    public abstract ID getId();

    @Transient
    private Auditable<ID> previousState;

    @PostLoad
    public void loadCurrentState() {
        this.previousState = SerializationUtils.clone(this);
    }

    public Auditable<ID> getPreviousState() {
        return previousState;
    }
}
