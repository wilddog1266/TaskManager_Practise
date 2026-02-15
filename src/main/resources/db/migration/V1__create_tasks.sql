create table tasks (
    id bigserial primary key,
    title varchar(255) not null,
    completed boolean not null
);