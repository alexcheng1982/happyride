package io.vividcode.happyride.addressservice;

import io.vividcode.happyride.common.AbstractEntity;
import io.vividcode.happyride.common.BaseEntityWithGeneratedId;
import io.vividcode.happyride.common.EntityWithGeneratedId;
import org.hibernate.dialect.PostgreSQLDialect;
import org.postgresql.util.PGInterval;
import org.postgresql.util.PGobject;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class DefaultRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    hints.reflection()
        .registerType(PostgreSQLDialect.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
        .registerType(PGobject.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
        .registerType(PGInterval.class, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.INVOKE_DECLARED_METHODS)
        .registerType(AbstractEntity.class, MemberCategory.DECLARED_FIELDS)
        .registerType(
            BaseEntityWithGeneratedId.class, MemberCategory.DECLARED_FIELDS)
        .registerType(
            EntityWithGeneratedId.class, MemberCategory.DECLARED_FIELDS);
  }
}
