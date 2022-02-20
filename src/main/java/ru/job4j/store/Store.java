package ru.job4j.store;

import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.model.User;
import java.util.List;

/**
 * @author Shegai Evgenii
 * @since 02.02.2022
 * @version 1.0
 */

public interface Store {

    List<Item> findAllItems();

    Item createItem(Item item, List<Integer> categoryIds);

    Item updateItem(int id);

    User findUserByEmail(String email);

    User createUser(User user);

    List<Category> findAllCategories();
}
