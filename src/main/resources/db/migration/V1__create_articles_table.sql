create sequence article_id_seq start with 1 increment by 50;

create table articles
(
    id          bigint    not null default nextval('article_id_seq'),
    prompt      text      not null,
    title       text      not null,
    content     text      not null,
    enh_content text,
    slug        text,
    images      text[],
    categories  text[],
    tags        text[],
    created_at  timestamp not null default now(),
    updated_at  timestamp,
    primary key (id)
);
