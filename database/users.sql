CREATE TABLE users
(
    email    VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100)                       NOT NULL,
    name     VARCHAR(20)                        NOT NULL,
    balance  REAL                               NOT NULL,
    discount REAL                               NOT NULL,
    admin    INT CHECK (admin = 1 OR admin = 0) NOT NULL
);

INSERT INTO users(email, password, name, balance, discount, admin)
VALUES ('mostafatarek7000@gmail.com', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4',
        'Mostafa Tarek', 0.0, 0.0, 1),
       ('youssef@gmail.com', '9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0', 'Youssef Hammam', 0.0,
        0.0, 0);