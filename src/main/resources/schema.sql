DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS book;

CREATE TABLE author (
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(128) NOT NULL,
    birthday    VARCHAR(32)
);

CREATE TABLE book (
    isbn        VARCHAR(128) PRIMARY KEY,
    title       VARCHAR(128),
    authors     VARCHAR(128),
    pubyear     INTEGER,
    price       DOUBLE,
    genre       VARCHAR(32)
);
