CREATE TABLE news (
       id           SERIAL PRIMARY KEY,
       title        TEXT,
       image_url    TEXT,
       category     TEXT,
       source_url   TEXT,
       sourc_name   TEXT,
       published_at TIMESTAMP
);
