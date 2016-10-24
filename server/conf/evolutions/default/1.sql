--- Users schema

# --- !Ups
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


# --- !Downs
drop table goods CASCADE;

drop table categories CASCADE;

drop table images CASCADE;
