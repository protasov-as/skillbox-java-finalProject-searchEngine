package main.dao;

import main.models.Site;
import main.models.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;
import main.utils.HibernateSessionFactoryUtil;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.List;

@Component
public class SiteDao implements DaoInterface<Site, Integer>{

    @Override
    public int save(Site site) {
        int id;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        id = (int) session.save(site);
        tx1.commit();
        session.close();
        return id;
    }

    @Override
    public void persist(Site site) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(site);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Site site) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(site);
        tx1.commit();
        session.close();
    }

    @Override
    public Site findById(Integer id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        return session.get(Site.class, id);
    }

    public Site findByName(String url) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Site where url like :url");
        query.setParameter("url", url);
        Site site = (Site) query.getSingleResult();
        return site;
    }

    public Site findByExactName(String url) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Site where url =:url");
        query.setParameter("url", url);
        Site site = (Site) query.getSingleResult();
        return site;
    }

    public Boolean checkIfSiteExistsByExactMatch(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Site where url =:url");
        query.setParameter("url", name);
        return (!query.getResultList().isEmpty());
    }

    public Boolean checkIfSiteExists(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        Query query = session.createQuery("from Site where url like :url");
        query.setParameter("url", name);
        return (!query.getResultList().isEmpty());
    }

    @Override
    public void delete(Site site) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(site);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Site> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        List<Site> sites = session.createQuery("From Site").list();
        return sites;
    }

    @Override
    public void deleteAll() {
        List<Site> sites = findAll();
        for (Site site : sites) {
            delete(site);
        }
    }

    public Long countSites() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        session.setDefaultReadOnly(true);
        return (Long) session.createQuery("SELECT count(*) from Site").getSingleResult();
    }

    public void markAllSitesAsFailed(){
        List<Site> allSites = findAll();
        allSites.forEach(s -> {
            s.setStatus(Status.FAILED);
            s.setLastError("Индексация прервана вручную!");
            update(s);
        });
    }

    @Override
    public void dropAndCreateTable() {
        String dropPage = "DROP TABLE IF EXISTS site";
        String createPage = "CREATE TABLE site(" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                "status ENUM('INDEXING', 'INDEXED', 'FAILED') NOT NULL, " +
                "status_time DATETIME NOT NULL, " +
                "last_error TEXT, " +
                "url VARCHAR(255) NOT NULL, " +
                "name VARCHAR(255) NOT NULL, " +
                        "PRIMARY KEY(id))";
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        System.out.println("Dropping table 'site'");
        session.createSQLQuery(dropPage).executeUpdate();
        System.out.println("Creating table 'site'");
        session.createSQLQuery(createPage).executeUpdate();
        tx1.commit();
        session.close();
    }
}
