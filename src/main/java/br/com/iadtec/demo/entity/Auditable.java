package br.com.iadtec.demo.entity;

import br.com.iadtec.demo.listener.AuditoryJpaListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Classe base para Entidades que possam ser auditadas. Essa classe se faz necessaria pois a auditoria necessita
 * de acesso ao id da entidade e para registrar o {@link AuditoryJpaListener} entre todas as entidades que serão
 * auditadas de forma facilitada.
 * @param <I> o tipo do id da entidade
 */
@EntityListeners(AuditoryJpaListener.class)
@MappedSuperclass
public abstract class Auditable<I extends Serializable> implements Serializable {

    private static final long serialVersionUID = -2884411994947722920L;

    public abstract I getId();

}
