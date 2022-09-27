package main.services;

import main.dao.FieldDao;
import main.repository.FieldRepository;
import main.models.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FieldService {

    private FieldDao fieldDao;
    private FieldRepository fieldRepository;

    @Autowired
    public FieldService(FieldDao fieldDao, FieldRepository fieldRepository) {
        this.fieldDao = fieldDao;
        this.fieldRepository = fieldRepository;
    }

    public FieldService() {
    }

    public Optional<Field> findField(int id) {
        return fieldRepository.findById(id);
    }

    public void saveField(Field field) {
        fieldRepository.save(field);
    }

    public void deleteField(Field field) {
        fieldRepository.delete(field);
    }

    public void updateField(Field field) {
        fieldDao.update(field);
    }

    public List<Field> findAllFields() {
        return fieldRepository.findAll();
    }

    public void deleteAllFields() {
        fieldRepository.deleteAll();
    }

    public void initializeFields(){
        deleteAllFields();

        Field title = new Field();
        title.setName("title");
        title.setSelector("title");
        title.setWeight(1.0f);
        saveField(title);

        Field body = new Field();
        body.setName("body");
        body.setSelector("body");
        body.setWeight(0.8f);
        saveField(body);

    }

    public void dropAndCreateTableField(){
        fieldDao.dropAndCreateTable();
    }
}
