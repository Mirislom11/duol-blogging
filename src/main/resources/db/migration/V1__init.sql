CREATE SEQUENCE sequence_generator
    INCREMENT BY 50
    MINVALUE 1
    START 1050
    CACHE 1
    NO CYCLE;

CREATE TABLE theme
(
    id       int8 primary key,
    name     varchar,
    created_date timestamp default current_timestamp,
    last_update_date timestamp default current_timestamp,
    created_by varchar(128),
    category varchar(128)
);


CREATE TABLE blog
(
    id           int8 PRIMARY KEY,
    title        varchar not null,
    theme_id        int8 not null references theme(id),
    text        text not null,
    created_date timestamp default current_timestamp,
    last_update_date timestamp default current_timestamp,
    number_views int8 default 0,
    verified     boolean default false,
    status varchar
);

CREATE TABLE comment (
    id int8 primary key,
    blog_id int8 not null references blog(id),
    content varchar(400) not null,
    created_date timestamp default current_timestamp,
    last_update_date timestamp default current_timestamp,
    usability int4 default 0,
    not_usability int4 default 0,
    verified boolean default false,
    status varchar(128)
);