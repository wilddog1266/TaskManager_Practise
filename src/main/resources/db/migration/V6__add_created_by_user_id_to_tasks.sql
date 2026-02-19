alter table tasks
add column created_by_user_id bigint;

update tasks t
set created_by_user_id = u.id
from users u
where u.username = 'admin'
  and t.created_by_user_id is null;

alter table tasks
alter column created_by_user_id set not null;

alter table tasks
add constraint fk_tasks_created_by
foreign key (created_by_user_id)
references users(id)
on delete restrict;

create index idx_tasks_created_by
on tasks(created_by_user_id);