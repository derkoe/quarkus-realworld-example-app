create table users
(
    id       uuid primary key,
    username varchar(255) UNIQUE,
    password varchar(255),
    email    varchar(255) UNIQUE,
    bio      text,
    image    varchar(511)
);

create table articles
(
    id          uuid primary key,
    user_id     uuid,
    slug        varchar(255) UNIQUE,
    title       varchar(255),
    description text,
    body        text,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
