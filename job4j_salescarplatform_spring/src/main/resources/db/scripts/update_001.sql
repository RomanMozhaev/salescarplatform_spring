CREATE TABLE car_owners
(
    id integer NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(255) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE car_tickets
(
    id integer NOT NULL,
    brand character varying(255) NOT NULL,
    date timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    model character varying(255) NOT NULL,
    picture character varying(255) NOT NULL,
    price bigint NOT NULL,
    sold boolean NOT NULL,
    type character varying(255) NOT NULL,
    usage integer NOT NULL,
    year integer NOT NULL,
    user_id integer,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES car_owners (id)
);

CREATE SEQUENCE hibernate_sequence;