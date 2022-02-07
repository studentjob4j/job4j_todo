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