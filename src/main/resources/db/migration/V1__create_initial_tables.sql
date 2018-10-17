CREATE TABLE users (
  user_id  SERIAL,
  username varchar(20)  NOT NULL,
  password varchar(200) NOT NULL,
  enabled  boolean      NOT NULL DEFAULT FALSE,
  primary key (username)
);

CREATE TABLE user_roles (
  user_role_id SERIAL,
  username     varchar(20) NOT NULL,
  role         varchar(20) NOT NULL,
  UNIQUE (username, role),
  FOREIGN KEY (username) REFERENCES users (username),
  primary key (user_role_id)
);

create table tabs (
  id        serial NOT null,
  artist    text   NOT null,
  title     text   NOT null,
  content   text   NOT null,
  author_id bigint NOT null,
  primary key (id)
);

CREATE TABLE comments (
  id                SERIAL,
  tab_id            bigint,
  parent_comment_id bigint,
  author_id         bigint,
  content           text,
  primary key (id)
)