CREATE TABLE users
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL
);

CREATE TABLE file
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(128) NOT NULL,
    file_path VARCHAR(128) NOT NULL,
    status VARCHAR(128) NOT NULL
);

CREATE TABLE event
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users (id),
    file_id INTEGER NOT NULL REFERENCES file (id)
);

