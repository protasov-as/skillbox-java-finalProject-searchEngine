package main.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;

@Component
public class HibernateSessionFactoryUtil {

    private static SessionFactory sessionFactory;
    private static Session currentSession;
    private static Transaction currentTransaction;

    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null) {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }
        return sessionFactory;
    }

    public static Session openCurrentSession() {
        if(currentSession == null) {
            currentSession = getSessionFactory().openSession();
        }
        return currentSession;
    }

    public static Session openCurrentSessionWithTransaction() {
        if(currentSession == null) {
            currentSession = getSessionFactory().openSession();
        }
        if(currentTransaction == null) {
            currentTransaction = currentSession.beginTransaction();
        }
        return currentSession;
    }

    public static void closeCurrentSession() {
        if(currentTransaction != null) {
            currentSession.close();
        }
    }

    public static void closeCurrentSessionWithTransaction() {
        if(currentSession != null && currentTransaction != null) {
            currentTransaction.commit();
            currentSession.close();
        }
    }

    public static void closeCurrentTransaction() {
        if(currentTransaction != null) {
            currentTransaction.commit();
        }
    }

    public static Session getCurrentSession() {
        return currentSession;
    }

}
