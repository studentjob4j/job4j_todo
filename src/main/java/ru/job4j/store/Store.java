package ru.job4j.store;

import ru.job4j.model.Item;
import java.util.List;

/**
 * @author Shegai Evgenii
 * @since 02.02.2022
 * @version 1.0
 */

public interface Store {

    List<Item> findAllItems();

    Item createItem(String description);

    Item updateItem(int id);
}
