package main.dao;

import main.models.Index;
import org.hibernate.Session;
import org.hibernate.Transaction;
import main.utils.HibernateSessionFactoryUtil;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.List;

@Component
public class IndexDao implements DaoInterface<Index, Integer>{

    @Override
    public int save(Index index) {
        int id;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        id = (int) session.save(index);
        tx1.commit();
        session.close();
        return id;
    }

    public int saveOrUpdateIndex(Index index) {
        int id;
        if(!checkIfIndexExists(index.getPageId(), index.getLemmaId())) {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            Transaction tx1 = session.beginTransaction();
            id = (int) session.save(index);
            tx1.commit();
            session.close();
        } else {
            System.out.println("Index exists!");
            Index indexFromDB = findByIdPair(index.getPageId(), index.getLemmaId());
            float rank = index.getRank() + indexFromDB.getRank();
            indexFromDB.setRank(rank);
            update(indexFromDB);
            id = indexFromDB.getId();
        }
        return id;
    }

    public Index findByIdPair(int page_id, int lemma_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Index where page_id = :page_id and lemma_id = :lemma_id");
        query.setParameter("page_id", page_id);
        query.setParameter("lemma_id", lemma_id);
        Index index = (Index) query.getSingleResult();
        return index;
    }

    public Boolean checkIfIndexExists(int page_id, int lemma_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Index where page_id = :page_id and lemma_id = :lemma_id");
        query.setParameter("page_id", page_id);
        query.setParameter("lemma_id", lemma_id);
        return (!query.getResultList().isEmpty());
    }

    @Override
    public void persist(Index index) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(index);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Index index) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(index);
        tx1.commit();
        session.close();
    }

    @Override
    public Index findById(Integer id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        return session.get(Index.class, id);
    }

    public List<Index> findByLemmaId(int lemma_id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Index where lemma_id = :lemma_id");
        query.setParameter("lemma_id", lemma_id);
        List<Index> indexes = query.getResultList();
        return indexes;
    }

    public void deleteByPageId(int page_id) {
        Query query = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Index where page_id = :page_id");
        query.setParameter("page_id", page_id);
        List<Index> indexes = query.getResultList();
        indexes.forEach(this::delete);
    }

    @Override
    public void delete(Index index) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(index);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Index> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        List<Index> indexes = session.createQuery("From Index").list();
        return indexes;
    }

    @Override
    public void deleteAll() {
        List<Index> indexes = findAll();
        if (!indexes.isEmpty()) {
            for (Index index : indexes) {
                delete(index);
            }
        }
    }

    public Long countIndex() {
        return (Long) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("SELECT count(*) from Index").getSingleResult();
    }

    @Override
    public void dropAndCreateTable() {
        String dropPage = "DROP TABLE IF EXISTS `index`";
        String createPage = "CREATE TABLE `index`(" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "page_id INT NOT NULL, " +
                "lemma_id INT NOT NULL, " +
                "`rank` FLOAT NOT NULL, " +
                "PRIMARY KEY(id))";
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        System.out.println("Dropping table 'index'");
        session.createSQLQuery(dropPage).executeUpdate();
        System.out.println("Creating table 'index'");
        session.createSQLQuery(createPage).executeUpdate();
        tx1.commit();
        session.close();
    }
}
