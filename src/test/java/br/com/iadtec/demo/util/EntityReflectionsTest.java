package br.com.iadtec.demo.util;

import br.com.iadtec.demo.util.entity.embeddedid.BasicEmbeddedIdEntity;
import br.com.iadtec.demo.util.entity.embeddedid.EmbeddedIdWithEmbeddedField;
import br.com.iadtec.demo.util.entity.embeddedid.EmbeddedIdWithForeignKeyEntity;
import br.com.iadtec.demo.util.entity.embeddedid.EmbeddedIdWithForeignKeyWithEmbeddedId;
import br.com.iadtec.demo.util.entity.singlecolumnid.CommonIdEntity;
import br.com.iadtec.demo.util.entity.singlecolumnid.CommonIdWithColumn;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityReflectionsTest {

    @Test
    void shouldReturnDeductedIdColumnNameAndValue() {
        CommonIdEntity entity = new CommonIdEntity(20L);
        Map<String, Object> idColumns = EntityReflections.getIdColumns(entity);
        assertTrue(idColumns.containsKey("id"));
        assertEquals(1, idColumns.size());
        assertEquals(20L, idColumns.get("id"));
    }

    @Test
    void shouldReturnIdColumnNameAndValue() {
        CommonIdWithColumn entity = new CommonIdWithColumn(55L);
        Map<String, Object> idColumns = EntityReflections.getIdColumns(entity);
        assertTrue(idColumns.containsKey("a_id"));
        assertEquals(1, idColumns.size());
        assertEquals(55L, idColumns.get("a_id"));
    }

    @Test
    void shouldReturnIdColumnNamesAndValues() {
        BasicEmbeddedIdEntity entity = new BasicEmbeddedIdEntity(
                new BasicEmbeddedIdEntity.Id(74L, "Test")
        );
        Map<String, Object> idColumns = EntityReflections.getIdColumns(entity);
        assertTrue(idColumns.containsKey("a_long_id"));
        assertTrue(idColumns.containsKey("string"));
        assertEquals(2, idColumns.size());
        assertEquals(74L, idColumns.get("a_long_id"));
        assertEquals("Test", idColumns.get("string"));
    }

    @Test
    void shouldReturnIdColumnNamesAndValuesFromForeignKeyId() {
        EmbeddedIdWithForeignKeyEntity entity = new EmbeddedIdWithForeignKeyEntity(
                new EmbeddedIdWithForeignKeyEntity.Id("A Test", new CommonIdEntity(89L))
        );
        Map<String, Object> idColumns = EntityReflections.getIdColumns(entity);
        assertTrue(idColumns.containsKey("common_id_entity_id"));
        assertTrue(idColumns.containsKey("string"));
        assertEquals(2, idColumns.size());
        assertEquals(89L, idColumns.get("common_id_entity_id"));
        assertEquals("A Test", idColumns.get("string"));
    }

    @Test
    void shouldReturnIdColumnNamesAndValuesFromEmbeddedField() {
        EmbeddedIdWithEmbeddedField entity = new EmbeddedIdWithEmbeddedField(
                new EmbeddedIdWithEmbeddedField.Id("TestEntity",
                        new EmbeddedIdWithEmbeddedField.Id.EmbeddedClass("TestEntityClass", 159L)
                )
        );
        Map<String, Object> idColumns = EntityReflections.getIdColumns(entity);
        assertTrue(idColumns.containsKey("string"));
        assertTrue(idColumns.containsKey("another_string"));
        assertTrue(idColumns.containsKey("a_long"));
        assertEquals(3, idColumns.size());
        assertEquals("TestEntity", idColumns.get("string"));
        assertEquals("TestEntityClass", idColumns.get("another_string"));
        assertEquals(159L, idColumns.get("a_long"));
    }

    @Test
    void shouldReturnIdColumnNamesAndValuesFromEmbeddedIdOfRelations() {
        EmbeddedIdWithForeignKeyWithEmbeddedId entity = new EmbeddedIdWithForeignKeyWithEmbeddedId(
                new EmbeddedIdWithForeignKeyWithEmbeddedId.Id("TestString",
                        new BasicEmbeddedIdEntity(new BasicEmbeddedIdEntity.Id(876L, "AnotherTestString")
                        )
                )
        );
        Map<String, Object> idColumns = EntityReflections.getIdColumns(entity);
        assertTrue(idColumns.containsKey("string"));
        assertTrue(idColumns.containsKey("embedded_id_string"));
        assertTrue(idColumns.containsKey("embedded_id_long"));
        assertEquals(3, idColumns.size());
        assertEquals("TestString", idColumns.get("string"));
        assertEquals("AnotherTestString", idColumns.get("embedded_id_string"));
        assertEquals(876L, idColumns.get("embedded_id_long"));
    }


}