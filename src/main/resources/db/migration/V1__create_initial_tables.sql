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
  id           serial    NOT null,
  artist       text      NOT null,
  title        text      NOT null,
  content      text      NOT null,
  author_id    bigint    NOT null,
  votes        int       NOT null,
  rating       float     NOT null,
  type         text      NOT null,
  version      int       NOT null,
  created_on   timestamp NOT null,
  views        int       NOT null,
  tuning       text,
  source       text,
  content_hash int       NOT null,
  primary key (id)
);

CREATE TABLE comment (
  id                SERIAL,
  tab_id            bigint,
  parent_comment_id bigint,
  author_id         bigint,
  content           text,
  primary key (id)
)