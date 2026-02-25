create table refresh_tokens (
    id bigserial primary key,
    user_id bigint not null,
    token_hash varchar(64) not null unique,
    expires_at timestamptz not null,
    revoked_at timestamptz,
    replaced_by_token_hash varchar(64),
    created_at timestamptz not null default now()
);

alter table refresh_tokens
 add constraint fk_refresh_tokens_user
 foreign key (user_id)
 references users(id)
 on delete cascade;

create index idx_refresh_tokens_user_id
on refresh_tokens(user_id);

create index idx_refresh_tokens_expires_at
on refresh_tokens(expires_at);