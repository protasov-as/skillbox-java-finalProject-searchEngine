package main.dao;

import main.models.Lemma;
import org.hibernate.Session;
import org.hibernate.Transaction;
import main.services.PageService;
import main.utils.HibernateSessionFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Component
public class LemmaDao implements DaoInterface<Lemma, Integer>{

    @Autowired
    PageService pageService;

    @Override
    public int save(Lemma lemma) {
        int id;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        id = (int) session.save(lemma);
        tx1.commit();
        session.close();
        return id;
    }

    public int saveOrUpdateLemma(Lemma lemma) {
        int id;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Query query = session.createQuery("from Lemma where lemma = :lemma and site_id = :site_id");
        query.setParameter("lemma", lemma.getLemma());
        query.setParameter("site_id", lemma.getSiteID());
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);

        if(!query.getResultList().isEmpty()) {
            Lemma lemmaFromDB = (Lemma) query.getSingleResult();
            int frequency = lemmaFromDB.getFrequency() + 1;
            lemmaFromDB.setFrequency(frequency);
            session.update(lemmaFromDB);
            id = lemmaFromDB.getId();
        } else {
            id = (int) session.save(lemma);
            }
        tx1.commit();
        session.close();
        return id;
    }

    public Boolean checkIfLemmaExists(String name) {
        Query query = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Lemma where lemma = :lemma");
        query.setParameter("lemma", name);
        return (!query.getResultList().isEmpty());
    }

    @Override
    public void persist(Lemma lemma) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(lemma);
        tx1.commit();
        session.close();

    }

    @Override
    public void update(Lemma lemma) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(lemma);
        tx1.commit();
        session.close();
    }

    public void updateLemmaCount(Lemma lemma) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(lemma);
        tx1.commit();
        session.close();
    }

    @Override
    public Lemma findById(Integer id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        return session.get(Lemma.class, id);
    }

    @Override
    public List<Lemma> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        List<Lemma> lemmas = session.createQuery("From Lemma").list();
        return lemmas;
    }

    public List<Lemma> findAllBySiteID(int siteID) {
        List<Lemma> lemmas = HibernateSessionFactoryUtil
                .getSessionFactory()
                .openSession()
                .createQuery("From Lemma where site_id = :site_id")
                .setParameter("site_id", siteID)
                .list();
        return lemmas;
    }

    public List<Lemma> findByName(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        List<Lemma> lemmas = session.createQuery("from Lemma where lemma = :lemma").setParameter("lemma", name).list();

        if(!lemmas.isEmpty()) {
            return lemmas;
        } else {
            ArrayList<Lemma> oneEmptyLemmaList = new ArrayList<>();
            Lemma lemma = new Lemma();
            lemma.setLemma(name);
            lemma.setFrequency(0);
            oneEmptyLemmaList.add(lemma);
            return oneEmptyLemmaList;
        }
    }

    public Lemma findByNameAndSiteID(String name, int siteID) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Lemma where lemma = :lemma and site_id = :site_id");
        query.setParameter("lemma", name);
        query.setParameter("site_id", siteID);
        if(!query.getResultList().isEmpty()) {
            return (Lemma) query.getSingleResult();
        } else {
            Lemma lemma = new Lemma();
            lemma.setLemma(name);
            lemma.setFrequency(0);
            return lemma;
        }
    }

    public boolean isLemmaTooFrequent(Lemma lemma) {
        int totalPagesCount = pageService.findAllPages().size();
        return lemma.getFrequency() >= totalPagesCount / 2;
    }

    @Override
    public void delete(Lemma lemma) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(lemma);
        tx1.commit();
        session.close();
    }

    @Override
    public void deleteAll() {
        List<Lemma> lemmas = findAll();
        if (!lemmas.isEmpty()) {
            for (Lemma lemma : lemmas) {
                delete(lemma);
            }
        }
    }

    public Long countLemmas() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        return (Long) session.createQuery("SELECT count(*) from Lemma").getSingleResult();
    }

    public Long countLemmasOnSite(int siteID) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("SELECT count(*) from Lemma where site_id = :site_id");
        query.setParameter("site_id", siteID);
        return (Long) query.getSingleResult();
    }

    @Override
    public void dropAndCreateTable() {
        String dropPage = "DROP TABLE IF EXISTS lemma";
        String createPage = "CREATE TABLE lemma(" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "lemma VARCHAR(255) NOT NULL, " +
                "frequency INT NOT NULL, " +
                "site_id INT NOT NULL, " +
                        "PRIMARY KEY(id))";
//                        "PRIMARY KEY(id), UNIQUE KEY(lemma(50)))";

        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        System.out.println("Dropping table 'lemma'");
        session.createSQLQuery(dropPage).executeUpdate();
        System.out.println("Creating table 'lemma'");
        session.createSQLQuery(createPage).executeUpdate();
        tx1.commit();
        session.close();
    }

}
