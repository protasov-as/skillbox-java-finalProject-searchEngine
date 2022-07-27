package main.services;

import main.dao.FieldDao;
import main.models.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FieldService {

    @Autowired
    private FieldDao fieldDao;

    public FieldService() {
    }

    public Field findField(int id) {
        return fieldDao.findById(id);
    }

    public void saveField(Field field) {
        fieldDao.save(field);
    }

    public void persistField(Field field) {
        fieldDao.persist(field);
    }

    public void deleteField(Field field) {
        fieldDao.delete(field);
    }

    public void updateField(Field field) {
        fieldDao.update(field);
    }

    public List<Field> findAllFields() {
        return fieldDao.findAll();
    }

    public void deleteAllFields() {
        fieldDao.deleteAll();
    }

    public void initializeFields(){
        deleteAllFields();

        Field title = new Field();
        title.setName("title");
        title.setSelector("title");
        title.setWeight(1.0f);
        persistField(title);

        Field body = new Field();
        body.setName("body");
        body.setSelector("body");
        body.setWeight(0.8f);
        persistField(body);

    }

    public void dropAndCreateTableField(){
        fieldDao.dropAndCreateTable();
    }
}
