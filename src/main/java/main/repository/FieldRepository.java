package main.repository;

import main.models.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FieldRepository extends CrudRepository<Field, Integer> {

    List<Field> findAll();

}
