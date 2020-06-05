package br.com.iadtec.demo.listener.consumer;

import br.com.iadtec.demo.entity.auditory.AuditoryRepository;
import br.com.iadtec.demo.listener.message.AuditoryMessage;

import javax.persistence.EntityManager;

public abstract class AuditorySpringConsumer<T extends AuditoryMessage> {

    protected final EntityManager entityManager;

    protected final AuditoryRepository auditoryRepository;

    protected AuditorySpringConsumer(EntityManager entityManager,
                                     AuditoryRepository auditoryRepository) {
        this.entityManager = entityManager;
        this.auditoryRepository = auditoryRepository;
    }

    public abstract void listenMessage(T message);
}
