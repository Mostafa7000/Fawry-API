CREATE TABLE services
(
    id           INT PRIMARY KEY,
    service_type VARCHAR(50) NOT NULL,
    provider     VARCHAR(50) NOT NULL,
    price        REAL        NOT NULL,
    discount     REAL
);

INSERT INTO services(id, service_type, provider, price, discount)
VALUES (1, 'Mobile Recharge', 'Vodafone', 10, 0.0),
       (2, 'Mobile Recharge', 'Etisalat', 10, 0.0),
       (3, 'Mobile Recharge', 'Orange', 15, 0.0),
       (4, 'Mobile Recharge', 'We', 15, 0.0),
       (5, 'Internet Payment', 'Vodafone', 210, 0),
       (6, 'Internet Payment', 'Etisalat', 120, 0),
       (7, 'Internet Payment', 'Orange', 150, 0),
       (8, 'Internet Payment', 'We', 140, 0),
       (9, 'Landline', 'We-monthly', 90, 0),
       (10, 'Landline', 'We-quarterly', 250, 0),
       (11, 'Donations', 'Cancer Hospital', 150, 0),
       (12, 'Donations', 'Schools', 100, 0),
       (13, 'Donations', 'Non profitable organizations', 300, 0);