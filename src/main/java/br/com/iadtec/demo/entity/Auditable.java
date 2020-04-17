package br.com.iadtec.demo.entity;

import br.com.iadtec.demo.listener.AuditJpaListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@EntityListeners(AuditJpaListener.class)
@MappedSuperclass
public abstract class Auditable<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = -2884411994947722920L;

    public abstract ID getId();

}
