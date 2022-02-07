package ru.job4j.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.model.User;
import ru.job4j.store.HibernateStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Shegai Evgenii
 * @since 08.02.2022
 * @version 1.0
 */

public class RegServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("registration.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(req.getReader().readLine(), User.class);
        User userWithSameEmail = HibernateStore.instanceOf().findUserByEmail(user.getEmail());
        if (userWithSameEmail != null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write("Email занят другим пользователем");
        } else {
            User createdUser = HibernateStore.instanceOf().createUser(user);
            HttpSession session = req.getSession();
            session.setAttribute("user", createdUser);
            String json = objectMapper.writeValueAsString(createdUser);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(json);
        }
    }
}
