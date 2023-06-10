CREATE TABLE users (
    user_id serial PRIMARY KEY,
    username varchar(16) NOT NULL,
    email varchar(256) NOT NULL,
    password varchar(64) NOT NULL,
    name varchar(32) NOT NULL,
    surname varchar(32) NOT NULL,
    avatar_id int DEFAULT 1,
    level_id int DEFAULT 1,
    status varchar(128),
);