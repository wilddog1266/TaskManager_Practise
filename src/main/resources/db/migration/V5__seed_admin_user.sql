insert into users (username, password_hash, role)
select 'admin',
       '$2a$10$FoG4VgJob63.UiVyZa0Zou2EBsWXN/Yr/zdiF0Yp/IAf1GLy7BdCm',
       'ADMIN'
where not exists (
    select 1 from users where username = 'admin'
);