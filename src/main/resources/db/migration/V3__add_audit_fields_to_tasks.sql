ALTER TABLE tasks
ADD COLUMN created_at timestamptz NOT NULL DEFAULT now(),
ADD COLUMN updated_at timestamptz NOT NULL DEFAULT now();