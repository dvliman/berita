CREATE TABLE news (
       id           SERIAL PRIMARY KEY,
       title        TEXT NOT NULL,
       image_url    TEXT NOT NULL,
       category     TEXT NOT NULL,
       content      TEXT NOT NULL,
       source_url   TEXT NOT NULL UNIQUE,
       source_name  TEXT NOT NULL,
       published_at TIMESTAMP
);
