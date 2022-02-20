   CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    description TEXT,
    done        BOOLEAN   DEFAULT FALSE,
    created     TIMESTAMP DEFAULT now(),
    user_id INT REFERENCES users(id)
    );

    CREATE TABLE users (
    id       SERIAL PRIMARY KEY,
    name     TEXT,
    email    TEXT NOT NULL UNIQUE,
    password TEXT,
    created  TIMESTAMP DEFAULT now()
);

   CREATE TABLE category (
    id SERIAL PRIMARY KEY ,
    name TEXT
   );

   CREATE TABLE item_category (
    item_id     INT REFERENCES item (id),
    categories_id INT REFERENCES category (id),
    PRIMARY KEY (item_id, categories_id)
);

   INSERT into category (name) VALUES ('Жизнь');
   INSERT into category (name) VALUES ('Здоровье');
   INSERT into category (name) VALUES ('Отношения');
   INSERT into category (name) VALUES ('Инвестирование');