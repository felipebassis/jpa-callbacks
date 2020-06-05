package br.com.iadtec.demo.listener;

import br.com.iadtec.demo.entity.Auditable;
import br.com.iadtec.demo.util.Autowirer;
import br.com.iadtec.demo.util.EntityReflections;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AuditJpaListener {

    private EntityManagerFactory entityManagerFactory;

    @PreUpdate
    public void getPreviousState(Auditable<?> entity) {
        String tableName = EntityReflections.getTableName(entity);

        Map<String, Object> idColumnName = EntityReflections.getIdColumns(entity);

        StringBuilder whereId = new StringBuilder(" where ");

        List<Map.Entry<String, Object>> entries = new ArrayList<>(idColumnName.entrySet());
        entries.sort(Map.Entry.comparingByKey());

        Iterator<Map.Entry<String, Object>> entryIterator = entries.iterator();

        while (entryIterator.hasNext()){
            Map.Entry<String, Object> next = entryIterator.next();
            whereId.append(next.getKey()).append(" = ? ");
            if (entryIterator.hasNext()) {
                whereId.append("and ");
            }
        }
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery("select * from " + tableName + whereId.toString(), entity.getClass());

        AtomicInteger count = new AtomicInteger(0);
        entries.forEach(entry -> query.setParameter(count.addAndGet(1), entry.getValue()));

        Auditable<?> result = (Auditable<?>) query.getSingleResult();
        AuditJpaListenerCache.putValueAudit(result);
        log.info("[PREVIOUS_STATE] {} - [NEW_STATE] {}", result, entity);
        entityManager.close();
    }

    @PostUpdate
    public void showPreviousAndCurrentState(Auditable<?> entity) {
        log.info("[PREVIOUS_STATE] - {}; [CURRENT_STATE] - {}", AuditJpaListenerCache.getPreviousState(entity), entity);
    }

    @PostPersist
    @PostRemove
    public void showStateOnDeletion(Auditable<?> entity) {
        log.info("[CURRENT_STATE] - {}", entity);
    }

    private EntityManager getEntityManager() {
        if (Objects.isNull(entityManagerFactory)) {
            entityManagerFactory = Autowirer.getBean(EntityManagerFactory.class);
        }
        return entityManagerFactory.createEntityManager();
    }
}
