package br.com.iadtec.demo.listener;

import br.com.iadtec.demo.entity.Auditable;
import br.com.iadtec.demo.listener.cache.AuditoryJpaListenerCache;
import br.com.iadtec.demo.listener.message.AuditoryMessageFactory;
import br.com.iadtec.demo.util.AuditoryUtils;
import br.com.iadtec.demo.util.Autowirer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Esta classe é responsável por capturar o estado das entidades pelos diferentes eventos disparados pela JPA.
 * Essa classe não deve ser gerenciada pelo Spring
 * (i.e: não pode ser anotada com {@link org.springframework.stereotype.Component}) pois sua instância sempre
 * será gerenciada pelo provedor do JPA, portanto, toda instância de objeto que seja gerenciada
 * pelo spring e que essa classe necessite, devera ser adquirida atraves de {@link Autowirer#getBean(Class)}.
 * @see PrePersist
 * @see PreUpdate
 * @see PreRemove
 * @see PostPersist
 * @see PostUpdate
 * @see PostRemove
 * @see PostLoad
 * @see Auditable
 */
@Slf4j
@NoArgsConstructor
public class AuditoryJpaListener {

    private EntityManagerFactory entityManagerFactory;

    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * Método anotado com {@link PreUpdate} responsável por fazer uma native query no banco de dados
     * capturando o estado anterior antes do novo estado ser persistido em banco e salvar
     * em cache em {@link AuditoryJpaListenerCache}. Deve ser feito por SQL nativo,
     * pois caso contrario ele trará o estado do objeto do cache de primeiro nível e não fara uma Query no banco.
     * O JPA realiza a chamada desse método antes de executar a Query de UPDATE no banco,
     * e não antes do {@link EntityManager#persist(Object)}, e dentro do contexto de persistência, esse método
     * não é chamado dentro de uma transação, portanto não é possivel se utilizar o
     * {@link org.springframework.transaction.event.TransactionalEventListener}
     * com a fase {@link org.springframework.transaction.event.TransactionPhase#AFTER_COMMIT}
     * para consumir o evento de UPDATE, portanto, deve ser levada em consideração a possibilidade de haver
     * um Rollback a qualquer momento caso seja enfileirada alguma mensagem na fila de UPDATE.
     *
     * @param newState novo estado da entidade que será persistido em banco.
     * @see AuditoryJpaListenerCache
     */
    @PreUpdate
    public void getPreviousState(Auditable<?> newState) {
        EntityManager entityManager = getEntityManager();
        Map<String, Object> previousState = AuditoryUtils.getEntityStateAsMap(newState, entityManager);
        entityManager.close();
        AuditoryJpaListenerCache.put(newState.getClass(), newState.getId(), previousState);
        log.info("[PREVIOUS_STATE] {} - [NEW_STATE] {}", previousState, newState);
    }

    /**
     * Método anotado com {@link PostUpdate} responsável por validar se todas as entidades que passaram pelo {@link PreUpdate}
     * tambem passaram pelo {@link PostUpdate}. A cada Query de UPDATE que é executada pelo JPA, esse método é chamado
     * uma vez, mesmo que a transação ao qual os objetos que iram passar por aqui eventualmente tenha que realizar
     * um Rollback devido a falha de alguma query de UPDATE, portanto as entidades que passarem por aqui só deverão
     * ser enfileiradas para auditar se todas as entidades pertencentes a transação passarem por esse método.
     *
     * @param entity estado da entidade que foi persistido no banco de dados
     * @see AuditoryJpaListenerCache#markAsCommitted(Class entityClass, Serializable id)
     * @see AuditoryJpaListenerCache#isAllCachedEntitiesCommitted()
     */
    @PostUpdate
    public void showPreviousAndCurrentState(Auditable<?> entity) {
        AuditoryJpaListenerCache.markAsCommitted(entity.getClass(), entity.getId());
        if (AuditoryJpaListenerCache.isAllCachedEntitiesCommitted()) {
            AuditoryJpaListenerCache.stream()
                    .map(value -> AuditoryMessageFactory.updateMessage(entity, value.getPreviousValue()))
                    .forEachOrdered(this.getApplicationEventPublisher()::publishEvent);
            AuditoryJpaListenerCache.clear();
        }
    }

    /**
     * Método anotado com {@link PostPersist} responsável por enfileirar o estado do objeto após sua persistência.
     * Esse método é chamado após o {@link EntityManager#persist(Object)} ser executado com sucesso, portanto esse
     * método não é chamado caso a transação sofra um Rollback, e mesmo que este comportamento falhe, o evento
     * disparado será consumido apenas se houver o commit da transação.
     *
     * @param entity estado atual da entidade apos ser persistida.
     * @see org.springframework.transaction.event.TransactionalEventListener
     * @see org.springframework.transaction.event.TransactionPhase#AFTER_COMMIT
     */
    @PostPersist
    public void showCurrentState(Auditable<?> entity) {
        log.info("[CURRENT_STATE] - {}", entity);
        this.getApplicationEventPublisher().publishEvent(AuditoryMessageFactory.persistMessage(entity));
    }

    /**
     * Método anotado com {@link PreRemove} responsável por enfileirar o estado do objeto antes de sua remoção.
     * Esse método é chamado antes o {@link EntityManager#remove(Object)} ser executado com sucesso, porem
     * como ele é chamado dentro dos limites da transação, não é necessário se preocupar com rollback pois
     * seu evento só será ouvido caso haja o commit da transação.
     *
     * @param entity estado atual da entidade antes de ser removida.
     * @see org.springframework.transaction.event.TransactionalEventListener
     * @see org.springframework.transaction.event.TransactionPhase#AFTER_COMMIT
     */
    @PreRemove
    public void showPreviousState(Auditable<?> entity) {
        log.info("[PREVIOUS_STATE] - {}", entity);
        EntityManager entityManager = getEntityManager();
        Map<String, Object> previousStateOfEntity = AuditoryUtils.getEntityStateAsMap(entity, entityManager);
        entityManager.close();
        this.getApplicationEventPublisher().publishEvent(AuditoryMessageFactory.deleteMessage(previousStateOfEntity));
    }

    /**
     * Método responsável por fornecer uma nova instância do {@link EntityManager}.
     *
     * @return uma nova instância não-transacional do {@link EntityManager}.
     */
    private EntityManager getEntityManager() {
        if (Objects.isNull(entityManagerFactory)) {
            entityManagerFactory = Autowirer.getBean(EntityManagerFactory.class);
        }
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Método responsável por fornecer uma instância do {@link ApplicationEventPublisher}.
     *
     * @return a instância de {@link ApplicationEventPublisher}
     */
    private ApplicationEventPublisher getApplicationEventPublisher() {
        if (Objects.isNull(applicationEventPublisher)) {
            applicationEventPublisher = Autowirer.getApplicationContext();
        }
        return applicationEventPublisher;
    }
}
