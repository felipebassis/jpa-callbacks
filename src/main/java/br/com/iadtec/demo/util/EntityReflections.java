package br.com.iadtec.demo.util;

import br.com.iadtec.demo.entity.Auditable;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class EntityReflections {

    private EntityReflections() {
        throw new UnsupportedOperationException();
    }


    public static Map<String, Object> getIdColumns(Auditable<?> entity) {
        Map<String, Object> columnNames = new HashMap<>();

        List<Field> fields = new ArrayList<>();

        Class<?> clazz = entity.getClass();
        while (Objects.nonNull(clazz)) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .ifPresentOrElse(field -> columnNames.put(Optional.ofNullable(field.getAnnotation(Column.class))
                                .map(Column::name)
                                .orElse("id"), getFieldFromInstance(field, entity)),
                        () -> {
                            List<Object> embeddedIds = fields.stream()
                                    .filter(field -> field.isAnnotationPresent(EmbeddedId.class))
                                    .map(field -> getFieldFromInstance(field, entity))
                                    .collect(Collectors.toList());
                            embeddedIds.forEach(id -> Stream.of(id.getClass()
                                    .getDeclaredFields())
                                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                                    .filter(field -> !field.isAnnotationPresent(Transient.class))
                                    .forEach(field -> columnNames.put(Optional.ofNullable(field.getAnnotation(Column.class))
                                            .map(Column::name)
                                            .or(() -> Optional.ofNullable(field.getAnnotation(JoinColumn.class))
                                                    .map(JoinColumn::name))
                                            .orElse(toSnakeCase(field, Field::getName)), getFieldFromInstance(field, id))));
                        });
        if (columnNames.isEmpty()) {
            throw new IllegalArgumentException("It was not possible to extract ID fields form entity of class: " + entity.getClass().getSimpleName());
        }
        return columnNames;
    }

    private static Object getFieldFromInstance(Field field, Object entity) {
        field.setAccessible(true);
        try {
            Object value = field.get(entity);
            field.setAccessible(false);
            return value;
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("Error while accessing field value");
        }
    }

    public static String getTableName(Auditable<?> entity) {
        return Optional.ofNullable(entity.getClass()
                .getAnnotation(Table.class))
                .map(Table::name)
                .orElse(toSnakeCase(entity.getClass(), Class::getSimpleName));
    }

    private static <T> String toSnakeCase(T object, Function<T, String> stringFunction) {
        return String.join("_", Arrays.asList(stringFunction.apply(object)
                .split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])")));
    }
}
