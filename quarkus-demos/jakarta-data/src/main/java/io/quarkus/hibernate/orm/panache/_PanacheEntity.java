package io.quarkus.hibernate.orm.panache;

import jakarta.data.metamodel.SortableAttribute;
import jakarta.data.metamodel.impl.SortableAttributeRecord;

/**
 * Jakarta Data static metamodel stub for {@link PanacheEntity}.
 * <p>
 * Hibernate Processor 7.x generates {@code _Entity} interfaces that extend the
 * parent entity's metamodel. Quarkus Panache does not yet provide this class,
 * so we supply a minimal stub to satisfy compilation.
 */
public interface _PanacheEntity {
    String ID = "id";
    SortableAttribute<PanacheEntity> id = new SortableAttributeRecord<>(ID);
}
