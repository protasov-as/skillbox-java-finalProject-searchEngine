package main.dao;

import main.models.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FieldDaoCrud extends CrudRepository<Field, Integer> {

    List<Field> findAll();

}
