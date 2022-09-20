package main.dao;

import main.models.Field;
import org.hibernate.Session;
import org.hibernate.Transaction;
import main.utils.HibernateSessionFactoryUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FieldDao implements DaoInterface<Field, Integer>{

    @Override
    public int save(Field field) {
        int id;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        id = (int) session.save(field);
        tx1.commit();
        session.close();
        return id;
    }

    @Override
    public void persist(Field field) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(field);
        tx1.commit();
        session.close();

    }

    @Override
    public void update(Field field) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(field);
        tx1.commit();
        session.close();
    }

    @Override
    public Field findById(Integer id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        return session.get(Field.class, id);
    }

    @Override
    public void delete(Field field) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(field);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Field> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        List<Field> fields = session.createQuery("From Field").list();
        return fields;
    }

    @Override
    public void deleteAll() {
        List<Field> fields = findAll();
        if (!fields.isEmpty()) {
            for (Field field : fields) {
                delete(field);
            }
        }
    }

    @Override
    public void dropAndCreateTable() {
        String dropPage = "DROP TABLE IF EXISTS field";
        String createPage = "CREATE TABLE field(" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "selector VARCHAR(255) NOT NULL, " +
                "weight FLOAT NOT NULL, " +
                        "PRIMARY KEY(id))";
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        System.out.println("Dropping table 'field'");
        session.createSQLQuery(dropPage).executeUpdate();
        System.out.println("Creating table 'field'");
        session.createSQLQuery(createPage).executeUpdate();
        tx1.commit();
        session.close();
    }


}
