package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shegai Evgenii
 * @since 02.02.2022
 * @version 1.0
 */

public class HibernateStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateStore.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();

    private final SessionFactory sessionFactory = new MetadataSources(registry)
            .buildMetadata()
            .buildSessionFactory();

    private static final class HibernateStoreHolder {
        private static final Store INSTANCE = new HibernateStore();
    }

    public static Store instanceOf() {
        return HibernateStoreHolder.INSTANCE;
    }

    @Override
    public List<Item> findAllItems() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List list = new ArrayList<>();
        try {
            list = session.createQuery("from ru.job4j.model.Item").getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
          LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public Item createItem(String description) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Item item = new Item();
        try {
            item.setDescription(description);
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return item;
    }

    @Override
    public Item updateItem(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            Item item = session.get(Item.class, id);
            if (item == null) {
                throw new IllegalStateException("Could not find a record in DB");
            }
            item.setDone(!item.isDone());
            session.update(item);
            session.getTransaction().commit();
            return item;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return null;
    }
}
