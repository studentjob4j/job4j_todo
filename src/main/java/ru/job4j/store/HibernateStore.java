package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Item;
import java.util.List;
import java.util.function.Function;

/**
 * @author Shegai Evgenii
 * @since 02.02.2022
 * @version 1.0
 */

public class HibernateStore implements Store, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateStore.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();

    private final SessionFactory sessionFactory = new MetadataSources(registry)
            .buildMetadata()
            .buildSessionFactory();

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    private static final class HibernateStoreHolder {
        private static final Store INSTANCE = new HibernateStore();
    }

    public static Store instanceOf() {
        return HibernateStoreHolder.INSTANCE;
    }

    @Override
    public List<Item> findAllItems() {
        return this.tx(session -> session.createQuery("from ru.job4j.model.Item").getResultList());
    }

    @Override
    public Item createItem(String description) {
        Item item = new Item();
        item.setDescription(description);
        this.tx(session -> session.save(item));
        return item;
    }

    @Override
    public Item updateItem(int id) {
        return this.tx(session -> {
           Item item = session.get(Item.class, id);
           item.setDone(!item.isDone());
           return item;
        });
    }


    private <T> T tx(final Function<Session, T> command) {
        final Session session = sessionFactory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
    }
}
