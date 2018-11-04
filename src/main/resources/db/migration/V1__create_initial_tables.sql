CREATE TABLE tab_user (
  user_id  SERIAL,
  username varchar(20)  NOT NULL,
  password varchar(200) NOT NULL,
  enabled  boolean      NOT NULL DEFAULT FALSE,
  primary key (username)
);

CREATE TABLE user_role (
  user_role_id SERIAL,
  username     varchar(20) NOT NULL,
  role         varchar(20) NOT NULL,
  UNIQUE (username, role),
  FOREIGN KEY (username) REFERENCES tab_user (username),
  primary key (user_role_id)
);

create table tab (
  hash         int       NOT null,
  artist       text      NOT null,
  name         text      NOT null,
  content      text      NOT null,
  number_rates int       NOT null default 0,
  rating       float     NOT null default 0,
  type         text      NOT null,
  tuning       text,
  source       text,
  difficulty   text,
  capo         text,
  tonality     text,
  url          text,
  author_id    bigint    NOT null default 0,
  views        int       NOT null default 0,
  created_on   timestamp NOT null default CURRENT_TIMESTAMP,
  primary key (hash)
);

CREATE TABLE comment (
  id                SERIAL,
  tab_id            bigint,
  parent_comment_id bigint,
  author_id         bigint,
  content           text,
  primary key (id)
)