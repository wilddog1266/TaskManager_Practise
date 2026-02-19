create table users (
    id bigserial primary key,
    username varchar(255) not null unique,
    password_hash varchar(255) not null,
    role varchar(50) not null default 'USER',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);