package br.com.iadtec.demo.util;

import br.com.iadtec.demo.entity.Auditable;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public final class AuditoryUtils {

    private AuditoryUtils() {
        throw new UnsupportedOperationException();
    }

    public static Map<String, Object> getEntityStateAsMap(Auditable<?> auditable, EntityManager entityManager) {
        String tableName = EntityReflections.getTableName(auditable);

        Map<String, Object> idColumnName = EntityReflections.getIdColumns(auditable);

        StringBuilder whereId = new StringBuilder(" where ");

        Iterator<String> entryIterator = idColumnName.keySet().iterator();

        while (entryIterator.hasNext()) {
            String next = entryIterator.next();
            whereId.append(next).append(" = :").append(next);
            if (entryIterator.hasNext()) {
                whereId.append(" and ");
            }
        }

        Query query = entityManager.createNativeQuery("select * from " + tableName + whereId.toString(), Tuple.class);
        idColumnName.forEach(query::setParameter);

        Tuple result;
        try {
            result = (Tuple) query.getSingleResult();
            return toTupleMap(result);
        } catch (NoResultException e) {
            log.warn("Entidade {} de Id: {} não encontrada", auditable.getClass().getName(), auditable.getId());
            return new HashMap<>();
        }
    }

    private static Map<String, Object> toTupleMap(Tuple tuple) {
        Map<String, Object> tuples = new HashMap<>();
        tuple.getElements()
                .forEach(tupleElement -> tuples.put(tupleElement.getAlias(), tuple.get(tupleElement)));
        return tuples;
    }
}
