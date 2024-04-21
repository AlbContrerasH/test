CREATE TABLE users (
    `uuid` UUID PRIMARY KEY,
    `name` VARCHAR(255),
    `email` VARCHAR(255),
    `password` VARCHAR(255),
    `created` DATE,
    `modified` DATE,
    `last_login` DATE,
    `token` VARCHAR,
    `is_active` BOOLEAN,
    `phone` BINARY
);
CREATE TABLE phone (
    `id` BIGINT PRIMARY KEY auto_increment,
    `number` BIGINT,
    `city_code` BIGINT,
    `country_code` BIGINT
);
CREATE TABLE USERS_PHONES (
    `phones_id` BIGINT,
    `users_uuid` UUID UNIQUE,
    FOREIGN KEY (users_uuid) REFERENCES users(uuid) ON DELETE CASCADE,
    FOREIGN KEY (phones_id) REFERENCES phone(id) ON DELETE CASCADE
)
