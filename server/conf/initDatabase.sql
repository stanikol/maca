--- Users schema
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

create table images (
    name        varchar primary key,
    content     varchar not null,
    bytes       bytea not null,
    changed     timestamp default(now()::timestamp)
);

create table categories (
    name        varchar primary key
);

create table goods (
    id          serial         primary key,
    category    varchar        references categories(name),
    title       varchar        not null,
    description varchar        not null,
    qnt         int            not null default(0),
    price       decimal(19, 2) not null default(0),
    show        int            not null default(0),
    image       varchar array  not null default(array[]::varchar[]),
    changed     timestamp default(now()::timestamp)
);

create table orders (
    id        serial    primary key,
    phone     varchar   not null,
    created   timestamp,
    closed    timestamp,
    changed   timestamp default(now()::timestamp)
);

insert into categories values('Макаруны');
insert into categories values('Наборы');
insert into goods(category, title, description, image) values('Макаруны', 'title', 'description',   ARRAY['img/maca1.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(category, title, description, image) values('Макаруны', 'title2', 'description2', ARRAY['img/maca2.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(category, title, description, image) values('Макаруны', 'title3', 'description3', ARRAY['img/maca3.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(category, title, description, image) values('Макаруны', 'title4', 'description4', ARRAY['img/maca1.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(category, title, description, image) values('Макаруны', 'title5', 'description5', ARRAY['img/maca3.jpg','img/maca2.jpg', 'img/maca3.jpg']);

