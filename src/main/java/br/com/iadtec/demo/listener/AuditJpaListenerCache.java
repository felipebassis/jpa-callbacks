package br.com.iadtec.demo.listener;


import br.com.iadtec.demo.entity.Auditable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class AuditJpaListenerCache {

    private static final ThreadLocal<Map<CacheKey, Auditable<?>>> PREVIOUS_STATE_CACHE = new ThreadLocal<>();

    private AuditJpaListenerCache() {
        throw new UnsupportedOperationException();
    }

    static void putValueAudit(Auditable<?> auditable) {
        if (Objects.isNull(PREVIOUS_STATE_CACHE.get())) {
            PREVIOUS_STATE_CACHE.set(new HashMap<>());
        }
        PREVIOUS_STATE_CACHE.get()
                .put(new CacheKey(auditable.getClass(), auditable.getId()), auditable);
    }

    static Auditable<?> getPreviousState(Auditable<?> auditable) {
        return PREVIOUS_STATE_CACHE.get()
                .get(new CacheKey(auditable.getClass(), auditable.getId()));
    }

    static void clear() {
        PREVIOUS_STATE_CACHE.remove();
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class CacheKey {
        private Class<?> entityClass;
        private Serializable id;
    }
}
