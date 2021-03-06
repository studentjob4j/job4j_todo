package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.model.User;
import org.hibernate.query.*;
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
        return this.tx(session ->   session.createQuery("SELECT DISTINCT item FROM Item item "
                + "JOIN FETCH item.categories ORDER BY item.created DESC").list());
    }

    public Item createItem(Item item, List<Integer> categoryIds) {
        int id = (int) this.tx(session -> {
            for (Integer categoryId : categoryIds) {
                Category category = session.get(Category.class, categoryId);
                item.addCategory(category);
            }
            return session.save(item);
        });
        item.setId(id);
        return item;
    }

    @Override
    public Item updateItem(int id) {
        Item item = tx(session -> {
            Query<Item> query = session.createQuery("FROM Item item JOIN FETCH item.categories WHERE item.id = :id");
            query.setParameter("id", id);
            return query.uniqueResult();
        });
        if (item == null) {
            throw new IllegalStateException("Could not find a record in DB");
        }
        item.setDone(!item.isDone());
        tx(session -> {
            Query query = session.createQuery(
                    "UPDATE Item item SET " + "done = :done " + "WHERE id = :id"
            );
            query.setParameter("id", item.getId());
            query.setParameter("done", item.isDone());
            return query.executeUpdate() > 0;
        });
        return item;

    }

    @Override
    public User createUser(User user) {
        return this.tx(session -> {
            session.save(user);
            return user;
        });
    }

    @Override
    public User findUserByEmail(String email) {
        return (User) this.tx(session -> {
            String hql = "FROM ru.job4j.model.User WHERE email = :email";
            Query query = session.createQuery(hql);
            query.setParameter("email", email);
            return query.uniqueResult();
        });
    }

    @Override
    public List<Category> findAllCategories() {
        return this.tx(session ->
                session.createQuery("from Category category order by category.name asc").list());
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
