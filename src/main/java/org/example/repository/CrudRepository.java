package org.example.repository;

import java.util.List;

public interface CrudRepository<T, ID> {

    List<T> selectAll();

    T save(T Entity);

    void delete(ID id);
}
