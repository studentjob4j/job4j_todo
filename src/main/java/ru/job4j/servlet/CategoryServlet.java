package ru.job4j.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.model.Category;
import ru.job4j.store.HibernateStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Shegai Evgenii
 * @since 20.02.2022
 * @version 1.0
 */

public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Category> items = HibernateStore.instanceOf().findAllCategories();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(items);
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().write(json);
    }
}
