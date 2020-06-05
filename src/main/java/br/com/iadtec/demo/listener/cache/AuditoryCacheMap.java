package br.com.iadtec.demo.listener.cache;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class AuditoryCacheMap {

    private final Map<Key, Value> cache = new HashMap<>();

    AuditoryCacheMap() {

    }

    void put(Class<?> entityClass, Serializable id, Map<String, Object> previousState) {
        cache.put(Key.from(entityClass, id), new Value(previousState));
    }

    Value get(Class<?> entityClass, Serializable id) {
        return cache.get(Key.from(entityClass, id));
    }

    Collection<Value> values() {
        return this.cache.values();
    }

    boolean isAllEntitiesCommitted() {
        return cache.values()
                .parallelStream()
                .allMatch(Value::isCommitted);
    }

    void markAsCommitted(Class<?> entityClass, Serializable id) {
        Value value = cache.get(Key.from(entityClass, id));
        value.committed = true;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "from")
    @EqualsAndHashCode
    private static class Key {
        private final Class<?> entityClass;

        private final Serializable id;
    }

    public static class Value {

        private Value(Map<String, Object> resultSet) {
            previousValue.putAll(resultSet);
        }

        @Getter
        private final Map<String, Object> previousValue = new HashMap<>();

        @Getter
        private boolean committed;

        public boolean isNotEmpty() {
            return !this.previousValue.isEmpty();
        }
    }
}
