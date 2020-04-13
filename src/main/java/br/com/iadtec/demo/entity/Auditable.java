package br.com.iadtec.demo.entity;

import br.com.iadtec.demo.listener.AuditJpaListener;
import org.apache.commons.lang3.SerializationUtils;

import javax.persistence.*;
import java.io.Serializable;

@EntityListeners(AuditJpaListener.class)
@MappedSuperclass
public abstract class Auditable<ID extends Serializable> implements Serializable, Cloneable {

    private static final long serialVersionUID = -2884411994947722920L;

    @Id
    public abstract ID getId();

    @Transient
    private Object previousState;

    @PostLoad
    public void loadCurrentState() throws CloneNotSupportedException {
        this.previousState = this.clone();
    }

    public Object getPreviousState() {
        return previousState;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
