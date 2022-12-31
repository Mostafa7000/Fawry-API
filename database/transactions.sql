CREATE TABLE transactions
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    user_email VARCHAR(50) REFERENCES users (email),
    service_id INT REFERENCES services (id),
    amount     REAL NOT NULL,
    refund     VARCHAR(10)
);