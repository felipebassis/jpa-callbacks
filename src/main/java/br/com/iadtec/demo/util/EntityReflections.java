package br.com.iadtec.demo.util;

import br.com.iadtec.demo.entity.Auditable;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class EntityReflections {

    private static final String SNAKE_CASE_REGEX = "(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])";

    private static final Predicate<Field> IS_VALID_COLUMN_FIELD = field -> !Modifier.isStatic(field.getModifiers()) && (
            !field.isAnnotationPresent(Transient.class) ||
                    !field.isAnnotationPresent(OneToMany.class));

    private EntityReflections() {
        throw new UnsupportedOperationException();
    }

    public static Map<String, Object> getIdColumns(Object entity) {
        Map<String, Object> columnNames = new HashMap<>();

        List<Field> fields = getAllColumnFieldsFromClass(entity.getClass());

        fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .ifPresentOrElse(field -> columnNames.put(Optional.ofNullable(field.getAnnotation(Column.class))
                                .map(Column::name)
                                .orElse(field.getName()), getFieldValueFromInstance(field, entity)),
                        () -> {
                            Object embeddedId = fields.stream()
                                    .filter(field -> field.isAnnotationPresent(EmbeddedId.class))
                                    .map(field -> getFieldValueFromInstance(field, entity))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalArgumentException("Entity id was not found"));
                            getAllColumnFieldsFromClass(embeddedId.getClass())
                                    .stream()
                                    .filter(field -> field.isAnnotationPresent(Column.class) || field.getAnnotations().length == 0)
                                    .forEach(field -> columnNames.put(getColumnName(field), getFieldValueFromInstance(field, embeddedId)));
                            columnNames.putAll(getIdColumnsFromRelations(embeddedId));
                            columnNames.putAll(getEmbeddedColumnsFromEmbeddedIdClass(embeddedId));
                        });
        if (columnNames.isEmpty()) {
            throw new IllegalArgumentException("It was not possible to extract ID fields form entity of class: " + entity.getClass().getSimpleName());
        }
        return columnNames;
    }

    private static Map<String, Object> getIdColumnsFromRelations(Object embeddedId) {
        Map<String, Object> idColumns = new HashMap<>();

        getAllColumnFieldsFromClass(embeddedId.getClass())
                .stream()
                .filter(field -> field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class))
                .forEach(field -> {
                    Object fieldValue = getFieldValueFromInstance(field, embeddedId);
                    getAllColumnFieldsFromClass(fieldValue.getClass())
                            .stream()
                            .filter(fieldFromField -> fieldFromField.isAnnotationPresent(Id.class))
                            .findFirst()
                            .ifPresentOrElse(id -> idColumns.put(getColumnName(field), getFieldValueFromInstance(id, fieldValue)),
                                    () -> idColumns.putAll(getEmbeddedIdsFromRelations(embeddedId)));
                });
        return idColumns;
    }

    private static Map<String, Object> getEmbeddedIdsFromRelations(Object embeddedId) {
        Map<String, Object> columns = new HashMap<>();
        getAllColumnFieldsFromClass(embeddedId.getClass())
                .stream()
                .filter(field -> field.isAnnotationPresent(JoinColumns.class))
                .parallel()
                .forEach(field -> Arrays.asList(field.getAnnotation(JoinColumns.class).value())
                        .parallelStream()
                        .forEach(joinColumn -> columns.put(joinColumn.name(), getValueBasedOnReferencedColumnName(embeddedId, field, joinColumn.referencedColumnName()))));
        return columns;
    }

    private static Object getValueBasedOnReferencedColumnName(Object embeddedId, Field relationField, String columnName) {
        Object relation = getFieldValueFromInstance(relationField, embeddedId);
        List<Field> allColumnFieldsFromClass = getAllColumnFieldsFromClass(relation.getClass());
        return allColumnFieldsFromClass
                .stream()
                .filter(field -> getColumnName(field).equalsIgnoreCase(columnName))
                .findFirst()
                .map(field -> {
                    if (field.isAnnotationPresent(ManyToOne.class) ||
                            field.isAnnotationPresent(OneToOne.class)) {
                        return getIdColumns(relation).get(columnName);
                    }
                    return getFieldValueFromInstance(field, relation);
                })
                .or(() -> allColumnFieldsFromClass
                        .stream()
                        .filter(field -> field.isAnnotationPresent(EmbeddedId.class) || field.isAnnotationPresent(Embedded.class))
                        .map(field -> {
                            Object fieldValueFromInstance = getFieldValueFromInstance(field, relation);
                            return getAllColumnFieldsFromClass(fieldValueFromInstance.getClass())
                                    .stream()
                                    .filter(field1 -> getColumnName(field1).equalsIgnoreCase(columnName))
                                    .findFirst()
                                    .map(field1 -> getFieldValueFromInstance(field1, fieldValueFromInstance));
                        })
                        .findFirst()
                        .map(Optional::get)
                )
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));
    }

    private static List<Field> getAllColumnFieldsFromClass(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (Objects.nonNull(clazz)) {
            if (clazz.isAnnotationPresent(Entity.class) ||
                    clazz.isAnnotationPresent(MappedSuperclass.class) ||
                    clazz.isAnnotationPresent(Embeddable.class)
            ) {
                fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            }
            clazz = clazz.getSuperclass();
        }
        return fields.stream()
                .filter(IS_VALID_COLUMN_FIELD)
                .collect(Collectors.toList());
    }

    private static String getColumnName(Field field) {
        return Optional.ofNullable(field.getAnnotation(Column.class))
                .map(Column::name)
                .or(() -> Optional.ofNullable(field.getAnnotation(JoinColumn.class))
                        .map(JoinColumn::name))
                .or(() -> {
                    if (field.isAnnotationPresent(OneToOne.class) ||
                            field.isAnnotationPresent(ManyToOne.class)) {
                        return Optional.of(toSnakeCase(field, Field::getName).concat("_id"));
                    }
                    return Optional.empty();
                })
                .orElse(toSnakeCase(field, Field::getName));
    }

    private static Map<String, Object> getEmbeddedColumnsFromEmbeddedIdClass(Object embeddedId) {
        Map<String, Object> embeddedFields = new HashMap<>();
        getAllColumnFieldsFromClass(embeddedId.getClass())
                .stream()
                .filter(field -> field.isAnnotationPresent(Embedded.class))
                .map(field -> getFieldValueFromInstance(field, embeddedId))
                .parallel()
                .forEach(objectField -> getAllColumnFieldsFromClass(objectField.getClass())
                        .forEach(field -> {
                            if (field.isAnnotationPresent(Embedded.class)) {
                                embeddedFields.putAll(getEmbeddedColumnsFromEmbeddedIdClass(Collections.singletonList(objectField)));
                                return;
                            }
                            embeddedFields.put(getColumnName(field), getFieldValueFromInstance(field, objectField));
                        }));
        return embeddedFields;
    }

    private static Object getFieldValueFromInstance(Field field, Object entity) {
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
                .orElse(toSnakeCase(entity.getClass(), Class::getSimpleName).toLowerCase());
    }

    private static <T> String toSnakeCase(T object, Function<T, String> stringFunction) {
        return String.join("_", Arrays.asList(stringFunction.apply(object)
                .split(SNAKE_CASE_REGEX)))
                .toLowerCase();
    }
}
