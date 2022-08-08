package main.services;

import main.dao.FieldDao;
import main.dao.FieldDaoCrud;
import main.models.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FieldService {

    @Autowired
    private FieldDao fieldDao;

    @Autowired
    private FieldDaoCrud fieldDaoCrud;

    public FieldService() {
    }

    public Optional<Field> findField(int id) {
        return fieldDaoCrud.findById(id);
    }

    public void saveField(Field field) {
        fieldDaoCrud.save(field);
    }

    public void deleteField(Field field) {
        fieldDaoCrud.delete(field);
    }

    public void updateField(Field field) {
        fieldDao.update(field);
    }

    public List<Field> findAllFields() {
        return fieldDaoCrud.findAll();
    }

    public void deleteAllFields() {
        fieldDaoCrud.deleteAll();
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
