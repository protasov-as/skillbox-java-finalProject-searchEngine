package main.dao;

import main.models.Page;
import org.hibernate.Session;
import org.hibernate.Transaction;
import main.utils.HibernateSessionFactoryUtil;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.List;

@Component
public class PageDao implements DaoInterface<Page, Integer>{

    @Override
    public int save(Page page) {
        int id;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        System.out.println("!!!!" + page);
        id = (int) session.save(page);
        tx1.commit();
        session.close();
        return id;
    }

    @Override
    public void persist(Page page) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(page);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Page page) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(page);
        tx1.commit();
        session.close();
    }

    @Override
    public Page findById(Integer id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        return session.get(Page.class, id);
    }

    public Page findByName(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Page where path = :path");
        query.setParameter("path", name);
        Page page = (Page) query.getSingleResult();
        return page;
    }

    public Boolean checkIfPageExists(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Page where path = :path");
        query.setParameter("path", name);
        return (!query.getResultList().isEmpty());
    }

    public Long countPages() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        return (Long) session.createQuery("SELECT count(*) from Page").getSingleResult();
    }

    public Long countPagesOnSite(int siteID) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("SELECT count(*) from Page where site_id = :site_id");
        query.setParameter("site_id", siteID);
        return (Long) query.getSingleResult();
    }

    @Override
    public void delete(Page page) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(page);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Page> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        List<Page> pages = session.createQuery("From Page").list();
        return pages;
    }

    public List<Page> findAllbySiteId(int siteID) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        List<Page> pages = session
                .createQuery("From Page where site_id = :site_id")
                .setParameter("site_id", siteID)
                .list();
        return pages;
    }

    @Override
    public void deleteAll() {
        List<Page> pages = findAll();
        for (Page page : pages) {
            delete(page);
        }
    }

    @Override
    public void dropAndCreateTable() {
        String dropPage = "DROP TABLE IF EXISTS page";
        String createPage = "CREATE TABLE page (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "path TEXT NOT NULL, " +
                "code INT NOT NULL, " +
                "content MEDIUMTEXT NOT NULL, " +
                "site_id INT NOT NULL, " +
//                        "PRIMARY KEY(id))");
                "PRIMARY KEY(id), KEY(path(200)))";
        String alterPage = "ALTER TABLE page CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        System.out.println("Dropping table 'page'");
        session.createSQLQuery(dropPage).executeUpdate();
        System.out.println("Creating table 'page'");
        session.createSQLQuery(createPage).executeUpdate();
        System.out.println("Altering charset in table 'page'");
        session.createSQLQuery(alterPage).executeUpdate();
        tx1.commit();
        session.close();
    }
}
