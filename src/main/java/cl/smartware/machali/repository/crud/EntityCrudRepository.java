package cl.smartware.machali.repository.crud;

import org.springframework.data.repository.CrudRepository;

import cl.smartware.machali.repository.model.EntityBase;

public interface EntityCrudRepository extends CrudRepository<EntityBase, Integer> {

}
