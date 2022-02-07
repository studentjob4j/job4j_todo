package ru.job4j.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Item;
import ru.job4j.model.User;
import ru.job4j.store.HibernateStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Shegai Evgenii
 * @since 02.02.2022
 * @version 1.0
 */

public class ItemServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ItemServlet.class.getName());

    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Item> items = HibernateStore.instanceOf().findAllItems();
        String json = objectMapper.writeValueAsString(items);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Item item = objectMapper.readValue(req.getReader().readLine(), Item.class);
            User user = (User) req.getSession().getAttribute("user");
            item.setUser(user);
            Item itemInDb = HibernateStore.instanceOf().createItem(item);
            String json = objectMapper.writeValueAsString(itemInDb);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(json);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Item item = objectMapper.readValue(req.getReader().readLine(), Item.class);
            Item itemInDb = HibernateStore.instanceOf().updateItem(item.getId());
            String json = objectMapper.writeValueAsString(itemInDb);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(json);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
