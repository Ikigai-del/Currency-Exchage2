package org.example.repository;

import org.eclipse.tags.shaded.org.apache.bcel.Repository;
import org.example.Entity.CurrencyEntity;

import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<CurrencyEntity, Long> {

    Optional<CurrencyEntity> findById (long id);

    Optional<CurrencyEntity> findByCode (String code);
}
